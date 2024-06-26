package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description 学生参与考试实体类
 * @create 2024/3/13 15:07
 */

@Data
@TableName("participant")
public class Participant implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("exam_id")
    private Integer examId;

    @TableField("user_id")
    private Integer userId;

    /**
     * true : participated
     * false : not participated
     *
     * `participated` int NOT NULL DEFAULT 0,
     */
    private Boolean participated;

    public Participant() {
    }
    public Participant(Integer examId, Integer userId) {
        this.examId = examId;
        this.userId = userId;
        this.participated = false;
    }
    public Participant(Integer examId, Integer userId, Boolean participated) {
        this.examId = examId;
        this.userId = userId;
        this.participated = participated;
    }
}
