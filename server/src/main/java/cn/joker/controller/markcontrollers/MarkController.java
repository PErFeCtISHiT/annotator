package cn.joker.controller.markcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.StdName;
import cn.joker.sevice.*;
import cn.joker.statisticalmethod.NaiveBayesianClassification;
import cn.joker.statisticalmethod.Segmentation;
import cn.joker.util.JsonHelper;
import cn.joker.pojo.RecNode;
import cn.joker.pojo.RecNodeList;
import cn.joker.pojo.WorkerAnswer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
     * @param request 请求内容
     * @param response 返回内容
     * @date: 9:06 2018/4/14
     */
    @RequestMapping(value = "/postMark", method = RequestMethod.POST)
    public void postMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        ImgMarkEntity imgMark = new ImgMarkEntity();
        imgMark.setWorker(userService.findByUsername(jsonObject.getString(StdName.WORKERNAME)));
        imgMark.setImage_imgMark(imgService.findByUrl(jsonObject.getString(StdName.IMGURL)));
        imgMark.setNotePolygon(jsonObject.get(StdName.NOTEPOLYGON).toString());
        imgMark.setNoteRectangle(jsonObject.get(StdName.NOTERECTANGLE).toString());
        imgMark.setNoteTotal(jsonObject.get(StdName.NOTETOTAL).toString());
        UserEntity userEntity = imgMark.getWorker();
        ImageEntity imageEntity = imgMark.getImage_imgMark();
        if(imageEntity.getImgMarkEntityList() == null)
            imageEntity.setImgMarkEntityList(new ArrayList<>());
        imageEntity.getImgMarkEntityList().add(imgMark);
        userEntity.setPoints(userEntity.getPoints() + 1);
        userEntity.setBonus(userEntity.getBonus() + 1);
        switch (imageEntity.getType()){
            case 1:
                userEntity.setType1Num(userEntity.getType1Num() + 1);
                break;
            case 2:
                userEntity.setType2Num(userEntity.getType1Num() + 1);
                break;
            case 3:
                userEntity.setType3Num(userEntity.getType1Num() + 1);
                break;
            default:
                break;
        }
        userEntity.setLev((int) (Math.log(userEntity.getBonus() + 1.0) / Math.log(10) + 1));
        imageEntity.setMarked(true);
        TaskEntity taskEntity = imageEntity.getImg_task();
        List<TagEntity> tagEntities = taskEntity.getTagEntityList();
        List<WorkerMatrixEntity> workerMatrixEntities = userEntity.getWorkerMatrixEntities();
        for(TagEntity tagEntity : tagEntities){
            WorkerMatrixEntity workerMatrixEntity = workerMatrixEntities.get(tagEntity.getId() - 1);
            workerMatrixEntity.setNum(workerMatrixEntity.getNum() + 1);
        }
        taskEntity.setActNum(taskEntity.getActNum() + 1);
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, userService.modify(userEntity) && imgService.modify(imageEntity) && taskService.modify(taskEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看图片标注
     * @param request 请求内容
     * @param response 返回内容
     * @date: 10:33 2018/4/14
     */
    @RequestMapping(value = "/checkImage", method = RequestMethod.POST)
    public void checkMark(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String imgURL = jsonObject.getString(StdName.IMGURL);
        ImageEntity imageEntity = imgService.findByUrl(imgURL);
        List<ImgMarkEntity> imgMarkEntities = imgMarkService.findByImage(imageEntity);
        JSONArray marksArray = new JSONArray();
        for (ImgMarkEntity imgMarkEntity : imgMarkEntities) {
            JSONObject mark = new JSONObject();
            mark.put(StdName.IMGURL, imageEntity.getUrl());
            JSONArray jsonArray = new JSONArray(imgMarkEntity.getNotePolygon());
            mark.put(StdName.NOTEPOLYGON, jsonArray);
            jsonArray = new JSONArray(imgMarkEntity.getNoteRectangle());
            mark.put(StdName.NOTERECTANGLE, jsonArray);
            jsonArray = new JSONArray(imgMarkEntity.getNoteTotal());
            mark.put(StdName.NOTETOTAL, jsonArray);
        }
        JSONObject ret = new JSONObject();
        ret.put(StdName.MARKS, marksArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看整合结果
     * @param request 请求内容
     * @param response 返回内容
     * @date: 19:29 2018/5/28
     */
    @RequestMapping(value = "/checkImageIntegration", method = RequestMethod.POST)
    public void checkImageIntegration(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(StdName.TASKID));
        String imgName = jsonObject.getString(StdName.IMGNAME);
        ImageEntity imageEntity = imgService.findByName(imgName.substring(0, imgName.lastIndexOf('.')));
        JSONArray marksArray = new JSONArray();


        List<ImgMarkEntity> imgMarkEntities = imgMarkService.findByImage(imageEntity);
        List<RecNodeList> recNodeLists = NaiveBayesianClassification.integration(imgMarkEntities);
        JSONObject mark = new JSONObject();
        mark.put(StdName.IMGURL, imageEntity.getUrl());
        mark.put(StdName.SPONSORNAME, taskEntity.getSponsor().getUsername());
        mark.put(StdName.TASKID, taskEntity.getId());
        JSONArray jsonArray = new JSONArray();
        mark.put(StdName.NOTEPOLYGON, jsonArray);
        jsonArray = new JSONArray();
        int count = 0;
        for (RecNodeList recNodeList : recNodeLists) {
            JSONObject recNodeObj = new JSONObject();
            Segmentation segmentation = new Segmentation();
            List<WorkerAnswer> workerAnswers = segmentation.segment(recNodeList);
            if (workerAnswers != null && !workerAnswers.isEmpty()) {
                recNodeObj.put(StdName.MARK, segmentation.getStrmax1());
            } else
                recNodeObj.put(StdName.MARK, StdName.NULL);
            RecNode recNode = recNodeList.getRecNode();
            recNodeObj.put(StdName.TOP, recNode.getTop());
            recNodeObj.put(StdName.LEFT, recNode.getLeft());
            recNodeObj.put(StdName.HEIGHT, recNode.getHeight());
            recNodeObj.put(StdName.WIDTH, recNode.getWidth());
            recNodeObj.put(StdName.AUTHOR, StdName.NULL);
            recNodeObj.put(StdName.ID, count);
            jsonArray.put(recNodeObj);
            count++;
        }
        mark.put(StdName.NOTERECTANGLE, jsonArray);
        jsonArray = new JSONArray();
        mark.put(StdName.NOTETOTAL, jsonArray);
        marksArray.put(mark);

        JSONObject ret = new JSONObject();
        ret.put(StdName.MARKS, marksArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人测试标注
     * @param request 请求内容
     * @param response 返回内容
     * @date: 15:22 2018/6/4
     */
    @RequestMapping(value = "/markTest", method = RequestMethod.GET)
    public void markTest(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer type = Integer.valueOf(map.get(StdName.TYPE)[0]);
        TagEntity tagEntity = tagService.findByTag(map.get(StdName.TAG)[0]);
        JSONArray imgArray = new JSONArray();
        List<ImageEntity> imageEntities;
        if (type == 1) {
            imageEntities = tagEntity.getTestImageList();
        } else
            imageEntities = tagEntity.getTestImageList1();
        for (ImageEntity imageEntity : imageEntities) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(StdName.IMGURL, imageEntity.getUrl());
            jsonObject.put(StdName.DESCRIPTION, imageEntity.getImg_task().getDescription());
            imgArray.put(jsonObject);
        }
        JSONObject ret = new JSONObject();
        ret.put(StdName.IMGS, imgArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人测试
     * @param request 请求内容
     * @param response 返回内容
     * @date: 12:14 2018/6/5
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        UserEntity userEntity = userService.findByUsername(map.get(StdName.USERNAME)[0]);
        TagEntity tagEntity = tagService.findByTag(map.get(StdName.TAG)[0]);
        JSONObject ret = new JSONObject();
        boolean b = tagService.markIntegration(userEntity, tagEntity, Integer.valueOf(map.get(StdName.TYPE)[0]));
        if (!b || userEntity.getWorkerMatrixEntities().get(tagEntity.getId() - 1).getCorrect() < 0.7) {
            ret.put(StdName.MES, false);
        } else {
            ret.put(StdName.MES, true);
        }
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 工人得到一张图
     * @param request 请求内容
     * @param response 返回内容
     * @date: 14:22 2018/6/5
     */
    @RequestMapping(value = "/markOne", method = RequestMethod.GET)
    public void markOne(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        TagEntity tagEntity = tagService.findByTag(map.get(StdName.TAG)[0]);
        Integer type = Integer.valueOf(map.get(StdName.TYPE)[0]);
        String username = map.get(StdName.USERNAME)[0];
        UserEntity user = userService.findByUsername(username);
        List<TaskEntity> taskEntities = tagEntity.getTaskEntityList();
        JSONObject ret = new JSONObject();
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.getType().equals(type)) {
                List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
                for (ImageEntity imageEntity : imageEntities) {
                    if (user.getLev() >= taskEntity.getWorkerLevel()) {
                        if (type == 3 && taskEntity.getPolygonNum() > taskEntity.getActNum()) {
                            int flag = 0;
                            List<UserEntity> userEntities = imageEntity.getWorkers();
                            for (UserEntity userEntity : userEntities) {
                                if (userEntity.getUsername().equals(username)) {
                                    flag = 1;
                                    break;
                                }
                            }
                            if (flag == 0) {
                                userEntities.add(userService.findByUsername(username));
                                imgService.modify(imageEntity);
                                ret.put(StdName.IMGURL, imageEntity.getUrl());
                                ret.put(StdName.DESCRIPTION, taskEntity.getDescription());
                                JsonHelper.jsonToResponse(response, ret);
                                return;
                            }
                        } else if (!imageEntity.getMarked() && type != 3) {
                            ret.put(StdName.IMGURL, imageEntity.getUrl());
                            ret.put(StdName.DESCRIPTION, taskEntity.getDescription());
                            JsonHelper.jsonToResponse(response, ret);
                            return;
                        }
                    }
                }
            }
        }
        ret.put(StdName.MES, StdName.NULL);
        JsonHelper.jsonToResponse(response, ret);
    }

}
