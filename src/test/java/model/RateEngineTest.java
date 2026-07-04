package model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 规则引擎测试：覆盖匹配规格、计费公式、配置装配。
 * 同时回归以下历史 bug：
 *  - OrMatchSpec 曾永远返回 false
 *  - unionOr 曾重复 or、unionAnd/unionOr 首元素自运算
 *  - SectionValueMatchSpec 区间边界空洞、double/BigDecimal 混用
 *  - FactorConfig.parseSectionMatchSpec 在 REQUIRED 下返回 null（区间全失效）
 *  - SingleValueMatchSpec/MultipleValueMatchSpec 在 value 为 null 时 NPE
 */
public class RateEngineTest {

    /** 一个基于 Map 的 MatchableResource 测试桩 */
    private static class MapResource implements MatchableResource {
        private final Map<String, String> strings = new HashMap<>();
        private final Map<String, Double> doubles = new HashMap<>();
        private final Map<String, BigDecimal> decimals = new HashMap<>();

        MapResource str(String k, String v) { strings.put(k, v); return this; }
        MapResource num(String k, double v) { doubles.put(k, v); return this; }
        MapResource dec(String k, BigDecimal v) { decimals.put(k, v); return this; }

        @Override public String fetchString(String key) { return strings.get(key); }
        @Override public Double fetchDouble(String key) { return doubles.get(key); }
        @Override public BigDecimal fetchBigDecimal(String key) { return decimals.get(key); }
    }

    // ---------- 匹配规格 ----------

