package cn.vph.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-17 15:55
 **/
public class CommonConstant {
    public final static String SESSION = "session";
    public final static String[] AT_LEAST_STUDENT = {"student", "teacher", "administrator"};
    public final static String[] AT_LEAST_TEACHER = {"teacher", "administrator"};
    public final static String[] AT_LEAST_ADMINISTRATOR = {"administrator"};
    public final static String DEFAULT_IMAGE = "https://img2.imgtp.com/2024/04/22/YHFFLtQf.png";

    public final static String DEFAULT_AVATAR = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png";
    /**
     * user type
     */
    public final static String USER_STUDENT = "student";
    public final static String USER_TEACHER = "teacher";
    public final static String USER_ADMIN = "administrator";

    /**
     * user upgrade
     */
    public final static Integer EXAM_NUM_FOR_UPGRADE = 5;
    public final static Integer MIN_LEVEL = 1;
    public final static Integer MAX_LEVEL = 5;

    public final static int SORT_ASC = 1;
    public final static int SORT_DESC = 2;

//    public final static String FILE_HOME = "/home/vph/files";
    @Value("${vph.file.path}")
    public final static String FILE_HOME = "E:\\vph\\files";
    @Getter
    enum FileLoc {
        DISEASE("disease"),
        USER_AVATAR("user_avatar"),
        MEDCASE_INFO("medcase_info"),
        OPERATION("operation"),
        ROOM("room"),
        ROOM_ASSET("room_asset"),
        ;

        private final String location;

        FileLoc(String location) {
            this.location = location;
        }
    }
}

