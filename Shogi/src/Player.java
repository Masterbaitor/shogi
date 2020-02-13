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

        Piece unpromotedPiece = new Piece(p.name, false);
        unpromotedPiece.isCaptured = true;
        addPiece(unpromotedPiece);
        CapturedZone.get(p.name).addPiece(unpromotedPiece);
    }

    public static Player getOpponentPlayer(Player player)
    {
        if(player == Shogi.Player1){
			return Shogi.Player2;
        }
        return Shogi.Player1;
    }
}