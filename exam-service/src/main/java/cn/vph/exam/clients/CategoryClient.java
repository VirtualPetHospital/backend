package cn.vph.exam.clients;

import cn.vph.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Caroline
 * @description 请求病种数据
 * @create 2024/3/17 23:55
 */

@FeignClient(name = "case-service")
public interface CategoryClient {

    @GetMapping("/api/categories/keyword-ids")
    List<Integer> getCategoryIds(@RequestParam(value = "category_keyword") String keyword);
}
