package cn.joker.controller.testcontrollers;

import cn.joker.entity.TestEntity;
import cn.joker.entity.UserEntity;
import cn.joker.entity.WorkerMatrixEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.TestService;
import cn.joker.sevice.UserService;
import cn.joker.sevice.WorkerMatrixService;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 3:26 2018/6/16
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService testService;
    @Resource
    private UserService userService;


    /**
     * @param response 返回内容
     * @author:pis
     * @description: 请求测试
     * @date: 3:48 2018/6/16
     */
    @RequestMapping(value = "/getTests", method = RequestMethod.GET)
    public void getTests(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        JSONArray data = new JSONArray();
        List<TestEntity> testEntities = testService.findAll();
        Random r = new Random();
        Integer rand = r.nextInt(testEntities.size() / 10) + 1;
        for (int i = 1; i <= 10; i++) {
            JSONObject testObj = new JSONObject(testEntities.get(i * rand - 1));
            JSONArray jsonArray = new JSONArray(testEntities.get(i * rand - 1).getChoices());
            testObj.put(StdName.CHOICES, jsonArray);
            data.put(testObj);

        }
        ret.put(StdName.DATA, data);
        JsonHelper.jsonToResponse(response, ret);
    }


    /**
     * @param request  请求内容
     * @param response 返回内容
     * @author:pis
     * @description: 返回测试结果
     * @date: 3:48 2018/6/16
     */
    @RequestMapping(value = "/postResult", method = RequestMethod.POST)
    public void postResult(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(StdName.USERNAME));
        List<WorkerMatrixEntity> workerMatrixEntities = userEntity.getWorkerMatrixEntities();
        Double rate = jsonObject.getDouble(StdName.RATE);
        Integer num = jsonObject.getInt(StdName.NUM);
        boolean b = true;
        for (WorkerMatrixEntity workerMatrixEntity : workerMatrixEntities) {
            workerMatrixEntity.setC00(workerMatrixEntity.getC00() + rate * num / 2);
            workerMatrixEntity.setC11(workerMatrixEntity.getC11() + rate * num / 2);
            workerMatrixEntity.setC10(workerMatrixEntity.getC10() + (1 - rate) * num / 2);
            workerMatrixEntity.setC01(workerMatrixEntity.getC01() + (1 - rate) * num / 2);
            if (workerMatrixEntity.getCorrect() < 0.7)
                b = false;
        }
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, userService.modify(userEntity) && b);
        JsonHelper.jsonToResponse(response, ret);
    }
}
