package cn.joker.serviceimpl;

import cn.joker.dao.TagRepository;
import cn.joker.entity.*;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.UserService;
import cn.joker.statisticalmethod.NaiveBayesianClassification;
import cn.joker.statisticalmethod.QuestionModel;
import cn.joker.statisticalmethod.Segmentation;
import cn.joker.vo.RecNode;
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

    private void refreshTest(TagEntity tagEntity) {
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
                    this.modify(tagEntity);
                    return;
                }
            }
        }
    }

    @Override
    public boolean markIntegration(UserEntity userEntity, TagEntity tagEntity, Integer type) {
        Logger logger = Logger.getLogger(TaskServiceImpl.class);
        boolean ret = true;
        int correctNumber = 0;
        Segmentation segmentation = new Segmentation();
        List<ImageEntity> imageEntities;
        if (type == 2) {//写标注
            imageEntities = tagEntity.getTestImageList1();
            for (ImageEntity imageEntity : imageEntities) {
                //得到标注
                List<ImgMarkEntity> imgMarkEntities = imageEntity.getImgMarkEntityList();
                //整合，簇里面有历史信息
                List<RecNodeList> recNodeLists = NaiveBayesianClassification.integration(imgMarkEntities);
                for (RecNodeList recNodeList : recNodeLists) {
                    //对每个簇分词，修改工人正确率
                    List<WorkerAnswer> workerAnswers = segmentation.segment(recNodeList);
                    logger.info("clause size:" + recNodeLists.size());
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
            }
        } else {//不写标注
            //目前所有测试图片
            imageEntities = tagEntity.getTestImageList();
            for (ImageEntity imageEntity : imageEntities) {
                List<ImgMarkEntity> imgMarkEntities = imageEntity.getImgMarkEntityList();
                List<RecNode> testMark= new ArrayList<>();
                //得到所有的标注
                List<RecNode> markList = NaiveBayesianClassification.getAllMark(imgMarkEntities);
                //先把测试的用户的标注结果去掉单独放在一边
                for(RecNode recNode: markList){
                    if(recNode.getWorker().getId().equals(userEntity.getId())){
                        testMark.add(recNode);
                        markList.remove(recNode);
                    }
                }

                correctNumber = NaiveBayesianClassification.getCorrectNumber(testMark, markList);
            }

            // 调整用户正确率
            if(correctNumber < 0.8) {
                // 把判断结果输出
                ret = false;
            }

            // 最后整合一次测试答案，更新用户正确率
        }
        return ret;
    }

    @Override
    public List<TagEntity> findAll() {
        return this.tagRepository.findAll();
    }

    @Scheduled(cron = "0 0 12 * * ?")   //每天执行一次
    public void tagRefresh() {
        log.info("refresh begin");
        List<TagEntity> tagEntities = this.findAll();
        for (TagEntity tagEntity : tagEntities) {
            if (tagEntity.getId() != 6)
                this.refreshTest(tagEntity);
        }
        log.info("refresh success");

    }
}
