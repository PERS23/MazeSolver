package github.com.PERS23.MazeSolver;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import javafx.embed.swing.SwingFXUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class MazeTest {

    @Test
    @TestInJfxThread
    public void test() {
        Maze instance = Maze.randomMaze(20, 20, new RecursiveBacktrack());
        AnimationBuilder imager = new AnimationBuilder(instance, 8, 16);

        SolvingStrategy solver = new DepthFirstSolve();
        solver.solve(instance, new Point(0,0), new Point(19,19));

        imager.highlight(0, 0);
        for (Point p : solver.getSolutionPath()) {
            imager.highlight(p.x, p.y);
        }

        File file = new File("CanvasImage.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imager.getImage(), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}