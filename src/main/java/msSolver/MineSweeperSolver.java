package msSolver;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MineSweeperSolver {
    private final Grid grid;

    public MineSweeperSolver(int sizeX, int sizeY, int totalMinesAmount) {
        this.grid = new Grid(sizeX, sizeY, totalMinesAmount);
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell getCell(int x, int y) {
        return grid.getCell(x, y);
    }

    public Cell[] solve(@NotNull int[][] updatedGrid) {
        Objects.requireNonNull(updatedGrid);
        grid.updateGrid(updatedGrid);

        for (Cell cell : grid) {
            if (cell.isSolved() || !cell.isRevealed()) {
                continue;
            }
            List<Cell> minesNearby = grid.findMinesNearby(cell);
            List<Cell> undefinedNearby = grid.findUndefinedNearby(cell);
            if (cell.getMineIndicator() - minesNearby.size() == undefinedNearby.size()) {
                for (Cell undefinedCell : undefinedNearby) {
                    undefinedCell.setMineProbability(1.0);
                }
                cell.setSolved();
            }
        }

        Set<Cell> cellsToReveal = new HashSet<>();
        for (Cell cell : grid) {
            if (cell.isSolved() || !cell.isRevealed()) {
                continue;
            }
            List<Cell> minesNearby = grid.findMinesNearby(cell);
            if (cell.getMineIndicator() == minesNearby.size()) {
                cellsToReveal.addAll(grid.findUndefinedNearby(cell));
                cell.setSolved();
            }
        }

        if (!cellsToReveal.isEmpty()) {
            return cellsToReveal.toArray(new Cell[0]);
        }

        System.out.println(grid);

        Set<Set<Cell>> pairsBySharedUndefined = new HashSet<>();
        for (Cell cell : grid) {
            if (cell.isSolved() || !cell.isRevealed()) {
                continue;
            }
            List<Cell> cellsBySharedUndefined = grid.findCellsBySharedUndefined(cell);
            for (Cell cellByShared : cellsBySharedUndefined) {
                Set<Cell> pairByShared = new TreeSet<>();
                pairByShared.add(cell);
                pairByShared.add(cellByShared);
                if (pairsBySharedUndefined.contains(pairByShared)) {
                    continue;
                }
                List<Map<Cell, Integer>> variations = generateVariations(new ArrayList<>(pairByShared));
                Map<Cell, Double> mineProbability = findMineProbability(variations);
                mineProbability.forEach((k, v) -> {
                    if (v == 1) {
                        k.setMineProbability(1.0);
                    } else if (v == 0) {
                        cellsToReveal.add(k);
                    }
                });
                pairsBySharedUndefined.add(pairByShared);
            }
        }

        System.out.println(grid);

        if (cellsToReveal.isEmpty()) {
            System.exit(0);
        }

        return cellsToReveal.toArray(new Cell[0]);
    }

    private List<Map<Cell, Integer>> generateVariationsForCell(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        if (!cell.isRevealed()) {
            throw new IllegalArgumentException(String.format("Cell %s is not revealed", cell.toString()));
        }

        List<Map<Cell, Integer>> variations = new ArrayList<>();
        List<Cell> undefinedNearby = grid.findUndefinedNearby(cell);
        List<Cell> minesNearby = grid.findMinesNearby(cell);
        variations.add(new HashMap<>());
        for (Cell undefinedCell : undefinedNearby) {
            variations.get(0).put(undefinedCell, 0);
        }
        int minesAmountInVariations = cell.getMineIndicator() - minesNearby.size();
        for (int i = 0; i < minesAmountInVariations; i++) {
            variations.get(0).replace(undefinedNearby.get(i), 1);
        }

        Map<Cell, Integer> newVariation = new HashMap<>(variations.get(variations.size() - 1));
        for (int i = minesAmountInVariations - 1, onesInStack = 0; onesInStack < minesAmountInVariations; i--) {
            if (newVariation.get(undefinedNearby.get(i)) == 1) {
                newVariation.replace(undefinedNearby.get(i), 0);
                onesInStack++;
                if (i + onesInStack < undefinedNearby.size()) {
                    while (onesInStack > 0) {
                        newVariation.replace(undefinedNearby.get(++i), 1);
                        onesInStack--;
                    }
                    variations.add(new HashMap<>(newVariation));
                    i++;
                    onesInStack = 0;
                }
            }
        }

        return variations;
    }

    private List<Map<Cell, Integer>> generateVariations(@NotNull List<Cell> cellsByShared) {
        Objects.requireNonNull(cellsByShared);
        List<Map<Cell, Integer>> variations = new ArrayList<>();
        if (cellsByShared.isEmpty()) {
            return variations;
        }

        variations = generateVariationsForCell(cellsByShared.get(0));
        for (Cell cell : cellsByShared) {
            List<Map<Cell, Integer>> nextCellVariations = generateVariationsForCell(cell);
            List<Map<Cell, Integer>> combinedVariations = new ArrayList<>();
            for (Map<Cell, Integer> variant : variations) {
                for (Map<Cell, Integer> variantNextCell : nextCellVariations) {
                    boolean validCombination = true;
                    for (Cell cellInVariant : variant.keySet()) {
                        if (variantNextCell.containsKey(cellInVariant)
                                && !variant.get(cellInVariant).equals(variantNextCell.get(cellInVariant))) {
                            validCombination = false;
                            break;
                        }
                    }
                    if (!validCombination) {
                        continue;
                    }
                    //TODO check for duplicates
                    combinedVariations.add(variant);
                    for (Map.Entry<Cell, Integer> entry : variantNextCell.entrySet()) {
                        if (combinedVariations.get(combinedVariations.size() - 1).containsKey(entry.getKey())) {
                            continue;
                        }
                        combinedVariations.get(combinedVariations.size() - 1).put(entry.getKey(), entry.getValue());
                    }
                }
            }
            variations = combinedVariations;
        }

        return variations;
    }

    private Map<Cell, Double> findMineProbability(@NotNull List<Map<Cell, Integer>> variations) {
        Objects.requireNonNull(variations);
        Map<Cell, Double> mineProbability = new HashMap<>();
        if (variations.isEmpty()) {
            return mineProbability;
        }

        variations.get(0).forEach((k, v) -> mineProbability.put(k, v.doubleValue()));

        for (Map<Cell, Integer> variant : variations) {
            variant.forEach((k, v) ->
                    mineProbability.replace(k, mineProbability.get(k) + v));
        }
        mineProbability.forEach((k, v) ->
                mineProbability.replace(k, v / variations.size()));

        return mineProbability;
    }
}
