
import java.io.*;		//imports libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.net.URL;

import javax.swing.border.Border; 
public class Piece extends JButton {
	
	public String name;
	public Map <Integer,Integer> moves = new HashMap <Integer,Integer>();
	public int[] position;
	
	
	

	public Piece(String n){
		super();
		name = n;
	//	setText(n); 
	//	ImageIcon image = new ImageIcon("C:\\Users\\Nefeli\\Documents\\IB\\COMPUTER SCIENCE\\IA program\\Shogi\\resources\\" + n + "\\image.png");
	//	setIcon(image);
	
		ImageIcon imageF = new ImageIcon("C:\\Users\\Nefeli\\Documents\\JCreator LE\\MyProjects\\Shogi\\resources\\" + n + "\\image.png" );
		Image img = imageF.getImage() ;  
		Image newimg = img.getScaledInstance(70, 80, java.awt.Image.SCALE_SMOOTH) ;  
		imageF = new ImageIcon(newimg);	
		setIcon(imageF);
	

		Border black = BorderFactory.createLineBorder(Color.BLACK);
   	 	setBorder(black);
  		
		setOpaque(false);
		setContentAreaFilled(false);	
			
		addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
            	highlight();
           	}
     	});
	}
	
	void highlight(){
		
		List<int[]> positionsToHighlight = new ArrayList<>();
		int[] direction = new int[2];
		

		for (Map.Entry<Integer,Integer> entry : moves.entrySet()) {
   		 	switch (entry.getKey()){
					case 1: 
						direction[0] = -1;
						direction[1] = -1;
					break;
					case 2:	
						direction[0] = 0;
						direction[1] = -1;
					break; 
					case 3:	
						direction[0] = 1;
						direction[1] = -1;
					break;
					case 4: 
						direction[0] = -1;
						direction[1] = 0;
					break;
					case 5: 
					break;
					case 6: 
						direction[0] = 1;
						direction[1] = 0;
					break;
					case 7: 
						direction[0] = -1;
						direction[1] = 1;
					break;
					case 8: 
						direction[0] = 0;
						direction[1] = 1;
					break;
					case 9: 
						direction[0] = 1;
						direction[1] = 1;
					break;
				}
				positionsToHighlight.addAll(getPossiblePositions(direction, entry.getValue()));
		}
		System.out.print("1");
	}

	private List<int[]> getPossiblePositions(int[] direction, int nspaces) {
		
		List<int[]> possiblePos = new ArrayList<>();
		int[] xyPosition = new int[2]; 
		
		int[] currentPos = position;

		for (int counter = 1; counter < nspaces+1; counter++) {		//cancer master
			
			xyPosition[0] = counter*direction[0] + currentPos[0];
			xyPosition[1] = counter*direction[1] + currentPos[1];
 			
			possiblePos.add(xyPosition);
		}

		return possiblePos;
	}
	
	static String[][] placement = new String[9][9];
		
}
	
