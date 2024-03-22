package cn.vph.files.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-21 12:16
 **/
@Data
@AllArgsConstructor
public class VphFile {
    private String fileName;
    private String location;
}
