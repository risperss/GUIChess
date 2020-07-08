import java.io.File;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Board {
    private Square[][] board;
    private Piece lastPieceToMove;

    private Image blackBishop = new Image(new File("PieceSprites\\blackBishop.png").toURI().toString());
    private Image blackKing = new Image(new File("PieceSprites\\blackKing.png").toURI().toString());
    private Image blackKnight = new Image(new File("PieceSprites\\blackKnight.png").toURI().toString());
    private Image blackPawn = new Image(new File("PieceSprites\\blackPawn.png").toURI().toString());
    private Image blackQueen = new Image(new File("PieceSprites\\blackQueen.png").toURI().toString());
    private Image blackRook = new Image(new File("PieceSprites\\blackRook.png").toURI().toString());

    private Image whiteBishop = new Image(new File("PieceSprites\\whiteBishop.png").toURI().toString());
    private Image whiteKing = new Image(new File("PieceSprites\\whiteKing.png").toURI().toString());
    private Image whiteKnight = new Image(new File("PieceSprites\\whiteKnight.png").toURI().toString());
    private Image whitePawn = new Image(new File("PieceSprites\\whitePawn.png").toURI().toString());
    private Image whiteQueen = new Image(new File("PieceSprites\\whiteQueen.png").toURI().toString());
    private Image whiteRook = new Image(new File("PieceSprites\\whiteRook.png").toURI().toString());

    private Image blank = new Image(new File("PieceSprites\\blank.png").toURI().toString());

    private GridPane squares;
    private GridPane pieceSprites;
    private GridPane buttons;

    private StackPane graphicBoard;

    private final double squareLen = 50.0;

    // this whole click queue thing is bound to fail and is not a good idea
    // i dont really know another way, but that is a task for another day
    private ArrayList<Square> clickQueue;
    private Piece inputtedPromoPiece;

    private Label endGameMessage;
    private HBox promoButtons;

    Board() {
        setNewBoard();

        clickQueue = new ArrayList<Square>();
        buttons = new GridPane();
        setButtons();
        squares = new GridPane();
        setSquares();
        pieceSprites = new GridPane();
        setPieceSprites();
        endGameMessage = new Label();
        promoButtons = new HBox();
        setRoot();
    }

    public ArrayList<Square> getClickQueue() {
        return clickQueue;
    }

    public void resetClickQueue() {
        clickQueue = new ArrayList<Square>();
    }

    public void updatePieceSprites() {
        Platform.runLater(() -> setPieceSprites());
    }

    public void refreshSquares() {
        Platform.runLater(() -> setSquares());
    }

    public StackPane getGraphicBoard() {
        return graphicBoard;
    }

    private void setRoot() {
        graphicBoard = new StackPane();
        graphicBoard.getChildren().add(squares);
        graphicBoard.getChildren().add(pieceSprites);
        graphicBoard.getChildren().add(buttons);
    }

    private void setSquares() {
        squares.getChildren().clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle square = new Rectangle();

                square.setHeight(squareLen);
                square.setWidth(squareLen);

                square.setStyle((i % 2 != j % 2) ? "-fx-fill: #769656;" : "-fx-fill: #eeeed2;");

                hilightSelectedSquare(getSquare(7 - i, j), square);

                squares.add(square, j, i);
            }
        }
    }

    public void hilightSelectedSquare(Square boardSquare, Rectangle square) {
        if (getClickQueue().contains(boardSquare)) {
            square.setStyle("-fx-fill: #FF0000;");
        }
    }

    public GridPane getPieceSprites() {
        return pieceSprites;
    }

    private void setPieceSprites() {
        pieceSprites.getChildren().clear();

        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                ImageView v = new ImageView(rightImageForPiece(getSquare(i, j).getPiece()));
                pieceSprites.add(v, j, 7 - i);
            }
        }
    }

    private Image rightImageForPiece(Piece piece) {
        if (piece == null) {
            return blank;
        } else if (piece instanceof Bishop) {
            return (piece.getColor() == ChessColor.WHITE) ? whiteBishop : blackBishop;
        } else if (piece instanceof King) {
            return (piece.getColor() == ChessColor.WHITE) ? whiteKing : blackKing;
        } else if (piece instanceof Knight) {
            return (piece.getColor() == ChessColor.WHITE) ? whiteKnight : blackKnight;
        } else if (piece instanceof Pawn) {
            return (piece.getColor() == ChessColor.WHITE) ? whitePawn : blackPawn;
        } else if (piece instanceof Queen) {
            return (piece.getColor() == ChessColor.WHITE) ? whiteQueen : blackQueen;
        } else {
            // ONLY OTHER PIECE TYPE IS ROOK
            return (piece.getColor() == ChessColor.WHITE) ? whiteRook : blackRook;
        }
    }

    private void setButtons() {
        buttons.getChildren().clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button button = new Button();
                button.setMaxSize(squareLen, squareLen);
                button.setMinSize(squareLen, squareLen);

                final int FINAL_ROW = 7 - i;
                final int FINAL_COL = j;

                button.setOnAction(e -> {
                    clickQueue.add(getSquare(FINAL_ROW, FINAL_COL));
                });

                button.setStyle("-fx-background-radius: 0; -fx-background-color: transparent;");

                buttons.add(button, j, i);
            }
        }
    }

    public void setEndGameMessage(String s) {
        Platform.runLater(() -> {

            endGameMessage.setText(s);
            endGameMessage.setPadding(new Insets(15.0, 15.0, 15.0, 15.0));
            endGameMessage.setStyle(
                    "-fx-background-color: #dedede; -fx-background-radius: 2; -fx-border-width: 1; -fx-border-color: #bfbfbf; -fx-border-radius: 3;");
            endGameMessage.setFont(Font.font("Verdana", 16));

            graphicBoard.getChildren().add(endGameMessage);
            StackPane.setAlignment(endGameMessage, Pos.CENTER);
        });
    }

    public void setPromoButtons(ChessColor color) {
        Platform.runLater(() -> {
            graphicBoard.getChildren().add(promoButtons);
            Button queen = new Button();
            Button rook = new Button();
            Button knight = new Button();
            Button bishop = new Button();

            if (color == ChessColor.WHITE) {
                queen.setGraphic(new ImageView(whiteQueen));
                queen.setOnAction(e -> inputtedPromoPiece = new Queen(color));
                rook.setGraphic(new ImageView(whiteRook));
                rook.setOnAction(e -> inputtedPromoPiece = new Rook(color));
                knight.setGraphic(new ImageView(whiteKnight));
                knight.setOnAction(e -> inputtedPromoPiece = new Knight(color));
                bishop.setGraphic(new ImageView(whiteBishop));
                bishop.setOnAction(e -> inputtedPromoPiece = new Bishop(color));

            } else {
                queen.setGraphic(new ImageView(blackQueen));
                queen.setOnAction(e -> inputtedPromoPiece = new Queen(color));
                rook.setGraphic(new ImageView(blackRook));
                rook.setOnAction(e -> inputtedPromoPiece = new Rook(color));
                knight.setGraphic(new ImageView(blackKnight));
                knight.setOnAction(e -> inputtedPromoPiece = new Knight(color));
                bishop.setGraphic(new ImageView(blackBishop));
                bishop.setOnAction(e -> inputtedPromoPiece = new Bishop(color));
            }

            promoButtons.getChildren().add(queen);
            promoButtons.getChildren().add(rook);
            promoButtons.getChildren().add(knight);
            promoButtons.getChildren().add(bishop);

            promoButtons.setAlignment(Pos.CENTER);
        });
    }

    public void clearPromoButtons() {
        setInputtedPromoPiece(null);;
        Platform.runLater(() -> {
            promoButtons.getChildren().clear();
            graphicBoard.getChildren().remove(promoButtons);
        });
    }

    public Piece getInputtedPromoPiece() {
        return inputtedPromoPiece;
    }

    public void setInputtedPromoPiece(Piece inputtedPromoPiece) {
        this.inputtedPromoPiece = inputtedPromoPiece;
    }

    public void clearInputtedPromoPiece() {
        inputtedPromoPiece = null;
    }

    //all backend from here down

    public Piece getLastPieceToMove() {
        return lastPieceToMove;
    }

    public void setLastPieceToMove(Piece lastPieceToMove) {
        this.lastPieceToMove = lastPieceToMove;
    }

    private void setNewBoard() {
        board = new Square[8][8];

        board[0][0] = new Square(0, 0, new Rook(ChessColor.WHITE));
        board[0][1] = new Square(0, 1, new Knight(ChessColor.WHITE));
        board[0][2] = new Square(0, 2, new Bishop(ChessColor.WHITE));
        board[0][3] = new Square(0, 3, new Queen(ChessColor.WHITE));
        board[0][4] = new Square(0, 4, new King(ChessColor.WHITE));
        board[0][5] = new Square(0, 5, new Bishop(ChessColor.WHITE));
        board[0][6] = new Square(0, 6, new Knight(ChessColor.WHITE));
        board[0][7] = new Square(0, 7, new Rook(ChessColor.WHITE));

        board[1][0] = new Square(1, 0, new Pawn(ChessColor.WHITE));
        board[1][1] = new Square(1, 1, new Pawn(ChessColor.WHITE));
        board[1][2] = new Square(1, 2, new Pawn(ChessColor.WHITE));
        board[1][3] = new Square(1, 3, new Pawn(ChessColor.WHITE));
        board[1][4] = new Square(1, 4, new Pawn(ChessColor.WHITE));
        board[1][5] = new Square(1, 5, new Pawn(ChessColor.WHITE));
        board[1][6] = new Square(1, 6, new Pawn(ChessColor.WHITE));
        board[1][7] = new Square(1, 7, new Pawn(ChessColor.WHITE));

        board[7][0] = new Square(7, 0, new Rook(ChessColor.BLACK));
        board[7][1] = new Square(7, 1, new Knight(ChessColor.BLACK));
        board[7][2] = new Square(7, 2, new Bishop(ChessColor.BLACK));
        board[7][3] = new Square(7, 3, new Queen(ChessColor.BLACK));
        board[7][4] = new Square(7, 4, new King(ChessColor.BLACK));
        board[7][5] = new Square(7, 5, new Bishop(ChessColor.BLACK));
        board[7][6] = new Square(7, 6, new Knight(ChessColor.BLACK));
        board[7][7] = new Square(7, 7, new Rook(ChessColor.BLACK));

        board[6][0] = new Square(6, 0, new Pawn(ChessColor.BLACK));
        board[6][1] = new Square(6, 1, new Pawn(ChessColor.BLACK));
        board[6][2] = new Square(6, 2, new Pawn(ChessColor.BLACK));
        board[6][3] = new Square(6, 3, new Pawn(ChessColor.BLACK));
        board[6][4] = new Square(6, 4, new Pawn(ChessColor.BLACK));
        board[6][5] = new Square(6, 5, new Pawn(ChessColor.BLACK));
        board[6][6] = new Square(6, 6, new Pawn(ChessColor.BLACK));
        board[6][7] = new Square(6, 7, new Pawn(ChessColor.BLACK));

        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j, null);
            }
        }
    }

    public void printBoard() {
        String out = "";
        for (int i = 7; i >= 0; i--) {
            String line = "";
            for (int j = 0; j < 8; j++) {
                if (j == 0) {
                    line += "" + (i + 1) + " ";
                }
                if (board[i][j].getPiece() == null) {
                    line += "- ";
                } else {
                    String s = String.valueOf(board[i][j].getPiece().getIcon());
                    if (board[i][j].getPiece().getColor() == ChessColor.BLACK) {
                        s = s.toLowerCase();
                    }
                    line += s + " ";
                }
            }
            out += line + "\n";
        }
        out += "  a b c d e f g h\n";
        System.out.println(out);
    }

    public Square getSquare(int row, int col) {
        return board[row][col];
    }

    public Square findKingSquare(ChessColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square currSquare = getSquare(i, j);
                Piece currPiece = currSquare.getPiece();

                if (currPiece != null && currPiece instanceof King && currPiece.getColor() == color) {
                    return currSquare;
                }
            }
        }

        return null;
    }

    public boolean squareCanBeAttacked(Square square, ChessColor friendlyColor) {
        // DONT DO PRE PROCESSING BECAUSE SHORT CIRCUIT OR IS MORE EFFICIENT
        return canBeAttackedLinear(1, 0, square, friendlyColor) || canBeAttackedLinear(-1, 0, square, friendlyColor)
                || canBeAttackedLinear(0, 1, square, friendlyColor) || canBeAttackedLinear(0, -1, square, friendlyColor)
                || canBeAttackedLinear(1, 1, square, friendlyColor) || canBeAttackedLinear(-1, 1, square, friendlyColor)
                || canBeAttackedLinear(1, -1, square, friendlyColor)
                || canBeAttackedLinear(-1, -1, square, friendlyColor) || canBeAttackedKnight(square, friendlyColor);
    }

    public boolean kingIsInCheck(ChessColor color) {
        return squareCanBeAttacked(findKingSquare(color), color);
    }

    private boolean canBeAttackedLinear(int rowDir, int colDir, Square square, ChessColor friendlyColor) {
        int row = square.getROW();
        int col = square.getCOL();

        int i = 1;
        while (i < 8) {
            int dRow = row + (i * rowDir);
            int dCol = col + (i * colDir);

            if (dRow >= 0 && dRow < 8 && dCol < 8 && dCol >= 0) {
                Square destSquare = getSquare(dRow, dCol);
                Piece destPiece = destSquare.getPiece();

                if (destPiece != null) {
                    if (destPiece.getColor() != friendlyColor) {
                        if (rowDir == 0 || colDir == 0) {
                            return straightLineThreats(destPiece, i);
                        } else {
                            return diagonalThreats(destPiece, i, rowDir, friendlyColor);
                        }
                    } else {
                        break;
                    }
                }

                // dont do anything if the piece is null
                // just keep checking along that line
            } else {
                break;
            }
            i++;
        }
        return false;
    }

    // found out a good way to
    private boolean straightLineThreats(Piece destPiece, int i) {
        if (i == 1) {
            return destPiece instanceof Rook || destPiece instanceof Queen || destPiece instanceof King;
        }

        return destPiece instanceof Rook || destPiece instanceof Queen;
    }

    private boolean diagonalThreats(Piece destPiece, int i, int rowDir, ChessColor friendlyColor) {
        // WE ALREADY KNOW THAT THE DESTPIECE IS OF THE OPPOSITE COLOR
        if (i == 1) {
            if (friendlyColor == ChessColor.WHITE && rowDir == 1 || friendlyColor == ChessColor.BLACK && rowDir == -1) {
                return destPiece instanceof Bishop || destPiece instanceof Queen || destPiece instanceof King
                        || destPiece instanceof Pawn;
            } else {
                return destPiece instanceof Bishop || destPiece instanceof Queen || destPiece instanceof King;
            }
        }

        return destPiece instanceof Bishop || destPiece instanceof Queen;
    }

    private boolean canBeAttackedKnight(Square square, ChessColor color) {
        int row = square.getROW();
        int col = square.getCOL();
        // not the basketball player
        int[] dRows = new int[] { 1, -1, 1, -1, 2, -2, 2, -2 };
        int[] dCols = new int[] { 2, 2, -2, -2, 1, 1, -1, -1 };

        for (int i = 0; i < 8; i++) {
            int dRow = row + dRows[i];
            int dCol = col + dCols[i];

            if (dRow < 8 && dRow >= 0 && dCol < 8 && dCol >= 0) {
                Square destSquare = getSquare(dRow, dCol);
                Piece destPiece = destSquare.getPiece();

                if (destPiece != null && destPiece.getColor() != color && destPiece instanceof Knight) {
                    return true;
                }
            }
        }

        return false;
    }
}