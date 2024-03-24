package cn.vph.files.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-24 18:48
 **/
@Getter
@Component
public class FileConstants {


    @Value("${vph.file.path}")
    private String fileDir;

}
