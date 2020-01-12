import java.util.*;

public class Player{
    
    public static Player ActivePlayer;

    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;
    public  Map<String, CaptureButton> CapturedZone = new HashMap <String, CaptureButton>();

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

        p.player.pieces.remove(p);
        Piece unpromotedPiece = new Piece(p.name, false);
        addPiece(unpromotedPiece);
        CapturedZone.get(p.name).addPiece(unpromotedPiece);
    }


}