package model;

import java.math.BigDecimal;
import java.util.List;


public interface RateFormula {

    /**
     * 打分
     * @param matchableResource
     * @param itemDetailList
     */
    BigDecimal calculate(MatchableResource matchableResource, List<RateItemDetail> itemDetailList);

    /**
     * 通用获取匹配中的打分明细
     * @param matchableResource
     * @param rateItemDetails
     * @return
     */
    default RateItemDetail getMatchedDetail(MatchableResource matchableResource, List<RateItemDetail> rateItemDetails) {
        for (RateItemDetail rateItemDetail : rateItemDetails) {
            if (rateItemDetail.getMatchSpec().isSatisfiedBy(matchableResource)) {
                return rateItemDetail;
            }
        }
        return null;
    }
}
