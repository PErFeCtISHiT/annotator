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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * @description: 上传文件（zip)
     * @date: 10:38 2018/4/17
     */
    @RequestMapping(value = "/zipFileUpload", method = RequestMethod.POST)
    public void zipFileUpload(HttpServletRequest request, HttpServletResponse response) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(StdName.FILENAME);
        String taskID = request.getParameter(StdName.TASKID);
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, FileHelper.saveZip(taskID, file));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 上传多个图片
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
            userEntity.setPoints(userEntity.getPoints() - taskEntity.getImageNum());
            if (userEntity.getPoints() < 0) {
                ret.put(StdName.MES, !taskService.delete(taskEntity));
            } else
                ret.put(StdName.MES, taskService.modify(taskEntity) && userService.modify(userEntity));
        }
        JsonHelper.jsonToResponse(response, ret);
    }
}
