import java.util.*;

public class Player{
    
    public static Player ActivePlayer;

    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;
    public  Map<String, Piece> CapturedZone = new HashMap <String, Piece>();

    public Player(boolean main){

        isMainPlayer=main;
    }

    public void addPiece(Piece p){

        pieces.add(p);
        p.player = p.player == null? this : p.player;
        if(p.player != this || !isMainPlayer){
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