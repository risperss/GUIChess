public enum MoveType {
    NORMAL, CASTLE, PAWN_PROMOTION, EN_PASSANT;

    /*
     * This is what I used to have, but then I stumbled upon enums and found that
     * they would be the absolute perfect thing to use for an application like
     * below. It just made too much sense for me to ignore it. type == 0 ? normal
     * move type == 1 ? castling type == 2 ? pawn promotion type == 3 ? en passant
     */
}