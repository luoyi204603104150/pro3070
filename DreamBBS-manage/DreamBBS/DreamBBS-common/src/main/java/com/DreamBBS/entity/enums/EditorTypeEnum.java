package com.DreamBBS.entity.enums;

public enum EditorTypeEnum {
    TEXT(0, "文本编辑器"),
    MARKDOWN(1, "Markdown编辑器");


    private Integer type;
    private String desc;

    EditorTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static EditorTypeEnum getByType(Integer type) {
        for (EditorTypeEnum item : EditorTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
