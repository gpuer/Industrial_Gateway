package com.cyxx.plcgateway.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyxx.plcgateway.config.AjaxResult;
import com.cyxx.plcgateway.config.BaseController;
import com.cyxx.plcgateway.model.TableDataInfo;
import com.cyxx.plcgateway.model.entry.IotTemplateEntry;
import com.cyxx.plcgateway.service.IIotTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("template")
public class TemplateController extends BaseController {
    @Autowired
    private IIotTemplateService iIotTemplateService;

    @GetMapping("/list")
    public TableDataInfo list(@ModelAttribute IotTemplateEntry iotTemplateEntry)
    {
        Page<IotTemplateEntry> page = new Page<>(iotTemplateEntry.getPageNum(), iotTemplateEntry.getPageSize());
        QueryWrapper<IotTemplateEntry> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(iotTemplateEntry.getFieldName())){
            wrapper.like("field_name",iotTemplateEntry.getFieldName());
        }
        if(StringUtils.isNotBlank(iotTemplateEntry.getTemplateName())){
            wrapper.like("template_name",iotTemplateEntry.getTemplateName());
        }
        wrapper.orderBy(true,true,"id");
        Page<IotTemplateEntry> resultPage = iIotTemplateService.page(page,wrapper);
        return getDataTable(resultPage);
    }

    @GetMapping("groupList")
    public AjaxResult groupList(){
        QueryWrapper<IotTemplateEntry> wrapper = new QueryWrapper<>();
        wrapper.groupBy("template_name");
        wrapper.select("template_name");
        return success(iIotTemplateService.list(wrapper));
    }



    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(iIotTemplateService.getById(id));
    }

    @PostMapping ("/add")
    public AjaxResult add(@RequestBody IotTemplateEntry iotTemplateEntry)
    {
      return success(iIotTemplateService.save(iotTemplateEntry));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody IotTemplateEntry iotTemplateEntry)
    {
       return success(iIotTemplateService.updateById(iotTemplateEntry));
    }

    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id)
    {
       return success(iIotTemplateService.removeById(id));
    }
}
