
import java.io.*;		//imports libraries
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.net.URL;

import javax.swing.border.Border;
import java.awt.geom.AffineTransform;

public class Piece extends JButton {
	
	public static Map<Float, Piece> Board = new HashMap <Float, Piece>();
	public static List<Piece> HighlightedPieces = new ArrayList<Piece>();
	public static Piece SelectedPiece;
	public String name;
	public ImageIcon picture;
	public Map <Integer,Integer> moves = new HashMap <Integer,Integer>();
	public int[] position;
	public boolean highlighted;
	public Player player;

	public Piece(String n){
		super();
		name = n;
	
		ImageIcon imageF = new ImageIcon(Shogi.ResourcesDir + n + "\\image.png" );
		Image img = imageF.getImage() ;  
		Image newimg = img.getScaledInstance(70, 80, java.awt.Image.SCALE_SMOOTH) ;  
		imageF = new ImageIcon(newimg);	
		setIcon(imageF);
		picture = imageF;
		setHighlighted(false);
		setOpaque(false);
		setContentAreaFilled(false);	


		addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
				onClicked();
           	}
		 });
	}

	public void setPosition(int x, int y){
		position = new int[]{x, y};
		if(name != null){
			int p = position[1] < 4 ? 1 : 2;
			//assignToPlayer(p);
		}
	}

/*	private void assignToPlayer(int player){
		
		if(player == 2){

			picture = flipImage(picture);
			setIcon(picture);
			
		}
		this.player = player;
		loadMoves();
	}
*/
	private ImageIcon flipImage(ImageIcon img){

			int w = img.getIconWidth();
			int h = img.getIconHeight();
			int type = BufferedImage.TYPE_4BYTE_ABGR_PRE;
			BufferedImage image = new BufferedImage(w, h, type);
			Graphics2D g2 = image.createGraphics();
			double x = (h - w)/2.0;
			double y = (w - h)/2.0;
			AffineTransform at = AffineTransform.getTranslateInstance(x, y);
			at.rotate(Math.toRadians(180), (w+y)/2.0, (h+x)/2.0);
			g2.drawImage(picture.getImage(), at, null);
			g2.dispose();
	 
			return new ImageIcon(image);
	}
	public void flip(){
		picture = flipImage(picture);
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
					if(player == 2 && Integer.parseInt(m[0]) < 10){
						m[0] = Integer.toString(10 - Integer.parseInt(m[0]));
					}
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
					case 51:
						direction[0] = 1;
						direction[1] = player == 2 ? -2 : 2;
					break;
					case 52:
						direction[0] = -1;
						direction[1] = player == 2 ? -2 : 2;
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
			this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
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
	
