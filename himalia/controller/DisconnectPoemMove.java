package himalia.controller;

import java.util.ArrayList;

import himalia.model.*;

/**
 * 
 * @author saadjei
 *
 */
public class DisconnectPoemMove extends AbstractMove {

	Poem poem;
	ArrayList<Poem> resultPoems;
	ArrayList<Word> resultPoemWords = new ArrayList<Word>();
	Word originalPoemDegradeWord;

	Poem disconnectedPoem;
	Poem additionalPoem;
	
	Position releasePosition;
	Position destPoemPosition;
	Position originalPos;
	Position thirdPoemPos;
	ProtectedRegion proReg;
	
	int disconnectionPoint = -1; //0 =>top; 1=> middle; 2=>bottom
	boolean fromUndo = false;
	boolean originalPoemDegraded = false;

	/** Constructor for the move. */
	public DisconnectPoemMove(Poem poem, Position releasedPos, Position originalPos, Position destPoemPos){
		this.poem = poem;
		this.releasePosition = releasedPos;
		this.originalPos = originalPos;
		this.destPoemPosition = destPoemPos;
		
		if (originalPos.x == poem.getPosition().x &&
				originalPos.y == poem.getPosition().y){
			disconnectionPoint = 0; //top of poem
		}else if(originalPos.y == poem.getRow(poem.getRows().size()-2).getPosition().y + 15){
			disconnectionPoint = 2; //bottom of poem
		}else{
			disconnectionPoint = 1; //middle of poem;
		}
	}
	
	@Override
	/** Execute the disconnect poem move.*/
	public boolean execute(Board board) {
		if(fromUndo){
			poem = proReg.getIntersectedPoem(originalPos.x, originalPos.y);
			//use the original position in the poem to determine which row to disconnect.
			resultPoems = poem.disconnectPoem(this.originalPos);
			//reset the position to the destination.
			resultPoems.get(0).setPosition(destPoemPosition);
			fromUndo=false;
		}else{
			resultPoems = poem.disconnectPoem(this.releasePosition);
		}
		proReg =board.getProtectedRegion();
		if (resultPoems.size() != 0){
			Word w; 
			for(Poem p: resultPoems){
				//check if any of the resulting poems is a one-word poem, convert it into a word.
				if(p.isSingleWordPoem()){
					w = proReg.degradeOneWordPoem(p);
					resultPoemWords.add(w);
				}else{
					proReg.addPoem(p);
				}
			}

			if(disconnectionPoint == 1){
				thirdPoemPos = resultPoems.get(1).getPosition();
			}
			
			if(poem.isSingleWordPoem()){
				originalPoemDegradeWord = proReg.degradeOneWordPoem(poem);
				
				originalPoemDegraded = true;
			}else{
				originalPoemDegraded = false;
			}
			
			return true;
		}
		
		return false;
	}

	
	@Override
	/** Undo the disconnect Poem move.*/
	public boolean undo(Board board) {
		// connect the poems
		
		recoverPoems();		
		
		switch (disconnectionPoint) {
		case 0:
			//top
			if(!poem.connectTopPoem(proReg.deletePoem(resultPoems.get(0)),false)){
				proReg.addPoem(resultPoems.get(0));
				resultPoems.get(0).setPosition(destPoemPosition);
				return false;
			}
			break;
		case 1:
			//middle
			if( !(poem.connectBottomPoem(proReg.deletePoem(resultPoems.get(0))) &&
					poem.connectBottomPoem(proReg.deletePoem(resultPoems.get(1)))) ){
				resultPoems.get(0).setPosition(destPoemPosition);
				proReg.addPoem(resultPoems.get(0));
				proReg.addPoem(resultPoems.get(1));
				return false;
			}
			break;
		case 2:
			//bottom
			if(!poem.connectBottomPoem(proReg.deletePoem(resultPoems.get(0)))){
				proReg.addPoem(resultPoems.get(0));
				resultPoems.get(0).setPosition(destPoemPosition);
				return false;
			}
			break;
		default:
			resultPoems.get(0).setPosition(destPoemPosition);
			return false;
		}	
		//undo was successful
		resultPoems = null;
		fromUndo=true;
		return true;
	}

	/** This helper function recovers all resulting one-word poems that were degraded to words. */
	private void recoverPoems() {
		Poem p;
		if (originalPoemDegraded){
			poem = proReg.convertWordToPoem(originalPoemDegradeWord);
			originalPoemDegradeWord=null;
		}
		
		//get the first poem
		p = proReg.getIntersectedPoem(releasePosition.x, releasePosition.y);
		if (p != null){
			resultPoems.set(0,p);
		}
		
		if(disconnectionPoint ==1){
			if (resultPoems.get(1).getRowCount()==0){
				resultPoems.set(1, proReg.getIntersectedPoem(thirdPoemPos.x, thirdPoemPos.y));
			}
		}
		
		//handle the situation where any of the poems is degraded;		
		for (int i = 0; i<resultPoemWords.size(); i++){
			proReg.addPoem(resultPoems.get(i));//
			//remove those previously converted words from the board.
			proReg.removeWord(resultPoemWords.get(i));
		}
		resultPoemWords.clear();

		//set the first resulting poem to the original position.
		resultPoems.get(0).setPosition(originalPos);

	}
	
}
