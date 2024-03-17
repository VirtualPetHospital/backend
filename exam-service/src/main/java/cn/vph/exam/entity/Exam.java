package cn.vph.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Caroline
 * @description 试卷实体类
 * @create 2024/3/13 15:00
 */
@Data
@TableName("exam")
public class Exam implements Serializable {

    @TableId(value = "exam_id", type = IdType.AUTO)
    private Integer examId;

    /**
     * 考试名，参见m_simple_name
     */
    private String name;

    /**
     * 开始时间，参见m_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间，参见m_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private Date endTime;

    /**
     * 考试时长，分钟，不为空，不为null，int类型，范围1-300
     */
    private Integer duration;

    @TableField("paper_id")
    private Integer paperId;

    /**
     * 等级，不为空，初始为1，最大为5，int类型
     */
    private Integer level;
}
