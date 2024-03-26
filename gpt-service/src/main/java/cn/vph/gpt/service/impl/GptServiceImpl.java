package cn.vph.gpt.service.impl;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import cn.vph.gpt.entity.FastGptDTO;
import cn.vph.gpt.entity.Message;
import cn.vph.gpt.service.GptService;
import cn.vph.gpt.util.SessionUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-26 13:29
 **/
@Service
public class GptServiceImpl implements GptService {
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${fastgpt.key}")
    private String FASTGPT_KEY;

    @Override
    public Object sendMsg(String msg) {
        Integer userId = sessionUtil.getUserId();
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().role("user").content(msg).build());
        FastGptDTO fastGptDTO = FastGptDTO.builder()
                .chatId(userId.toString())
                .stream(false)
                .detail(false)
                .messages(messages)
                .build();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("https://api.fastgpt.in/api/v1/chat/completions").build(true);
        URI uri = uriComponents.toUri();
        RequestEntity<FastGptDTO> requestEntity = RequestEntity.post(uri).
                header(("Authorization"), FASTGPT_KEY).
                contentType(MediaType.APPLICATION_JSON).
                body(fastGptDTO);


        // 使用Jsonobject 接收返回参数
        try {
            ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
            JSONObject jsonObject = responseEntity.getBody();
            return jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        } catch (Exception e) {
            throw new CommonException(CommonErrorCode.GPT_CONNECTION_ERROR);
        }

    }
}
