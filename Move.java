public class Move {
    private Player player;
    private Square startSquare;
    private Square endSquare;
    private Piece startPiece;
    private Piece endPiece;
    private Board board;
    private MoveType type;
    private Piece promotionPiece;

    /////// FOR WHEN THE COMPUTER CREATES THE LIST OF ITS LEGAL MOVES/////////
    //// THIS HAS FULL INFORMATION WHICH ALLOW YOU THE MOVE TO BE MADE/////

    Move(Player player, Square startSquare, Square endSquare, Board board, MoveType type) {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.board = board;
        this.type = type;
    }

    Move(Player player, Square startSquare, Square endSquare, Board board, MoveType type, Piece promotionPiece) {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.board = board;
        this.type = type;
        this.promotionPiece = promotionPiece;
    }

    // FOR WHEN THE USER IS INPUTTING THE MOVE
    // DONT NEED TO INPUT FULL INFORMATION BECAUSE THAT CAN BE FOUND OUT
    // THROUGH THE START AND END SQUARES

    Move(Player player, Square startSquare, Square endSquare) {
        this.player = player;
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.startPiece = startSquare.getPiece();
        this.endPiece = endSquare.getPiece();
        this.type = null;
    }

    public void movePiece() {
        switch (type) {
            case NORMAL:
                moveNormally();
                break;
            case CASTLE:
                castle();
                break;
            case PAWN_PROMOTION:
                promotePawn();
                break;
            case EN_PASSANT:
                enPassant();
                break;
        }
    }

    private void moveNormally() {
        startPiece.setMoveCount(startPiece.getMoveCount() + 1);
        endSquare.setPiece(startPiece);
        startSquare.setPiece(null);
    }

    private void castle() {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();
        Move moveRook = null;

        if (destSquare.equalsSquare(currBoard.getSquare(0, 6))) {
            moveRook = new Move(getPlayer(), board.getSquare(0, 7), board.getSquare(0, 5), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(0, 2))) {
            moveRook = new Move(getPlayer(), board.getSquare(0, 0), board.getSquare(0, 3), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 6))) {
            moveRook = new Move(getPlayer(), board.getSquare(7, 7), board.getSquare(7, 5), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 2))) {
            moveRook = new Move(getPlayer(), board.getSquare(7, 0), board.getSquare(7, 3), getBoard(), MoveType.NORMAL);
        }

        // will never be null because all castline moves follow these rules. Didn't want
        // an else statement becaues I wanted
        // to see all the start and end squares clearly
        moveNormally();
        moveRook.moveNormally();
    }

    private void promotePawn() {
        moveNormally();
        endSquare.setPiece(promotionPiece);
    }

    private void enPassant() {
        moveNormally();

        int destRow = getEndSquare().getROW();
        int destCol = getEndSquare().getCOL();
        int behind = (getPlayer().getColor() == ChessColor.WHITE) ? -1 : 1;
        Square oppPawnSquare = board.getSquare(destRow + behind, destCol);

        oppPawnSquare.setPiece(null);
    }

    private void undoMovePiece() {
        switch (type) {
            case NORMAL:
                undoMoveNormally();
                break;
            case CASTLE:
                undoCastle();
                break;
            case PAWN_PROMOTION:
                undoPromotePawn();
                break;
            case EN_PASSANT:
                undoEnPassant();
                break;
        }
    }

    private void undoMoveNormally() {
        startPiece.setMoveCount(startPiece.getMoveCount() - 1);
        endSquare.setPiece(endPiece);
        startSquare.setPiece(startPiece);
    }

    private void undoCastle() {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();
        Move moveRook = null;

        if (destSquare.equalsSquare(currBoard.getSquare(0, 6))) {
            moveRook = new Move(getPlayer(), board.getSquare(0, 7), board.getSquare(0, 5), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(0, 2))) {
            moveRook = new Move(getPlayer(), board.getSquare(0, 0), board.getSquare(0, 3), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 6))) {
            moveRook = new Move(getPlayer(), board.getSquare(7, 7), board.getSquare(7, 5), getBoard(), MoveType.NORMAL);
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 2))) {
            moveRook = new Move(getPlayer(), board.getSquare(7, 0), board.getSquare(7, 3), getBoard(), MoveType.NORMAL);
        }

        // will never be null because all castline moves follow these rules. Didn't want
        // an else statement becaues I wanted
        // to see all the start and end squares clearly
        undoMoveNormally();
        moveRook.undoMoveNormally();
    }

    private void undoPromotePawn() {
        undoMoveNormally();
        endSquare.setPiece(getEndPiece());
    }

    private void undoEnPassant() {
        undoMoveNormally();

        int destRow = getEndSquare().getROW();
        int destCol = getEndSquare().getCOL();

        ChessColor currColor = getPlayer().getColor();
        ChessColor oppColor = (currColor == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;

        int behind = (currColor == ChessColor.WHITE) ? -1 : 1;
        Square oppPawnSquare = board.getSquare(destRow + behind, destCol);

        oppPawnSquare.setPiece(new Pawn(oppColor));
        oppPawnSquare.getPiece().setMoveCount(1);
    }

    public Player getPlayer() {
        return player;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public Piece getStartPiece() {
        return startPiece;
    }

    public Piece getEndPiece() {
        return endPiece;
    }

    public Board getBoard() {
        return board;
    }

    public MoveType getType() {
        return type;
    }

    public void setType(MoveType type) {
        this.type = type;
    }

    public Piece getPromotionPiece() {
        return promotionPiece;
    }

    public void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    public String toString() {
        return (getType() == MoveType.PAWN_PROMOTION) ? promoToString() : elseToString();
    }

    private String elseToString() {
        return startSquare.toString() + "-" + endSquare.toString();
    }

    private String promoToString() {
        return elseToString() + "(" + String.valueOf(promotionPiece.getIcon()) + ")";
    }

    public boolean isLegal() {
        switch (getType()) {
            case CASTLE:
                return castleIsLegal();
            default:
                return normalIsLegal();
        }
    }

    private boolean normalIsLegal() {
        return !putsKingIntoCheck();
    }

    // private boolean castleIsLegal()
    // {
    // return !getBoard().kingIsInCheck(getPlayer().getColor()) &&
    // !castlesThroughCheck();
    // }

    private boolean castleIsLegal() {
        // FOR SOME REASON I CANNOT REPLACE THE FIRST CONDITION WITH PUTS KING INTO
        // CHECK METHOD... IDK WHY THO
        // THAT WAS THE CAUSE OF THE BUG AND I HAVE NO IDEA WHY
        return !getBoard().squareCanBeAttacked(getEndSquare(), getStartPiece().getColor()) && !castlesThroughCheck();
    }

    private boolean castlesThroughCheck() {
        Square destSquare = getEndSquare();
        Board currBoard = getBoard();

        if (destSquare.equalsSquare(currBoard.getSquare(0, 6))) {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(0, 5), getStartPiece().getColor());
        } else if (destSquare.equalsSquare(currBoard.getSquare(0, 2))) {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(0, 3), getStartPiece().getColor());
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 6))) {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(7, 5), getStartPiece().getColor());
        } else if (destSquare.equalsSquare(currBoard.getSquare(7, 2))) {
            return currBoard.squareCanBeAttacked(currBoard.getSquare(7, 3), getStartPiece().getColor());
        }

        // will never run below. all four castle destSquares are accounted for
        return false;
    }

    private boolean putsKingIntoCheck() {
        movePiece();

        // all I do is go from the king's square, and then just check all around it for
        // pieces of the opposite color
        boolean kingIsInCheck = getBoard().kingIsInCheck(getPlayer().getColor());

        undoMovePiece();

        return kingIsInCheck;
    }

    // BETTER THAN NORMAL .EQUALS() BECAUSE YOU ARE COMPARING MEMORY
    // ADDRESSED OTHERWISE. JUST THE START AND END SQUARES ARE ENOUGH TO
    // MAKE UP A UNIQUE MOVE

    public boolean equalsMove(Move otherMove) {
        // A COMBO OF A START AND END SQUARE ALWAYS PRODUCES A UNIQUE MOVE
        return getStartSquare().equalsSquare(otherMove.getStartSquare())
                && getEndSquare().equalsSquare(otherMove.getEndSquare());
    }
}