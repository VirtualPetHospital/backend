package cn.vph.exam.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Caroline
 * @description 试卷实体类
 * @create 2024/3/13 14:55
 */
@Data
@TableName("paper")
public class Paper implements Serializable {

    @TableId(value = "paper_id", type = IdType.AUTO)
    private Integer paperId;

    /**
     * 试卷名，参见m_simple_name
     */
    @VphValidation("simpleName")
    private String name;


    /**
     * 题目个数，不为空，不为null，int，范围1-40
     */
    @TableField(value = "question_num")
    @VphValidation("questionNum")
    private Integer questionNum;

    /**
     * 本试卷的所有题目 的列表
     */
    @TableField(exist = false)
    private List<Question> questions;

}
