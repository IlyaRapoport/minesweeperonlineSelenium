import javafx.util.Pair;

import java.util.*;

public class MineSweeperSolver {
    private Pair<Integer, Integer> size;
    private int[][] openedCells;
    private double[][] mines;
    private boolean[][] solved;
    private Integer totalMinesAmount;

    public MineSweeperSolver() {
    }

    public MineSweeperSolver(int totalMinesAmount) {
        this.totalMinesAmount = totalMinesAmount;
    }

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

    private List<Pair<Integer, Integer>> findCellsWithSharedBlanks(int x, int y) {
        Set<Pair<Integer, Integer>> cellsWithSharedBlanks = new HashSet<>();
        List<Pair<Integer, Integer>> blankCellsNearby = findBlankCellsNearby(x, y);
        for (Pair<Integer, Integer> bcn : blankCellsNearby) {
            cellsWithSharedBlanks.addAll(findOpenedCellsNearby(bcn.getKey(), bcn.getValue()));
        }
        cellsWithSharedBlanks.remove(new Pair<>(x, y));
        return new ArrayList<>(cellsWithSharedBlanks);
    }

    private List<Pair<Integer, Integer>> findAllCellsBySharedBlanks(int x, int y) {
        List<Pair<Integer, Integer>> cellsWithSharedBlanks = new ArrayList<>();
        List<Pair<Integer, Integer>> cellsTocCheck = new ArrayList<>();
        cellsWithSharedBlanks.add(new Pair<>(x, y));
        cellsTocCheck.add(new Pair<>(x, y));
        while (cellsTocCheck.size() > 0) {
            List<Pair<Integer, Integer>> allNewCellsWithSharedBlanks = new ArrayList<>();
            for (Pair<Integer, Integer> ncwsb : cellsTocCheck) {
                List<Pair<Integer, Integer>> blankCellsNearby = findBlankCellsNearby(ncwsb.getKey(), ncwsb.getValue());
                for (Pair<Integer, Integer> bcn : blankCellsNearby) {
                    List<Pair<Integer, Integer>> newCellsWithSharedBlanks = findOpenedCellsNearby(bcn.getKey(), bcn.getValue());
                    for (int i = 0; i < newCellsWithSharedBlanks.size(); i++) {
                        if (cellsWithSharedBlanks.contains(newCellsWithSharedBlanks.get(i))) {
                            newCellsWithSharedBlanks.remove(i);
                            i--;
                        }
                    }
                    allNewCellsWithSharedBlanks.addAll(newCellsWithSharedBlanks);
                    cellsWithSharedBlanks.addAll(newCellsWithSharedBlanks);
                }
            }
            cellsTocCheck.clear();
            cellsTocCheck.addAll(allNewCellsWithSharedBlanks);
        }
        return cellsWithSharedBlanks;
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

    private List<Map<Pair<Integer, Integer>, Integer>> generateValidVariations(
            List<Pair<Integer, Integer>> sharedBlanks, List<Pair<Integer, Integer>> sharedBlanksOwners) {
        List<Map<Pair<Integer, Integer>, Integer>> variants = new ArrayList<>();
        List<List<Integer>> sequences = generateBinarySequences(sharedBlanks.size());
        for (int i = 0; i < sequences.size(); i++) {
            variants.add(new HashMap<>());
            for (int j = 0; j < sharedBlanks.size(); j++) {
                variants.get(i).put(sharedBlanks.get(j), sequences.get(i).get(j));
            }
        }

        for (int i = 0; i < variants.size(); i++) {
            for (int j = 0; j < sharedBlanksOwners.size(); j++) {
                List<Pair<Integer, Integer>> minesNearby
                        = findMinesNearby(sharedBlanksOwners.get(j).getKey(), sharedBlanksOwners.get(j).getValue());
                List<Pair<Integer, Integer>> blankCellsNearby
                        = findBlankCellsNearby(sharedBlanksOwners.get(j).getKey(), sharedBlanksOwners.get(j).getValue());
                int variantMinesCount = 0;
                for (Pair<Integer, Integer> bcn : blankCellsNearby) {
                    if (variants.get(i).containsKey(bcn) && variants.get(i).get(bcn) == 1) {
                        variantMinesCount++;
                    }
                }
                if (openedCells[sharedBlanksOwners.get(j).getKey()][sharedBlanksOwners.get(j).getValue()]
                        - minesNearby.size() - variantMinesCount != 0) {
                    variants.remove(i);
                    i--;
                    break;
                }
            }
        }
        return variants;
    }

    private Map<Pair<Integer, Integer>, Double> findMineProbabilityForBlanks(List<Map<Pair<Integer, Integer>, Integer>> validVariations) {
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
            mines = new double[size.getKey()][size.getValue()];
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

        Set<Pair<Integer, Integer>> cellsToOpen = new HashSet<>();
        for (int x = 0; x < size.getKey(); x++) {
            for (int y = 0; y < size.getValue(); y++) {
                if (openedCells[x][y] == -1 || openedCells[x][y] == 0 || solved[x][y]) {
                    continue;
                }
                List<Pair<Integer, Integer>> minesNearby = findMinesNearby(x, y);
                if (openedCells[x][y] == minesNearby.size()) {
                    cellsToOpen.addAll(findBlankCellsNearby(x, y));
                }
            }
        }

        if (cellsToOpen.isEmpty()) {
            Set<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> cellsWithSharedBlanksCombinations = new HashSet<>();
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {
                    if (openedCells[x][y] == -1 || openedCells[x][y] == 0 || solved[x][y]) {
                        continue;
                    }
                    List<Pair<Integer, Integer>> cellsWithSharedBlanks = findCellsWithSharedBlanks(x, y);
                    for (Pair<Integer, Integer> cwsb : cellsWithSharedBlanks) {
                        if (cellsWithSharedBlanksCombinations.contains(new Pair<>(new Pair<>(x, y), cwsb))
                                || cellsWithSharedBlanksCombinations.contains(new Pair<>(cwsb, new Pair<>(x, y)))) {
                            continue;
                        }
                        Set<Pair<Integer, Integer>> sharedBlanks = new HashSet<>();
                        sharedBlanks.addAll(findBlankCellsNearby(x, y));
                        sharedBlanks.addAll(findBlankCellsNearby(cwsb.getKey(), cwsb.getValue()));
                        List<Pair<Integer, Integer>> sharedBlanksOwners = new ArrayList<>();
                        sharedBlanksOwners.add(new Pair<>(x, y));
                        sharedBlanksOwners.add(cwsb);
                        List<Map<Pair<Integer, Integer>, Integer>> validVariations = generateValidVariations(new ArrayList<>(sharedBlanks), sharedBlanksOwners);
                        Map<Pair<Integer, Integer>, Double> mineProbabilityForSharedBlanks = findMineProbabilityForBlanks(validVariations);
                        mineProbabilityForSharedBlanks.forEach((k, v) -> {
                            if (v == 1) {
                                mines[k.getKey()][k.getValue()] = 1;
                            } else if (v == 0) {
                                cellsToOpen.add(k);
                            }
                        });
                        cellsWithSharedBlanksCombinations.add(new Pair<>(new Pair<>(x, y), cwsb));
                    }
                }
            }
        }

        if (cellsToOpen.isEmpty()) {
            List<List<Pair<Integer, Integer>>> islandsBySharedBlanks = new ArrayList<>();
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {

                    if (openedCells[x][y] <= 0 || solved[x][y]) {
                        continue;
                    }
                    boolean toContinue = false;
                    for (List<Pair<Integer, Integer>> cellsWithSharedBlanks : islandsBySharedBlanks) {
                        if (cellsWithSharedBlanks.contains(new Pair<>(x, y))) {
                            toContinue = true;
                            break;
                        }
                    }
                    if (toContinue) {
                        continue;
                    }

                    islandsBySharedBlanks.add(findAllCellsBySharedBlanks(x, y));
                }
            }

            boolean exit = false;
            while (!exit) {
                exit = true;
                for (int i = 0; i < islandsBySharedBlanks.size(); i++) {
                    if (islandsBySharedBlanks.get(i).size() > 9) {
                        islandsBySharedBlanks.add(islandsBySharedBlanks.get(i).subList(0, islandsBySharedBlanks.get(i).size() / 2));
                        islandsBySharedBlanks.add(islandsBySharedBlanks.get(i).subList(islandsBySharedBlanks.get(i).size() / 2, islandsBySharedBlanks.get(i).size()));
                        islandsBySharedBlanks.remove(i);
                        exit = false;
                        break;
                    }
                }
            }

            List<List<Pair<Integer, Integer>>> sharedBlanksForIslands = new ArrayList<>();
            for (List<Pair<Integer, Integer>> cellsWithSharedBlanks : islandsBySharedBlanks) {
                Set<Pair<Integer, Integer>> sharedBlanksForCells = new HashSet<>();
                for (Pair<Integer, Integer> cell : cellsWithSharedBlanks) {
                    sharedBlanksForCells.addAll(findBlankCellsNearby(cell.getKey(), cell.getValue()));
                }
                sharedBlanksForIslands.add(new ArrayList<>(sharedBlanksForCells));
            }
            List<List<Map<Pair<Integer, Integer>, Integer>>> validVariationsForIslands = new ArrayList<>();
            for (int i = 0; i < sharedBlanksForIslands.size(); i++) {
                validVariationsForIslands.add(generateValidVariations(sharedBlanksForIslands.get(i), islandsBySharedBlanks.get(i)));
            }
            List<Map<Pair<Integer, Integer>, Double>> mineProbabilityForIslands = new ArrayList<>();
            for (List<Map<Pair<Integer, Integer>, Integer>> validVariationsForCells : validVariationsForIslands) {
                mineProbabilityForIslands.add(findMineProbabilityForBlanks(validVariationsForCells));
            }

            Pair<Integer, Integer> cellWithMinMineProbability;
            Double minProbability;
            mineProbabilityForIslands.forEach((mineProbabilityForSharedBlanks) ->
                    mineProbabilityForSharedBlanks.forEach((k, v) -> {
                        if (minProbability == null){

                        }
                        if (v == 1) {
                            mines[k.getKey()][k.getValue()] = 1;
                        } else if (v == 0) {
                            cellsToOpen.add(k);
                        }
                    }));

            // TODO empty cells probability
            // TODO use total mine count
        }

        if (cellsToOpen.isEmpty()) {
            for (int x = 0; x < size.getKey(); x++) {
                for (int y = 0; y < size.getValue(); y++) {
                    if (openedCells[x][y] == -1 && mines[x][y] != 1) {
                        cellsToOpen.add(new Pair<>(x, y));
                        return cellsToOpen.toArray(new Pair[0]);
                    }
                }
            }
        }

        //TODO check duplicates
        return cellsToOpen.toArray(new Pair[0]);
    }
}
