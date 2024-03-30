package cn.vph.exam.entity;


import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author Caroline
 * @description 题目实体类
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
    @VphValidation("text")
    private String description;

    /**
     * 答案（选项），一位大写字母
     */
    @VphValidation("answer")
    private String answer;

    @VphValidation("optionDescription")
    private String a;

    @VphValidation("optionDescription")
    private String b;

    @VphValidation("optionDescription")
    private String c;

    @VphValidation("optionDescription")
    private String d;

    @TableField(value = "category_id")
    private Integer categoryId;
}
