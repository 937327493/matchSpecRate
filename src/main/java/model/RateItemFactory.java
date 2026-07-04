package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置 → 领域对象 装配工厂。
 *
 * 把 RateItemConfig（外部配置）解析为 RateItem（领域对象）：
 * 每条 RateItemDetailConfig 的 FactorConfig 列表用 unionAnd 组合成一个 MatchSpec，
 * 再装配出 RateItemDetail / RateItem。
 */
public class RateItemFactory {

    /**
     * 从配置解析出一个计费项
     */
    public RateItem parseRateItem(RateItemConfig config) {
        RateFormula rateFormula = FormulaEnum.getFormula(config.getFormulaCode(), config.getFactorKey());
        List<RateItemDetailConfig> detailConfigList = config.getRateItemDetailConfigList();
        List<RateItemDetail> detailList = new ArrayList<>();
        for (RateItemDetailConfig detailConfig : detailConfigList) {
            detailList.add(parseRateItemDetail(detailConfig));
        }
        return new RateItem(rateFormula, detailList);
    }

    private RateItemDetail parseRateItemDetail(RateItemDetailConfig detailConfig) {
        List<FactorConfig> factorConfigList = detailConfig.getFactorConfigList();
        List<MatchSpec> matchSpecs = new ArrayList<>();
        for (FactorConfig factorConfig : factorConfigList) {
            MatchSpec spec = factorConfig.toMatchSpec();
            if (spec != null) {
                matchSpecs.add(spec);
            }
        }
        // 修复：原实现把 null 也塞进数组，可能导致 unionAnd 对 null 调用方法 NPE
        if (matchSpecs.isEmpty()) {
            throw new IllegalStateException("no valid match spec parsed from detail config");
        }
        MatchSpec[] array = matchSpecs.toArray(new MatchSpec[0]);
        MatchSpec matchSpec = MatchSpec.unionAnd(array);
        return new RateItemDetail(matchSpec, detailConfig.getValue());
    }
}
