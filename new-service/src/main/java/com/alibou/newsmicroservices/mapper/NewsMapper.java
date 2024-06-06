package com.alibou.newsmicroservices.mapper;

import com.alibou.newsmicroservices.dto.NewsCreateDto;
import com.alibou.newsmicroservices.dto.NewsDto;
import com.alibou.newsmicroservices.dto.NewsFilter;
import com.alibou.newsmicroservices.entity.News;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsDto mapToDto(News news);

    News mapToEntity(NewsCreateDto newsCreateDto);

    News mapFromFilter(NewsFilter newsFilter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    News mapToUpdate(@MappingTarget News news, NewsCreateDto newsCreateDto);

}
