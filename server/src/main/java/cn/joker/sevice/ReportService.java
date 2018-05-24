package cn.joker.sevice;

import cn.joker.entity.ReportmessageEntity;

import java.util.List;

public interface ReportService extends PubService {


    List<ReportmessageEntity> checkWorkerReport();

    List<ReportmessageEntity> checkTaskReport();

    boolean dealReport(String reportTime, Integer type, String description);
}
