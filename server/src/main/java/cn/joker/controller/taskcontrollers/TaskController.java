package cn.joker.controller.taskcontrollers;

import cn.joker.entity.*;
import cn.joker.namespace.StdName;
import cn.joker.sevice.ReportService;
import cn.joker.sevice.TagService;
import cn.joker.sevice.TaskService;
import cn.joker.sevice.UserService;
import cn.joker.util.DateHelper;
import cn.joker.util.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 任务处理类
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private ReportService reportService;
    @Resource
    private UserService userService;
    @Resource
    private TagService tagService;


    /**
     * 发起任务
     *
     * @param request  http
     * @param response http
     * @return 任务是否发布成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/releaseTask")
    public void releaseTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity task = new TaskEntity();
        task.setTaskName(jsonObject.getString(StdName.TASKNAME));
        task.setDescription(jsonObject.getString(StdName.DESCRIPTION));
        String endDate = jsonObject.getString(StdName.ENDDATE);
        java.sql.Date startDate = new java.sql.Date(new Date().getTime());
        task.setStartDate(startDate);
        task.setEndDate(DateHelper.convertStringToDate(endDate));
        task.setType(jsonObject.getInt(StdName.TYPE));
        UserEntity userEntity = userService.findByUsername(jsonObject.getString(StdName.SPONSORNAME));
        task.setSponsor(userEntity);
        task.setActNum(0);
        task.setPolygonNum(5);


        JSONArray tagArray = jsonObject.getJSONArray(StdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setImageNum(0);
        task.setWorkerLevel(jsonObject.getInt(StdName.WORKERLEVEL));
        task.setState(1);
        JSONObject ret = new JSONObject();

        ret.put(StdName.MES, userService.modify(userEntity));
        ret.put(StdName.TASKID, taskService.releaseTask(task));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 修改任务
     *
     * @param request http
     * @param response http
     * @return 任务是否修改
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/modifyTask")
    public void modifyTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity task = (TaskEntity) taskService.findByID(jsonObject.getInt(StdName.TASKID));
        task.setDescription(jsonObject.getString(StdName.DESCRIPTION));
        task.setEndDate(DateHelper.convertStringToDate(jsonObject.getString(StdName.ENDDATE)));
        JSONArray tagArray = jsonObject.getJSONArray(StdName.TAG);
        List<TagEntity> tagEntities = new ArrayList<>();

        addTag(tagArray, tagEntities);
        task.setTagEntityList(tagEntities);
        task.setTaskName(jsonObject.getString(StdName.TASKNAME));
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, taskService.modify(task));
        JsonHelper.jsonToResponse(response, ret);
    }


    /**
     * 查看某个用户的所有任务
     *
     * @param request  http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/myTasks")
    public void checkMyTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String username = jsonObject.getString(StdName.USERNAME);
        String tag = jsonObject.getString(StdName.TAG);
        Integer status = jsonObject.getInt(StdName.STATUS);
        Integer userRole = jsonObject.getInt(StdName.USERROLE);
        JSONObject ret = new JSONObject();

        List<TaskEntity> tasks = taskService.checkMyTask(username, status, userRole, tag);
        JSONArray taskArray = new JSONArray();
        getTasks(tasks, taskArray);
        ret.put(StdName.TASKS, taskArray);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 查看当前所有任务以及搜索任务
     *
     * @param request http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allTasks")
    public void search(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        Integer userRole = jsonObject.getInt(StdName.USERROLE);
        String tag = jsonObject.getString(StdName.TAG);
        Integer status = jsonObject.getInt(StdName.STATUS);
        List<TaskEntity> tasks = taskService.search(userRole, tag, status);
        JSONObject ret = new JSONObject();
        JSONArray taskArray = new JSONArray();
        getTasks(tasks, taskArray);
        ret.put(StdName.TASKS, taskArray);
        JsonHelper.jsonToResponse(response, ret);

    }

    /**
     *
     * @param tasks 任务列表
     * @param taskArray json
     */
    private void getTasks(List<TaskEntity> tasks, JSONArray taskArray) {
        for (TaskEntity task : tasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put(StdName.TASKID, task.getId());
            taskObject.put(StdName.TASKNAME, task.getTaskName());
            taskObject.put(StdName.DESCRIPTION, task.getDescription());
            taskObject.put(StdName.IMGNUM, task.getImageNum());
            taskObject.put(StdName.SPONSORNAME, task.getSponsor().getUsername());
            taskObject.put(StdName.STATUS,task.getState());
            JSONArray tags = new JSONArray();
            List<TagEntity> tagEntities = task.getTagEntityList();
            for (TagEntity tagEntity : tagEntities) {
                tags.put(tagEntity.getTag());
            }
            taskObject.put(StdName.TAG, tags);
            taskObject.put(StdName.STARTDATE, DateHelper.convertDateToString(task.getStartDate()));
            taskObject.put(StdName.ENDDATE, DateHelper.convertDateToString(task.getEndDate()));
            taskArray.put(taskObject);
        }
    }

    /**
     * 结束任务
     *
     * @param request 编号
     * @param response http
     * @return 是否成功结束该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/endTask")
    public void endTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(StdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, taskService.endTask(taskID));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 删除任务
     *
     * @param request id
     * @param response http
     * @return 是否成功删除该任务
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/deleteTask")
    public void deleteTask(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(StdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, taskService.deleteTask(taskID));
        JsonHelper.jsonToResponse(response, ret);
    }


    /**
     * 举报任务
     *
     * @param request http
     * @param response http
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportTask")
    public void reportTask(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(StdName.TASKID));
        UserEntity respondent = userService.findByUsername(jsonObject.getString(StdName.RESPONDENT));
        UserEntity reporter = userService.findByUsername(jsonObject.getString(StdName.REPORTER));
        ReportmessageEntity reportmessageEntity = new ReportmessageEntity();
        JSONObject ret = new JSONObject();
        reportmessageEntity.setReportTime(new java.sql.Date(System.currentTimeMillis()));
        reportmessageEntity.setDescription(jsonObject.getString(StdName.DESCRIPTION));
        reportmessageEntity.setIsDealt((byte) 0);
        reportmessageEntity.setReporter(reporter);
        reportmessageEntity.setRespondent(respondent);
        reportmessageEntity.setTask(taskEntity);
        reportmessageEntity.setType(0);
        ret.put(StdName.MES, reportService.add(reportmessageEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 举报工人
     *
     * @param request http
     * @param response http
     * @return boolean 成功与否
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/reportWorker")
    public void reportWorker(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(jsonObject.getInt(StdName.TASKID));
        UserEntity respondent = userService.findByUsername(jsonObject.getString(StdName.RESPONDENT));
        UserEntity reporter = userService.findByUsername(jsonObject.getString(StdName.REPORTER));
        ReportmessageEntity reportmessageEntity = new ReportmessageEntity();
        JSONObject ret = new JSONObject();
        reportmessageEntity.setReportTime((java.sql.Date) new Date());
        reportmessageEntity.setDescription(jsonObject.getString(StdName.DESCRIPTION));
        reportmessageEntity.setIsDealt((byte) 0);
        reportmessageEntity.setReporter(reporter);
        reportmessageEntity.setRespondent(respondent);
        reportmessageEntity.setTask(taskEntity);
        reportmessageEntity.setType(1);
        ret.put(StdName.MES, reportService.add(reportmessageEntity));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * 管理员查看用户被举报的所有信息
     * return 举报列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkWorkerReport")
    public List<ReportmessageEntity> checkWorkerReport() {
        return reportService.checkWorkerReport();
    }

    /**
     * 管理员查看任务被举报的所有信息
     * return 举报信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskReport")
    public List<ReportmessageEntity> checkTaskReport() {
        return reportService.checkTaskReport();
    }

    /**
     * 所有人员查看任务细节
     * 参数是taskID和人员角色
     * @param request http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.GET, value = "/checkTaskDetail")
    public void checkTaskDetail(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(StdName.TASKID)[0]);
        TaskEntity task = (TaskEntity) taskService.findByID(taskID);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(StdName.TASKID, task.getId());

        jsonObject.put(StdName.SPONSORNAME, task.getSponsor().getUsername());
        jsonObject.put(StdName.TASKNAME, task.getTaskName());
        jsonObject.put(StdName.DESCRIPTION, task.getDescription());
        JSONArray tags = new JSONArray();
        List<TagEntity> tagEntities = task.getTagEntityList();
        for (TagEntity tagEntity : tagEntities) {
            tags.put(tagEntity.getTag());
        }
        jsonObject.put(StdName.TAG, tags);
        jsonObject.put(StdName.LEVEL, task.getWorkerLevel());

        jsonObject.put(StdName.STARTDATE, DateHelper.convertDateToString(task.getStartDate()));
        jsonObject.put(StdName.ENDDATE, DateHelper.convertDateToString(task.getEndDate()));
        jsonObject.put(StdName.IMGNUM, task.getImageNum());

        JSONArray userInfos = new JSONArray();
        List<ImageEntity> imageEntities = task.getImageEntityList();
        Set<UserEntity> userEntities = new HashSet<>();
        for(ImageEntity imageEntity : imageEntities){
            List<UserEntity> users = imageEntity.getWorkers();
            for(UserEntity userEntity : users){
                if(!userEntities.contains(userEntity))
                    userEntities.add(userEntity);
            }
        }
        for(UserEntity userEntity : userEntities){
            JSONObject obj = new JSONObject();
            obj.put(StdName.USERNAME,userEntity.getUsername());
            obj.put(StdName.LEVEL,userEntity.getLev());
            userInfos.put(obj);
        }
        jsonObject.put(StdName.WORKERINFO, userInfos);
        JsonHelper.jsonToResponse(response, jsonObject);
    }

    /**
     * 管理员处理举报信息
     *
     * @param request  http
     * @param response http
     */
    @RequestMapping(method = RequestMethod.POST, value = "/dealReport")
    public void dealReport(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JsonHelper.requestToJson(request);
        String reportTime = jsonObject.getString(StdName.REPORTTIME);
        String description = jsonObject.getString(StdName.DESCRIPTION);
        Integer type = jsonObject.getInt(StdName.TYPE);

        JSONObject ret = new JSONObject();
        ret.put(StdName.MES, reportService.dealReport(reportTime, type, description));
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 查看某用户某次任务的所有图片的url
     * @param request  http
     * @param response http
     * @date: 22:58 2018/4/24
     */
    @RequestMapping(value = "/checkImages", method = RequestMethod.GET)
    public void checkImages(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(StdName.TASKID)[0]);
        JSONObject ret = new JSONObject();
        JSONArray array = new JSONArray();
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
        for (ImageEntity imageEntity : imageEntities) {
            array.put(imageEntity.getUrl());
        }
        ret.put(StdName.IMGURLS, array);
        JsonHelper.jsonToResponse(response, ret);
    }

    /**
     * @author:pis
     * @description: 发起者下载任务标注的数据集
     * @param request  http
     * @param response http
     * @date: 14:30 2018/6/5
     * return 数据集下载
     */
    @RequestMapping(value = "/getDataSet", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getDataSet(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> map = request.getParameterMap();
        Integer taskID = Integer.valueOf(map.get(StdName.TASKID)[0]);
        TaskEntity taskEntity = (TaskEntity) taskService.findByID(taskID);
        return taskService.getDataSet(taskEntity);
    }

    private void addTag(JSONArray tagArray, List<TagEntity> tagEntities) {
        for (Object o : tagArray) {
            String str = (String) o;
            TagEntity tagEntity = tagService.findByTag(str);
            if (tagEntity != null)
                tagEntities.add(tagEntity);
        }
    }
}
