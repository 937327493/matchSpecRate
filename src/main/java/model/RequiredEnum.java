package model;

public enum RequiredEnum {
    /**
     * 1:必传 2:可空
     */
    REQUIRED(1, "必传"),
    NULLABLE(2, "可空"),
    ;

    private Integer code;
    private String desc;

    RequiredEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}