package sample.Model;

import javafx.util.Pair;
import sample.Model.Tools.SmallUsefullTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardMapGenerator {
    private ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> ans;
    private final boolean[][] mark;
    private final Random random;

    public BoardMapGenerator() {
        mark = new boolean[BoardMapBarriers.boardSize.getX() + 2][BoardMapBarriers.boardSize.getY() + 2];
        ans = new ArrayList<>();
        random = new Random();
    }

    public ArrayList<Pair<IntegerCoordinates, IntegerCoordinates>> getRandomMapBarriers() {
        ans = new ArrayList<>();

        for (int i = 0; i < BoardMapBarriers.boardSize.getX(); i++)
            for (int j = 0; j < BoardMapBarriers.boardSize.getY() - 1; j++) {
                ans.add(new Pair<>(new IntegerCoordinates(i, j), new IntegerCoordinates(i, j + 1)));
                ans.add(new Pair<>(new IntegerCoordinates(i, j + 1), new IntegerCoordinates(i, j)));
                ans.add(new Pair<>(new IntegerCoordinates(j, i), new IntegerCoordinates(j + 1, i)));
                ans.add(new Pair<>(new IntegerCoordinates(j + 1, i), new IntegerCoordinates(j, i)));
            }

        for (boolean[] ints : mark) Arrays.fill(ints, false);
        dfs(new IntegerCoordinates(0, 0));

        int sizeofRemove = ans.size() / 4;
        while (sizeofRemove-- >= 0) {
            int id = random.nextInt(ans.size() / 2) * 2;
            ans.remove(id);
            ans.remove(id);
        }
        return ans;
    }

    private void dfs(IntegerCoordinates cell) {
        mark[cell.getX()][cell.getY()] = true;
        int[] X = {0, 0, -1, 1};
        int[] Y = {1, -1, 0, 0};
        getRandomNeighborOrder(X, Y);
        for (int i = 0; i < X.length; i++) {
            IntegerCoordinates neighbor = new IntegerCoordinates(cell.getX() + X[i], cell.getY() + Y[i]);

            if (SmallUsefullTool.badCell(neighbor) || mark[neighbor.getX()][neighbor.getY()])
                continue;

            dfs(neighbor);
            Pair<IntegerCoordinates, IntegerCoordinates> searchFor1 = new Pair<>(cell, neighbor);
            Pair<IntegerCoordinates, IntegerCoordinates> searchFor2 = new Pair<>(neighbor, cell);
            while (true) {
                Pair<IntegerCoordinates, IntegerCoordinates> tmp;
                if ((tmp = SmallUsefullTool.findObjectInCollection(searchFor1, ans)) != null)
                    ans.remove(tmp);
                else if ((tmp = SmallUsefullTool.findObjectInCollection(searchFor2, ans)) != null)
                    ans.remove(tmp);
                else
                    break;
            }
        }
    }

    private void getRandomNeighborOrder(int[] X, int[] Y) {
        assert X.length == Y.length;
        for (int i = 1; i < X.length; i++) {
            int newPosition = random.nextInt(i + 1);

            int tmp;
            tmp = X[i];
            X[i] = X[newPosition];
            X[newPosition] = tmp;

            tmp = Y[i];
            Y[i] = Y[newPosition];
            Y[newPosition] = tmp;
        }
    }
}
