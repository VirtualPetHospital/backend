package cn.vph.common;

import lombok.Getter;

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

    /**
     * user type
     */
    public final static String USER_STUDENT = "student";
    public final static String USER_TEACHER = "teacher";
    public final static String USER_ADMIN = "administrator";


    public final static int SORT_ASC = 1;
    public final static int SORT_DESC = 2;

//    public final static String FILE_HOME = "/home/vph/files";
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

