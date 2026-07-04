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
        // 修复：原实现把开区间和闭区间边界拆成两段判断，端点附近会出现空洞；
        // 且混用 double 比较与 BigDecimal.compareTo，精度口径不一致。
        // 改为统一用 BigDecimal 比较，按 includeStart/includeEnd 决定端点开闭。
        BigDecimal value = BigDecimal.valueOf(rateableResource.fetchDouble(code));
        BigDecimal left = BigDecimal.valueOf(start);
        BigDecimal right = BigDecimal.valueOf(end);

        int cmpLeft = value.compareTo(left);
        int cmpRight = value.compareTo(right);

        boolean passStart = includeStart ? cmpLeft >= 0 : cmpLeft > 0;
        boolean passEnd = includeEnd ? cmpRight <= 0 : cmpRight < 0;

        return passStart && passEnd;
    }
}
