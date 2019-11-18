
import java.io.*;		//imports libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.net.URL;

import javax.swing.border.Border;

public class Piece extends JButton {
	
	public static Map<Float, Piece> Board = new HashMap <Float, Piece>();
	public static List<Piece> HighlightedPieces = new ArrayList<Piece>();
	public static Piece SelectedPiece;
	public String name;
	public ImageIcon picture;
	public Map <Integer,Integer> moves = new HashMap <Integer,Integer>();
	public int[] position;
	public boolean highlighted = false;

	public Piece(String n){
		super();
		name = n;
		loadMoves();		

		ImageIcon imageF = new ImageIcon(Shogi.ResourcesDir + n + "\\image.png" );
		Image img = imageF.getImage() ;  
		Image newimg = img.getScaledInstance(70, 80, java.awt.Image.SCALE_SMOOTH) ;  
		imageF = new ImageIcon(newimg);	
		setIcon(imageF);
		picture = imageF;
	

		Border black = BorderFactory.createLineBorder(Color.BLACK);
   	 	setBorder(black);
  		
		setOpaque(false);
		setContentAreaFilled(false);	
			
		addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
				onClicked();
           	}
     	});
	}

	private void onClicked(){
		if(HighlightedPieces.contains(this)){
			this.name = SelectedPiece.name;
			this.moves = SelectedPiece.moves;
			this.picture = SelectedPiece.picture;
			this.setIcon(this.picture);
			SelectedPiece.name = null;
			SelectedPiece.moves = null;
			SelectedPiece.picture = null;
			SelectedPiece.setIcon(SelectedPiece.picture);
			clearHighlights();
		}
		else{
			SelectedPiece = this;
			highlight();
		}
	}

	private void loadMoves(){

		File f = new File(Shogi.ResourcesDir + name + "\\moves.txt");
		try{
			BufferedReader in = new BufferedReader(new FileReader(f));
			try{
				String line;
				while (( line = in.readLine()) != null) {
					   String[] m = line.split(":");
					   moves.put(Integer.parseInt(m[0]),Integer.parseInt(m[1])); 	 
				}
			} catch (IOException e){}
		} catch (FileNotFoundException e){}
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

		clearHighlights();
		
		for (int[] position : positionsToHighlight) {

			Piece p = Piece.Board.get((float) position[0] + (float) position[1]/10);
			HighlightedPieces.add(p);
			p.setHighlighted(true);
		}
	}

	private void clearHighlights(){
		for (Iterator<Piece> iterator = HighlightedPieces.iterator(); iterator.hasNext();) {
			Piece p = iterator.next();
			iterator.remove();
			p.setHighlighted(false);
		}
	}

	private void setHighlighted(boolean h){
		highlighted = h;
		if(highlighted){
			this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		}
		else{
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
	}

	private List<int[]> getPossiblePositions(int[] direction, int nspaces) {
		
		List<int[]> possiblePos = new ArrayList<>();
		int[] xyPosition;
		
		int[] currentPos = position;

		for (int counter = 1; counter < nspaces+1; counter++) {
			xyPosition = new int[2];
			xyPosition[0] = counter*direction[0] + currentPos[0];
			xyPosition[1] = counter*direction[1] + currentPos[1];
			if((xyPosition[0]<0 || xyPosition[0]>8) || (xyPosition[1]<0 || xyPosition[1]>8)){
				continue;
			}
			possiblePos.add(xyPosition);
		}
		return possiblePos;
	}
	
	static String[][] placement = new String[9][9];
		
}
	
