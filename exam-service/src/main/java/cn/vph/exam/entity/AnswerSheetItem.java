package cn.vph.exam.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 14:59
 */
@Data
@TableName("answer_sheet_item")
public class AnswerSheetItem implements Serializable {

    @TableId(value = "`order`")
    private Integer order;

    /**
     * 作答，一位大写字母；
     */
    @TableField("`option`")
    private String option;

    /**
     * 该题获得的分数，参见m_score
     */
    private Integer score;

    /**
     * 该题目在试卷中的顺序，不为空，不为null，int，从1开始，最大40（和paper题目数量一致）
     */
    @TableField("answer_sheet_id")
    private Integer answerSheetId;
}