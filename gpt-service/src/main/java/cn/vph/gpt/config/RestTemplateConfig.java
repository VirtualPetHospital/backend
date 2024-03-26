package cn.vph.gpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-26 13:44
 **/
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate( ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //超时设置
        factory.setReadTimeout(10000);//ms
        factory.setConnectTimeout(15000);//ms
        return factory;
    }
}
