package github.com.PERS23.MazeSolver;

public enum SolvingStrategyPolicy {
    DFS(new DepthFirstSolve(), "Depth First Search");

    private String mName;
    private SolvingStrategy mSolver;

    SolvingStrategyPolicy(SolvingStrategy solver, String name) {
        mName = name;
        mSolver = solver;
    }

    public SolvingStrategy getSolver() {
        return mSolver;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
