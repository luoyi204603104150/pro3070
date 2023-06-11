package com.DreamBBS.entity.enums;

public enum ArticleStatusEnum {
    DEL(0, "已删除"),
    NORMAL(1, "已审核");


    private Integer status;
    private String desc;

    ArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ArticleStatusEnum getByStatus(Integer status) {
        for (ArticleStatusEnum item : ArticleStatusEnum.values()) {
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
