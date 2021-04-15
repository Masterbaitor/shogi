
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
	public static int captureWidth = 3, captureHeight = 9;

	public static String ResourcesDir = System.getProperty("user.dir") + "\\resources\\";

	static public Player Player1;
	static public Player Player2;

	static public Shogi board;

	public Shogi() {
		
		Player1 = new Player(true, JOptionPane.showInputDialog(board, "Please enter Player 1's name: "));
		Player2 = new Player(false, JOptionPane.showInputDialog(board, "Please enter Player 2's name: "));
		Player.ActivePlayer = Player1;
		loadBackground(Shogi.ResourcesDir + "woodenbackground.png");
		getContentPane().setLayout(new GridLayout(width, height));
		createMenuBar();
		setIcon();
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
						if(piece.name.equals("King")){
							player.King = piece;
						}
					}
					Piece.Board[piece.position[0]][piece.position[1]] = piece;
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
		newgameItem.addActionListener((event) -> resign());
		gameMenu.add(newgameItem);
		menuBar.add(aboutMenu); 
		menuBar.add(gameMenu);
		setJMenuBar(menuBar);
	}

	private void resign(){
		endGame(Player.getOpponentPlayer(Player.ActivePlayer), false);
	}

	public static void endGame(Player winner, boolean checkmate)
	{
		JOptionPane.showMessageDialog(Shogi.board, (checkmate ? "Checkmate! " : "Forfeit. ") + winner.Name + " wins!");
		System.exit(0);
	}

	private void openRules(){
		try{
			Desktop.getDesktop().browse(new URI("http://ancientchess.com/page/play-shogi.htm"));
		} catch (IOException e){} catch(URISyntaxException e){};
	
	}

	static void buildPlacement(String fileName) {

		File f = new File(ResourcesDir+"\\"+fileName+".txt");
		try{
			BufferedReader in = new BufferedReader(new FileReader(f));
			try{
				String line;
				while (( line = in.readLine()) != null) {
					String[] placement = line.split(":");
					String[] coordinateStr = placement[0].split(",");
					int[] coordinateInt = {Integer.parseInt(coordinateStr[0]), Integer.parseInt(coordinateStr[1])};
					if(fileName.equals("board"))
						Piece.placement[coordinateInt[0]][coordinateInt[1]] = placement[1];
					else if(fileName.equals("capturePile"))
						Piece.CapturedPlacement[coordinateInt[0]][coordinateInt[1]] = placement[1];
				}
			} catch (IOException e){}
		} catch (FileNotFoundException e){}
	}
	
	private void setIcon()
	{
		Image frameImage = new ImageIcon(Shogi.ResourcesDir+"\\icon.png").getImage();
		setIconImage(frameImage);
	}

	public static void main(String[] args) {
		Piece.placement = new String[width][height];
		Piece.CapturedPlacement = new String[width+captureWidth][captureHeight];
		buildPlacement("board");
		buildPlacement("capturePile");
		buildBoard();
	}

}
