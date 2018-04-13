package cn.joker66.serviceImpl;

import cn.joker66.dao.ReportDao;
import cn.joker66.entity.ReportMessage;
import cn.joker66.sevice.ReportService;

import java.util.Date;
import java.util.List;

public class ReportServiceImpl implements ReportService{
    private ReportDao reportDao = new ReportDao();

    @Override
    public boolean reportWorker(ReportMessage reportMessage) {
        return reportDao.reportWorker(reportMessage);
    }

    @Override
    public boolean reportTask(ReportMessage reportMessage) {
        return reportDao.reportTask(reportMessage);
    }

    @Override
    public List<ReportMessage> checkWorkerReport() {
        return reportDao.checkWorkerReport();
    }

    @Override
    public List<ReportMessage> checkTaskReport() {
        return reportDao.checkTaskReport();
    }

    @Override
    public boolean dealReport(String reportTime, Integer type, String description) {
        return reportDao.dealReport(reportTime, type, description);
    }
}
