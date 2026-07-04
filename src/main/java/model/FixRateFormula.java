package model;

import java.math.BigDecimal;
import java.util.List;

public class FixRateFormula implements RateFormula {
    @Override
    public BigDecimal calculate(MatchableResource matchableResource, List<RateItemDetail> itemDetailList) {
        RateItemDetail matchedDetail = getMatchedDetail(matchableResource, itemDetailList);

        if (matchedDetail == null) {
            /**
             * 没有匹配到的明细
             */

            return BigDecimal.ZERO;
        }
        return matchedDetail.getValue();
    }
}
