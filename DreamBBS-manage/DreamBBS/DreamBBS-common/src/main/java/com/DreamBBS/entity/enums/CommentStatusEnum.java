package com.DreamBBS.entity.enums;

public enum CommentStatusEnum {
    DEL(0, "已删除"),
    NORMAL(1, "正常");


    private Integer status;
    private String desc;

    CommentStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CommentStatusEnum getByStatus(Integer status) {
        for (CommentStatusEnum item : CommentStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
