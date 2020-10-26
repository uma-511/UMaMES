package me.zhengjie.uma_mes.service.mapper;


import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.ErrorText;
import me.zhengjie.uma_mes.service.dto.ErrorTextDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErrorTextMapper  extends BaseMapper<ErrorTextDTO, ErrorText> {
}
