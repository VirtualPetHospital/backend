package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 15:09
 */

@Data
@TableName("question")
public class Question implements Serializable {

    @TableId(value = "question_id", type = IdType.AUTO)
    private Integer questionId;

    /**
     * 题目表述，参见m_text
     */
    private String description;

    /**
     * 答案（选项），一位大写字母
     */
    private String answer;

    private String A;

    private String B;

    private String C;

    private String D;

    @TableField(value = "category_id")
    private Integer categoryId;
}
