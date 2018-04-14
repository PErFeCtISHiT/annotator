package cn.joker.controller;

import cn.joker.entity.ReportMessage;
import cn.joker.entity.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/task")
public class TaskController {
    /**
     * 发起任务
     * @param task
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/releaseTask")
    public void releaseTask(Task task, HttpServletRequest response) {
        //TODO
        return;
    }

    /**
     * 修改任务
     * @param task
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modifyTask")
    public void modifyTask(Task task, HttpServletRequest response) {
        //TODO
        return;
    }

    /**
     * 查看某个用户的所有任务
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/myTask")
    public void checkMyTask(HttpServletRequest request, HttpServletRequest response) {
        //TODO

    }

    /**
     * 查看当前所有任务以及搜索任务
     * @param request
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public void search(HttpServletRequest request, HttpServletRequest response){
        //TODO
    }

    /**
     * 结束任务
     * @param taskID
     * @return 是否成功结束该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/endTask")
    public void endTask(String taskID, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 删除任务
     * @param taskID
     * @return 是否成功删除该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTask")
    public void deleteTask(String taskID, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 完成任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/completeTask")
    public void completeTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 放弃任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/abortTask")
    public void abortTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 接受任务
     * @param taskID 任务ID
     * @param workerName 工人用户名
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/acceptTask")
    public void acceptTask(Integer taskID, String workerName, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 举报任务
     * @param reportMessage 举报信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportTask")
    public void reportTask(ReportMessage reportMessage, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 举报工人
     * @param reportMessage 举报信息
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportWorker")
    public void reportWorker(ReportMessage reportMessage, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员查看用户被举报的所有信息
     * @param response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkWorkerReport")
    public void checkWorkerReport(HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员查看任务被举报的所有信息
     * @param response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskReport")
    public void checkTaskReport(HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 所有人员查看任务细节
     * 参数是taskID和人员角色
     * @param response
     */
    @RequestMapping(method = RequestMethod.POST, value = "/checkTaskDetail")
    public void checkTaskDetail(HttpServletRequest request, HttpServletRequest response){
        //TODO
        return;
    }

    /**
     * 管理员处理举报信息
     * @param request
     * @param response
     */
    public void dealReport(HttpServletRequest request, HttpServletRequest response){
        //todo
    }

}
