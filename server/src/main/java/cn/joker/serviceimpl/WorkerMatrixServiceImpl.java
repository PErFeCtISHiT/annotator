package cn.joker.serviceimpl;

import cn.joker.dao.WorkerMatrixRepository;
import cn.joker.sevice.WorkerMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 18:08 2018/5/28
 */
@Service
public class WorkerMatrixServiceImpl extends PubServiceImpl implements WorkerMatrixService {

    @Autowired
    public WorkerMatrixServiceImpl(WorkerMatrixRepository workerMatrixRepository) {
        this.repository = workerMatrixRepository;
    }
}
