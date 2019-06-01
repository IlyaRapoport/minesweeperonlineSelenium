package msSolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MineSweeperSolver {
    private final Grid grid;

    public MineSweeperSolver(int sizeX, int sizeY, int totalMinesAmount) {
        this.grid = new Grid(sizeX, sizeY, totalMinesAmount);
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell getCell(int x, int y){
        return grid.getCell(x, y);
    }

    public Cell[] solve(int[][] updatedGrid) {
        grid.updateGrid(updatedGrid);

        for (Cell cell : grid) {
            if (cell.isSolved() || !cell.isRevealed()){
                continue;
            }
            List<Cell> minesNearby = grid.findMinesNearby(cell);
            List<Cell> undefinedNearby = grid.findUndefinedCellsNearby(cell);
            if (cell.getMineIndicator() - minesNearby.size() == undefinedNearby.size()) {
                for (Cell undefinedCell : undefinedNearby) {
                    undefinedCell.setMineProbability(1.0);
                }
                cell.setSolved();
            }
        }

        Set<Cell> cellsToReveal = new HashSet<>();
        for (Cell cell : grid){
            if (cell.isSolved() || !cell.isRevealed()){
                continue;
            }
            List<Cell> minesNearby = grid.findMinesNearby(cell);
            if (cell.getMineIndicator() == minesNearby.size()){
                cellsToReveal.addAll(grid.findUndefinedCellsNearby(cell));
                cell.setSolved();
            }
        }

        return cellsToReveal.toArray(new Cell[0]);
    }
}
