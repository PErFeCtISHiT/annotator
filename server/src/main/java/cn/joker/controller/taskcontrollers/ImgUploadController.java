package cn.joker.controller.taskcontrollers;

import cn.joker.entity.TaskEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.TaskService;
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

    /**
     * @author:pis
     * @description: 上传文件（zip)
     * @date: 10:38 2018/4/17
     */
    @RequestMapping(value = "/zipFileUpload", method = RequestMethod.POST)
    public void zipFileUpload(HttpServletRequest request, HttpServletResponse response) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(stdName.FILENAME);
        String taskID = request.getParameter(stdName.TASKID);
        JSONObject ret = new JSONObject();
        ret.put(stdName.MES, FileHelper.saveZip(taskID, file));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 上传多个图片
     * @date: 15:57 2018/4/17
     */
    @RequestMapping(value = "/imagesUpload", method = RequestMethod.POST)
    public void imagesUpload(@RequestParam(stdName.FILE) MultipartFile file, @RequestParam(stdName.TASKID) Integer taskID, HttpServletResponse response) {
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        JSONObject ret = new JSONObject();
        if (taskEntity == null) {
            ret.put(stdName.MES, stdName.NULL);
        } else {
            taskEntity.setImageNum(FileHelper.saveFiles(taskEntity, file));
            System.out.println(taskEntity.getImageEntityList().get(0).getImgName());
            ret.put(stdName.MES, taskService.modify(taskEntity));
        }
        JsonHelper.jsonToResponse(response, ret);
    }
}
