package com.blisgo.domain.mapper;

import com.blisgo.domain.entity.Guide;
import com.blisgo.domain.mapper.cmmn.GenericMapper;
import com.blisgo.web.dto.GuideDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuideMapper extends GenericMapper<GuideDTO, Guide> {
    GuideMapper INSTANCE = Mappers.getMapper(GuideMapper.class);
}
