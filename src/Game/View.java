package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
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
	private Color scoreColor;
	private UnicodeFont scoreFont;
	
	public View() {
		super(0, 0, Game.WIDTH, Game.HEIGHT);
		this.scrollingSpeed = 0.10f;
		this.scoreColor = Color.red;//new Color(210, 50, 40, 255);
		
		this.scoreFont =
			new UnicodeFont(Font.decode("Comic Sans MS"), 40, false, false);
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
		this.increaseScrollingSpeed(delta);
		
	}
	
	public void increaseScrollingSpeed(int delta){
		this.scrollingSpeed +=0.000005*delta;
		
		
		
	}
	
	public void drawScore(int score, Graphics g){
		g.setColor(this.scoreColor);
		String scoreTxt = Integer.toString(score);
		
		g.drawString(scoreTxt, Game.WIDTH - 50, 20); // xxx testi piirto
		
		// miks alla oleva ei toimi? xxx
		Font font = new Font("Comic Sans MS", Font.BOLD, 40);
		//g.setFont(Font);
		
		
		UnicodeFont foont = new UnicodeFont(Font.decode("Comic Sans MS"), 48, false, false);
				//new UnicodeFont(font); 
		foont.drawString(80, 40, "heippa");
		// allaoleva pŠŠttyy  xxx
	}
}
