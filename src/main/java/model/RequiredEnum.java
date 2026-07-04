package model;

import lombok.Getter;

/**
 * 是否必传
 */
@Getter
public enum RequiredEnum {
    /**
     * 1:必传 2:可空
     */
    REQUIRED(1, "必传"),
    NULLABLE(2, "可空"),
    ;

    private final Integer code;
    private final String desc;

    RequiredEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
