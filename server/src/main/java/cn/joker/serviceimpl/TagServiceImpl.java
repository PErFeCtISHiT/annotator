package cn.joker.serviceimpl;

import cn.joker.dao.TagRepository;
import cn.joker.entity.*;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.UserService;
import cn.joker.statisticalmethod.NaiveBayesianClassification;
import cn.joker.statisticalmethod.QuestionModel;
import cn.joker.statisticalmethod.Segmentation;
import cn.joker.vo.RecNodeList;
import cn.joker.vo.WorkerAnswer;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:08 2018/5/20
 */
@Service
public class TagServiceImpl extends PubServiceImpl implements TagService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository tagRepository;
    @Resource
    private ImgService imgService;
    @Resource
    private UserService userService;

    private List<Boolean> testTable;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.repository = tagRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public TagEntity findByTag(String tag) {
        return tagRepository.findByTag(tag);
    }

    @Override
    public boolean refreshTest(TagEntity tagEntity) {
        List<TaskEntity> taskEntities = tagEntity.getTaskEntityList();
        List<ImageEntity> imageEntities = new ArrayList<>();
        List<ImageEntity> imageEntities1 = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntities) {
            List<ImageEntity> images = taskEntity.getImageEntityList();
            for (ImageEntity imageEntity : images) {
                if (imageEntity.getMarked().equals(false)) {
                    if (imageEntity.getType() == 1)
                        imageEntities.add(imageEntity);
                    else
                        imageEntities1.add(imageEntity);
                    imageEntity.setMarked(true);
                    imgService.modify(imageEntity);
                }
                if (imageEntities.size() == 10 && imageEntities1.size() == 10) {
                    tagEntity.setTestImageList(imageEntities);
                    return this.modify(tagEntity);
                }
            }
        }
        return false;
    }

    @Override
    public boolean markIntegration(TagEntity tagEntity, Integer type) {
        Logger logger = Logger.getLogger(TaskServiceImpl.class);
        boolean ret = true;
        Segmentation segmentation = new Segmentation();
        List<ImageEntity> imageEntities;
        if (type == 1)
            imageEntities = tagEntity.getTestImageList();
        else
            imageEntities = tagEntity.getTestImageList1();
        List<ImgMarkEntity> imgMarkEntities = new ArrayList<>();
        for (ImageEntity imageEntity : imageEntities) {
            List<ImgMarkEntity> imgMarkEntities1 = imageEntity.getImgMarkEntityList();
            imgMarkEntities.addAll(imgMarkEntities1);
        }
        List<RecNodeList> recNodeLists = NaiveBayesianClassification.integration(imgMarkEntities);
        logger.info("clause size:" + recNodeLists.size());
        if (type == 2) {//写标注
            for (RecNodeList recNodeList : recNodeLists) {
                List<WorkerAnswer> workerAnswers = segmentation.segment(recNodeList);
                logger.info("workers size:" + workerAnswers.size());
                QuestionModel questionModel = new QuestionModel();
                for (WorkerAnswer workerAnswer : workerAnswers) {
                    logger.info("answer:" + workerAnswer.getAnswer());
                    UserEntity worker = workerAnswer.getUserEntity();
                    WorkerMatrixEntity workerMatrixEntity = worker.getWorkerMatrixEntities().get(tagEntity.getId() - 1);
                    Double gamma = (workerMatrixEntity.getC00() + workerMatrixEntity.getC11())
                            / (workerMatrixEntity.getC11() + workerMatrixEntity.getC00() + workerMatrixEntity.getC01() + workerMatrixEntity.getC10());
                    questionModel.psUpdate(gamma, workerAnswer.getAnswer());

                }
                for (WorkerAnswer workerAnswer : workerAnswers) {
                    UserEntity worker = workerAnswer.getUserEntity();

                    logger.info(tagEntity.getTag());
                    WorkerMatrixEntity workerMatrixEntity = worker.getWorkerMatrixEntities().get(tagEntity.getId() - 1);
                    assert workerMatrixEntity != null;
                    if (workerAnswer.getAnswer()) {
                        workerMatrixEntity.setC10(workerMatrixEntity.getC10() + questionModel.getP1());
                        workerMatrixEntity.setC11(workerMatrixEntity.getC11() + questionModel.getP0());

                    } else {
                        workerMatrixEntity.setC00(workerMatrixEntity.getC00() + questionModel.getP1());
                        workerMatrixEntity.setC01(workerMatrixEntity.getC01() + questionModel.getP0());
                    }
                    logger.info("c00: " + workerMatrixEntity.getC00());
                    logger.info("c01: " + workerMatrixEntity.getC01());
                    logger.info("c10: " + workerMatrixEntity.getC10());
                    logger.info("c11: " + workerMatrixEntity.getC11());
                    logger.info("rate:" + (workerMatrixEntity.getC00() + workerMatrixEntity.getC11())
                            / (workerMatrixEntity.getC11() + workerMatrixEntity.getC00() + workerMatrixEntity.getC01() + workerMatrixEntity.getC10()));

                    ret = ret && userService.modify(worker);
                }
            }
        } else {//不写标注

        }
        return ret;
    }

    @Override
    public List<TagEntity> findAll() {
        return this.tagRepository.findAll();
    }

    @Override
    public Double mapTestTable(List<Boolean> test) {
        Double temp = 0.0;
        for (int i = 0; i < 10; i++) {
            assert test.get(i) != null && testTable.get(i) != null;
            if (test.get(i).equals(testTable.get(i))) {
                temp++;
            }
        }
        return temp / 10;
    }

    @Scheduled(cron = "0 0 12 * * ?")   //每天执行一次
    public void tagRefresh() {
        log.info("refresh begin");
        List<TagEntity> tagEntities = this.findAll();
        for (TagEntity tagEntity : tagEntities) {
            if (tagEntity.getId() != 6)
                this.refreshTest(tagEntity);
        }
        testTable = new ArrayList<>();
        this.refreshTable(testTable);
        log.info("refresh success");

    }

    private void refreshTable(List<Boolean> table) {
        for (int i = 0; i < 5; i++) {
            table.add(true);
            table.add(false);
        }
    }
}
