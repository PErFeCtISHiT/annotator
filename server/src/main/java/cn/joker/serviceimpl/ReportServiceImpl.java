package cn.joker.serviceimpl;

import cn.joker.dao.ReportDao;
import cn.joker.entity.ReportMessage;
import cn.joker.sevice.ReportService;

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
