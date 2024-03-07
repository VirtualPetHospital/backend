package cn.vph.cases.clients;


import org.springframework.cloud.openfeign.FeignClient;

/**
 * 发送http请求对方
 */
@FeignClient("login")
public interface UserClient {
}
