public class Cell {
    private int row;
    private int col;
    private boolean isWall;

    public Cell(boolean isWall, int row, int col) {
        this.isWall = isWall;
        this.row = row;
        this.col = col;
    }

    public void refreshValue() {
        this.setWall(this.isWall());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    @Override
    public String toString() {
        if (isWall) {
            return "X";
        } else {
            return ".";
        }
    }

    public String returnValue() {
        if (isWall) {
            return "X";
        } else {
            return ".";
        }
        
    }
}