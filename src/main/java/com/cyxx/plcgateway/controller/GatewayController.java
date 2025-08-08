package com.cyxx.plcgateway.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyxx.plcgateway.config.AjaxResult;
import com.cyxx.plcgateway.config.BaseController;
import com.cyxx.plcgateway.model.TableDataInfo;
import com.cyxx.plcgateway.model.entry.IotGatewayEntry;
import com.cyxx.plcgateway.service.IIotGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("gateway")
public class GatewayController extends BaseController {
    @Autowired
    private IIotGatewayService iIotGatewayService;

    @GetMapping("/list")
    public TableDataInfo list(@ModelAttribute IotGatewayEntry iotGatewayEntry)
    {
        try {
            Page<IotGatewayEntry> page = new Page<>(iotGatewayEntry.getPageNum(), iotGatewayEntry.getPageSize());

            QueryWrapper<IotGatewayEntry> wrapper = new QueryWrapper<>();
            if(StringUtils.isNotBlank(iotGatewayEntry.getName())){
                wrapper.like("name",iotGatewayEntry.getName());
            }

            wrapper.orderBy(true,true,"id");

            Page<IotGatewayEntry> resultPage = iIotGatewayService.page(page,wrapper);
            log.info("list:{}", resultPage);
            return getDataTable(resultPage);
        } catch (Exception e) {
            log.error("分页查询异常", e);
            throw e;
        }
    }

    @GetMapping("/all")
    public AjaxResult all()
    {
        return success(iIotGatewayService.list());
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(iIotGatewayService.getById(id));
    }

    @PostMapping ("/add")
    public AjaxResult add(@RequestBody IotGatewayEntry iotGatewayEntry)
    {
        return success(iIotGatewayService.save(iotGatewayEntry));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody IotGatewayEntry iotGatewayEntry)
    {
        return success(iIotGatewayService.updateById(iotGatewayEntry));
    }

    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id)
    {
        return success(iIotGatewayService.removeById(id));
    }
}
