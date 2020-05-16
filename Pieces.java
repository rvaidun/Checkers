package Checkers;

public class Pieces {
    private int row;
    private int col;
    private boolean king;
    private String symbol;
    private boolean alive;
    private boolean color; // 0 is white, 1 is black
    private boolean mustMove;

    public Pieces(int r, int c, boolean color, int n) {
        this.row = r;
        this.col = c;
        this.symbol = (color ? "b" : "w") + (n < 10?"0"+n:n);
        this.king = false;
        this.alive = true;
        this.color = color;
//        this.canCut = false;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isKing() {
        return king;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getColor() {
        return color;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setKing(boolean king) {
        this.king = king;
        this.symbol = this.symbol.toUpperCase();
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean mustMove() {
        return mustMove;
    }

    public void setMustMove(boolean mustMove) {
        this.mustMove = mustMove;
    }
}
