package cn.vph.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: vph-backend
 * @description: 提取Controller层公共方法
 * @author: astarforbae
 * @create: 2024-03-18 15:34
 **/
public class BaseController {
        public Map<String, Object> getData(IPage<?> page){
        Map<String, Object> data = new HashMap<>();
        data.put("records", page.getRecords());
        data.put("total", page.getTotal());
        return data;
    }
}
