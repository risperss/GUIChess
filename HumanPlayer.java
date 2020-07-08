public class HumanPlayer extends Player {
    HumanPlayer(ChessColor color) {
        super(color);
    }

    @Override
    public Move getMove(Board board) {
        boolean oneTime = true;
        while (board.getClickQueue().size() < 2) {
            // complete trash... needs it though
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (oneTime && board.getClickQueue().size() == 1) {
                board.refreshSquares();
                oneTime = false;
            }
        }
        return new Move(this, board.getClickQueue().get(0), board.getClickQueue().get(1));
    }

    @Override
    public Piece getPromotionPiece(Board board) {
        while (board.getInputtedPromoPiece() == null) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return board.getInputtedPromoPiece();
    }
}