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
    // 修复：原 formulaCode 误写为 "fixRateFormula"（复制粘贴 bug），应为 "fullPriceRateFormula"
    fullPriceRateFormula("fullPriceRateFormula", "全额累进");

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
        // 修复：原实现 getByCode 返回 null 时 switch(byCode) 会 NPE
        FormulaEnum byCode = getByCode(code);
        if (byCode == null) {
            throw new IllegalArgumentException("unknown formula code: " + code);
        }
        switch (byCode) {
            case fixRateFormula: {
                return new FixRateFormula();
            }
            case fullPriceRateFormula: {
                CalculateFactor calculateFactor = new CalculateFactor(factorCode);
                return new FullPriceRateFormula(calculateFactor);
            }
            default:
                throw new IllegalArgumentException("unsupported formula: " + byCode);
        }
    }
}
