package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 打分明细配置
 */
@Getter
@Setter
public class RateItemDetailConfig {
    private List<FactorConfig> factorConfigList;

    private BigDecimal value;
}
