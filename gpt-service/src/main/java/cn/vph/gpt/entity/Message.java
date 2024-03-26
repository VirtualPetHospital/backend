package cn.vph.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-26 13:51
 **/
@Data
@Builder
@AllArgsConstructor
public class Message {
    private String content;
    private String role;


}
