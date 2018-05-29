package cn.joker.controller.markcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.stdName;
import cn.joker.sevice.ImgMarkService;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
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
        imgMark.setImgMark_task((TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID)));
        imgMark.setNotePolygon(jsonObject.get(stdName.NOTEPOLYGON).toString());
        imgMark.setNoteRectangle(jsonObject.get(stdName.NOTERECTANGLE).toString());
        imgMark.setNoteTotal(jsonObject.get(stdName.NOTETOTAL).toString());
        TaskEntity taskEntity = imgMark.getImgMark_task();
        UserEntity userEntity = imgMark.getWorker();
        ImageEntity imageEntity = imgMark.getImage_imgMark();
        List<WorkersForTheTaskEntity> workersForTheTaskEntities = taskEntity.getWorkersForTheTaskEntityList();
        for (WorkersForTheTaskEntity workersForTheTaskEntity : workersForTheTaskEntities) {
            if (workersForTheTaskEntity.getWorker().getUsername().equals(userEntity.getUsername())) {
                workersForTheTaskEntity.setMarkedNum(workersForTheTaskEntity.getMarkedNum() + 1);
                if (!jsonObject.getBoolean(stdName.ISMODIFYED)) {
                    workersForTheTaskEntity.setCompletedNum(workersForTheTaskEntity.getCompletedNum() + 1);
                } else {
                    ImgMarkEntity imgMarkEntity = imgMarkService.findByImage_imgMarkAndImgMark_taskAndWorker(imageEntity, taskEntity, userEntity);
                    imgMarkService.delete(imgMarkEntity);

                }
                break;
            }
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, imgMarkService.add(imgMark));
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
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(stdName.TASKID));
        String imgName = jsonObject.getString(stdName.IMGNAME);
        ImageEntity imageEntity = imgService.findByName(imgName.substring(0, imgName.lastIndexOf('.')));
        JSONArray userArray = jsonObject.getJSONArray(stdName.USERS);
        JSONArray marksArray = new JSONArray();
        for (Object o : userArray) {
            JSONObject obj = (JSONObject) o;
            String username = obj.getString(stdName.USERNAME);
            UserEntity userEntity = userService.findByUsername(username);
            ImgMarkEntity imgMarkEntity = imgMarkService.findByImage_imgMarkAndImgMark_taskAndWorker(imageEntity, taskEntity, userEntity);
            if (imgMarkEntity != null) {
                JSONObject mark = new JSONObject();
                mark.put(stdName.IMGURL, imageEntity.getUrl());
                mark.put(stdName.WORKERNAME, userEntity.getUsername());
                mark.put(stdName.SPONSORNAME, taskEntity.getSponsor());
                mark.put(stdName.TASKID, taskEntity.getId());
                JSONArray jsonArray = new JSONArray(imgMarkEntity.getNotePolygon());
                mark.put(stdName.NOTEPOLYGON, jsonArray);
                jsonArray = new JSONArray(imgMarkEntity.getNoteRectangle());
                mark.put(stdName.NOTERECTANGLE, jsonArray);
                jsonArray = new JSONArray(imgMarkEntity.getNoteTotal());
                mark.put(stdName.NOTETOTAL, jsonArray);
                marksArray.put(mark);
            }
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


        List<ImgMarkEntity> imgMarkEntities = imgMarkService.findByImageAndTask(imageEntity, taskEntity);
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
            if(workerAnswers != null && workerAnswers.size() != 0){
                recNodeObj.put(stdName.MARK,segmentation.getStrmax1());
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

}
