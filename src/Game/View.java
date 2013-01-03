package Game;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


/**
 * Pelista piirretaan naytolle suorakulmion sisaan jaava osuus. Suorakulmion 
 * sijaintiarvot ovat absoluuttisen koordinaatiston arvoja. Pelin edetessa 
 * suorakulmio liikkuu ylemmas eli sen y-koordinaatit pienenevat. 
 * Koska suorakulmio liikkuu ylospain, nayttaa kuin kaikki valuisi 
 * itsestaan alaspain.
 */

public class View extends Rectangle {
	private float scrollingSpeed;
	
	public View() {
		super(0, 0, Game.WIDTH, Game.HEIGHT);
		this.scrollingSpeed = 0.1f;
	}
	/**
	 * 
	 * @return kuinka paljon maailmaa on kelattu
	 */
	public float getScrolledDistance(){
		return Math.abs(this.getMinY());
	}
	
	public void draw(GameObject o){
		Vector2f location = this.getLocationOnScreen(o);
		o.getImage().draw(location.getX(), location.getY());
	}
	
	public Vector2f getLocationOnScreen(GameObject o){
		float X = o.getLocationAbs().getX();
		float Y = o.getLocationAbs().getY();
		
		float viewScrolled = this.getScrolledDistance();
	
		
		float YonScreen = Y + viewScrolled;
		
		return new Vector2f(X, YonScreen);
	}
	
	/**
	 * 
	 * @param delta viive edellisesta paivityksesta
	 */
	public void scroll(int delta){
		float oldY = this.getY();
		float newY = oldY - delta*this.scrollingSpeed;
		this.setY(newY);
		
	}
}
