
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
	
	public static String[][] placement;
	public static String[][] CapturedPlacement;
	public static Map<Float, Piece> Board = new HashMap <Float, Piece>();
	public static List<Piece> HighlightedPieces = new ArrayList<Piece>();
	public static Piece SelectedPiece;

	public String folder;
	public boolean isPromoted;
	public boolean canPromote;
	public String name;
	public ImageIcon picture;
	public Map <Integer,Integer> moves = new HashMap <Integer,Integer>();
	public int[] position;
	public boolean highlighted;
	public Player player;

	public Piece(String n, boolean promoted){
		super();
		folder = Shogi.ResourcesDir + n;
		isPromoted = promoted;
		name = n;
		ImageIcon imageF = new ImageIcon(folder + (isPromoted ? "\\imagePromoted.png" : "\\image.png"));
		Image img = imageF.getImage();  
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
	}

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
		setIcon(picture);
	}

	public static void switchPieces(Piece pieceA, Piece pieceB){
		pieceA.name = pieceB.name;
		pieceA.player = pieceB.player;
		pieceA.folder = pieceB.folder;
		pieceA.canPromote = pieceB.canPromote;
		pieceA.isPromoted = pieceB.isPromoted;
		pieceA.moves = pieceB.moves;
		pieceA.picture = pieceB.picture;
		pieceA.setIcon(pieceA.picture);
		pieceB.name = null;
		pieceB.moves = null;
		pieceB.picture = null;
		pieceB.player = null;
		pieceB.folder = null;
		pieceB.canPromote = false;
		pieceB.isPromoted = false;
		pieceB.setIcon(pieceB.picture);
	}

	private void onClicked(){
		boolean isHighlighted = HighlightedPieces.contains(this);
		canPromote = isInPromotionZone();
		if(player == Player.ActivePlayer || isHighlighted){
			if(isHighlighted){
				switchPieces(this, SelectedPiece);
				canPromote = isInPromotionZone() || canPromote;
				if(canPromote && !isPromoted && (mustPromote() || (askForPromotion() == JOptionPane.YES_OPTION))){
					promote();
				}
				clearHighlights();
				Player.ActivePlayer = Player.ActivePlayer == Shogi.Player2 ? Shogi.Player1 : Shogi.Player2; 
			}
			else{
				SelectedPiece = this;
				highlight();
			}
		}
	}

	public void loadMoves(){

		File f = new File(folder + (isPromoted? "\\movesPromoted.txt" : "\\moves.txt"));
		try{
			BufferedReader in = new BufferedReader(new FileReader(f));
			try{
				String line;
				while (( line = in.readLine()) != null) {
					String[] m = line.split(":");
					if(!player.isMainPlayer && Integer.parseInt(m[0]) < 10){
						m[0] = Integer.toString(10 - Integer.parseInt(m[0]));
					}
					moves.put(Integer.parseInt(m[0]),Integer.parseInt(m[1]));
				}
			} catch (IOException e){}
		} catch (FileNotFoundException e){}
	}
	
	void highlight(){
		
		List<int[]> positionsToHighlight = new ArrayList<>();
		
		for (Map.Entry<Integer,Integer> entry : moves.entrySet()) {
				positionsToHighlight.addAll(getPossiblePositions(convertDirection(entry.getKey()), entry.getValue()));
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
			Piece pieceAtPosition = Board.get((float) xyPosition[0] + (float) xyPosition[1]/10);
			if((xyPosition[0]<0 || xyPosition[0]>8) || (xyPosition[1]<0 || xyPosition[1]>8) || pieceAtPosition.player == player){
				break;
			}
			possiblePos.add(xyPosition);
			if(pieceAtPosition.name != null){
				break;
			}
		}
		return possiblePos;
	}

	public int[] convertDirection(int i){
		int[] direction = new int[2];
		switch (i){
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
				direction[1] = player.isMainPlayer ? 2 : -2;
			break;
			case 52:
				direction[0] = -1;
				direction[1] = player.isMainPlayer ? 2 : -2;
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
		return direction;
	}

	public boolean hasPossiblePositions(){

		List<int[]> store;

		for (Map.Entry<Integer,Integer> entry : moves.entrySet()) {
			store = getPossiblePositions(convertDirection(entry.getKey()), entry.getValue());
			if (!store.isEmpty()){
				return true;
			}
		}
		return false;
	}

	public void promote(){
		
		Piece promotedPiece = new Piece(name, true);
		player.addPiece(promotedPiece);
		switchPieces(this, promotedPiece);
	}

	private boolean isInPromotionZone(){
		return ((name != "King" && name != "Gold") && (isInLastNRanks(3)));
	}

	private boolean mustPromote(){
		return ((name == "Pawn" || name == "Lance") && isInLastNRanks(1) || (name == "Knight" && isInLastNRanks(2)));
	}

	private boolean isInLastNRanks(int n){
		return (position[1]>(Shogi.height-1-n) && player == Shogi.Player1) || (position[1]<(n) && player == Shogi.Player2);
	}

	private int askForPromotion(){
		return JOptionPane.showConfirmDialog(Shogi.board, "Do you want to promote this piece?", "", JOptionPane.YES_NO_OPTION);
	}

}
	
