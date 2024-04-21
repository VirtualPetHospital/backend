package cn.vph.files.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-21 20:01
 **/
@Data
@AllArgsConstructor
@TableName("file")
public class FileDTO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("file_identifier")
    private String fileIdentifier;
    @TableField("file_name")
    private String fileName;
}
