import java.util.LinkedList;

public class Player{
    
    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;

    public Player(boolean main){

        isMainPlayer=main;

    }

    public void addPiece(Piece p){
        
        pieces.add(p);
        if (!isMainPlayer || p.player != this && p.player != null){
            p.flip();
        }
		p.player = this;
        p.loadMoves();
    }
    
    private void removePiece(Piece p){

    }

    void capture(){
   
    }


}