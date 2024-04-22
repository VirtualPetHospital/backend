package cn.vph.files.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-31 15:58
 **/
@Component
public class UploadConfig {
    @Bean(name="multipartResolver")
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
