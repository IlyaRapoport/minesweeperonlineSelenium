package msSolver;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class Grid implements Iterable<Cell> {
    private final int sizeX;
    private final int sizeY;
    private final Cell[][] grid;
    private final int totalMinesAmount;

    public Grid(int sizeX, int sizeY, int totalMinesAmount) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.totalMinesAmount = totalMinesAmount;
        grid = new Cell[this.sizeX][this.sizeY];

        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    public int sizeX() {
        return sizeX;
    }

    public int sizeY() {
        return sizeY;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    void updateGrid(@NotNull int[][] grid) {
        Objects.requireNonNull(grid);
        for (Cell cell : this) {
            if (cell.isSolved() || cell.isRevealed() || grid[cell.getX()][cell.getY()] == -1) {
                continue;
            }
            cell.setMineIndicator(grid[cell.getX()][cell.getY()]);
        }
    }

    public boolean checkCellInsideBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < sizeX && y < sizeY;
    }

    public List<Cell> findMinesNearby(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        List<Cell> minesNearby = new ArrayList<>();
        forEachCellsAround(cell, (cellNearby) -> {
            if (cellNearby.isMine()) {
                minesNearby.add(cellNearby);
            }
        });
        return minesNearby;
    }

    public List<Cell> findUndefinedNearby(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        List<Cell> undefinedNearby = new ArrayList<>();
        forEachCellsAround(cell, (cellNearby) -> {
            if (cellNearby.isUndefined()) {
                undefinedNearby.add(cellNearby);
            }
        });
        return undefinedNearby;
    }

    public List<Cell> findRevealedNearby(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        List<Cell> revealedNearby = new ArrayList<>();
        forEachCellsAround(cell, (cellNearby) -> {
            if (cellNearby.isRevealed()) {
                revealedNearby.add(cellNearby);
            }
        });
        return revealedNearby;
    }

    public List<Cell> findCellsBySharedUndefined(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        Set<Cell> cellsBySharedUndefined = new HashSet<>();
        List<Cell> undefinedNearby = findUndefinedNearby(cell);
        for (Cell undefinedCell : undefinedNearby) {
            cellsBySharedUndefined.addAll(findRevealedNearby(undefinedCell));
        }
        cellsBySharedUndefined.remove(cell);
        return new ArrayList<>(cellsBySharedUndefined);
    }

    @Override
    public String toString() {
        StringBuilder gridToString = new StringBuilder("\n");
        StringJoiner sj = new StringJoiner(" ", "   ", "\n");
        for (int i = 0; i < sizeX; i++) {
            sj.add(i + (i < 10 ? " " : ""));
        }
        gridToString.append(sj);
        for (int y = 0; y < this.sizeY; y++) {
            sj = new StringJoiner("  ", y + (y < 10 ? "  " : " "), "\n");
            for (int x = 0; x < this.sizeX; x++) {
                sj.add(grid[x][y].charRepresentation());
            }
            gridToString.append(sj);
        }
        return gridToString.toString();
    }

    @Override
    @NotNull
    public Iterator<Cell> iterator() {
        return new GridIterator();
    }

    @Override
    public void forEach(@NotNull Consumer<? super Cell> action) {
        Objects.requireNonNull(action);
        for (Cell cell : this) {
            action.accept(cell);
        }
    }

    public void forEachCellsAround(@NotNull Cell cell, @NotNull Consumer<? super Cell> action) {
        Objects.requireNonNull(cell);
        Objects.requireNonNull(action);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0 || !checkCellInsideBounds(cell.getX() + dx, cell.getY() + dy)) {
                    continue;
                }
                action.accept(getCell(cell.getX() + dx, cell.getY() + dy));
            }
        }
    }

    private class GridIterator implements Iterator<Cell> {
        private int indexX = 0;
        private int indexY = 0;

        @Override
        public boolean hasNext() {
            if (indexY == sizeY()) {
                return indexX < sizeX() - 1;
            }
            return true;
        }

        @Override
        public Cell next() {
            if (indexY == sizeY()) {
                indexX += 1;
                indexY = 0;
            }
            return grid[indexX][indexY++];
        }
    }
}