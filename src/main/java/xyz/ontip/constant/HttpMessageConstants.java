package xyz.ontip.constant;

import java.io.Serializable;

public class HttpMessageConstants implements Serializable {

    // 登录验证失败
    public static final int LOGIN_ERROR_CODE = 411;
    public static final String LOGIN_ERROR_MSG = "账号或密码错误";
    // 注册失败
    public static final int REGISTER_ERROR_CODE = 412;
    public static final String REGISTER_ERROR_MSG = "注册失败";
    // 用户的登录信息校验失败
    public static final int LOGIN_VERIFY_ERROR_CODE = 413;
    public static final String LOGIN_VERIFY_ERROR_MSG = "校验失败";
}
