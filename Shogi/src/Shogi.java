/**
 * @(#)Shogi.java
 *
 * Shogi application
 *
 * @author 
 * @version 1.00 2019/9/16
 */
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*; 
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage; 
import java.awt.Panel;

public class Shogi extends JFrame {

	int width=9,height=9;

	public static String ResourcesDir = System.getProperty("user.dir")+"\\resources\\";

	static public Player Player1;
	static public Player Player2;

	static public Shogi board;

	public Shogi(){
		Player1 = new Player(true);
		Player2 = new Player(false);
		Player.ActivePlayer = Player1;
		loadBackground(Shogi.ResourcesDir + "woodenbackground.png");
		getContentPane().setLayout(new GridLayout(width,height));
		placePieces();	
	}

	private void loadBackground(String filepath){
		try{		
			BufferedImage myImage = ImageIO.read(new File(filepath));
				setContentPane(new ImagePanel(myImage));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void placePieces(){
		for (int y=0; y<height; y++){
			for (int x=0; x<width; x++){
				String name = Piece.placement[x][y];
				Piece button = new Piece(name, false);
				button.setPosition(x, height-1-y);
				if(button.name != null){
					if(y>5){
						Player1.addPiece(button);
					}else{
						Player2.addPiece(button);
					}
				}
				Piece.Board.put((float) button.position[0] + (float) button.position[1]/10, button);	
				getContentPane().add(button);
			}
		}
	}

	static void buildBoard(){
		board = new Shogi();
		
		board.setSize(900,900); 
		board.setLocationRelativeTo(null);
    	board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	board.setVisible(true);
    	board.setResizable(false);	
	}
	
	static void buildPlacement(){
		
		Piece.placement[0][0] = "Lance";	Piece.placement[0][8] = "Lance";
		Piece.placement[8][0] = "Lance";	Piece.placement[8][8] = "Lance";
		Piece.placement[1][0] = "Knight";	Piece.placement[1][8] = "Knight";
		Piece.placement[7][0] = "Knight";	Piece.placement[7][8] = "Knight";
		Piece.placement[2][0] = "Silver"; 	Piece.placement[2][8] = "Silver";
		Piece.placement[6][0] = "Silver"; 	Piece.placement[6][8] = "Silver";
		Piece.placement[3][0] = "Gold";		Piece.placement[3][8] = "Gold";
		Piece.placement[5][0] = "Gold"; 	Piece.placement[5][8] = "Gold";
		Piece.placement[4][0] = "King";		Piece.placement[4][8] = "King";
			
		Piece.placement[1][1] = "Rook"; 	Piece.placement[7][7] = "Rook";
		Piece.placement[7][1] = "Bishop";	Piece.placement[1][7] = "Bishop";
		
		for (int i=0; i<9; i++){
			Piece.placement[i][2] = "Pawn"; 
			Piece.placement[i][6] = "Pawn";
		}			
	}	
	

	public static void main(String[]args){
		Piece.placement = new String[9][9];
		buildPlacement();
		buildBoard();
	}
	
}
