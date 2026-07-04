package model;

public class OrMatchSpec implements MatchSpec {

    /**
     * 左操作数
     */
    private MatchSpec x;

    /**
     * 右操作数
     */
    private MatchSpec y;

    public OrMatchSpec(MatchSpec x, MatchSpec y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isSatisfiedBy(MatchableResource matchableResource) {
        // 修复：原来写死 return false，应满足任一即匹配
        return x.isSatisfiedBy(matchableResource) || y.isSatisfiedBy(matchableResource);
    }

}
