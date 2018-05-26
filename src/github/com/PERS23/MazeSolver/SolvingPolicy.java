package github.com.PERS23.MazeSolver;

public enum SolvingPolicy {
    DFS(new DepthFirstSolve(), "Depth First Search"),
    BFS(new BreadthFirstSolve(), "Breadth First Search"),
    DIJKSTRA(new DijkstraSolve(), "Djikstra");

    private String mName;
    private SolvingStrategy mSolver;

    SolvingPolicy(SolvingStrategy solver, String name) {
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
