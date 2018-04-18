package cn.joker.sevice;

import cn.joker.entity.ReportMessage;

import java.util.Date;
import java.util.List;

public interface ReportService {
    public boolean reportWorker(ReportMessage reportMessage);

    public boolean reportTask(ReportMessage reportMessage);

    public List<ReportMessage> checkWorkerReport();

    public List<ReportMessage> checkTaskReport();

    public boolean dealReport(String reportTime, Integer type, String description);
}
