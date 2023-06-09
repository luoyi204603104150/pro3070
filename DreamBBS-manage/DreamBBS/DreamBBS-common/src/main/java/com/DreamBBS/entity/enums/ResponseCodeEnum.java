package com.DreamBBS.entity.enums;


public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址有误"),
    CODE_600(600, "请求参数有误"),
    CODE_601(601, "信息已经存在"),
    CODE_500(500, "你传了什么勾8,快tm叫何润生看后台"),
    CODE_501(501, "文件类型有误"),
    CODE_502(502, "没登陆你点勾呢"),
    CODE_503(503, "上传失败"),
    CODE_900(900, "HTTP请求超时");

    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
