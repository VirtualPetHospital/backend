package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 15:01
 */
@Data
@TableName("paper_question")
public class PaperQuestion implements Serializable {

    @TableField("paper_id")
    private Integer paperId;

    @TableField("question_id")
    private Integer questionId;

    private Integer score;

    @TableField("`order`")
    private Integer order;
}