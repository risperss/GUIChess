public class Square {
    private Piece piece;
    private final int ROW;
    private final int COL;

    Square(int ROW, int COL, Piece piece) {
        this.ROW = ROW;
        this.COL = COL;
        setPiece(piece);
        ;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }

    public String toString() {
        String alph = "ABCDEFGH";

        String startCol = String.valueOf(alph.charAt(getCOL()));
        int startRow = getROW() + 1;

        return startCol + startRow;
    }

    public boolean equalsSquare(Square otherSquare) {
        int r1 = getROW();
        int c1 = getCOL();

        int r2 = otherSquare.getROW();
        int c2 = otherSquare.getCOL();

        return r1 == r2 && c1 == c2;
    }
}