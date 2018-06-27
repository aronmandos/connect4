package nl.aronmandos.connect4.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.ArrayList;




@Entity
public class PlayingField {
	
	@Id
	@GeneratedValue
	private Long id = null;
	
	//@Type(type="BINARY")
	//@Size(min=4096, max=8192)
	@Lob
	private ArrayList<ArrayList<Integer>> places = new ArrayList<>();;
	private int height,width,playerCount;

	private static final int defaultGridsize = 6;
	private static final int defaultPlayerCount  =2;
	private static final int winningConsecutiveLength = 4;

	public PlayingField() {
		this(6,6,2);
	}
	public PlayingField(int height, int width){
		this(height,width,2);
	}
	public PlayingField(int height, int width, int playerCount) {
		this.height = height;
		this.width = width;
		this.playerCount=playerCount;
		
		for (int i=0; i<width; i++) {
			ArrayList<Integer> column = new ArrayList<>();
			for (int j = 0; j < height; j++) {
				column.add(0);
			}
			places.add(column);
		}
	}
	
	public ArrayList<ArrayList<Integer>> getPlaces() {
		return places;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	public int getPlayerCount(){
		return playerCount;
	}
	public boolean isMoveValid(int column, int player) {
		if (column > width && column>=0) {
			return false;
		}
		if (column > this.height) {
			return false;
		}
		if (player > playerCount) {
			return false;
		}
		return true;
	}
	
	public void doMove(int column, int player) {
		places.get(column).set(places.get(column).lastIndexOf((Integer)0), player);
	}
	
	public int checkForVictory() {


		// check verticals
		for (ArrayList<Integer> gridCol :
				places) {
			int[] consecutivePlaces = new int[playerCount];
			int consecutiveStringStartedForIndex=0;
			for (int gridRowCell :
					gridCol) {
				// if place is filled with a move and the move belongs to player of previous loop iteration, the add one to the string of consecutive places
				if(gridRowCell >0 && (gridRowCell==consecutiveStringStartedForIndex||consecutiveStringStartedForIndex==0)) {
					consecutivePlaces[gridRowCell - 1]++;
					consecutiveStringStartedForIndex=gridRowCell;
				}else{
					//string interrupted, reset back to 0
					consecutiveStringStartedForIndex=0;
					if(gridRowCell>0) consecutivePlaces[gridRowCell-1]=0;
				}
			}

			int i = checkForWinningPlayer(consecutivePlaces);
			if (i>0) return i;


		}
		//check horizontal
//		int rowPointer=0;
//		for (int i = 0; i < playerCount; i++) {
//			consecutivePlaces[i]=0;
//		}
//		while(rowPointer<places.size()) {
//			for (ArrayList<Integer> l :
//					places) {
//				if(l.get(rowPointer)==1)
//					consecutiveCountPlayerOne++;
//				else if(l.get(rowPointer)==2)
//					consecutiveCountPlayerTwo++;
//			}
//
//			int i = checkForWinningPlayer(consecutivePlaces);
//			if (i>0) return i;
//
//			rowPointer++;
//		}
//
//		//check diagonal
//		int startPoint=0;
//		Boolean directionInverse=false;
//		for (int i = 0; i < playerCount; i++) {
//			consecutivePlaces[i]=0;
//		}
//
//		while(startPoint<places.size()) {
//			for (int i = 0; i < places.size(); i++) {
//				ArrayList<Integer> l = places.get(i);
//				int rowIndex = startPoint-i;
//
//				if(rowIndex>=0 && rowIndex<l.size()) {
//					if (l.get(rowIndex) == 1)
//						consecutiveCountPlayerOne++;
//					else if (l.get(rowIndex) == 2)
//						consecutiveCountPlayerTwo++;
//				}
//			}
//
//			if(consecutiveCountPlayerOne>=4)
//				return 1;
//			else if(consecutiveCountPlayerTwo>=4)
//				return 2;
//
//			consecutiveCountPlayerOne=0;
//			consecutiveCountPlayerTwo=0;
//			startPoint++;
//		}
		//TODO check for winstate
		//TODO return winner
		return 0;
	}

	private Integer checkForWinningPlayer(int[] consecutivePlaces) {
		for (int i = 0; i < consecutivePlaces.length; i++) {
			if(consecutivePlaces[i]>=4){
				return i+1;
			}
		}
		return 0;
	}
}
