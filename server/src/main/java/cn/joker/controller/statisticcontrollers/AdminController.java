package cn.joker.controller.statisticcontrollers;

import cn.joker.entity.SysRoleEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.entity.UserEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.JsonHelper;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private UserService userService;
    @Resource
    private TaskService taskService;

    /**
     *
     * @param response 返回用户数量
     */
    @RequestMapping(value = "/checkUserNum", method = RequestMethod.GET)
    public void checkUserNum(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        int sponsorNum = 0;
        int workerNum = 0;
        int adminNum = 0;
        List<UserEntity> userEntities = userService.findAll();
        for (UserEntity userInfo : userEntities) {
            List<SysRoleEntity> sysRoles = userInfo.getRoleEntityList();
            for (SysRoleEntity sysRole : sysRoles) {
                if (sysRole.getId() == 4)
                    workerNum++;
                else if (sysRole.getId() == 2)
                    adminNum++;
                else if (sysRole.getId() == 3)
                    sponsorNum++;
            }
        }
        ret.put(StdName.SPONSORNUM, sponsorNum);
        ret.put(StdName.WORKERNUM, workerNum);
        ret.put(StdName.ADMINNUM, adminNum);
        JsonHelper.jsonToResponse(response, ret);

    }

    /**
     *
     * @param response 返回任务的数量
     */
    @RequestMapping(value = "/checkTaskNum", method = RequestMethod.GET)
    public void checkTaskNum(HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        int taskNum;
        int producing = 0;
        int finished = 0;
        List<TaskEntity> tasks = taskService.findAll();
        taskNum = tasks.size();
        for (TaskEntity task : tasks) {
            if (task.getState() == 0)
                producing++;
            else
                finished++;
        }
        ret.put(StdName.TASKNUM, taskNum);
        ret.put(StdName.PRODUCING, producing);
        ret.put(StdName.FINISHED, finished);
        JsonHelper.jsonToResponse(response, ret);
    }
}
