package msSolver;

public class Cell {
    private final int x;
    private final int y;
    private Double mineProbability = null;
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

    public Double getMineProbability() {
        return mineProbability;
    }

    void setMineProbability(double mineProbability) throws IllegalArgumentException {
        if (this.mineProbability == 0 || this.mineProbability == 1) {
            throw new IllegalArgumentException(String.format("Mine probability is already certain at %d:%d", x, y));
        }
        this.mineProbability = mineProbability;
        if (this.mineProbability == 1) {
            solved = true;
        }
    }

    public boolean isMine() {
        return mineProbability == 1;
    }

    public Integer getMineIndicator() {
        return mineIndicator;
    }

    void setMineIndicator(int mineIndicator) throws IllegalArgumentException {
        if (this.mineIndicator != null && !this.mineIndicator.equals(mineIndicator)) {
            throw new IllegalArgumentException(String.format("Mine indicator is already defined as %d at %d:%d",
                    this.mineIndicator, x, y));
        }
        if (isMine()) {
            throw new IllegalArgumentException(String.format("msSolver.Cell is already defined as a mine at %d:%d", x, y));
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
        return mineIndicator == null && mineProbability != 1;
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
    public boolean equals(Object o) {
        return this == o;
    }
}
