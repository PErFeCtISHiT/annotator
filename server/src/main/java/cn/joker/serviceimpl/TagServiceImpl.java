package cn.joker.serviceimpl;

import cn.joker.dao.TagRepository;
import cn.joker.entity.TagEntity;
import cn.joker.sevice.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 21:08 2018/5/20
 */
@Service
public class TagServiceImpl extends PubServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.repository = tagRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public TagEntity findByTag(String tag) {
        return tagRepository.findByTag(tag);
    }
}
