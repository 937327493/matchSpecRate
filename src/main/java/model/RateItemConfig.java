package model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 打分项配置
 */
@Getter
@Setter
public class RateItemConfig {
    private String warehouseCode;

    private String bizNatureCode;

    private String name;

    private String formulaCode;

    private String factorKey;

    private List<RateItemDetailConfig> rateItemDetailConfigList;
}
