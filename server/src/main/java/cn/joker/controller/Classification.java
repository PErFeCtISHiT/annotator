package cn.joker.controller;

import cn.joker.namespace.stdName;
import cn.joker.statisticalMethod.NaiveBayesianClassification;
import cn.joker.util.JsonHelper;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/test")
public class Classification {
    @Resource
    NaiveBayesianClassification naiveBayesianClassification;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public void get(HttpServletResponse response) {
        naiveBayesianClassification.getAllRecNode();
        JSONObject msg = new JSONObject();
        msg.put(stdName.MES, "success");
        JsonHelper.jsonToResponse(response, msg);
    }
}
