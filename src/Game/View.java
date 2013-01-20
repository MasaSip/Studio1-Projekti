package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Line;
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
	 * jos autoScrollLimit % Avatrista on ruudun ylapuolella, scrollataan 
	 * ruutua ylospain kasvattamatta scrollausnopeutta
	 */
	private final float autoScrollLimit = -10;
	
	/**
	 * määrittää rivivälin
	 */
	private final float gap = 50.0f;
	
	/**
	 * bonarivoimapalkki piirretaan talla kuvalla.
	 */
	private Image bonusBar;
	
	
	
	public View() throws SlickException {
		super(0, 0, Game.WIDTH, Game.HEIGHT);
		this.scrollFunction = new ScrollFunction();
		this.scoreColor = Color.red;
		
		Font awtFont = new Font("Chalkduster", Font.BOLD, 36);

		this.scoreFont = new UnicodeFont(awtFont);
			
		
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
	
	public void draw(GameObject o, Graphics g, boolean extraInfo){
		Vector2f location = this.getLocationOnScreen(o.getLocationAbs());
		o.getImage().draw(location.getX(), location.getY());
		
		if (extraInfo && o instanceof Layer){
			Layer l = (Layer) o;
			Line collisionLine = l.getCollisionLine();
			
			Vector2f startAbs = collisionLine.getStart();
			Vector2f endAbs = collisionLine.getEnd();
			Vector2f start = this.getLocationOnScreen(startAbs);
			Vector2f end = this.getLocationOnScreen(endAbs);
			
			g.setColor(Color.magenta);
			g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
			
		}
	}
	
	/**
	 * 
	 * @param abs sijainti absoluuttisessa koordinaatistossa.
	 * @return Sijainti naytolla
	 */
	public Vector2f getLocationOnScreen(Vector2f abs){
		float X = abs.getX();
		float Y = abs.getY();
		
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
		String scoreTxt = 
				"PARAS KORKEUS \n" + Integer.toString(score);
		
		this.scoreFont.drawString(Game.WIDTH - 490 , 30, scoreTxt);
		
		
		
	}
	
	public void initFont() throws SlickException{
		
		this.scoreFont.addAsciiGlyphs();
		java.awt.Color menuColor = new java.awt.Color(245, 0, 0);
		ColorEffect red = new ColorEffect(menuColor);//java.awt.Color.red);
		this.scoreFont.getEffects().add(red);
		this.scoreFont.loadGlyphs();
		
	}
	
	
	public void drawBonusBar(Avatar avatar, Graphics g){
		
		//tämänhetkisten bonareitten osuus maximi bonareista
		float portion = avatar.getBonusPercentage()/100;
		float full = 300f;
		
		this.bonusBar.draw(10, 10 + this.gap, portion*full, this.gap);
	
		
	}
	
	
	public void drawInformation(Avatar avatar, float average, boolean extra){
		float bonus = avatar.getBonusPercentage();
		
	
		String txt = "BONARIVOIMAT: ";
		txt += String.format("%.0f", bonus);
		txt += " %";
		scoreFont.drawString(10, 10, txt);
		
		if (!extra) return;
		
		String scrl = "Scrollaus nopeus ";
		scrl += this.format(2, this.scrollFunction.scrollingSpeed);
				//String.format("%.2f", this.scrollFunction.scrollingSpeed);
		scoreFont.drawString(10, 10 +2*gap, scrl);
		
		String increase = "kiihtyvyys: ";
		increase += this.format(3, this.scrollFunction.lastIncrease);
				//String.format("%.3f", this.scrollFunction.lastIncrease);
		scoreFont.drawString(10, 10 +3*gap, increase);
		
		String averg = "Viive keskimäärin (ms): ";
		averg += this.format(3, average);
		scoreFont.drawString(10, 10 + 4*gap, averg);
		
		String status = "Viimeisin liikesuunta: " +  
				avatar.getMovingStatus().toString();
		scoreFont.drawString(10, 10 + 5*gap, status);
		
		
	}
	
	public String format(int desimals, float number){
		String dsmals = Integer.toString(desimals);
		return String.format("%." + dsmals + "f", number);
	}
	
	
	public void drawBackground(Graphics g){
		Color pink = new Color(245, 215, 235);
		g.setBackground(pink);
	}
	
	public void loadImages() throws SlickException{
		this.bonusBar = new Image("data/redBar.png");
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
		private final float[] value = {0.008f, 0.005f};
			
		private final float[] limit = {0.35f};
		
		
		
		
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
