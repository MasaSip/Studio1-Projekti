package game;

/**
 * Millainen on avatarin liike vaakasuunnassa?
 * Vasemmalle, paikallaan vain oikealle
 * @author Masa
 *
 */
public enum MovingStatus {
	LEFT, STATIC, RIGHT;
	
	
	/**
	 * vasemman vastakohta on oikea. Oikean vasen, ja staticin static
	 * @param status
	 * @return vastakohta
	 */
	public boolean isOppositeTo(MovingStatus status){
		
		if (this.equals(MovingStatus.STATIC)) return false;
		
		if (status.equals(MovingStatus.STATIC)) return false;
		
		if (this.equals(status)) return false;
		
		return true;
	}
	
	@Override
	public String toString(){
		switch (this){
		case LEFT : return "vasen"; 
		case RIGHT : return "oikea";
		default : return "ei liikettä";
				
		}
		
	}
}
