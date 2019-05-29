import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MineSweeperSolver {
    private Pair<Integer, Integer> size;
    private int[][] openedSquares;
    private int[][] mines;
    private boolean[][] solved;

    private boolean checkGridBorder(int x, int y) {
        return x >= 0 && y >= 0 && x < size.getKey() && y < size.getValue();
    }

    private List<Pair<Integer, Integer>> findBlankSquaresNearby(int x, int y) {
        List<Pair<Integer, Integer>> blankSquaresNearby = new ArrayList<>();
        for (int x1 = -1; x1 <= 1; x1++) {
            for (int y1 = -1; y1 <= 1; y1++) {
                if (x1 == 0 && y1 == 0 || !checkGridBorder(x + x1, y + y1)) {
                    continue;
                }
                if (openedSquares[x + x1][y + y1] == -1 && mines[x + x1][y + y1] == 0) {
                    blankSquaresNearby.add(new Pair<>(x + x1, y + y1));
                }
            }
        }
        return blankSquaresNearby;
    }

    private List<Pair<Integer, Integer>> findMinesNearby(int x, int y) {
        List<Pair<Integer, Integer>> minesNearby = new ArrayList<>();
        for (int x1 = -1; x1 <= 1; x1++) {
            for (int y1 = -1; y1 <= 1; y1++) {
                if (x1 == 0 && y1 == 0 || !checkGridBorder(x + x1, y + y1)) {
                    continue;
                }
                if (mines[x + x1][y + y1] == 1) {
                    minesNearby.add(new Pair<>(x + x1, y + y1));
                }
            }
        }
        return minesNearby;
    }

    private void printGrid(int[][] grid) {
        for (int y = 0; y < size.getValue(); y++) {
            for (int x = 0; x < size.getKey(); x++) {
                System.out.print(grid[x][y] != -1 ? grid[x][y] : "_");
            }
            System.out.println();
        }
        System.out.println("x:" + grid.length);
        System.out.println("y:" + grid[grid.length - 1].length);
    }

    public Pair[] solve(int[][] grid) {
        openedSquares = grid;
        if (size == null) {
            size = new Pair<>(openedSquares.length, openedSquares[openedSquares.length - 1].length);
        }
        if (mines == null) {
            mines = new int[size.getKey()][size.getValue()];
        }
        if (solved == null){
            solved = new boolean[size.getKey()][size.getValue()];
        }

//        System.out.println("Grid:");
//        System.out.println();
//        printGrid(openedSquares);

        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedSquares[x][y] == -1 || openedSquares[x][y] == 0 || solved[x][y]) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                List<Pair<Integer, Integer>> blankSquaresNearby = findBlankSquaresNearby(x, y);
                if (blankSquaresNearby.size() == 0) {
                    solved[x][y] = true;
                }
                if (openedSquares[x][y] - minesNearby.size() == blankSquaresNearby.size()) {
                    for (Pair<Integer, Integer> bs : blankSquaresNearby) {
                        mines[bs.getKey()][bs.getValue()] = 1;
                    }
                }
//                System.out.println("Finding mines for square " + x + " " + y + ":");
//                System.out.println(minesNearby);
//                System.out.println("Finding blanks for square " + x + " " + y + ":");
//                System.out.println(blankSquaresNearby);
            }
        }

        Set<Pair<Integer, Integer>> squaresToOpen = new HashSet<>();
        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedSquares[x][y] == -1 || openedSquares[x][y] == 0 || solved[x][y]) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
//                System.out.println("Finding mines for square " + x + " " + y + ":");
//                System.out.println(minesNearby);
                if (openedSquares[x][y] == minesNearby.size()) {
                    squaresToOpen.addAll(findBlankSquaresNearby(x, y));
                }
            }
        }
//        System.out.println("Found mines:");
//        printGrid(mines);
//        System.out.println("Squares to open:");
//        System.out.println(squaresToOpen);
        if (squaresToOpen.isEmpty()) {
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {
                    if (openedSquares[x][y] != -1 || mines[x][y] == 1) {
                        continue;
                    }
                    squaresToOpen.add(new Pair<>(x, y));
                    return squaresToOpen.toArray(new Pair[0]);
                }
            }
        }

        return squaresToOpen.toArray(new Pair[0]);
    }
}
