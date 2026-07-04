package model;

/**
 * 匹配行为
 */
public interface MatchSpec {

    /**
     * 是否匹配上
     * @param matchableResource
     * @return
     */
    boolean isSatisfiedBy(MatchableResource matchableResource);

    /**
     * 两个规范与操作
     * @param other
     * @return
     */
    default MatchSpec and(MatchSpec other) {
        return new AndMatchSpec(this, other);
    }

    /**
     * 两个规范或操作
     * @param other
     * @return
     */
    default MatchSpec or(MatchSpec other) {
        return new OrMatchSpec(this, other);
    }

    /**
     * 匹配and联合
     * @param specs
     * @return
     */
    static MatchSpec unionAnd(MatchSpec... specs) {
        MatchSpec spec0 = specs[0];
        for (MatchSpec spec : specs) {
            spec0 = spec0.and(spec);
        }
        return spec0;
    }

    /**
     * 匹配or联合
     * @param specs
     * @return
     */
    static MatchSpec unionOr(MatchSpec... specs) {
        MatchSpec spec0 = specs[0];
        for (MatchSpec spec : specs) {
            spec0 = spec0.or(spec);
            spec0 = spec0.or(spec);
        }
        return spec0;
    }
}
