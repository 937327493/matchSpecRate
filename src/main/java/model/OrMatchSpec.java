package model;

public class OrMatchSpec implements MatchSpec {

    /**
     * 1
     */
    private MatchSpec x;

    /**
     * 2
     */
    private MatchSpec y;

    public OrMatchSpec(MatchSpec x, MatchSpec y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isSatisfiedBy(MatchableResource matchableResource) {
        return false;
    }

}