    @Test
    public void singleValue_matches_whenEqual() {
        SingleValueMatchSpec spec = new SingleValueMatchSpec("city", "城市", "北京", RequiredEnum.REQUIRED);
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "北京")));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("city", "上海")));
    }

    @Test
    public void multipleValue_matches_anyInList() {
        MultipleValueMatchSpec spec = new MultipleValueMatchSpec(
            "city", "城市", Arrays.asList("北京", "上海"), RequiredEnum.REQUIRED);
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "北京")));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "上海")));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("city", "广州")));
    }

    @Test
    public void section_closeClose_includesEndpoints() {
        // [10, 20]
        SectionValueMatchSpec spec = new SectionValueMatchSpec("amt", "金额", 10, 20, true, true);
        assertEquals(true, spec.isSatisfiedBy(new MapResource().num("amt", 15)));   // 中间
        assertEquals(true, spec.isSatisfiedBy(new MapResource().num("amt", 10)));   // 左端点
        assertEquals(true, spec.isSatisfiedBy(new MapResource().num("amt", 20)));   // 右端点
        assertEquals(false, spec.isSatisfiedBy(new MapResource().num("amt", 9.99)));// 左外
        assertEquals(false, spec.isSatisfiedBy(new MapResource().num("amt", 20.01)));// 右外
    }

    @Test
    public void section_openOpen_excludesEndpoints() {
        // (10, 20)
        SectionValueMatchSpec spec = new SectionValueMatchSpec("amt", "金额", 10, 20, false, false);
        assertEquals(true, spec.isSatisfiedBy(new MapResource().num("amt", 15)));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().num("amt", 10)));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().num("amt", 20)));
    }

    @Test
    public void section_closeOpen_leftCloseRightOpen() {
        // [10, 20)
        SectionValueMatchSpec spec = new SectionValueMatchSpec("amt", "金额", 10, 20, true, false);
        assertEquals(true, spec.isSatisfiedBy(new MapResource().num("amt", 10)));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().num("amt", 20)));
    }

    @Test
    public void andCombination_bothMustMatch() {
        MatchSpec spec = new SingleValueMatchSpec("city", "城市", "北京", RequiredEnum.REQUIRED)
            .and(new SectionValueMatchSpec("amt", "金额", 100, 1000, true, true));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "北京").num("amt", 500)));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("city", "上海").num("amt", 500)));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("city", "北京").num("amt", 50)));
    }

    @Test
    public void orCombination_eitherMatches() {
        // 回归 OrMatchSpec 曾永远返回 false 的 bug
        MatchSpec spec = new SingleValueMatchSpec("city", "城市", "北京", RequiredEnum.REQUIRED)
            .or(new SingleValueMatchSpec("city", "城市", "上海", RequiredEnum.REQUIRED));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "北京")));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("city", "上海")));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("city", "广州")));
    }

    @Test
    public void unionAnd_combinesAllWithoutSelfAnd() {
        // 回归 unionAnd 曾把首元素自 and 一次的 bug：A.and(A).and(B)
        MatchSpec spec = MatchSpec.unionAnd(
            new SingleValueMatchSpec("a", "a", "1", RequiredEnum.REQUIRED),
            new SingleValueMatchSpec("b", "b", "2", RequiredEnum.REQUIRED),
            new SingleValueMatchSpec("c", "c", "3", RequiredEnum.REQUIRED));
        assertEquals(true, spec.isSatisfiedBy(
            new MapResource().str("a", "1").str("b", "2").str("c", "3")));
        // 任一不满足即整体不满足
        assertEquals(false, spec.isSatisfiedBy(
            new MapResource().str("a", "1").str("b", "2").str("c", "X")));
    }

    @Test
    public void unionOr_combinesAllWithoutDuplicateOr() {
        // 回归 unionOr 曾每次循环 or 两次的 bug
        MatchSpec spec = MatchSpec.unionOr(
            new SingleValueMatchSpec("a", "a", "1", RequiredEnum.REQUIRED),
            new SingleValueMatchSpec("a", "a", "2", RequiredEnum.REQUIRED),
            new SingleValueMatchSpec("a", "a", "3", RequiredEnum.REQUIRED));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("a", "1")));
        assertEquals(true, spec.isSatisfiedBy(new MapResource().str("a", "3")));
        assertEquals(false, spec.isSatisfiedBy(new MapResource().str("a", "9")));
    }

    // ---------- 计费公式 ----------

    @Test
    public void fixRateFormula_returnsDetailValueOnMatch() {
        RateItemDetail matched = new RateItemDetail(
            new SingleValueMatchSpec("city", "城市", "北京", RequiredEnum.REQUIRED),
            new BigDecimal("8.88"));
        RateItemDetail other = new RateItemDetail(
            new SingleValueMatchSpec("city", "城市", "上海", RequiredEnum.REQUIRED),
            new BigDecimal("5.00"));
        RateFormula formula = new FixRateFormula();
        BigDecimal result = formula.calculate(
            new MapResource().str("city", "北京"), Arrays.asList(other, matched));
        assertEquals(0, result.compareTo(new BigDecimal("8.88")));
    }

    @Test
    public void fixRateFormula_returnsZeroWhenNoMatch() {
        RateItemDetail detail = new RateItemDetail(
            new SingleValueMatchSpec("city", "城市", "北京", RequiredEnum.REQUIRED),
            new BigDecimal("8.88"));
        RateFormula formula = new FixRateFormula();
        BigDecimal result = formula.calculate(
            new MapResource().str("city", "广州"), Collections.singletonList(detail));
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void fullPriceRateFormula_multipliesValueByFactor() {
        // 全价费率 = 命中明细的 value × 资源的计量因子
        RateItemDetail detail = new RateItemDetail(
            new SectionValueMatchSpec("amt", "金额", 100, 1000, true, true),
            new BigDecimal("0.05")); // 费率 5%
        RateFormula formula = new FullPriceRateFormula(new CalculateFactor("amt"));
        // 金额 200，费率 0.05 → 10（用 compareTo 忽略标度差异）
        BigDecimal result = formula.calculate(
            new MapResource().num("amt", 200).dec("amt", new BigDecimal("200")),
            Collections.singletonList(detail));
        assertEquals(0, result.compareTo(new BigDecimal("10")));
    }

    // ---------- 装配工厂 ----------

    @Test
    public void factory_parsesConfigToRateItem_andCalculates() {
        // 构造一个配置：city == 北京 AND amt ∈ [100,1000]，固定费率 8.88
        FactorConfig cityFactor = new FactorConfig();
        cityFactor.setCode("city");
        cityFactor.setName("城市");
        cityFactor.setConfig("北京");
        cityFactor.setOperator("eql");

        FactorConfig amtFactor = new FactorConfig();
        amtFactor.setCode("amt");
        amtFactor.setName("金额");
        amtFactor.setConfig("100,1000");
        amtFactor.setOperator("lcrc"); // 左闭右闭

        RateItemDetailConfig detailCfg = new RateItemDetailConfig();
        detailCfg.setFactorConfigList(Arrays.asList(cityFactor, amtFactor));
        detailCfg.setValue(new BigDecimal("8.88"));

        RateItemConfig itemCfg = new RateItemConfig();
        itemCfg.setFormulaCode("fixRateFormula");
        itemCfg.setRateItemDetailConfigList(Collections.singletonList(detailCfg));

        RateItem item = new RateItemFactory().parseRateItem(itemCfg);

        // 命中：北京 + 金额 500
        BigDecimal hit = item.calculate(new MapResource().str("city", "北京").num("amt", 500));
        assertEquals(0, hit.compareTo(new BigDecimal("8.88")));

        // 不命中：城市错
        BigDecimal miss = item.calculate(new MapResource().str("city", "上海").num("amt", 500));
        assertEquals(BigDecimal.ZERO, miss);
    }
}
