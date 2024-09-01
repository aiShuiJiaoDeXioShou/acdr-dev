package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import com.yskj.acdr.common.response.GlobalResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
        <#if restControllerStyle>
        <#else>
import org.springframework.stereotype.Controller;
        </#if>
        <#if superControllerClassPackage??>
import ${superControllerClassPackage};
        </#if>

import java.util.List;

/**
 * <p>
 * ${table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "${table.comment}")
        <#if restControllerStyle>
@RestController
        <#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??></#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
        <#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
        <#else>
        <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
        <#else>
public class ${table.controllerName} {
</#if>

        @Autowired
        private ${table.serviceName} service;

        @ApiOperation(value = "${table.comment}分页列表", response = ${entity}.class)
        @PostMapping(value = "/page")
        public  GlobalResponse<${entity}> list(GlobalResponse<${entity}> page) {
            return service.lambdaQuery().page(page);
        }

        @ApiOperation(value = "${table.comment}详情", response = ${entity}.class)
        @GetMapping(value = "/info/{id}")
        public  GlobalResponse<${entity}> info(@Validated({GetMapping.class}) @PathVariable Long id) {
            ${entity} ${entity?uncap_first} = service.getById(id);
            return GlobalResponse.success(${entity?uncap_first});
        }

        @ApiOperation(value = "${table.comment}新增")
        @PostMapping(value = "/add")
        public  GlobalResponse<${entity}> add(@Validated({PostMapping.class, Default.class}) @RequestBody ${entity} param) {
            service.save(param);
            return GlobalResponse.success("${table.comment}新增成功！");
        }

        @ApiOperation(value = "${table.comment}修改")
        @PostMapping(value = "/modify")
        public  GlobalResponse<${entity}> modify(@Validated({PutMapping.class, Default.class}) @RequestBody ${entity} param) {
            service.updateById(param);
            return GlobalResponse.success("${table.comment}修改成功！");
        }

        @ApiOperation(value = "${table.comment}删除(单个条目)")
        @GetMapping(value = "/remove/{id}")
        public  GlobalResponse<${entity}> remove(@Validated({DeleteMapping.class}) @PathVariable Long id) {
            service.removeById(id);
            return GlobalResponse.success("${table.comment}删除(单个条目)！");
        }

        @ApiOperation(value = "${table.comment}删除(多个条目)")
        @PostMapping(value = "/removes")
        public  GlobalResponse<${entity}> removes(@RequestBody List<Long> ids) {
            service.removeBatchByIds(ids);
            return GlobalResponse.success("${table.comment}删除(多个条目)！");
        }
}
</#if>
