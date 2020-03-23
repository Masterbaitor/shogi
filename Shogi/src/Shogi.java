
/**
 * @(#)Shogi.java
 *
 * Shogi application
 *
 * @author 
 * @version 1.00 2019/9/16
 */

import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JMenu; 
import javax.swing.JMenuBar; 
import javax.swing.JMenuItem;

public class Shogi extends JFrame {

	static final long serialVersionUID = 107;

	public static int width = 9, height = 9;

	public static String ResourcesDir = System.getProperty("user.dir") + "\\resources\\";

	static public Player Player1;
	static public Player Player2;

	static public Shogi board;

	public Shogi() {
		
		Player1 = new Player(true);
		Player2 = new Player(false);
		Player.ActivePlayer = Player1;
		loadBackground(Shogi.ResourcesDir + "woodenbackground.png");
		getContentPane().setLayout(new GridLayout(width, height));
		createMenuBar();
		placePieces();

	}

	private void loadBackground(String filepath) {
		try {
			BufferedImage myImage = ImageIO.read(new File(filepath));
			setContentPane(new ImagePanel(myImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void placePieces() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width + 3; x++) {
				Player player = y > 5 ? Player1 : Player2;
				if (x > width - 1) {
					CaptureButton cbutton = new CaptureButton(Piece.CapturedPlacement[x][y], player);
					cbutton.setBorder(BorderFactory.createEmptyBorder());
					if (Piece.CapturedPlacement[x][y] != null) {
						player.CapturedZone.put(Piece.CapturedPlacement[x][y], cbutton);
					}
					getContentPane().add(cbutton);
				} else {
					String name = Piece.placement[x][y];
					Piece piece = new Piece(name, false);
					piece.setPosition(x, height - 1 - y);
					if (piece.name != null) {
						player.addPiece(piece);
						if(piece.name == "King"){
							player.King = piece;
						}
					}
					Piece.Board.put((float) piece.position[0] + (float) piece.position[1] / 10, piece);
					getContentPane().add(piece);
				}
			}
		}
	}

	static void buildBoard() {
		board = new Shogi();
		board.setSize(1206, 896);
		board.setLocationRelativeTo(null);
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.setVisible(true);
		board.setResizable(false);
	}

	private void createMenuBar() { 
		JMenuBar menuBar = new JMenuBar(); 
		JMenu aboutMenu = new JMenu("About"); 
		JMenu gameMenu = new JMenu("Game");
		JMenuItem rulesItem = new JMenuItem("rules"); 
		rulesItem.addActionListener((event) -> openRules()); 
		aboutMenu.add(rulesItem); 
		JMenuItem newgameItem = new JMenuItem("forfeit");
		newgameItem.addActionListener((event) -> resignMessage());
		gameMenu.add(newgameItem);
		menuBar.add(aboutMenu); 
		menuBar.add(gameMenu);
		setJMenuBar(menuBar); 
		
	}

	private void resignMessage(){
		JOptionPane.showMessageDialog(Shogi.board, "You lost");
		System.exit(0);
		
	}

	private void openRules(){
		try{
			Desktop.getDesktop().browse(new URI("http://ancientchess.com/page/play-shogi.htm"));
		} catch (IOException e){} catch(URISyntaxException e){};
	
	}

	static void buildPlacement() {

		Piece.placement[0][0] = "Lance";
		Piece.placement[0][8] = "Lance";
		Piece.placement[8][0] = "Lance";
		Piece.placement[8][8] = "Lance";
		Piece.placement[1][0] = "Knight";
		Piece.placement[1][8] = "Knight";
		Piece.placement[7][0] = "Knight";
		Piece.placement[7][8] = "Knight";
		Piece.placement[2][0] = "Silver";
		Piece.placement[2][8] = "Silver";
		Piece.placement[6][0] = "Silver";
		Piece.placement[6][8] = "Silver";
		Piece.placement[3][0] = "Gold";
		Piece.placement[3][8] = "Gold";
		Piece.placement[5][0] = "Gold";
		Piece.placement[5][8] = "Gold";
		Piece.placement[4][0] = "King";
		Piece.placement[4][8] = "King";
		Piece.placement[1][1] = "Rook";
		Piece.placement[7][7] = "Rook";
		Piece.placement[7][1] = "Bishop";
		Piece.placement[1][7] = "Bishop";

		for (int i = 0; i < 9; i++) {
			Piece.placement[i][2] = "Pawn";
			Piece.placement[i][6] = "Pawn";
		}
	}

	static void buildCapturedPlacement() {
		Piece.CapturedPlacement[10][0] = "Gold";
		Piece.CapturedPlacement[10][8] = "Gold";
		Piece.CapturedPlacement[9][1] = "Lance";
		Piece.CapturedPlacement[9][7] = "Lance";
		Piece.CapturedPlacement[10][1] = "Silver";
		Piece.CapturedPlacement[10][7] = "Silver";
		Piece.CapturedPlacement[11][1] = "Knight";
		Piece.CapturedPlacement[11][7] = "Knight";
		Piece.CapturedPlacement[9][2] = "Bishop";
		Piece.CapturedPlacement[9][6] = "Bishop";
		Piece.CapturedPlacement[10][2] = "Pawn";
		Piece.CapturedPlacement[10][6] = "Pawn";
		Piece.CapturedPlacement[11][2] = "Rook";
		Piece.CapturedPlacement[11][6] = "Rook";
	}
	public static void main(String[] args) {
		Piece.placement = new String[12][9];
		Piece.CapturedPlacement = new String[12][9];
		buildPlacement();
		buildCapturedPlacement();
		buildBoard();
	}

}
