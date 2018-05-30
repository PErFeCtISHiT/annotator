package cn.joker.controller;

import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.stdName;
import cn.joker.serviceimpl.TaskServiceImpl;
import cn.joker.sevice.TaskService;
import cn.joker.statisticalMethod.NaiveBayesianClassification;
import cn.joker.util.JsonHelper;
import cn.joker.vo.RecNode;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/test")
public class Classification {
    @Resource
    private TaskService taskService;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public void get(HttpServletResponse response) {
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(16);
        List<ImgMarkEntity> imgMarkEntities = taskEntity.getImgMarkEntityList();
        List<RecNode> recNodes = NaiveBayesianClassification.integration(imgMarkEntities).get(0).getRecNodes();
        Logger logger = Logger.getLogger(TaskServiceImpl.class);
        logger.info("clause size:" + NaiveBayesianClassification.integration(imgMarkEntities).size());
        logger.info("clause1 size:" + recNodes.size());
        for(RecNode recNode : recNodes){
            logger.info(recNode.getTop());
            logger.info(recNode.getLeft());
            logger.info(recNode.getHeight());
            logger.info(recNode.getWidth());
        }
        JSONObject msg = new JSONObject();
        msg.put(stdName.MES, "success");
        JsonHelper.jsonToResponse(response, msg);
    }
}
