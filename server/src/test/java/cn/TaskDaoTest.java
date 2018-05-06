package cn;

import cn.joker.dao.TaskDao;
import cn.joker.entity.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TaskDaoTest {
    private Task task = new Task();

    @Before
    public void setTask(){
        task.setTaskID(0);
        task.setTaskName("嘿嘿嘿");
        task.setDescription("描述");
        task.setTag(new String[]{"书", "学习"});
        task.setPoints(123);
        task.setExpectedNumber(3000);
        task.setEndDate(new Date());
        task.setWorkerLevel(3);
        task.setSponsorName("keith");
        task.setImageNum(13);

    }

    @Test
    public void releaseTask() {
        assertEquals(true, new TaskDao().releaseTask(task));
    }

    @Test
    public void modifyTask() {
        assertEquals(true, new TaskDao().modifyTask(task));
    }

    @Test
    public void checkMyTask() {
        System.out.println(new TaskDao().checkMyTask(null, 0, 1, null));
    }

    @Test
    public void search() {
        assertEquals(3, new TaskDao().search(1, null, 0).size());
    }

    @Test
    public void endTask() {
        assertEquals(true, new TaskDao().endTask(1));
    }

    @Test
    public void deleteTask() {
        assertEquals(true, new TaskDao().deleteTask(1));
    }

    @Test
    public void completeTask() {
        assertEquals(true, new TaskDao().completeTask(2, "worker2"));
    }

    @Test
    public void abortTask() {
        assertEquals(true, new TaskDao().abortTask(2, "worker2"));
    }

    @Test
    public void acceptTask() {
        assertEquals(true, new TaskDao().acceptTask(2, "amy"));
    }

    @Test
    public void checkTaskDetail() {

    }

    @Test
    public void checkTaskProgress() {
        System.out.println(new TaskDao().checkTaskProgress(2, "worker1"));
    }
}