package nl.aronmandos.connect4.models;

import org.hibernate.annotations.Type;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;




@Entity
public class PlayingField {
	
	@Id
	@GeneratedValue
	private Long id = null;
	
	//@Type(type="BINARY")
	//@Size(min=4096, max=8192)
	@Lob
	private ArrayList<ArrayList<Integer>> places = new ArrayList<>();;
	private int height;
	private int width;
	
	private PlayingField() {
	
	}
	
	public PlayingField(int height, int width) {
		this.height = height;
		this.width = width;
		
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
	
	public boolean isMoveValid(int move, int player) {
		if (move > width && move>=0) {
			return false;
		}
		if (move > this.height) {
			return false;
		}
		if (player > 2) {
			return false;
		}
		return true;
	}
	
	public void doMove(int move, int player) {
		places.get(move).set(places.get(move).lastIndexOf((Integer)0), player);
	}
	
	public int checkForVictory() {
		int consecutiveCountPlayerOne=0;
		int consecutiveCountPlayerTwo=0;
		// check verticals
		for (ArrayList<Integer> l :
				places) {
			for (int m :
					l) {
				if(m==1)
					consecutiveCountPlayerOne++;
				else if(m==2)
					consecutiveCountPlayerTwo++;
			}
			
			if(consecutiveCountPlayerOne>=4)
				return 1;
			else if(consecutiveCountPlayerTwo>=4)
				return 2;
				
			consecutiveCountPlayerOne=0;
			consecutiveCountPlayerTwo=0;
			
		}
		//check horizontal
		int rowPointer=0;
		while(rowPointer<places.size()) {
			for (ArrayList<Integer> l :
					places) {
				if(l.get(rowPointer)==1)
					consecutiveCountPlayerOne++;
				else if(l.get(rowPointer)==2)
					consecutiveCountPlayerTwo++;
			}
			
			if(consecutiveCountPlayerOne>=4)
				return 1;
			else if(consecutiveCountPlayerTwo>=4)
				return 2;
			
			consecutiveCountPlayerOne=0;
			consecutiveCountPlayerTwo=0;
			rowPointer++;
		}
		
		//check diagonal
		int startPoint=0;
		Boolean directionInverse=false;
		
		while(startPoint<places.size()&&!directionInverse) {
			for (int i = 0; i < places.size(); i++) {
				ArrayList<Integer> l = places.get(i);
				int rowIndex = (directionInverse?startPoint+i:startPoint-i);
				
				if(rowIndex>=0 && rowIndex<l.size()) {
					if (l.get(rowIndex) == 1)
						consecutiveCountPlayerOne++;
					else if (l.get(rowIndex) == 2)
						consecutiveCountPlayerTwo++;
				}
			}
			
			if(consecutiveCountPlayerOne>=4)
				return 1;
			else if(consecutiveCountPlayerTwo>=4)
				return 2;
			
			consecutiveCountPlayerOne=0;
			consecutiveCountPlayerTwo=0;
			startPoint++;
		}
		//TODO check for winstate
		//TODO return winner
		return 0;
	}
}
