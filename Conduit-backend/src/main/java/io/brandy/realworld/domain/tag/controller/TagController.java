package io.brandy.realworld.domain.tag.controller;

import io.brandy.realworld.domain.tag.dto.TagDto;
import io.brandy.realworld.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("tags")
@CrossOrigin(origins = "*")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public TagDto.TagList getTagList() {
        return TagDto.TagList.builder().tags(tagService.getTagList()).build();
    }

}
