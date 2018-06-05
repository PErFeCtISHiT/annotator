package cn.joker.serviceimpl;

import cn.joker.config.MyConfig;
import cn.joker.dao.TagRepository;
import cn.joker.entity.*;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.UserService;
import cn.joker.statisticalMethod.NaiveBayesianClassification;
import cn.joker.statisticalMethod.QuestionModel;
import cn.joker.statisticalMethod.Segmentation;
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
        for (TaskEntity taskEntity : taskEntities) {
            List<ImageEntity> images = taskEntity.getImageEntityList();
            for (ImageEntity imageEntity : images) {
                if (imageEntity.getMarked().equals(false)) {
                    imageEntities.add(imageEntity);
                    imageEntity.setMarked(true);
                    imgService.modify(imageEntity);
                }
                if (imageEntities.size() == 10) {
                    tagEntity.setTestImageList(imageEntities);
                    return this.modify(tagEntity);
                }
            }
        }
        return false;
    }

    @Override
    public boolean markIntegration(TagEntity tagEntity) {
        Logger logger = Logger.getLogger(TaskServiceImpl.class);
        boolean ret = true;
        Segmentation segmentation = new Segmentation();
        List<ImageEntity> imageEntities = tagEntity.getTestImageList();
        List<ImgMarkEntity> imgMarkEntities = new ArrayList<>();
        for (ImageEntity imageEntity : imageEntities) {
            List<ImgMarkEntity> imgMarkEntities1 = imageEntity.getImgMarkEntityList();
            imgMarkEntities.addAll(imgMarkEntities1);
        }
        List<RecNodeList> recNodeLists = NaiveBayesianClassification.integration(imgMarkEntities);
        logger.info("clause size:" + recNodeLists.size());
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
        return ret;

    }

    @Override
    public List<TagEntity> findAll() {
        return this.tagRepository.findAll();
    }

    @Scheduled(cron="0 0 12 * * ?" )   //每10秒执行一次
    public void tagRefresh() {
        log.info("refresh begin");
        List<TagEntity> tagEntities = this.findAll();
        for (TagEntity tagEntity : tagEntities) {
            this.refreshTest(tagEntity);
        }
        log.info("refresh success");

    }
}
