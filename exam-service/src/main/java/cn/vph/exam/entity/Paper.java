package cn.vph.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 14:55
 */
@Data
@TableName("paper")
public class Paper implements Serializable {

    @TableId(value = "paper_id")
    private Integer paperId;

    /**
     * 试卷名，参见m_simple_name
     */
    private String name;

    /**
     * 试卷总分数
     */
    private Integer totalScore;

    /**
     * 题目个数，不为空，不为null，int，范围1-40
     */
    private Integer questionNum;
}
