import java.util.*;

public class Player{
    
    public static Player ActivePlayer;

    LinkedList<Piece> pieces = new LinkedList<>();
    boolean isMainPlayer;
    public  Map<String, CaptureButton> CapturedZone = new HashMap <String, CaptureButton>();
    public Piece King;
    public String Name;

    public Player(boolean main, String name){
        isMainPlayer=main;
        Name = name;
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
        getOpponentPlayer(this).pieces.remove(p);
        CapturedZone.get(p.name).addPiece(unpromotedPiece);
    }

    public static Player getOpponentPlayer(Player player)
    {
        if(player == Shogi.Player1){
			return Shogi.Player2;
        }
        return Shogi.Player1;
    }

    public boolean isInCheck()
    {
        int[] kingPosition = King.position;
        for (Piece p : getOpponentPlayer(this).pieces) {
            try{
                for (Map.Entry<Integer,Integer> entry : p.moves.entrySet()) {
                    for (int[] position : p.getPossiblePositions(p.convertDirection(entry.getKey()), entry.getValue())){
                        if(position[0] == kingPosition[0] && position[1] == kingPosition[1]){
                            return true;
                        }
                    }
                }
            }
            catch(NullPointerException e){
                continue;
            }
        }   
        return false;
    }

    public boolean isCheckmated(){
        for (Piece p : pieces){
            try{
                if (p.hasLegalMoves()){
                    return false;
                }
            }
            catch(NullPointerException e){
                continue;
            }
        }
        return true;
    }

}