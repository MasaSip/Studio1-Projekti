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
	private final float autoScrollLimit = 40;
	
	
	
	public View() throws SlickException {
		super(0, 0, Game.WIDTH, Game.HEIGHT);
		this.scrollFunction = new ScrollFunction();
		this.scoreColor = Color.red;//new Color(210, 50, 40, 255);
		
		Font awtFont = new Font("Comic Sans MS", Font.BOLD, 25);
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
		this.scroll(1.0f, delta, speedIncrease);
	}
	
	/**
	 * @param k scrollataa k kertaa vakioarvo
	 * @param delta viive edellisesta paivityksesta
	 * @param speedIncrease kasvatetaanko samalla scrollausnopeutta
	 */
	public void scroll(float k, int delta, boolean speedIncrease){
		
		float oldY = this.getY();
		float newY = oldY - delta*this.scrollFunction.scrollingSpeed*k;
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
	
	public void drawInformation(Avatar avatar, float average, boolean extra){
		float bonus = avatar.getJumpingBonus();
		float gap = 50.0f;
		
		
		String txt = "Bonari Voimat: ";
		txt += String.format("%.0f", bonus);
		scoreFont.drawString(10, 10, txt);
		
		if (!extra) return;
		
		String scrl = "Scrollaus Nopeus ";
		scrl += this.format(2, this.scrollFunction.scrollingSpeed);
				//String.format("%.2f", this.scrollFunction.scrollingSpeed);
		scoreFont.drawString(10, 10 +gap, scrl);
		
		String increase = "increase: ";
		increase += this.format(3, this.scrollFunction.lastIncrease);
				//String.format("%.3f", this.scrollFunction.lastIncrease);
		scoreFont.drawString(10, 10 +2*gap, increase);
		
		String averg = "average delta: ";
		averg += this.format(5, average);
		scoreFont.drawString(10, 10 + 3*gap, averg);
		
		
	}
	
	public String format(int desimals, float number){
		String dsmals = Integer.toString(desimals);
		return String.format("%." + dsmals + "f", number);
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
		private float lastIncrease = 0.0f;
		
		//value taulukon tulee olla yhden pidempi kuin limit taulukko
		private final float[] value = {0.01f, 0.005f};
			//{0.005f, 0.015f, 0.025f,0.015f, 0.01f, 0.005f};
		private final float[] limit = {0.4f};
			//{0.05f, 0.07f, 0.40f, 0.48f,0.5f};
		
		
		
		public ScrollFunction(){
			this.scrollingSpeed = 0.03f;
		}
		
		public void increaseScrollingSpeed(int delta){
	
			float increase = 0;
			
			for (int i = 0; i < this.limit.length; i++){
				
				if(this.scrollingSpeed < limit[i]){
					increase = value[i];
					break;
				}
			}
			/*jos scrolling speed on suurempi kuin suurin limit tauluko arvo,
			* niin increase saa arvokseen value-taulukon viimeisimmian arvon
			*/
			if (increase == 0){
				increase = value[value.length -1];
			}
				
			
			//jaetaan 1000:lla koska delta on millisekuntteja
			this.scrollingSpeed += increase*delta/1000;
			this.lastIncrease = increase;
			
		}
	}
	
}
