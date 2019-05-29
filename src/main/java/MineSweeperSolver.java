import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MineSweeperSolver {
    private Pair<Integer, Integer> size;
    private int[][] openedSquares;
    private int[][] mines;

    private boolean checkGridBorder(int x, int y) {
        return x < 0 || y < 0 || x >= size.getKey() || y >= size.getValue();
    }

    private List<Pair<Integer, Integer>> findBlankSquaresNearby(int x, int y){
        List<Pair<Integer, Integer>> blankSquaresNearby = new ArrayList<Pair<Integer, Integer>>();
        for (int x1 = -1; x1 < 1; x1++) {
            for (int y1 = -1; y1 < 1; y1++) {
                if (x1 == 0 && y1 == 0 || checkGridBorder(x + x1, y + y1)) {
                    continue;
                }
                if (openedSquares[x + x1][y + y1] == -1 && mines[x + x1][y + y1] == 0) {
                    blankSquaresNearby.add(new Pair<Integer, Integer>(x + x1, y + y1));
                }
            }
        }
        return blankSquaresNearby;
    }

    private List<Pair<Integer, Integer>> findMinesNearby(int x, int y){
        List<Pair<Integer, Integer>> minesNearby = new ArrayList<Pair<Integer, Integer>>();
        for (int x1 = -1; x1 <= 1; x1++) {
            for (int y1 = -1; y1 <= 1; y1++) {
                if (x1 == 0 && y1 == 0 || checkGridBorder(x + x1, y + y1)) {
                    continue;
                }
                if (mines[x + x1][y + y1] == 1) {
                    minesNearby.add(new Pair<Integer, Integer>(x + x1, y + y1));
                }
            }
        }
        return minesNearby;
    }

    public Pair[] solve(int[][] grid) {
        openedSquares = grid;
        if (size == null){
            size = new Pair<Integer, Integer>(openedSquares.length, openedSquares[openedSquares.length - 1].length);
        }
        if (mines == null){
            mines = new int[size.getKey()][size.getValue()];
        }

        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedSquares[x][y] == -1) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                List<Pair<Integer, Integer>> blankSquaresNearby = findBlankSquaresNearby(x, y);
                if (openedSquares[x][y] - minesNearby.size() == blankSquaresNearby.size()) {
                    for (Pair<Integer, Integer> bs : blankSquaresNearby) {
                        mines[bs.getKey()][bs.getValue()] = 1;
                    }
                }
            }
        }

        List<Pair<Integer, Integer>> squaresToOpen = new ArrayList<Pair<Integer, Integer>>();
        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedSquares[x][y] == -1) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                if (openedSquares[x][y] == minesNearby.size()){
                    squaresToOpen.addAll(findBlankSquaresNearby(x, y));
                }
            }
        }
        return squaresToOpen.toArray(new Pair[0]);
    }
}
