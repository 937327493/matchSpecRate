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
        // 修复：原实现从 specs[0] 起循环又 and 了 specs[0]，变成 A.and(A).and(B)
        if (specs == null || specs.length == 0) {
            throw new IllegalArgumentException("unionAnd requires at least one spec");
        }
        MatchSpec spec0 = specs[0];
        for (int i = 1; i < specs.length; i++) {
            spec0 = spec0.and(specs[i]);
        }
        return spec0;
    }

    /**
     * 匹配or联合
     * @param specs
     * @return
     */
    static MatchSpec unionOr(MatchSpec... specs) {
        // 修复：原实现每次循环 or 了两次，且首元素自 or
        if (specs == null || specs.length == 0) {
            throw new IllegalArgumentException("unionOr requires at least one spec");
        }
        MatchSpec spec0 = specs[0];
        for (int i = 1; i < specs.length; i++) {
            spec0 = spec0.or(specs[i]);
        }
        return spec0;
    }
}
