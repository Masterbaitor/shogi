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

import javax.swing.*; 
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage; 
import java.awt.Panel; 
public class Shogi extends JFrame {
	
	int width=9,height=9;
	
	public Shogi(){
 	try{
        
        BufferedImage myImage = ImageIO.read(new File("C:\\Users\\Nefeli\\Documents\\JCreator LE\\MyProjects\\Shogi\\resources\\woodenbackground.png"));
			setContentPane(new ImagePanel(myImage));
        } catch (IOException e){
        	e.printStackTrace();
        }
	getContentPane().setLayout(new GridLayout(width,height));
		
		for (int y=0; y<height; y++){
			for (int x=0; x<width; x++){
				String name = Piece.placement[x][y];
				Piece button = new Piece(name);
				button.position = new int[]{x,height-1-y};
				
				File f = new File("C:\\Users\\Nefeli\\Documents\\IB\\COMPUTER SCIENCE\\IA program\\Shogi\\resources\\" + name + "\\moves.txt");
				try{
					BufferedReader in = new BufferedReader(new FileReader(f));
					try{
						String line;
						while (( line = in.readLine()) != null) {
           					//System.out.print(line);
           					String[] m = line.split(":");
           					
           					button.moves.put(Integer.parseInt(m[0]),Integer.parseInt(m[1])); 	 
        				}
					} catch (IOException e){}
				} catch (FileNotFoundException e){}
					
				getContentPane().add(button);		
			}
		}
	}
	
	static void buildBoard(){

	//	public Map <Integer, Piece> initialise = new HashMap<Integer,Piece>(); 

		Shogi board = new Shogi();
	
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
		buildPlacement();
		buildBoard();
			
	}
	
}
