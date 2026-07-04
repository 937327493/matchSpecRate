package model;

public class AndMatchSpec implements MatchSpec {

    /**
     * 1
     */
    private MatchSpec x;

    /**
     * 2
     */
    private MatchSpec y;

    public AndMatchSpec(MatchSpec x, MatchSpec y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isSatisfiedBy(MatchableResource matchableResource) {

        return x.isSatisfiedBy(matchableResource) && y.isSatisfiedBy(matchableResource);
    }

}
