package me.zhengjie.uma_mes.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.uma_mes.domain.BookAccountType;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookAccountTypeMapper extends BaseMapper<BookAccountTypeDTO, BookAccountType> {

}