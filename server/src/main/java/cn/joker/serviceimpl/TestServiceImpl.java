package cn.joker.serviceimpl;

import cn.joker.dao.TestRepository;
import cn.joker.entity.TestEntity;
import cn.joker.sevice.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 3:25 2018/6/16
 */
@Service
public class TestServiceImpl extends PubServiceImpl implements TestService{
    private final TestRepository testRepository;

    @Autowired
    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
        this.repository = testRepository;
    }

    @Override
    public List<TestEntity> findAll() {
        return testRepository.findAll();
    }
}
