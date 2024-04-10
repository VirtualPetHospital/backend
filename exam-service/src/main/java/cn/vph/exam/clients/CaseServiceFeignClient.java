package cn.vph.exam.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Caroline
 * @description 请求病种数据
 * @create 2024/3/17 23:55
 */

@FeignClient(name = "case-service")
public interface CaseServiceFeignClient {

    @GetMapping("/api/categories/keyword-ids")
    List<Integer> getCategoryIds(@RequestParam(value = "category_keyword", required = false) String keyword);

    @PostMapping("/api/users/upgrade")
    void upgrade(@RequestParam(value = "num_current_level") Integer numCurrentLevel, @RequestParam(value = "user_id") Integer userId, @RequestParam(value = "session_id") String sessionId);

    @GetMapping("/api/users/nickname/list/{values}")
    List<String> getNicknamesByIds(@PathVariable List<Integer> values);
}
