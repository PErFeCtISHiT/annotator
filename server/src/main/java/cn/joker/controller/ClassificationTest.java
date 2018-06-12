package cn.joker.controller;

import cn.joker.entity.ImgMarkEntity;
import cn.joker.namespace.StdName;
import cn.joker.serviceimpl.TaskServiceImpl;
import cn.joker.statisticalmethod.NaiveBayesianClassification;
import cn.joker.util.JsonHelper;
import cn.joker.vo.RecNode;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class ClassificationTest {

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public void get(HttpServletResponse response) {
        List<ImgMarkEntity> imgMarkEntities = new ArrayList<>();
        List<RecNode> recNodes = NaiveBayesianClassification.integration(imgMarkEntities).get(0).getRecNodes();
        Logger logger = Logger.getLogger(TaskServiceImpl.class);
        logger.info("clause size:" + NaiveBayesianClassification.integration(imgMarkEntities).size());
        logger.info("clause1 size:" + recNodes.size());
        for (RecNode recNode : recNodes) {
            logger.info(recNode.getTop());
            logger.info(recNode.getLeft());
            logger.info(recNode.getHeight());
            logger.info(recNode.getWidth());
        }
        JSONObject msg = new JSONObject();
        msg.put(StdName.MES, "success");
        JsonHelper.jsonToResponse(response, msg);
    }
}
