package cn;

import cn.joker.dao.ReportDao;
import cn.joker.entity.ReportMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportDaoTest {

    static ReportMessage reportMessage = new ReportMessage();

    @Before
    public void initialize(){
        reportMessage.setDescription("任务量太大");
        reportMessage.setReporter("worker1");
        reportMessage.setReportTime("2018-02-03 23:03:12");
        reportMessage.setTaskID(0);
        reportMessage.setRespondent("amy");
        reportMessage.setTaskName("aaa");
        reportMessage.setType(1);
    }

    @Test
    public void reportWorker() {
        assertEquals(true, new ReportDao().reportWorker(reportMessage));
    }

    @Test
    public void reportTask() {
        assertEquals(true, new ReportDao().reportTask(reportMessage));
    }

    @Test
    public void checkWorkerReport() {
        assertEquals(2, new ReportDao().checkWorkerReport().size());
    }

    @Test
    public void checkTaskReport() {
        assertEquals(3, new ReportDao().checkTaskReport().size());
    }

    @Test
    public void dealReport() {
        assertEquals(true, new ReportDao().dealReport("2018-09-03 23:34:21", 1,"通过"));
    }
}