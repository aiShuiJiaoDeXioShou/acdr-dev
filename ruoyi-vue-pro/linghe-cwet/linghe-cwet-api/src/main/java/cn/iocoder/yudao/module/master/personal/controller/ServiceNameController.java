package cn.iocoder.yudao.module.master.personal.controller;

import cn.iocoder.yudao.module.master.personal.entity.ServiceName;
import cn.iocoder.yudao.module.master.personal.service.ServiceNameService;
import cn.iocoder.yudao.module.common.response.GlobalResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 林河
 * @since 2024-08-25
 */
@Api(tags = "")
@RestController
@RequestMapping("/pet/service-name")
public class ServiceNameController {

        @Autowired
        private ServiceNameService service;

        @ApiOperation(value = "分页列表", response = ServiceName.class)
        @PostMapping(value = "/page")
        public GlobalResponse<ServiceName> list(GlobalResponse<ServiceName> page) {
            return service.lambdaQuery().page(page);
        }

        @ApiOperation(value = "详情", response = ServiceName.class)
        @GetMapping(value = "/info/{id}")
        public  GlobalResponse<ServiceName> info(@Validated({GetMapping.class}) @PathVariable Long id) {
            ServiceName serviceName = service.getById(id);
            return GlobalResponse.success(serviceName);
        }

        @ApiOperation(value = "新增")
        @PostMapping(value = "/add")
        public  GlobalResponse<ServiceName> add(@Validated({PostMapping.class, Default.class}) @RequestBody ServiceName param) {
            service.save(param);
            return GlobalResponse.success("新增成功！");
        }

        @ApiOperation(value = "修改")
        @PostMapping(value = "/modify")
        public  GlobalResponse<ServiceName> modify(@Validated({PutMapping.class, Default.class}) @RequestBody ServiceName param) {
            service.updateById(param);
            return GlobalResponse.success("修改成功！");
        }

        @ApiOperation(value = "删除(单个条目)")
        @GetMapping(value = "/remove/{id}")
        public  GlobalResponse<ServiceName> remove(@Validated({DeleteMapping.class}) @PathVariable Long id) {
            service.removeById(id);
            return GlobalResponse.success("删除(单个条目)！");
        }

        @ApiOperation(value = "删除(多个条目)")
        @PostMapping(value = "/removes")
        public  GlobalResponse<ServiceName> removes(@RequestBody List<Long> ids) {
            service.removeBatchByIds(ids);
            return GlobalResponse.success("删除(多个条目)！");
        }
}
