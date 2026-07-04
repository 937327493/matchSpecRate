package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 范围匹配
 */
@AllArgsConstructor
@Getter
public class SectionValueMatchSpec implements MatchSpec {

    private String code;

    private String name;

    private double start;

    private double end;

    private boolean includeStart;

    private boolean includeEnd;


    @Override
    public boolean isSatisfiedBy(MatchableResource rateableResource) {
        double value = rateableResource.fetchDouble(code);
        //是否在区间内
        if (value > start && value < end) {
            return true;
        }
        //如果包含开始值，是否等于开始值
        boolean includeStartResult = false;
        if (includeStart) {
            includeStartResult = BigDecimal.valueOf(value).compareTo(BigDecimal.valueOf(start)) == 0;
        }
        //如果包含结束值，是否等于结束值
        boolean includeEndResult = false;
        if (includeEnd) {
            includeEndResult = BigDecimal.valueOf(value).compareTo(BigDecimal.valueOf(end)) == 0;
        }
        boolean result = includeStartResult | includeEndResult;
        return result;
    }
}
