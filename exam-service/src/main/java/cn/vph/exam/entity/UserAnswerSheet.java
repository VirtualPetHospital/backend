package cn.vph.exam.entity;

import lombok.Data;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-07 10:05
 **/
@Data
public class UserAnswerSheet {
    private Integer userId;
    private String nickname;
    private Integer answerSheetId;
}
