package cn.vph.gpt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-26 13:50
 **/
@Data
@AllArgsConstructor
@Builder
public class FastGptDTO {
    private String chatId;
    private Boolean stream;
    private Boolean detail;
    private List<Message> messages;
}
