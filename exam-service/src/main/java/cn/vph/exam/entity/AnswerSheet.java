package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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

    @TableField("exam_id")
    private Integer examId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    List<AnswerSheetItem> answers;
}