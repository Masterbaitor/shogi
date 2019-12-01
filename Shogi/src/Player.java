import java.util.LinkedList;

public class Player{
    
    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;

    private void addPiece(Piece p){
        
        pieces.add(p);
        if (!isMainPlayer || p.player != this){
            p.flip();
        }
        
		p.player = this;
        
        loadMoves();
	}


}