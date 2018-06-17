package cn.joker.controller.taskcontrollers;

import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.ImgService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.FileHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:57 2018/4/15
 */
@Controller
@RequestMapping("/task")
public class ImgUploadController {
    @Resource
    private TaskService taskService;
    @Resource
    private ImgService imgService;
    @Resource
    private UserService userService;


    /**
     * @author:pis
     * @param file 请求文件名
     * @param response 返回内容
     * @description: 上传图片
     * @date: 15:57 2018/4/17
     */
    @RequestMapping(value = "/imagesUpload", method = RequestMethod.POST)
    public void imagesUpload(@RequestParam(StdName.FILE) MultipartFile file, @RequestParam(StdName.TASKID) Integer taskID, HttpServletResponse response) {
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        JSONObject ret = new JSONObject();
        if (taskEntity == null) {
            ret.put(StdName.MES, StdName.NULL);
        } else {
            UserEntity userEntity = taskEntity.getSponsor();
            taskEntity.setImageNum(FileHelper.saveFiles(taskEntity, file, imgService));
            userEntity.setPoints((int) (userEntity.getPoints() - taskEntity.getImageNum() * 1.5));
            if (userEntity.getPoints() < 0) {
                ret.put(StdName.MES, !taskService.delete(taskEntity));
            } else
                ret.put(StdName.MES, taskService.modify(taskEntity) && userService.modify(userEntity));
        }
        JsonHelper.jsonToResponse(response, ret);
    }
}
