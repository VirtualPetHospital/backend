package cn.vph.common;

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
}
