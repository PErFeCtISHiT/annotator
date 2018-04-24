package cn.joker.sevice;

import cn.joker.entity.ReportMessage;

import java.util.Date;
import java.util.List;

public interface ReportService {
    boolean reportWorker(ReportMessage reportMessage);

    boolean reportTask(ReportMessage reportMessage);

    List<ReportMessage> checkWorkerReport();

    List<ReportMessage> checkTaskReport();

    boolean dealReport(String reportTime, Integer type, String description);
}
