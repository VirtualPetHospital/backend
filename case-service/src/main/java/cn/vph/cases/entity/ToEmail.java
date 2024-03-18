package cn.vph.cases.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-18 08:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToEmail implements java.io.Serializable {
    // 接收方
    private String to;
    // 主题
    private String subject;
    // 内容
    private String content;
}
