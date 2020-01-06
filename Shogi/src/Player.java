import java.util.LinkedList;

public class Player{
    
    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;
    
    public static Player ActivePlayer;

    public Player(boolean main){

        isMainPlayer=main;

    }

    public void addPiece(Piece p){

        pieces.add(p);
        if (p.player != this && p.player != null){
            p.flip();
        }
		p.player = this;
        p.loadMoves();
    }
    
    void capture(Piece p){
   
        Piece unpromotedPiece = new Piece(p.name, false);
        Piece.switchPieces(p, unpromotedPiece);
        p.player.pieces.remove(p);

    }


}