package github.com.PERS23.MazeSolver;

public enum GenStrategyPolicy {
    RECURSIVE_BACKTRACK(new RecursiveBacktrack(), "Recursive Backtracker");

    private String mName;
    private GenStrategy mGenerator;

    private GenStrategyPolicy(GenStrategy generator, String name) {
        mGenerator = generator;
        mName = name;
    }

    public GenStrategy getGenerator() {
        return mGenerator;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
