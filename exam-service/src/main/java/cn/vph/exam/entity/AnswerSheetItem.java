package cn.vph.exam.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @author Caroline
 * @description 答题卡单项 实体类
 * @create 2024/3/19 2:21
 */
@Data
@TableName("answer_sheet_item")
public class AnswerSheetItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("answer_sheet_id")
    private Integer answerSheetId;

    @TableField("question_id")
    private Integer questionId;

    @TableField(updateStrategy = FieldStrategy.IGNORED, insertStrategy = FieldStrategy.IGNORED)
    @VphValidation("answer")
    private String answer;

    public AnswerSheetItem() {
    }
    public AnswerSheetItem(Integer answerSheetId, Integer questionId, String answer) {
        this.answerSheetId = answerSheetId;
        this.questionId = questionId;
        this.answer = answer;
    }
}
