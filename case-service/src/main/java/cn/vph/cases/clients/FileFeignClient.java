package cn.vph.cases.clients;

import cn.vph.cases.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-17 19:08
 **/
@FeignClient(name = "file-service", configuration = FeignConfig.class)
public interface FileFeignClient {
    @Async
    @DeleteMapping("/api/files")
    void delete(@RequestParam("file_name") String fileName);
}
