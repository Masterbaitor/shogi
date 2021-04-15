import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class CaptureButton extends JButton {

    static final long serialVersionUID = 10007;

    public Player player;
    public String pieceName;
    public LinkedList<Piece> pieces = new LinkedList<>();

    public CaptureButton(String pieceName, Player player){
        this.player = player;
        this.pieceName = pieceName;
        setVerticalTextPosition(player == Shogi.Player2 ? JButton.BOTTOM : JButton.TOP);
        setIconTextGap(-5);
        setForeground(Color.decode("#ffdd00"));
        setOpaque(false);
        setContentAreaFilled(false);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));

        addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
				onClicked();
           	}
		 });
    }

    private void onClicked(){
        if(player == Player.ActivePlayer && !pieces.isEmpty()){
            Piece.SelectedPiece = pieces.peekLast();
            for (int i = 0; i < Piece.Board.length; i++) {
                for (int j = 0; j < Piece.Board[i].length; j++) {
                    Piece button = Piece.Board[i][j];
                    if(button == null || button.name != null || Piece.SelectedPiece.name.equals("Pawn") && ((pawnInFile(button.position[0]) || button.isInLastNRanks(1))) || Piece.SelectedPiece.name.equals("Lance")){
                        continue;
                    }
                    Piece.HighlightedPieces.add(button);
                    button.setHighlighted(true);
                }
            }
        }
    }

    static boolean pawnInFile(int file){
        for(int y = 0; y< Shogi.height; y++){
            Piece p = Piece.Board[file][y];
            if(p.player == Player.ActivePlayer && p.name.equals("Pawn") && !p.isPromoted){
                return true;
            }
        }
        return false;
    }

    public void updateText(){
        String text = pieces.isEmpty() ? "" : String.valueOf(pieces.size());
        setText(text);
    }

    public void addPiece(Piece p){
        if(pieces.isEmpty()){
            setIcon(p.picture);
        }
        pieces.add(p);
        updateText();
    }

    public void removePiece(Piece p){
        pieces.remove(p);
        if(pieces.isEmpty()){
            setIcon(null);
        }
        updateText();
    }

    public static boolean wasCaptured(Piece p){
        try{
            return p.player.CapturedZone.get(p.name).pieces.contains(p);
        }
        catch(java.lang.NullPointerException e){
            return false;
        }
    }

}