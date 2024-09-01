package cn.iocoder.yudao.module.enums;


public enum AuthenticationErrorCode {

    SUCCESS("0000", "身份证信息匹配"),
    EMPTY_USERNAME("0001", "开户名不能为空"),
    INVALID_USERNAME("0002", "开户名不能包含特殊字符"),
    EMPTY_IDNO("0003", "身份证号不能为空"),
    INVALID_IDNO_FORMAT("0004", "身份证号格式错误"),
    IDNO_NOT_FOUND("0007", "无此身份证号码"),
    IDNO_NOT_MATCH("0008", "身份证信息不匹配"),
    SYSTEM_MAINTENANCE("0010", "系统维护，请稍后再试"),
    UNKNOWN_ERROR("9999", "未知错误");

    private final String code;
    private final String message;

    AuthenticationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static AuthenticationErrorCode fromCode(String code) {
        for (AuthenticationErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return UNKNOWN_ERROR;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByCode(String code) {
        for (AuthenticationErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode.getMessage();
            }
        }
        return "未知错误";
    }
}

