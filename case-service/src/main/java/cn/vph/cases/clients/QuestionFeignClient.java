package cn.vph.cases.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-01 14:53
 **/
@FeignClient(name = "exam-service")
public interface QuestionFeignClient {

    @GetMapping("/api/questions/questions-count/{category_id}")
    Long getQuestionCountByCategoryId(@PathVariable("category_id") Integer categoryId);

}
