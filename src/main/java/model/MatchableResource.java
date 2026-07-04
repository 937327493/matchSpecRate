package model;

import java.math.BigDecimal;

/**
 * 被匹配的资源
 */
public interface MatchableResource {
    // 瘦身：删除未被任何 MatchSpec/Formula 使用的 fetch 方法（fetchBizCode、
    // fetchStringList、fetchLocalDateTime、fetchBoolean），避免 YAGNI 膨胀。
    // 注意：原 fetchStringList 声明返回 String，命名与类型不符，一并删除。

    /** 取字符串属性（Single/MultipleValueMatchSpec 使用） */
    String fetchString(String key);

    /** 取数值属性（SectionValueMatchSpec 使用） */
    Double fetchDouble(String key);

    /** 取金额属性（FullPriceRateFormula 计量因子使用） */
    BigDecimal fetchBigDecimal(String key);
}
