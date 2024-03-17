package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 答题卡单项 实体类
 * @create 2024/3/13 15:00
 */
@Data
@TableName("answer_sheet")
public class AnswerSheet implements Serializable {

    @TableId(value = "answer_sheet_id", type = IdType.AUTO)
    private Integer answerSheetId;

    @TableField("user_id")
    private Integer userId;

    private String option;

    @TableField("exam_id")
    private Integer examId;

    @TableField("question_id")
    private Integer questionId;
}