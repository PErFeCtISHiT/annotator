package cn.joker.controller;

import cn.joker.entity.PaySaPi;
import cn.joker.util.JsonHelper;
import cn.joker.util.PayUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/pays")
//@CrossOrigin
public class PayController {

    @RequestMapping("/pay")
    @ResponseBody
    public Map<String, Object> pay(HttpServletRequest request) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> remoteMap = new HashMap<>();

        remoteMap.put("price", jsonObject.getDouble("price"));
        remoteMap.put("istype", jsonObject.getInt("istype"));
        remoteMap.put("orderid", PayUtil.getOrderIdByUUId());
        remoteMap.put("orderuid", new Date().toString());
        remoteMap.put("goodsname", "hhh");

        resultMap.put("data", PayUtil.payOrder(remoteMap));
        return resultMap;
    }

    @RequestMapping("/notifyPay")
    @CrossOrigin
    public void notifyPay(HttpServletRequest request, HttpServletResponse response, PaySaPi paySaPi) {
        // 保证密钥一致性
        if (PayUtil.checkPayKey(paySaPi)) {
            // TODO 做自己想做的
        } else {
            // TODO 该怎么做就怎么做
        }
    }

    @RequestMapping("/returnPay")
    @CrossOrigin
    public ModelAndView returnPay(HttpServletRequest request, HttpServletResponse response, String orderid) {
        boolean isTrue = false;
        ModelAndView view = null;
        // 根据订单号查找相应的记录:根据结果跳转到不同的页面
        if (isTrue) {
            view = new ModelAndView("/正确的跳转地址");
        } else {
            view = new ModelAndView("/没有支付成功的地址");
        }
        return view;
    }
}
