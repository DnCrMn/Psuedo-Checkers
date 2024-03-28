
public enum PieceType {
	RED(1), WHITE(-1);
	
	final int moveDir; // movement direction (Corresponds to the Piece Type)
	
	PieceType (int moveDir) {
		this.moveDir = moveDir;
	}
}
