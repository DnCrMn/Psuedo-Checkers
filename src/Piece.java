import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane{
	private PieceType type;
	
	private double mouseX, mouseY;
	private double oldX, oldY;
	private boolean isInEnemyTerritory = false;
	
	// CONSTRUCTOR
	public Piece(PieceType type, int x, int y) {
		this.type = type;
		
		move(x , y);
		
		// Creating the piece shadow
		Ellipse bg = new Ellipse(0.3125 * CheckersMain.TILE_SIZE, 0.26 * CheckersMain.TILE_SIZE);
		bg.setFill(Color.BLACK);
		// Drawing the piece shadow
		bg.setStroke(Color.BLACK);
		bg.setStrokeWidth(CheckersMain.TILE_SIZE * 0.03);
		bg.setTranslateX((CheckersMain.TILE_SIZE - (0.3125 * CheckersMain.TILE_SIZE * 2)) /2);
		bg.setTranslateY((CheckersMain.TILE_SIZE - (0.26 * CheckersMain.TILE_SIZE * 2)) /2 + CheckersMain. TILE_SIZE * 0.07);
		
		
		// Creating the Checkers piece
		Ellipse pieceEllipse = new Ellipse(0.3125 * CheckersMain.TILE_SIZE, 0.26 * CheckersMain.TILE_SIZE);
		// Fills the color depending on the Piece Type
		pieceEllipse.setFill(type == PieceType.RED ? Color.valueOf("#c40003") : Color.valueOf("#fcf2d8"));
		// Drawing the piece shadow
		pieceEllipse.setStroke(Color.BLACK);
		pieceEllipse.setStrokeWidth(CheckersMain.TILE_SIZE * 0.03);
		// Centering the piece in the tile
		pieceEllipse.setTranslateX((CheckersMain.TILE_SIZE - (0.3125 * CheckersMain.TILE_SIZE * 2)) /2);
		pieceEllipse.setTranslateY((CheckersMain.TILE_SIZE - (0.26 * CheckersMain.TILE_SIZE * 2)) /2);
		
		getChildren().addAll(bg, pieceEllipse);
		
		// Set mouse coordinates when clicking
		setOnMousePressed(e -> {
			mouseX = e.getSceneX();
			mouseY = e.getSceneY();
		});
		
		// Moves the piece 
		setOnMouseDragged(e -> {
			relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
		});
	}
	
	// Moves the pieces
	public void move(int x, int y) {
		oldX = x * CheckersMain.TILE_SIZE;
		oldY = y * CheckersMain.TILE_SIZE;
		
		relocate(oldX, oldY);
		
	}
	
	// Resets the position of the piece with the old coordinates
	public void abortMove() {
		relocate(oldX, oldY);
	}
	
	// GETTERS
	public double getOldX() {
		return oldX;
	}

	public double getOldY() {
		return oldY;
	}
	
	public PieceType getType() {
		return type;
	}
	
	public boolean getIsEnemyTerritory() {
		return isInEnemyTerritory;
	}
	
	public void setIsEnemyTerritory() {
		this.isInEnemyTerritory = true;
	}
	
}
