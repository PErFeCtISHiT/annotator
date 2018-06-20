package cn.joker.sevice;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 2:37 2018/5/18
 */
public interface PubService {
    Object findByID(Integer id);

    boolean add(Object o);

    boolean modify(Object o);


    boolean delete(Object o);

    Integer count();

    boolean has(Integer integer);
}
