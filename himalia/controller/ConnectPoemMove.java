
package himalia.controller;

import himalia.model.*;

/**
 * This class handles connecting one poem to another.
 * @author saadjei
 *
 */

public class ConnectPoemMove extends AbstractMove {

    Poem poem2;
    Poem poem1;
    Word singlePoem1Word;
    Word singlePoem2Word;
    Position poem2Pos;

    int numOfRows;//number of rows poem 1 have
    boolean fromRedo = false;
    boolean connectedFromTop = false;
    
    Position sourcePos, destPos;
	ProtectedRegion proReg;

    /** This is the constructor for the ConnectPoemMove class. */
    public ConnectPoemMove(Poem poem1, Poem poem2, Position sourcePos, Position destPos) {
        this.poem2 = poem2;
        this.poem1 = poem1;
        this.sourcePos = sourcePos;
        this.destPos = destPos;
        int intersect = poem1.getConnectionDirection(poem2);

        //determine bottom or top from here
        if(intersect==0){//top
        	connectedFromTop = true;
            this.numOfRows = poem2.getRows().size();
        }else if(intersect ==1){//bottom
            this.numOfRows = poem1.getRows().size();
        }
    }

    /** This function executes the ConnectPoemMove. */
    @Override
    public boolean execute(Board board) {
    	proReg = board.getProtectedRegion();
    	//for redo
    	if(fromRedo){
    		//convert all single word poems to their previous status if this is from redo.
    		if(singlePoem1Word != null){
    			poem1=proReg.formOneWordPoem(singlePoem1Word);
    		}else{
    			poem1 = proReg.getIntersectedPoem(poem1.getPosition().x, poem1.getPosition().y);
    		}
    		
    		if(singlePoem2Word != null){
    			poem2=proReg.formOneWordPoem(singlePoem2Word);
    		}else{
    			poem2 = proReg.getIntersectedPoem(sourcePos.x, sourcePos.y);
    		}
    		
    		poem2.setPosition(destPos);

    	}
    	
        if (proReg.connectPoemToPoem(poem1, poem2)){
            return true;
        }
        
        if(fromRedo){
        	if (poem1.isSingleWordPoem()){
        		proReg.degradeOneWordPoem(poem1);
        	}
        	if (poem2.isSingleWordPoem()){
        		//singlePoem1Word.setPosition(sourcePos);
        		proReg.degradeOneWordPoem(poem2);
        	}
        }
        return false;
    }

    /** This function undoes a connect move. */
    @Override
    public boolean undo(Board board) {
        //remove all the rows that belonged to poem 2 and use them to create a new poem    	
    	//create the second poem
    	boolean results;
		poem1 = proReg.getIntersectedPoem(poem1.getPosition().x, poem1.getPosition().y);
    	poem2 = poem1.disconnectPoemFromRowIndex(numOfRows, connectedFromTop);

    	if (poem2 != null){
    		board.getProtectedRegion().addPoem(poem2);
    		poem2.setPosition(sourcePos);
    		
    		//check if any of the poems is a one word poem. degrade it if so afterwards.
    		if(poem1.isSingleWordPoem()){
    			singlePoem1Word = board.getProtectedRegion().degradeOneWordPoem(poem1);
    		}else{
    			singlePoem1Word = null;
    		}
    		
    		if(poem2.isSingleWordPoem()){
    			singlePoem2Word = board.getProtectedRegion().degradeOneWordPoem(poem2);
    		}else{
    			singlePoem2Word=null;
    		}
    		results = true;
    	}else{
    		results= false;
    	}
    	
    	fromRedo = true;
    	return results;
    }

}

