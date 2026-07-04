package model;

import lombok.Getter;

/**
 * 比较符枚举
 */
@Getter
public enum OperatorEnum {
    /**
     * 数字型等于
     */
    EQUAL("number", "==", "数字型等于", "eq", 14),

    /**
     * 字符型等于
     */
    EQUAL_LITERAL("literal", "==", "字符型等于", "eql", 1),
    /**
     * 字面量 - 包含
     */
    CONTAIN_LITERAL("literal", "containl", "包含", "containl", 6),
    /**
     * 左开右闭
     */
    LEFT_OPEN_RIGHT_CLOSE("number", "lorc", "左开右闭", "lorc", 15),
    /**
     * 左闭右开
     */
    LEFT_CLOSE_RIGHT_OPEN("number", "lcro", "左闭右开", "lcro", 16),

    /**
     * 左闭右闭
     */
    LEFT_CLOSE_RIGHT_CLOSE("number", "lcrc", "左闭右闭", "lcrc", 17),
    ;

    private String type;
    private String desc;
    private String operator;
    private String value;
    private Integer intValue;

    OperatorEnum(String type, String desc, String value) {
        this.type = type;
        this.desc = desc;
        this.value = value;
    }

    OperatorEnum(String type, String operator, String desc, String value, Integer intValue) {
        this.type = type;
        this.operator = operator;
        this.desc = desc;
        this.value = value;
        this.intValue = intValue;
    }

    public static OperatorEnum fromValue(String value) {
        for (OperatorEnum operator : values()) {
            if (operator.getValue().equals(value)) {
                return operator;
            }
        }
        return EQUAL_LITERAL;
    }

}