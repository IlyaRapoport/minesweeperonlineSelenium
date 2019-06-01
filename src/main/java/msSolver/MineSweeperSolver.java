package msSolver;

import javafx.util.Pair;

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

    public Cell[] solve(int[][] updatedGrid) {
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

        Set<Pair<Cell, Cell>> pairsBySharedUndefined = new HashSet<>();
        for (Cell cell : grid) {
            if (cell.isSolved() || !cell.isRevealed()) {
                continue;
            }
            List<Cell> cellsBySharedUndefined = grid.findCellsBySharedUndefined(cell);
            for (Cell cellByShared : cellsBySharedUndefined) {
                Pair<Cell, Cell> pairByShared;
                if (cell.compareTo(cellByShared) < 0) {
                    pairByShared = new Pair<>(cell, cellByShared);
                } else {
                    pairByShared = new Pair<>(cellByShared, cell);
                }
                if (pairsBySharedUndefined.contains(pairByShared)) {
                    continue;
                }
                List<Map<Cell, Integer>> validVariations = generateVariations(pairByShared);
            }
        }

        if (cellsToReveal.isEmpty()) {
            System.exit(0);
        }

        return cellsToReveal.toArray(new Cell[0]);
    }

    private List<Map<Cell, Integer>> generateVariations(Cell cell) {
        List<Map<Cell, Integer>> variations = new ArrayList<>();
        List<Cell> undefinedNearby = grid.findUndefinedNearby(cell);
        List<Cell> minesNearby = grid.findMinesNearby(cell);
        variations.add(new HashMap<>());
        for (Cell undefinedCell : undefinedNearby) {
            variations.get(0).put(undefinedCell, 0);
        }
        for (int i = 0; i < cell.getMineIndicator() - minesNearby.size(); i++) {
            variations.get(0).replace(undefinedNearby.get(i), 1);
        }
        
        //TODO simplify
        int index = cell.getMineIndicator() - minesNearby.size() - 1;
        Map<Cell, Integer> lastVariation = variations.get(variations.size() - 1);
        boolean exit = false;
        while (!exit) {
            variations.add(new HashMap<>(lastVariation));
            lastVariation = variations.get(variations.size() - 1);
            int onesToReset = 0;
            while (true) {
                if (lastVariation.get(undefinedNearby.get(index)) != 1) {
                    index--;
                } else if (index < undefinedNearby.size() - 1 - onesToReset) {
                    lastVariation.replace(undefinedNearby.get(index), 0);
                    for (int i = 0; i <= onesToReset + 1; i++) {
                        lastVariation.replace(undefinedNearby.get(++index), 1);
                    }
                    break;
                } else if (onesToReset < cell.getMineIndicator() - minesNearby.size()){
                    lastVariation.replace(undefinedNearby.get(index), 0);
                    onesToReset++;
                } else {
                    variations.remove(variations.size() - 1);
                    exit = true;
                    break;
                }
            }
        }

        return variations;
    }

    private List<Map<Cell, Integer>> generateVariations(Pair<Cell, Cell> pairByShared) {
        List<Map<Cell, Integer>> variations = new ArrayList<>();
        Set<Cell> sharedUndefinedSet = new HashSet<>();
        sharedUndefinedSet.addAll(grid.findUndefinedNearby(pairByShared.getKey()));
        sharedUndefinedSet.addAll(grid.findUndefinedNearby(pairByShared.getValue()));
        List<Cell> sharedUndefined = new ArrayList<>(sharedUndefinedSet);
        int[] sequence = new int[sharedUndefined.size()];
        for (int i = 0; i < sharedUndefined.size(); i++) {

        }
    }
}
