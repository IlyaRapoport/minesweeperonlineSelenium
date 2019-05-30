import javafx.util.Pair;

import java.util.*;

public class MineSweeperSolver {
    private Pair<Integer, Integer> size;
    private int[][] openedCells;
    private int[][] mines;
    private boolean[][] solved;

    public static boolean checkGridBorder(int x, int y, Pair<Integer, Integer> size) {
        return x >= 0 && y >= 0 && x < size.getKey() && y < size.getValue();
    }

    private List<Pair<Integer, Integer>> findBlankCellsNearby(int x, int y) {
        List<Pair<Integer, Integer>> blankCellsNearby = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0 || !checkGridBorder(x + dx, y + dy, size)) {
                    continue;
                }
                if (openedCells[x + dx][y + dy] == -1 && mines[x + dx][y + dy] == 0) {
                    blankCellsNearby.add(new Pair<>(x + dx, y + dy));
                }
            }
        }
        return blankCellsNearby;
    }

    private List<Pair<Integer, Integer>> findMinesNearby(int x, int y) {
        List<Pair<Integer, Integer>> minesNearby = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0 || !checkGridBorder(x + dx, y + dy, size)) {
                    continue;
                }
                if (mines[x + dx][y + dy] == 1) {
                    minesNearby.add(new Pair<>(x + dx, y + dy));
                }
            }
        }
        return minesNearby;
    }

    private List<Pair<Integer, Integer>> findOpenedCellsNearby(int x, int y) {
        List<Pair<Integer, Integer>> openedCellsNearby = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0 || !checkGridBorder(x + dx, y + dy, size)) {
                    continue;
                }
                if (openedCells[x + dx][y + dy] != -1) {
                    openedCellsNearby.add(new Pair<>(x + dx, y + dy));
                }
            }
        }
        return openedCellsNearby;
    }

    //TODO find a non-direct neighbor
    private List<Pair<Integer, Integer>> findNeighborsWithSharedBlanks(int x, int y) {
        List<Pair<Integer, Integer>> neighborsWithSharedBlanks = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0 || !checkGridBorder(x + dx, y + dy, size)
                        || solved[x + dx][y + dy] || openedCells[x + dx][y + dy] == -1) {
                    continue;
                }
                List<Pair<Integer, Integer>> neighbor1BlankCellsNearby = findBlankCellsNearby(x, y);
                List<Pair<Integer, Integer>> neighbor2BlankCellsNearby = findBlankCellsNearby(x + dx, y + dy);
                for (Pair<Integer, Integer> n2bcn : neighbor2BlankCellsNearby) {
                    if (neighbor1BlankCellsNearby.contains(n2bcn)) {
                        neighborsWithSharedBlanks.add(new Pair<>(x + dx, y + dy));
                        break;
                    }
                }
            }
        }
        return neighborsWithSharedBlanks;
    }

    private List<List<Integer>> generateBinarySequences(int size) {
        List<List<Integer>> sequences = new ArrayList<>();
        sequences.add(new ArrayList<>());
        for (int i = 0; i < size; i++) {
            sequences.get(0).add(0);
        }
        double until = Math.pow(2, size);
        for (int i = 1; i < until; i++) {
            sequences.add(new ArrayList<>(sequences.get(i - 1)));
            for (int j = size - 1; j >= 0; j--) {
                if (sequences.get(i).get(j) == 0) {
                    sequences.get(i).set(j, 1);
                    for (int k = j + 1; k < size; k++) {
                        sequences.get(i).set(k, 0);
                    }
                    break;
                }
            }
        }
        return sequences;
    }

    private List<Map<Pair<Integer, Integer>, Integer>> generateValidVariations(List<Pair<Integer, Integer>> sharedBlanks) {
        List<Map<Pair<Integer, Integer>, Integer>> variants = new ArrayList<>();
        List<List<Integer>> sequences = generateBinarySequences(sharedBlanks.size());
        for (int i = 0; i < sequences.size(); i++) {
            variants.add(new HashMap<>());
            for (int j = 0; j < sharedBlanks.size(); j++) {
                variants.get(i).put(sharedBlanks.get(j), sequences.get(i).get(j));
            }
        }

        for (int i = 0; i < variants.size(); i++) {
            Set<Pair<Integer, Integer>> sharedOpened = new HashSet<>();
            variants.get(i).forEach((k, v) -> sharedOpened.addAll(findOpenedCellsNearby(k.getKey(), k.getValue())));
            List<Pair<Integer, Integer>> sharedOpenedList = new ArrayList<>(sharedOpened);
            for (int j = 0; j < sharedOpenedList.size(); j++) {
                List<Pair<Integer, Integer>> minesNearby
                        = findMinesNearby(sharedOpenedList.get(j).getKey(), sharedOpenedList.get(j).getValue());
                List<Pair<Integer, Integer>> blankCellsNearby
                        = findBlankCellsNearby(sharedOpenedList.get(j).getKey(), sharedOpenedList.get(j).getValue());
                int variantMinesCount = 0;
                for (Pair<Integer, Integer> bcn : blankCellsNearby) {
                    if (variants.get(i).containsKey(bcn) && variants.get(i).get(bcn) == 1) {
                        variantMinesCount++;
                    }
                }
                if (openedCells[sharedOpenedList.get(j).getKey()][sharedOpenedList.get(j).getValue()]
                        - minesNearby.size() - variantMinesCount < 0) {
                    variants.remove(i);
                    i--;
                    break;
                }
            }
        }

        return variants;
    }

    private Map<Pair<Integer, Integer>, Double> findMineProbabilityForSharedBlanks(List<Map<Pair<Integer, Integer>, Integer>> validVariations) {
        Map<Pair<Integer, Integer>, Double> mineProbabilityForSharedBlanks = new HashMap<>();
        if (!validVariations.isEmpty()) {
            validVariations.get(0).forEach((k, v) -> mineProbabilityForSharedBlanks.put(k, v.doubleValue()));
        }
        for (int i = 1; i < validVariations.size(); i++) {
            validVariations.get(i).forEach((k, v) ->
                    mineProbabilityForSharedBlanks.replace(k, mineProbabilityForSharedBlanks.get(k) + v));
        }
        mineProbabilityForSharedBlanks.forEach((k, v) ->
                mineProbabilityForSharedBlanks.replace(k, v / validVariations.size()));

        return mineProbabilityForSharedBlanks;
    }

    public Pair[] solve(int[][] grid) {
        openedCells = grid;
        if (size == null) {
            size = new Pair<>(openedCells.length, openedCells[openedCells.length - 1].length);
        }
        if (mines == null) {
            mines = new int[size.getKey()][size.getValue()];
        }
        if (solved == null) {
            solved = new boolean[size.getKey()][size.getValue()];
        }

        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedCells[x][y] == -1 || openedCells[x][y] == 0 || solved[x][y]) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                List<Pair<Integer, Integer>> blankCellsNearby = findBlankCellsNearby(x, y);
                if (blankCellsNearby.size() == 0) {
                    solved[x][y] = true;
                }
                if (openedCells[x][y] - minesNearby.size() == blankCellsNearby.size()) {
                    for (Pair<Integer, Integer> bs : blankCellsNearby) {
                        mines[bs.getKey()][bs.getValue()] = 1;
                    }
                }
            }
        }

        Set<Pair<Integer, Integer>> CellsToOpen = new HashSet<>();
        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedCells[x][y] == -1 || openedCells[x][y] == 0 || solved[x][y]) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                if (openedCells[x][y] == minesNearby.size()) {
                    CellsToOpen.addAll(findBlankCellsNearby(x, y));
                }
            }
        }

        if (CellsToOpen.isEmpty()) {
            Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> checkedNeighborCombinations = new HashSet<>();
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {
                    if (openedCells[x][y] == -1 || openedCells[x][y] == 0 || solved[x][y]) {
                        continue;
                    }
                    List<Pair<Integer, Integer>> neighborsWithSharedBlanks = findNeighborsWithSharedBlanks(x, y);
                    for (Pair<Integer, Integer> neighbor : neighborsWithSharedBlanks) {
                        if (checkedNeighborCombinations.contains(new Pair<>(new Pair<>(x, y), neighbor))
                                || checkedNeighborCombinations.contains(new Pair<>(neighbor, new Pair<>(x, y)))) {
                            continue;
                        }
                        Set<Pair<Integer, Integer>> sharedBlanks = new HashSet<>();
                        sharedBlanks.addAll(findBlankCellsNearby(x, y));
                        sharedBlanks.addAll(findBlankCellsNearby(neighbor.getKey(), neighbor.getValue()));
                        List<Map<Pair<Integer, Integer>, Integer>> validVariations = generateValidVariations(new ArrayList<>(sharedBlanks));
                        Map<Pair<Integer, Integer>, Double> mineProbabilityForSharedBlanks = findMineProbabilityForSharedBlanks(validVariations);
                        mineProbabilityForSharedBlanks.forEach((k, v) -> {
                            if (v == 1) {
                                mines[k.getKey()][k.getValue()] = 1;
                            } else if (v == 0) {
                                CellsToOpen.add(k);
                            }
                        });
                        checkedNeighborCombinations.add(new Pair<>(new Pair<>(x, y), neighbor));
                    }
                }
            }
        }

        if (CellsToOpen.isEmpty()) {
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {
                    if (openedCells[x][y] == -1 && mines[x][y] != 1) {
                        CellsToOpen.add(new Pair<>(x, y));
                        return CellsToOpen.toArray(new Pair[0]);
                    }
                }
            }
        }

        //TODO check duplicates
        return CellsToOpen.toArray(new Pair[0]);
    }
}
