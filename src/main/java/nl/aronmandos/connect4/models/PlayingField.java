package nl.aronmandos.connect4.models;

import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;


@Entity
public class PlayingField {
	
	@Id
	@GeneratedValue
	private Long id = null;
	
	//@Type(type="BINARY")
	//@Size(min=4096, max=8192)
	@Lob
	private ArrayList<ArrayList<Integer>> places = new ArrayList<>();

	@Transient
	private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
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
				// if place is filled with a move and the move belongs to player of previous loop iteration, the add one to the string of consecutive place
				if(gridRowCell >0 && (gridRowCell==consecutiveStringStartedForIndex||consecutiveStringStartedForIndex==0)) {
					consecutivePlaces[gridRowCell - 1]++;
					if(gridRowCell==0) consecutiveStringStartedForIndex=gridRowCell;
				}else{
					//string interrupted, reset back to 0 or the found player
					// set string of player checking to 0
					if(gridRowCell>0) consecutivePlaces[consecutiveStringStartedForIndex]=0;
					// set player checking to newfound player (or 0)
					consecutiveStringStartedForIndex=gridRowCell;
					// set string for new found player to 1
					if(gridRowCell>0) consecutivePlaces[gridRowCell-1]=1;
				}
			}

			int i = checkForWinningPlayer(consecutivePlaces);
			if (i>0) return i;


		}

		// check horizontals
		int row=0;
		boolean finished = false;
		while(places.size()>0 && row<places.get(0).size()){

			// setup counter vars for this row check
			int[] consecutivePlaces = new int[playerCount];
			int consecutiveStringStartedForIndex=0;

			// go through columns
			for (ArrayList<Integer> gridCol :
					places) {

					int gridRowCell = gridCol.get(row);
					// if place is filled with a move and the move belongs to player of previous loop iteration, the add one to the string of consecutive places
					if(gridRowCell >0 && (gridRowCell==consecutiveStringStartedForIndex||consecutiveStringStartedForIndex==0)) {
						consecutivePlaces[gridRowCell - 1]++;
						if(gridRowCell==0) consecutiveStringStartedForIndex=gridRowCell;
					}else{
						//string interrupted, reset back to 0 or the found player
						// set string of player checking to 0
						if(gridRowCell>0) consecutivePlaces[consecutiveStringStartedForIndex]=0;
						// set player checking to newfound player (or 0)
						consecutiveStringStartedForIndex=gridRowCell;
						// set string for new found player to 1
						if(gridRowCell>0) consecutivePlaces[gridRowCell-1]=1;
					}


				int i = checkForWinningPlayer(consecutivePlaces);
				if (i>0) return i;
			}
			row++;
		}

		//check diagonal
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int val = places.get(i).get(j);
				if(val==0){
					continue;
				}
				if(val>0){
					int stringLength = 1;
					int shiftCount=0;
					while(stringLength>0){
//						if(i+1+shiftCount>=width || j+1+shiftCount>=height || j-1-shiftCount<0){
//							stringLength=0;
//							shiftCount=0;
//							continue;
//						}
						if(i+1+shiftCount<width&&j+1+shiftCount<height && places.get(i+1+shiftCount).get(j+1+shiftCount)==val){
							stringLength++;
							shiftCount++;
						}else if(i+1+shiftCount<width&&j-1-shiftCount>0 && places.get(i+1+shiftCount).get(j-1-shiftCount)==val){
							stringLength++;
							shiftCount++;
						}else{
							stringLength=0;
							shiftCount=0;
							continue;
						}
						if(stringLength>3){
							return val;
						}

					}
				}
			}
		}
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
