import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle{
	private Piece piece;

	// CONSTRUCTORS
	public Tile (boolean light, int x, int y) {
		// Draws the tile board 
		setWidth(CheckersMain.TILE_SIZE);
		setHeight(CheckersMain.TILE_SIZE);
		
		relocate(x * CheckersMain.TILE_SIZE, y * CheckersMain.TILE_SIZE);
		
		// If light is true, use lighter color for tile vice versa
		setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));
	}
	
	// GETTERS AND SETTERS
	public boolean hasPiece() {
		// Checks if the tile has a piece
		return piece != null;
	}
	
	
	public Piece getPiece() {
		return piece;
	}


	public void setPiece(Piece piece) {
		this.piece = piece;
	}
}
