package model;

import lombok.Getter;

/**
 * 公式枚举
 */
@Getter
public enum FormulaEnum {
    /**
     * 一口价
     */
    fixRateFormula("fixRateFormula", "一口价"),

    /**
     * 全额累进
     */
    fullPriceRateFormula("fixRateFormula", "全额累进");

    private String formulaCode;

    private String desc;

    FormulaEnum(String formulaCode, String desc) {
        this.formulaCode = formulaCode;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     * @param code
     * @return
     */
    public static FormulaEnum getByCode(String code) {
        for (FormulaEnum value : FormulaEnum.values()) {
            if (value.getFormulaCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取打分公式
     * @param code
     * @param factorCode
     * @return
     */
    public static RateFormula getFormula(String code, String factorCode) {
        FormulaEnum byCode = getByCode(code);
        switch (byCode) {
            case fixRateFormula: {
                return new FixRateFormula();
            }
            case fullPriceRateFormula: {
                CalculateFactor calculateFactor = new CalculateFactor(factorCode);
                return new FullPriceRateFormula(calculateFactor);
            }
        }
        return null;
    }
}
