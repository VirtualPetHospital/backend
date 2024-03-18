package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * @author Caroline
 * @description paper question 多对多关系表 实体类
 * @create 2024/3/13 15:01
 */
@Data
@TableName("paper_question")
public class PaperQuestion implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "paper_id")
    private Integer paperId;

    @TableField(value = "question_id")
    private Integer questionId;

    public PaperQuestion() {
    }
    public PaperQuestion(Integer paperId, Integer questionId) {
        this.paperId = paperId;
        this.questionId = questionId;
    }
}