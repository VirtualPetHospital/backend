package cn.vph.cases.controller;

import cn.vph.cases.entity.Medcase;
import cn.vph.cases.service.MedcaseService;
import cn.vph.common.Result;
import cn.vph.common.annotation.Administrator;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("medcases")
@Api(value = "MedcaseController", tags = {"病例服务接口"})
public class MedcaseController {
    @Autowired
    private MedcaseService medcaseService;

    @Administrator
    @PostMapping
    public Result<?> add(@RequestBody Medcase medcase) {
        return Result.success(medcaseService.add(medcase));
    }


}
