package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.SysRole;
import cn.joker.entity.Task;
import cn.joker.entity.UserInfo;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserInfoService;
import cn.joker.util.JsonHelper;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:47 2018/4/17
 */
@RequestMapping("/statistic")
@Controller
public class AdminController {
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private TaskService taskService;

    @RequestMapping(value = "/checkUserNum", method = RequestMethod.GET)
    public void checkUserNum(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        int sponsorNum = 0;
        int workerNum = 0;
        int adminNum = 0;
        List<UserInfo> userInfos = userInfoService.findAllUser();
        for (UserInfo userInfo : userInfos) {
            List<SysRole> sysRoles = userInfo.getRoleList();
            for (SysRole sysRole : sysRoles) {
                if (sysRole.getSrid() == 3)
                    workerNum++;
                else if (sysRole.getSrid() == 1)
                    adminNum++;
                else if (sysRole.getSrid() == 2)
                    sponsorNum++;
            }
        }
        ret.put("sponsorNum", sponsorNum);
        ret.put("workerNum", workerNum);
        ret.put("adminNum", adminNum);
        JsonHelper.jsonToResponse(response, ret);

    }

    @RequestMapping(value = "/checkTaskNum", method = RequestMethod.GET)
    public void checkTaskNum(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        int taskNum ;
        int producing = 0;
        int finished = 0;
        List<Task> tasks = taskService.search(1, null, 0);
        taskNum = tasks.size();
        for (Task task : tasks) {
            Date now = new Date();
            Date taskDate = task.getEndDate();
            if (now.compareTo(taskDate) > 0) {
                finished++;
            } else
                producing++;
        }
        ret.put("taskNum", taskNum);
        ret.put("producing", producing);
        ret.put("finished", finished);
        JsonHelper.jsonToResponse(response, ret);
    }
}
