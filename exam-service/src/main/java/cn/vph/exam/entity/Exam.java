package cn.vph.exam.entity;

import cn.vph.common.validation.VphValidation;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @VphValidation("simpleName")
    private String name;

    /**
     * 开始时间，参见m_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间，参见m_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 考试时长，分钟，不为空，不为null，int类型，范围1-300
     */
    @VphValidation("duration")
    private Integer duration;

    @TableField("paper_id")
    private Integer paperId;

    /**
     * 等级，不为空，初始为1，最大为5，int类型
     */
    @VphValidation("level")
    private Integer level;

    @TableField(exist = false)
    private Boolean participated;

    @TableField(exist = false)
    private Paper paper;


}
