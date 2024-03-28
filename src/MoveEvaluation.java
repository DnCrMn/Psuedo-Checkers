
public class MoveEvaluation {
	private MoveType type;
	private Piece piece;
	
	// CONSTUCTORS
	public MoveEvaluation(MoveType type, Piece piece) {
		this.type = type;
		this.piece = piece;
	}
	
	public MoveEvaluation(MoveType type) {
		this(type, null);
	}
	
	// GETTERS
	public MoveType getType() {
		return type;
	}
	
	public Piece getPiece() {
		return piece;
	}
}
