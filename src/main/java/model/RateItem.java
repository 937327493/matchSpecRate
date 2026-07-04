package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 打分项
 */
@AllArgsConstructor
@Getter
public class RateItem {

    /**
     * 打分公式
     */
    private RateFormula rateFormula;

    /**
     * 打分明细
     */
    private List<RateItemDetail> rateItemDetailList;

    /**
     * 打分
     * @param matchableResource
     * @return
     */
    public BigDecimal calculate(MatchableResource matchableResource) {
        return rateFormula.calculate(matchableResource, rateItemDetailList);
    }
}
