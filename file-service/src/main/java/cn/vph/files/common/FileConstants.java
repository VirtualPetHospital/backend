package cn.vph.files.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-24 18:48
 **/
@Component
public class FileConstants {


    @Value("${vph.file.path}")
    public String FILE_DIR;

    public final String[] LOCATIONS = {"user-avatar", "disease", "medcase", "operation", "room", "room-asset"};

    public final String[] PHOTO_TYPES = {"jpg", "jpeg", "png", "gif", "bmp"};

    public final String[] VIDEO_TYPES = {"mp4", "avi", "mov", "wmv", "flv", "rmvb", "3gp", "mkv"};
    public final String SEPARATOR = "_";
}
