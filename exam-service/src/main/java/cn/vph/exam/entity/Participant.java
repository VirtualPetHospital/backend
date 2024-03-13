package cn.vph.exam.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Caroline
 * @description todo
 * @create 2024/3/13 15:07
 */

@Data
@TableName("participant")
public class Participant implements Serializable {

    @TableField("exam_id")
    private Integer examId;

    @TableField("user_id")
    private Integer userId;

    /**
     * `participated` int NOT NULL DEFAULT 0,
     */
    private Integer participated;
}
