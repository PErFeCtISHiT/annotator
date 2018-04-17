package cn.joker.controller;

import cn.joker.util.FileHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:57 2018/4/15
 */
@Controller
@RequestMapping("/task")
public class ImgUploadController {
    /**
    *@author:pis
    *@description: 上传文件（zip)
    *@date: 10:38 2018/4/17
    */
    @RequestMapping(value = "/zipFileUpload",method = RequestMethod.POST)
    public void zipFileUpload(HttpServletRequest request,HttpServletResponse response){
        MultipartFile file = ((MultipartHttpServletRequest)request).getFile("fileName");
        String taskID = request.getParameter("taskID");
        JSONObject ret = new JSONObject();
        ret.put("mes", FileHelper.saveZip(taskID,file));
        JsonHelper.jsonToResponse(response,ret);
    }
    /**
    *@author:pis
    *@description: 上传多个图片
    *@date: 15:57 2018/4/17
    */
    @RequestMapping(value = "/imagesUpload",method = RequestMethod.POST)
    public void imagesUpload(HttpServletRequest request,HttpServletResponse response){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");
        String taskID = request.getParameter("taskID");
        JSONObject ret = new JSONObject();
        ret.put("mes",FileHelper.saveFiles(taskID,files));
        JsonHelper.jsonToResponse(response,ret);
    }
}
