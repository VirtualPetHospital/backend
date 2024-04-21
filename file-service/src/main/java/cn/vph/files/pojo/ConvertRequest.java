package cn.vph.files.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-21 20:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertRequest {
    private String fileName;
    private String identifier;
    private String type;
}
