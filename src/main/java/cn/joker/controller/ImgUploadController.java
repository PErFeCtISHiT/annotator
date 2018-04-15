package cn.joker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 15:57 2018/4/15
 */
@Controller
@RequestMapping("task")
public class ImgUploadController {
    @RequestMapping(value = "uploadImg",method = RequestMethod.POST)
    public void uploadImg(HttpServletRequest request, HttpServletResponse response){

    }
}
