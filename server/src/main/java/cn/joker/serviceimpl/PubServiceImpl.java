package cn.joker.serviceimpl;

import cn.joker.namespace.stdName;
import cn.joker.sevice.PubService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 2:40 2018/5/18
 */
@Service
public abstract class PubServiceImpl implements PubService{
    JpaRepository repository;
    @Override
    public Object findByID(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public boolean add(Object o) {
        try {
            repository.save(o);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean modify(Object o) {
        try {
            repository.save(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Object> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(Object o) {
        try {
            repository.delete(o);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Integer count() {
        return Math.toIntExact(repository.count());
    }

    @Override
    public boolean has(Integer integer){
        return repository.exists(integer);
    }
}
