import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CheckersMain extends Application {
	public static final int TILE_SIZE = 100;
	
	public static final int WIDTH = 5;
	public static final int HEIGHT = 7;
	
	private Tile[][] board = new Tile[WIDTH][HEIGHT];
	
	private int turn = -1;
	
	// Counts the remaining pieces in the game
	private int countRed = 0;
	private int countWhite = 0;
	
	// Counts the red/white pieces that went into the enemy territory
	private int countRedInWhite = 0;
	private int countWhiteInRed = 0;
	
	
	// Initializing Media Player for bg and win audio
	private Media bgMusic;
	private MediaPlayer bgPlayer;
	
	private Media winMusic;
	private MediaPlayer winPlayer;
	
	// 2 Groups for drawing the board tiles (One with pieces and one without pieces aka the empty Tiles)
	private Group tileGroup = new Group();
	private Group pieceGroup = new Group();
	
	private Parent createContent() {
		Pane root = new Pane();
		
		// Initialize board size (7x5 board)
		root.setPrefSize(WIDTH * TILE_SIZE , HEIGHT * TILE_SIZE);
		root.getChildren().addAll(tileGroup, pieceGroup); // Adds the colored tiles and pieces into the root pane
		
		// for loop (Generates the game)
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				// Constructs the tiles 
				Tile tile = new Tile((i + j) % 2 == 0, j, i); // The light boolean depends if the tile is odd or even (Odd is light, Even is dark)
				board[j][i] = tile; //Save tile to board array
				
				tileGroup.getChildren().add(tile); // Add the tiles to the tile group
				
				Piece piece = null; // Initialize the piece to null 
				
				// Initializes the starting positions of the game pieces
				if (i <= 1 && (i + j) % 2 != 0) { // Generates the red pieces
					piece = generatePiece(PieceType.RED, j, i);
					countRed++;
				}
				
				if (i >= 5 && (i + j) % 2 != 0) { // Generates the white pieces
					piece = generatePiece(PieceType.WHITE, j, i);
					countWhite++;
				}
				
				// If there are pieces in the board
				if (piece != null) {
					tile.setPiece(piece);
					pieceGroup.getChildren().add(piece);
				}
			}
		}
		return root;
		
	}
	
	private MoveEvaluation movePiece(Piece piece, int newX, int newY) {
		// If it's not your turn or the location you want to move is a white space 
		if ((piece.getType().moveDir != turn) || (newX + newY) % 2 == 0) {
            return new MoveEvaluation(MoveType.ILLEGAL);
        }
		
		// Previous coordinates of the held piece
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        // If the move is legal and the pieces are moving upwards or downwards depending on the type
        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
        	// If there is a enemy piece in the tile, the current player eats the enemy piece
            if (board[newX][newY].hasPiece() && board[newX][newY].getPiece().getType() != piece.getType()) {
            	if (board[newX][newY].getPiece().getType() == PieceType.RED) {
            		countRed--;
            		countRedInWhite = (countRedInWhite == 0) ? 0: countRedInWhite--;
            		
            		if (newY < 2) {
                    	// Increases the countWhiteInRed variable when white is in red's territory (within row's 1 and 2)
                    	if (board[x0][y0].getPiece().getType() == PieceType.WHITE && board[x0][y0].getPiece().getIsEnemyTerritory() == false) {
                    		countWhiteInRed++;
                    		board[x0][y0].getPiece().setIsEnemyTerritory();
                    	}
                    	
                    }
            	}
            	else {
            		countWhite--;
            		countWhiteInRed = (countWhiteInRed == 0) ? 0: countWhiteInRed--;
            		
            		if (newY >= 5) {
                    	// Increases the countRedinWhite variable when white is in red's territory (within row's 6 and 7)
                    	if (board[x0][y0].getPiece().getType() == PieceType.RED && board[x0][y0].getPiece().getIsEnemyTerritory() == false) { 
                    		countRedInWhite++;
                    		board[x0][y0].getPiece().setIsEnemyTerritory();
                    	}
                    		
                    }
            	}
            	
            	turn *= -1; // Ends the turn (Switches from red to white turn and vice versa)
            	
                return new MoveEvaluation(MoveType.EAT, board[newX][newY].getPiece());
            }
            
            // Check's if the either red/piece has gone to the starting
            if (newY < 2) {
            	// Increases the countWhiteInRed variable when white is in red's territory (within row's 1 and 2)
            	if (board[x0][y0].getPiece().getType() == PieceType.WHITE && board[x0][y0].getPiece().getIsEnemyTerritory() == false) {
            		countWhiteInRed++;
            		board[x0][y0].getPiece().setIsEnemyTerritory();
            	}
            	
            }
            else if (newY >= 5) {
            	// Increases the countRedinWhite variable when white is in red's territory (within row's 6 and 7)
            	if (board[x0][y0].getPiece().getType() == PieceType.RED && board[x0][y0].getPiece().getIsEnemyTerritory() == false) { 
            		countRedInWhite++;
            		board[x0][y0].getPiece().setIsEnemyTerritory();
            	}
            		
            }
            
            turn *= -1; // Ends the turn
            
            System.out.println("Red Pieces: " + countRed);
    		System.out.println("Red Pieces in White: " + countRedInWhite);
    		System.out.println("White Pieces: " + countWhite);
    		System.out.println("White Pieces in Red: " + countWhiteInRed);
    		
            // If tile is free space
            return new MoveEvaluation(MoveType.NORMAL);
        } 

        
        return new MoveEvaluation(MoveType.ILLEGAL);
	}
	
	private int toBoard(double pixel) {
		// Converts pixel coordinates to board coordinates
		return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
	}
	
	public void win(int red, int white) {
		// Win screen appears when win condition is met (Either red or white is completely gone)
		Alert winScreen = new Alert(AlertType.INFORMATION);
		
		// Win Condition for White
		if(red == 0 || (countWhiteInRed == countWhite && (countWhite > 0))) { 
			/*If there are no remaining red pieces or if there are remaining white pieces that are 
			 * all on the red territory
			*/
			// Stops the current BG and plays win audio
			stopBG();
			playWin();
			winScreen.setTitle("White Victory");
			winScreen.setHeaderText("White Wins!");
			winScreen.setContentText("Congratulations White, you won!");
			winScreen.showAndWait();
		}
		// Win Condition for Red
		else if (white == 0 || (countRedInWhite == countRed && (countRed > 0))) {
			/*If there are no remaining white pieces or if there are remaining red pieces that are 
			 * all on the red territory
			*/
			
			// Stops the current BG and plays win audio
			stopBG();
			playWin();
			
			// Shows victory screen
			winScreen.setTitle("Red Victory");
			winScreen.setHeaderText("Red Wins!");
			winScreen.setContentText("Congratulations Red, you won!");
			winScreen.showAndWait();
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Starts the GUI
		Scene scene = new Scene(createContent());
		
		try {
			bgMusic = new Media(getClass().getResource("bg.mp3").toURI().toString());
			bgPlayer = new MediaPlayer(bgMusic);
			winMusic = new Media(getClass().getResource("win.mp3").toURI().toString());
			winPlayer = new MediaPlayer(winMusic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		playBG(); // Play the BG audio
		primaryStage.setTitle("Pseudo Checkers");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void playBG() {
		// Plays the background music
		bgPlayer.play();
		bgPlayer.setVolume(35);
		
		// If BG ends, loop audio
		bgPlayer.setOnEndOfMedia(() -> {
			bgPlayer.seek(Duration.ZERO);
			bgPlayer.play();
		});
	}	
	
	public void stopBG() {
		bgPlayer.stop();
	}
	
	public void playWin() {
		winPlayer.play();
	}
	
	private Piece generatePiece(PieceType type, int x, int y) {
		// Draws the pieces on the board
		Piece piece = new Piece(type, x, y);
		
		// When you release the mouse click
        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());
            int newY = toBoard(piece.getLayoutY());
            
            MoveEvaluation result;

            // If the move is out of bounds
            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveEvaluation(MoveType.ILLEGAL);
            } else {
                result = movePiece(piece, newX, newY);
            }

            // Previous piece coordinates
            int x0 = toBoard(piece.getOldX());
            int y0 = toBoard(piece.getOldY());

            switch (result.getType()) {
                case ILLEGAL:
                    piece.abortMove(); // Resets the piece position
                    break;
                case NORMAL:
                	// Deletes the previous piece and places it in the new coordinates
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
                case EAT:
                	// Deletes the enemy piece first (to avoid overlapping of pieces)
                    Piece otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);
                    pieceGroup.getChildren().remove(otherPiece);
                    
                    // Same as normal case (remove previous piece and places in new coordinates)
                    piece.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);
                    break;
            }
            
            win(countRed, countWhite); // Check for win condition
        });

        return piece;
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
