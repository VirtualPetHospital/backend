package cn.vph.files.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-31 19:23
 **/

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${vph.file.path}")
    private String filePath;

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:" + filePath);
    }
}
