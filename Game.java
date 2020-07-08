import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Game implements Runnable {
    private int turn;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currPlayer;
    private ArrayList<Move> allMovesInGame;
    private Board board;
    private GameState gameState;
    private int turnsWithoutCapture;
    private int turnsWithoutPawnMove;

    Game(Player whitePlayer, Player blackPlayer) {
        setTurn(1);
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        allMovesInGame = new ArrayList<Move>();
        this.board = new Board();
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public ArrayList<Move> getAllMovesInGame() {
        return allMovesInGame;
    }

    public void addMoveToAllMovesInGame(Move move) {
        allMovesInGame.add(move);
    }

    public void setAllMovesInGame(ArrayList<Move> allMovesInGame) {
        this.allMovesInGame = allMovesInGame;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getTurnsWithoutCapture() {
        return turnsWithoutCapture;
    }

    public void setTurnsWithoutCapture(int turnsWithoutCapture) {
        this.turnsWithoutCapture = turnsWithoutCapture;
    }

    public void setTurnsWithoutCapture(Move realMove) {
        // INCLUDE THE OR BECUASE EN PASSANT HAS NULL END PIECE BUT IS STILL A CAPTURE
        setTurnsWithoutCapture((realMove.getEndPiece() == null || realMove.getType() == MoveType.EN_PASSANT)
                ? getTurnsWithoutCapture() + 1
                : 0);
    }

    public int getTurnsWithoutPawnMove() {
        return turnsWithoutPawnMove;
    }

    public void setTurnsWithoutPawnMove(int turnsWithoutPawnMove) {
        this.turnsWithoutPawnMove = turnsWithoutPawnMove;
    }

    public void setTurnsWithoutPawnMove(Move realMove) {
        setTurnsWithoutPawnMove((realMove.getStartPiece() instanceof Pawn) ? 0 : getTurnsWithoutPawnMove() + 1);
    }

    @Override
    public void run() {
        try {
            play();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void play() throws InterruptedException, IOException {
        while (true) {
            setCurrPlayer((getTurn() % 2 == 1) ? getWhitePlayer() : getBlackPlayer());

            getCurrPlayer().setAllLegalMoves(getBoard());

            getBoard().updatePieceSprites();

            // SEES IF SOMEONE GOT CHECKMATED OR STALEMATED
            setGameState();
            if (getGameState() != GameState.ONGOING) {
                break;
            }

            // PARSING MOVE WITH FULL INFORMATION FROM USER INPUT BECAUSE THE METHOD
            // 'VALID MOVE FROM USER' JUST RETURNS THE START AND END SQUARES
            Move realMove = currPlayer.getEquivalentLegalMove(getValidMoveFromUser());

            if (realMove.getType() == MoveType.PAWN_PROMOTION) {
                if (currPlayer instanceof HumanPlayer) {
                    getBoard().setPromoButtons(currPlayer.getColor());
                }
                realMove.setPromotionPiece(currPlayer.getPromotionPiece(getBoard()));
            }
            getBoard().clearPromoButtons();

            // WAITING SO THAT CPU MOVES DON'T GO TOO FAST
            if (currPlayer instanceof CPUPlayer) {
                Thread.sleep(1000);
            }

            // MAKING THE MOVE AND SETTING NEEDED VALUES FOR NEXT TURN
            realMove.movePiece();

            getBoard().setLastPieceToMove(realMove.getStartPiece());
            addMoveToAllMovesInGame(realMove);

            setTurn(getTurn() + 1);
            setTurnsWithoutCapture(realMove);
            setTurnsWithoutPawnMove(realMove);
        }

        getBoard().setEndGameMessage(endGameMessage());
        writeAllMovesInGameToFile();
    }

    private String endGameMessage() {
        // i pad the text this way because the way that javafx handles
        // centering is a nightmare and I just cannot with it right now
        // so i am doing this... i am ashamed
        switch (getGameState()) {
            case WHITE_WIN:
                return "White has won the game!";
            case BLACK_WIN:
                return "Black has won the game!";
            case STALEMATE:
                return "The game is a draw by stalemate!";
            case FIFTY_MOVE_RULE:
                return "The game is a draw by 50 move rule!";
            default:
                // should never reach this
                return "Houston, we have a problem!";
        }
    }

    private Move getValidMoveFromUser() {
        Move userMove = null;
        do {
            userMove = getCurrPlayer().getMove(getBoard());
            getBoard().resetClickQueue();
            getBoard().refreshSquares();
        } while (!currPlayer.legalMovesContains(userMove));
        return userMove;
    }

    private void writeAllMovesInGameToFile() throws IOException {
        FileWriter fileWriter = new FileWriter("AllMovesInGame.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Move move : getAllMovesInGame()) {
            bufferedWriter.write(move.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.write(gameResultString());
        bufferedWriter.close();
    }

    private String gameResultString() {
        switch (getGameState()) {
            case WHITE_WIN:
                return "1-0";
            case BLACK_WIN:
                return "0-1";
            default:
                return "0.5-0.5";
        }
    }

    private void setGameState() {
        if (inCheckMate()) {
            setGameState((getCurrPlayer().getColor() == ChessColor.WHITE) ? GameState.BLACK_WIN : GameState.WHITE_WIN);
        } else if (inStalemate()) {
            setGameState(GameState.STALEMATE);
        }
        // IS 100 BECAUSE BOTH PLAYERS HAVE TO MOVE TO COUNT FOR 1
        else if (getTurnsWithoutCapture() >= 100 && getTurnsWithoutPawnMove() >= 100) {
            setGameState(GameState.FIFTY_MOVE_RULE);
        } else {
            setGameState(GameState.ONGOING);
        }
    }

    private boolean inCheckMate() {
        return getBoard().kingIsInCheck(getCurrPlayer().getColor()) && getCurrPlayer().getAllLegalMoves().isEmpty();
    }

    private boolean inStalemate() {
        return !getBoard().kingIsInCheck(getCurrPlayer().getColor()) && getCurrPlayer().getAllLegalMoves().isEmpty();
    }
}