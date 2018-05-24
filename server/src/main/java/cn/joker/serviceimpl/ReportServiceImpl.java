package cn.joker.serviceimpl;

import cn.joker.dao.ReportmessageRepository;
import cn.joker.entity.ReportmessageEntity;
import cn.joker.sevice.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 18:39 2018/5/21
 */
@Service
public class ReportServiceImpl extends PubServiceImpl implements ReportService {
    private final ReportmessageRepository reportmessageRepository;

    @Autowired
    public ReportServiceImpl(ReportmessageRepository reportmessageRepository) {
        this.reportmessageRepository = reportmessageRepository;
        this.repository = reportmessageRepository;
    }

    @Override
    public List<ReportmessageEntity> checkWorkerReport() {
        return reportmessageRepository.findByType(0);
    }

    @Override
    public List<ReportmessageEntity> checkTaskReport() {
        return reportmessageRepository.findByType(1);
    }

    @Override
    public boolean dealReport(String reportTime, Integer type, String description) {
        return false;
    }
}
