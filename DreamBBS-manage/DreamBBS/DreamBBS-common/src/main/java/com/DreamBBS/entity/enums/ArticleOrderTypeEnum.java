package com.DreamBBS.entity.enums;

public enum ArticleOrderTypeEnum {
    //文章顺序
    HOT(0, "top_type desc,comment_count desc,good_count desc,read_count desc", "最热"),
    SEND(1, "post_time asc", "发布时间"),
    NEW(2, "post_time desc", "最新");


    private Integer type;
    private String orderSql;
    private String desc;

    ArticleOrderTypeEnum(Integer type, String orderSql, String desc) {
        this.type = type;
        this.orderSql = orderSql;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public String getDesc() {
        return desc;
    }

    //遍历枚举中的所有值
    public static ArticleOrderTypeEnum getByType(Integer type) {
        for (ArticleOrderTypeEnum item : ArticleOrderTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }


}
