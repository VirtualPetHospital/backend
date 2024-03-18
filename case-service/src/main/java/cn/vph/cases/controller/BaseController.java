package cn.vph.cases.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Caroline
 * @description Controller层公共方法提取
 * @create 2024/3/17 12:12
 */
public class BaseController {
    public Map<String, Object> getData(IPage<?> page){
        Map<String, Object> data = new HashMap<>();
        data.put("records", page.getRecords());
        data.put("total", page.getTotal());
        return data;
    }
}
