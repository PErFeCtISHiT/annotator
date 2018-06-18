package cn.joker.controller;

import cn.joker.entity.PaySaPi;
import cn.joker.entity.UserEntity;
import cn.joker.sevice.UserService;
import cn.joker.util.JsonHelper;
import cn.joker.util.PayUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付的controller
 */
@Controller
//@CrossOrigin
public class PayController {
    private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);

    @Resource
    private UserService userService;

    /**
     *
     * @param request http
     * @return 支付接口相关内容
     */
    @RequestMapping("/pays/pay")
    @ResponseBody
    public Map<String, Object> pay(HttpServletRequest request) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> remoteMap = new HashMap<>();

        remoteMap.put("price", jsonObject.getString("price"));
        remoteMap.put("istype", jsonObject.getInt("istype"));
        remoteMap.put("orderid", PayUtil.getOrderIdByUUId());
        remoteMap.put("orderuid", jsonObject.getString("orderuid"));
        remoteMap.put("goodsname", "recharge");

        resultMap.put("data", PayUtil.payOrder(remoteMap));
        return resultMap;
    }

    /**
     *
     * @param request 请求
     * @param response 回复
     * @param paySaPi 类
     */
    @RequestMapping("/pays/notifyPay")
    @CrossOrigin
    public void notifyPay(HttpServletRequest request, HttpServletResponse response, PaySaPi paySaPi) {
        logger.info("一样");
        // 保证密钥一致性
        if (PayUtil.checkPayKey(paySaPi)) {
            logger.info("一样");
            System.out.println("一样");
            UserEntity userEntity = userService.findByUsername(paySaPi.getOrderuid());
            userEntity.setPoints(userEntity.getPoints() + (int)(Double.parseDouble(paySaPi.getPrice())*100));
            userService.modify(userEntity);
        } else {
            System.out.println("不一样");
            logger.info("不一样");
        }
    }

    /**
     *
     * @param request 请求
     * @param response 回复
     * @param orderid 订单号
     * @return
     */
    @RequestMapping("/pays/returnPay")
    @CrossOrigin
    @ResponseBody
    public void returnPay(HttpServletRequest request, HttpServletResponse response, String orderid) throws IOException {
        boolean isTrue = true;
        //ModelAndView view = null;
        new ModelAndView("3-1");
        // 根据订单号查找相应的记录:根据结果跳转到不同的页面
//        if (isTrue) {
//
//        } else {
//
//        }
        response.sendRedirect("http://localhost:8080/#/3-1");
        //return new ModelAndView("3-1");
    }
}
