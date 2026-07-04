package model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class FullPriceRateFormula implements RateFormula {

    private CalculateFactor calculateFactor;

    public FullPriceRateFormula() {

    }

    public FullPriceRateFormula(CalculateFactor calculateFactor) {
        this.calculateFactor = calculateFactor;
    }

    @Override
    public BigDecimal calculate(MatchableResource matchableResource, List<RateItemDetail> itemDetailList) {
        RateItemDetail matchedDetail = getMatchedDetail(matchableResource, itemDetailList);

        if (matchedDetail == null) {
            /**
             * 没有匹配到的明细
             */
            return BigDecimal.ZERO;
        }

        return matchedDetail.getValue().multiply(matchableResource.fetchBigDecimal(calculateFactor.getFactorKey()));

    }
}
