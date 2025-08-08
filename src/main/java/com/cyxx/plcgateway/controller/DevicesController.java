package com.cyxx.plcgateway.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyxx.plcgateway.config.AjaxResult;
import com.cyxx.plcgateway.config.BaseController;
import com.cyxx.plcgateway.model.TableDataInfo;
import com.cyxx.plcgateway.model.entry.IotDeviceEntry;
import com.cyxx.plcgateway.service.IIotDevicesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("devices")
public class DevicesController extends BaseController {
    @Autowired
    private IIotDevicesService iIotDevicesService;

    @GetMapping("/list")
    public TableDataInfo list(@ModelAttribute IotDeviceEntry iotDeviceEntry)
    {
        try {
            Page<IotDeviceEntry> page = new Page<>(iotDeviceEntry.getPageNum(), iotDeviceEntry.getPageSize());
            QueryWrapper<IotDeviceEntry> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(iotDeviceEntry.getDeviceName())) {
                wrapper.like("device_name", iotDeviceEntry.getDeviceName());
            }

            if(StringUtils.isNotBlank(iotDeviceEntry.getGatewayName())){
                wrapper.like("gateway_name",iotDeviceEntry.getGatewayName());
            }
            wrapper.orderBy(true,true,"id");

            Page<IotDeviceEntry> resultPage = iIotDevicesService.page(page,wrapper);
            log.info("list:{}", resultPage);
            return getDataTable(resultPage);
        } catch (Exception e) {
            log.error("分页查询异常", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(iIotDevicesService.getById(id));
    }

    @PostMapping ("/add")
    public AjaxResult add(@RequestBody IotDeviceEntry iotDeviceEntry)
    {
        return success(iIotDevicesService.save(iotDeviceEntry));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody IotDeviceEntry iotDeviceEntry)
    {
        return success(iIotDevicesService.updateById(iotDeviceEntry));
    }

    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id)
    {
        return success(iIotDevicesService.removeById(id));
    }
}
