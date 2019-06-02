package msSolver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Cell implements Comparable<Cell> {
    private final int x;
    private final int y;
    @Nullable
    private Double mineProbability = null;
    @Nullable
    private Integer mineIndicator = null;
    private boolean solved = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Nullable
    public Double getMineProbability() {
        return mineProbability;
    }

    void setMineProbability(double mineProbability) throws IllegalArgumentException {
        if (this.mineProbability != null && (this.mineProbability == 0 || this.mineProbability == 1)) {
            throw new IllegalArgumentException(String.format("Mine probability is already certain for cell %s", toString()));
        }
        this.mineProbability = mineProbability;
        if (this.mineProbability == 1) {
            solved = true;
        }
    }

    public boolean isMine() {
        return mineProbability != null && mineProbability == 1;
    }

    @Nullable
    public Integer getMineIndicator() {
        return mineIndicator;
    }

    void setMineIndicator(int mineIndicator) throws IllegalArgumentException {
        if (this.mineIndicator != null && !this.mineIndicator.equals(mineIndicator)) {
            throw new IllegalArgumentException(String.format("Mine indicator is already defined as %d for cell %s",
                    this.mineIndicator, toString()));
        }
        if (isMine()) {
            throw new IllegalArgumentException(String.format("Cell %s is already defined as a mine", toString()));
        }
        this.mineIndicator = mineIndicator;
        mineProbability = 0.0;
        if (this.mineIndicator == 0) {
            solved = true;
        }
    }

    public boolean isRevealed() {
        return mineIndicator != null;
    }

    public boolean isUndefined() {
        return mineIndicator == null && (mineProbability == null || mineProbability != 1);
    }

    public boolean isSolved() {
        return solved;
    }

    void setSolved() {
        solved = true;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    @SuppressWarnings("ConstantConditions")
    public String charRepresentation() {
        if (isMine()) {
            return "*";
        }
        if (isRevealed()) {
            return mineIndicator.toString();
        }
        return "_";
    }

    @Override
    public int compareTo(@NotNull Cell cell) {
        Objects.requireNonNull(cell);
        if (this.x == cell.getX()) {
            return this.y - cell.getY();
        }
        return this.x - cell.getX();
    }
}
