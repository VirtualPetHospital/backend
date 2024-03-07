package cn.vph.cases.controller;

import cn.vph.cases.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cases")
public class CaseController {
    @Autowired
    private CaseService caseService;


}
