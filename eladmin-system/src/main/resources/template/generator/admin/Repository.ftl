package ${package}.repository;

import ${package}.domain.${className};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author ${author}
* @date ${date}
*/
public interface ${className}Repository extends JpaRepository<${className}, ${pkColumnType?default('Integer')}>, JpaSpecificationExecutor<${className}> {
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>

    ${className} findBy${column.capitalColumnName}(${column.columnType} ${column.columnName});
        </#if>
    </#list>
</#if>
}