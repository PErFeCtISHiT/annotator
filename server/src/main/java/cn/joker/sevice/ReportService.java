package cn.joker.sevice;

import cn.joker.entity.ReportmessageEntity;

import java.util.List;

public interface ReportService {
    boolean reportWorker(ReportmessageEntity reportMessage);

    boolean reportTask(ReportmessageEntity reportMessage);

    List<ReportmessageEntity> checkWorkerReport();

    List<ReportmessageEntity> checkTaskReport();

    boolean dealReport(String reportTime, Integer type, String description);
}
