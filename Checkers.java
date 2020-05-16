package Checkers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Checkers {
    private Pieces[][] board = new Pieces[8][8];
    private Pieces[][] pieces = new Pieces[2][12];
    private int aliveBlack = 12;
    private int aliveWhite = 12;
    private int moveCount = 0;
    private Pieces[] mustMove = new Pieces[12];
    private Pieces[] cuttablePieces = new Pieces[4];
    private int cuttableCount = 0;
    Scanner pen;

    public Checkers(Scanner pen) {
        this.pen = pen;
        int total = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 8; c++) {
                if (c % 2 != r % 2) {
                    Pieces piece = new Pieces(r, c, false, total);
                    pieces[0][total] = piece;
                    board[r][c] = piece;
                    total++;
                }
            }
        }
        total = 0;
        for (int r = 7; r > 4; r--) {
            for (int c = 0; c < 8; c++) {
                if (c % 2 != r % 2) {
                    Pieces piece = new Pieces(r, c, true, total);
                    pieces[1][total] = piece;
                    board[r][c] = piece;
                    total++;
                }
            }
        }
    }

    private void printBoard() {
        System.out.println("               White " + aliveWhite);
        for (int r = 0; r < 8; r++) {
            System.out.println("|---+---+---+---+---+---+---+---|");
            System.out.print("|");
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    System.out.print(board[r][c].getSymbol() + "|");
                } else {

                    System.out.print("   |");
                }
            }
            System.out.println();
        }
        System.out.println("|---+---+---+---+---+---+---+---|");
        System.out.println("               Black " + aliveBlack);
    }

    public Pieces validPiece(int pieceToMove, int turn) {

        if (pieceToMove >= 0 && pieceToMove < 12) {
            if (pieces[turn][pieceToMove].isAlive()) {
                return pieces[turn][pieceToMove];
            }
        }
        return null;
    }

    public boolean inRange (int r, int c) {
        return (r >= 0 && r < 8 && c >= 0 && c < 8);
    }

    public Pieces canCut(Pieces p, int f, int l) {
        int r = p.getRow();
        int c = p.getCol();
        boolean color = p.getColor();
        r += f * (color ? -1 : 1);
        c += l;
        if (inRange(r,c) && (board[r][c] != null && (board[r][c].getColor() != color))) { // else if next spot is opposite color then check next spot is blank.
                int newr = r + f * (color ? -1 : 1);
                int newc = c + l;
                if (inRange(newr, newc) && board[newr][newc] == null) {
                    p.setMustMove(true);
                    return board[r][c];
                }
        }
        return null;
    }

    private boolean movePiece(Pieces p, boolean f, boolean l) {
        int r = p.getRow();
        int c = p.getCol();
        boolean color = p.getColor();
        r += (f ? 1 : -1) * (color ? -1 : 1);
        c += (l ? 1 : -1);
        if (inRange(r,c) && (board[r][c] == null)) { //if next spot is free move there
                updateBoard(p, r, c);
                return true; // 1 means move and next Player
        }
        System.out.println("Invalid move");
        return false;
    }

    private void updateBoard(Pieces p, int r, int c) {
        board[p.getRow()][p.getCol()] = null;
        board[r][c] = p;
        p.setCol(c);
        p.setRow(r);
        if ((r == 0 && p.getColor()) ||
                (r == 7 && !p.getColor())) { //  Piece became King
            p.setKing(true);
        }
        System.out.println();
        printBoard();
    }
    private void findMoves(int t) {
        moveCount = 0;
        for(int i = 0; i < 12; i++) {
            Pieces p = pieces[t][i];
            if(p.isAlive()) {
                findCuts(p, false);
                if (p.mustMove()) {
                    mustMove[moveCount] = p;
                    moveCount++;
                }
            }
        }
    }
    private void findCuts(Pieces p, boolean all){
        Pieces cp;

        cuttableCount = 0;
        p.setMustMove(false);
        for (int r = 1; r >=-1; r-=2) {
            for (int c = 1; c >= -1; c -= 2) {
                cp = canCut(p,r,c);
                if (cp != null)
                    if (all) {
                        cuttablePieces[cuttableCount++] = cp;
                    } else{
                        return;
                    }
            }
            if (!p.isKing()) break;
        }
    }

    public void pickFromMoves() {
        int x;
        Pieces moveP, cutP;
        moveP = pickPiece(mustMove, moveCount);
        findCuts(moveP, true);
        while(cuttableCount > 0) {
            cutP = findPiecetoCut(moveP);
            cut(moveP,cutP);
            findCuts(moveP, true);
        }
    }
    private void cut(Pieces moveP, Pieces cutP) {
        int r = moveP.getRow();
        int c = moveP.getCol();
        int nr = cutP.getRow();
        int nc = cutP.getCol();
        int newr = r +(nr-r)*2;
        int newc = c +(nc-c)*2;
        board[nr][nc] = null;
        cutP.setAlive(false);
        if (cutP.getColor()) {
            aliveBlack--;
        } else {
            aliveWhite--;
        }
        System.out.println(moveP.getSymbol() + " is cutting " + cutP.getSymbol() + ". \nPress return to continue.");
        pen.nextLine();
        updateBoard(moveP,newr,newc);
    }

    private Pieces findPiecetoCut(Pieces moveP) {
        Pieces cutP;
        if (cuttableCount > 1) {
                System.out.println("Which piece to cut with " + moveP.getSymbol());
                cutP = pickPiece(cuttablePieces,cuttableCount);
        } else {
            cutP = cuttablePieces[0];
        }
        return cutP;
    }

    private Pieces pickPiece(Pieces [] arr, int n) {
        int x;
        if (n == 1) {
            return arr[0];
        }
        while (true) {
            System.out.println("Choose a number 1-" + n);
            for (int i = 0; i < n; i++) {
                System.out.println(i + 1 + ". " + arr[i].getSymbol());
            }
            x = getValidInt()-1;
            if (x < 0 || x >= n) {
                System.out.println("Invalid Choice");
                continue;
            }
            return arr[x];
        }
    }

    private int getValidInt() throws InputMismatchException {
        while (true) {
            try {
                int x = pen.nextInt();
                pen.nextLine();
                return x;
            } catch (InputMismatchException e) {
                pen.nextLine();
                System.out.println("Invalid Input");
            }
        }
    }

    public static void main(String[] args) throws InputMismatchException {
        Scanner pen = new Scanner(System.in);
        Checkers checkers = new Checkers(pen);
        int turn = 1; //White
        Pieces p;
        boolean front = true;
        boolean east = true;
        checkers.printBoard();
        while (checkers.aliveWhite != 0 && checkers.aliveBlack != 0) {
            System.out.println(turn == 1 ? "Black" : "White");
            checkers.findMoves(turn);
            if(checkers.moveCount != 0) {
                checkers.pickFromMoves();
            } else {
                while (true) {
                    System.out.println("Select which piece to move");
                    int pieceToMove = checkers.getValidInt();
                    p = checkers.validPiece(pieceToMove, turn);
                    if (p == null) {
                        System.out.println("Invalid Piece Selected");
                        continue;
                    }
                    front = true;
                    if (p.isKing()) {
                        System.out.println("Forward or Backward (F or B)");
                        front = pen.nextLine().toLowerCase().equals("f");
                    }
                    System.out.println("Right or Left (R or L)");
                    east = pen.nextLine().toLowerCase().equals("r");
                    if (checkers.movePiece(p, front, east)) {
                        break;
                    }
                }
            }
            turn = (turn + 1) % 2;

        }
        if (checkers.aliveBlack == 0) {
            System.out.println("White Player wins");
        } else {
            System.out.println("Black Player wins");
        }

    }
}