package cn.joker.controller.markcontrollers;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.ImgMarkService;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        imgMark.setNotePolygon(jsonObject.getString(stdName.NOTEPOLYGON));
        imgMark.setNoteRectangle(jsonObject.getString(stdName.NOTERECTANGLE));
        imgMark.setNoteTotal(jsonObject.getString(stdName.NOTETOTAL));
        boolean b = true;
        if (!jsonObject.getBoolean(stdName.ISMODIFYED)) {
            b = taskService.postMark(imgMark.getWorker(), imgMark.getImgMark_task());
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, imgMarkService.add(imgMark) && b);
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
        ImageEntity imageEntity = imgService.findByName(jsonObject.getString(stdName.IMGNAME));
        JSONArray userArray = jsonObject.getJSONArray(stdName.USERS);
        JSONArray marksArray = new JSONArray();
        for (Object o : userArray) {
            String username = (String) o;
            UserEntity userEntity = userService.findByUsername(username);
            ImgMarkEntity imgMarkEntity = imgMarkService.findByImage_imgMarkAndImgMark_taskAndWorker(imageEntity, taskEntity, userEntity);
            if (imgMarkEntity != null) {
                JSONObject mark = new JSONObject();
                mark.put(stdName.IMGURL, imageEntity.getUrl());
                mark.put(stdName.WORKERNAME, userEntity.getUsername());
                mark.put(stdName.SPONSORNAME, taskEntity.getSponsor());
                mark.put(stdName.TASKID, taskEntity.getId());
                mark.put(stdName.NOTEPOLYGON, imgMarkEntity.getNotePolygon());
                mark.put(stdName.NOTERECTANGLE, imgMarkEntity.getNoteRectangle());
                mark.put(stdName.NOTETOTAL, imgMarkEntity.getNoteTotal());
                marksArray.put(mark);
            }
        }
        JSONObject ret = new JSONObject();
        ret.put(stdName.MARKS, marksArray);
        JsonHelper.jsonToResponse(response, ret);
    }
}
