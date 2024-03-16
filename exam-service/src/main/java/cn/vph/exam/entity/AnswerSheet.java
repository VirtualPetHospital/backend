package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 15:00
 */
@Data
@TableName("answer_sheet")
public class AnswerSheet implements Serializable {

    @TableId(value = "answer_sheet_id")
    private Integer answerSheetId;

    @TableField("user_id")
    private Integer userId;

    @TableField("exam_id")
    private Integer examId;

    /**
     *  总分，参见m_score
     */
    @TableField("total_score")
    private Integer totalScore;
}