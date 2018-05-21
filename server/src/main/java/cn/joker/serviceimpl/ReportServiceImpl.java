package cn.joker.serviceimpl;

import cn.joker.entity.ReportmessageEntity;
import cn.joker.sevice.ReportService;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 18:39 2018/5/21
 */
public class ReportServiceImpl extends PubServiceImpl implements ReportService{

    @Override
    public List<ReportmessageEntity> checkWorkerReport() {
        return null;
    }

    @Override
    public List<ReportmessageEntity> checkTaskReport() {
        return null;
    }

    @Override
    public boolean dealReport(String reportTime, Integer type, String description) {
        return false;
    }
}
