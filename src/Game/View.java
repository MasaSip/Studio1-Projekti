package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


/**
 * Pelista piirretaan naytolle suorakulmion sisaan jaava osuus. Suorakulmion 
 * sijaintiarvot ovat absoluuttisen koordinaatiston arvoja. Pelin edetessa 
 * suorakulmio liikkuu ylemmas eli sen y-koordinaatit pienenevat. 
 * Koska suorakulmio liikkuu ylospain, nayttaa kuin kaikki valuisi 
 * itsestaan alaspain.
 * 
 *
 * 
 */

public class View extends Rectangle {
	
	private Color scoreColor;
	private UnicodeFont scoreFont;
	private ScrollFunction scrollFunction;
	
	/**
	 * jos autoScrollLimit % Avatrista on ruudun ylapuolella, scrollataan ruutua ylospain
	 * kasvattamatta scrollausnopeutta
	 */
	private final float autoScrollLimit = 70;
	
	
	
	public View() throws SlickException {
		super(0, 0, Game.WIDTH, Game.HEIGHT);
		this.scrollFunction = new ScrollFunction();
		this.scoreColor = Color.red;//new Color(210, 50, 40, 255);
		
		Font awtFont = new Font("Comic Sans MS", Font.BOLD, 20);
		this.scoreFont = new UnicodeFont(awtFont);
			//new UnicodeFont(Font.decode("Comic Sans MS"), 40, false, false);
		
	}
	
	public float getAutoScrollLimit(){
		return this.autoScrollLimit;
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
	 * @param speedIncrease kasvatetaanko samalla scrollausnopeutta
	 */
	public void scroll(int delta, boolean speedIncrease){
		
		float oldY = this.getY();
		float newY = oldY - delta*this.scrollFunction.scrollingSpeed;
		this.setY(newY);
		if (speedIncrease){			
			this.scrollFunction.increaseScrollingSpeed(delta);
		}
		
	}
	
	
	

	
	public void drawScore(int score, Graphics g){
		g.setColor(this.scoreColor);
		String scoreTxt = "Score \n" +  Integer.toString(score);
		
		this.scoreFont.drawString(Game.WIDTH - 490 , 30, scoreTxt);
		
		
		
	}
	
	public void initFont() throws SlickException{
		
		this.scoreFont.addAsciiGlyphs();
		ColorEffect red = new ColorEffect(java.awt.Color.red);
		this.scoreFont.getEffects().add(red);
		this.scoreFont.loadGlyphs();
		
	}
	
	public void drawExtraInformation(Avatar avatar){
		float bonus = avatar.getJumpingBonus();
		String txt = "Bonari Voimat: ";
		txt += String.format("%.0f", bonus);
		
		String scrl = "Scrollaus Nopeus ";
		scrl += String.format("%.2f", this.scrollFunction.scrollingSpeed);
		scoreFont.drawString(10, 10, txt);
		scoreFont.drawString(10, 60, scrl);
		
	}
	
	public void drawBackground(Graphics g){
		Color pink = new Color(245, 215, 235);
		g.setBackground(pink);
	}
	
	/**
	 * scrollSpeedLimitit kertovat, milloin on aika muuttaa kiihtyvyytta.
	 * Aluksi scrollausnopeutta kasvatetaan delta*value taulukon 1. arvo. 
	 * Kun limit taulukon eka arvo on ylittynyt, scrollausnopeutta kasvatetaan 
	 * delta*value taulukon 2. arvo jne.
	 */
	public class ScrollFunction {
		
		private float scrollingSpeed;
		
		//XXX value-limit j�rjestelm� tuskin toimii kunnolla
		private final float[] value = {0.002f, 0.005f, 0.005f, 0.003f};
		private final float[] limit = {0.12f, 0.24f, 0.30f};
		
		
		
		public ScrollFunction(){
			this.scrollingSpeed = 0.10f;
		}
		
		public void increaseScrollingSpeed(int delta){
			System.out.println(this.scrollingSpeed);
			float increase = 0;
			
			for (int i = 0; i < this.limit.length; i++){
				
				if (i == this.limit.length-1){
					increase = value[i+1];
				}
				
				else if(this.scrollingSpeed < limit[i]){
					increase = value[i];
					break;
				}
						
			}
			//jaetaan 1000:lla koska delta on millisekuntteja
			this.scrollingSpeed += increase*delta/1000;
			
		}
	}
	
}
