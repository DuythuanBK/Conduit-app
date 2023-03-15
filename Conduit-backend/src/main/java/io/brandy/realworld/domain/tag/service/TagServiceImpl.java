package io.brandy.realworld.domain.tag.service;

import io.brandy.realworld.domain.tag.dto.TagDto;
import io.brandy.realworld.domain.tag.entity.TagArticleRelationEntity;
import io.brandy.realworld.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    @Override
    public List<String> getTagList() {
        return tagRepository.findAll().stream().map(TagArticleRelationEntity::getTag).distinct().collect(Collectors.toList());
    }
}
