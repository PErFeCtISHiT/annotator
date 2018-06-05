package cn.joker.controller.markcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.stdName;
import cn.joker.sevice.*;
import cn.joker.statisticalMethod.NaiveBayesianClassification;
import cn.joker.statisticalMethod.Segmentation;
import cn.joker.util.JsonHelper;
import cn.joker.vo.RecNode;
import cn.joker.vo.RecNodeList;
import cn.joker.vo.WorkerAnswer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 20:26 2018/4/13
 */
@RestController
@RequestMapping("/mark")
public class MarkController {
    @Resource
    private ImgMarkService imgMarkService;
    @Resource
    private TaskService taskService;
    @Resource
    private ImgService imgService;
    @Resource
    private UserService userService;
    @Resource
    private TagService tagService;

    /**
     * @author:pis
     * @description: 上传图片标注
     * @date: 9:06 2018/4/14
     */
    @RequestMapping(value = "/postMark", method = RequestMethod.POST)
    public void postMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        ImgMarkEntity imgMark = new ImgMarkEntity();
        imgMark.setWorker(userService.findByUsername(jsonObject.getString(stdName.WORKERNAME)));
        imgMark.setImage_imgMark(imgService.findByUrl(jsonObject.getString(stdName.IMGURL)));
        imgMark.setNotePolygon(jsonObject.get(stdName.NOTEPOLYGON).toString());
        imgMark.setNoteRectangle(jsonObject.get(stdName.NOTERECTANGLE).toString());
        imgMark.setNoteTotal(jsonObject.get(stdName.NOTETOTAL).toString());
        UserEntity userEntity = imgMark.getWorker();
        ImageEntity imageEntity = imgMark.getImage_imgMark();
        imageEntity.getImgMarkEntityList().add(imgMark);
        userEntity.setPoints(userEntity.getPoints() + 1);
        imageEntity.setMarked(true);
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, userService.modify(userEntity) && imgService.modify(imageEntity) && imgMarkService.add(imgMark));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看图片标注
     * @date: 10:33 2018/4/14
     */
    @RequestMapping(value = "/checkImage", method = RequestMethod.POST)
    public void checkMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String imgName = jsonObject.getString(stdName.IMGNAME);
        ImageEntity imageEntity = imgService.findByName(imgName.substring(0, imgName.lastIndexOf('.')));
        List<ImgMarkEntity> imgMarkEntities = imgMarkService.findByImage(imageEntity);
        JSONArray marksArray = new JSONArray();
        for (ImgMarkEntity imgMarkEntity : imgMarkEntities) {
            JSONObject mark = new JSONObject();
            mark.put(stdName.IMGURL, imageEntity.getUrl());
            JSONArray jsonArray = new JSONArray(imgMarkEntity.getNotePolygon());
            mark.put(stdName.NOTEPOLYGON, jsonArray);
            jsonArray = new JSONArray(imgMarkEntity.getNoteRectangle());
            mark.put(stdName.NOTERECTANGLE, jsonArray);
            jsonArray = new JSONArray(imgMarkEntity.getNoteTotal());
            mark.put(stdName.NOTETOTAL, jsonArray);
            marksArray.put(mark);
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.MARKS, marksArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看整合结果
     * @date: 19:29 2018/5/28
     */
    @RequestMapping(value = "/checkImageIntegration", method = RequestMethod.POST)
    public void checkImageIntegration(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID));
        String imgName = jsonObject.getString(stdName.IMGNAME);
        ImageEntity imageEntity = imgService.findByName(imgName.substring(0, imgName.lastIndexOf('.')));
        JSONArray marksArray = new JSONArray();


        List<ImgMarkEntity> imgMarkEntities = imgMarkService.findByImage(imageEntity);
        List<RecNodeList> recNodeLists = NaiveBayesianClassification.integration(imgMarkEntities);
        JSONObject mark = new JSONObject();
        mark.put(stdName.IMGURL, imageEntity.getUrl());
        mark.put(stdName.SPONSORNAME, taskEntity.getSponsor().getUsername());
        mark.put(stdName.TASKID, taskEntity.getId());
        JSONArray jsonArray = new JSONArray();
        mark.put(stdName.NOTEPOLYGON, jsonArray);
        jsonArray = new JSONArray();
        int count = 0;
        for (RecNodeList recNodeList : recNodeLists) {
            JSONObject recNodeObj = new JSONObject();
            Segmentation segmentation = new Segmentation();
            List<WorkerAnswer> workerAnswers = segmentation.segment(recNodeList);
            if (workerAnswers != null && workerAnswers.size() != 0) {
                recNodeObj.put(stdName.MARK, segmentation.getStrmax1());
            }
            RecNode recNode = recNodeList.getRecNode();
            recNodeObj.put(stdName.TOP, recNode.getTop());
            recNodeObj.put(stdName.LEFT, recNode.getLeft());
            recNodeObj.put(stdName.HEIGHT, recNode.getHeight());
            recNodeObj.put(stdName.WIDTH, recNode.getWidth());
            recNodeObj.put(stdName.AUTHOR, stdName.NULL);
            recNodeObj.put(stdName.ID, count);
            recNodeObj.put(stdName.MARK, stdName.NULL);
            jsonArray.put(recNodeObj);
            count++;
        }
        mark.put(stdName.NOTERECTANGLE, jsonArray);
        jsonArray = new JSONArray();
        mark.put(stdName.NOTETOTAL, jsonArray);
        marksArray.put(mark);

        JSONObject ret = new JSONObject();
        ret.put(stdName.MARKS, marksArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人测试标注
     * @date: 15:22 2018/6/4
     */
    @RequestMapping(value = "/markTest", method = RequestMethod.GET)
    public void markTest(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        TagEntity tagEntity = tagService.findByTag(map.get(stdName.TAG)[0]);
        JSONArray imgArray = new JSONArray();
        List<ImageEntity> imageEntities = tagEntity.getTestImageList();
        for (ImageEntity imageEntity : imageEntities) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(stdName.IMGURL, imageEntity.getUrl());
            jsonObject.put(stdName.DESCRIPTION, imageEntity.getImg_task().getDescription());
            imgArray.put(jsonObject);
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.IMGS, imgArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人测试
     * @date: 12:14 2018/6/5
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        UserEntity userEntity = userService.findByUsername(map.get(stdName.USERNAME)[0]);
        TagEntity tagEntity = tagService.findByTag(map.get(stdName.TAG)[0]);
        JSONObject ret = new JSONObject();
        boolean b = tagService.markIntegration(tagEntity);
        if (!b || userEntity.getWorkerMatrixEntities().get(tagEntity.getId() - 1).getCorrect() < 0.7) {
            ret.put(stdName.MES, false);
        } else {
            ret.put(stdName.MES, true);
        }
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人得到一张图
     * @date: 14:22 2018/6/5
     */
    @RequestMapping(value = "/markOne", method = RequestMethod.GET)
    public void markOne(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        TagEntity tagEntity = tagService.findByTag(map.get(stdName.TAG)[0]);
        List<TaskEntity> taskEntities = tagEntity.getTaskEntityList();
        JSONObject ret = new JSONObject();
        for (TaskEntity taskEntity : taskEntities) {
            List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
            for (ImageEntity imageEntity : imageEntities) {
                if (!imageEntity.getMarked()) {
                    ret.put(stdName.IMGURL, imageEntity.getUrl());
                    ret.put(stdName.DESCRIPTION, taskEntity.getDescription());
                    JsonHelper.jsonToResponse(response, ret);
                    return;
                }
            }
        }
        ret.put(stdName.MES, stdName.NULL);
        JsonHelper.jsonToResponse(response, ret);
    }

}
