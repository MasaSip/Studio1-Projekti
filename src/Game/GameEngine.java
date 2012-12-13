package Game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
/**
 * Absoluuttisen koordinaatiston origo on vasen ylakulma aloitusnaytolta.
 * Absoluuttinen koordinaatisto kasvaa alasoikealle
 * @author 
 *
 */
public class GameEngine {
	/**
	 * Kaytetaan kun avataria halutaan liikuttaa vasemmalle
	 */
	public static final int LEFT = -1;
	
	/**
	 * Kaytetaan kun avataria halutaan liikuttaa oikealle
	 */
	public static final int RIGHT = -2; 
	
	/**
	 * Tietaa mika osa pelista tulee nayttaa ruudulle. Polygonin sijainti
	 * absoluuttisen koordinaatiston arvoja. 
	 */
	private Polygon viewWindow;
	private Avatar avatar;
	
	/**
	 * kuinka korkealla nakyma on lahtotasosta
	 */
	private float distanceFromGround;
	
	public GameEngine() throws SlickException {
		float[] figure = 
			{0f,0f, Game.WIDTH,0f, Game.WIDTH,Game.HEIGHT, 0f,Game.HEIGHT};
		this.viewWindow = new Polygon(figure);
		this.avatar = new Avatar();
	}
	
	public void putAvatarIntoGame() throws SlickException{
		this.avatar.setLocation(Location.BOTTOM_CENTER);
	}
	
	public void moveAvatar(int direction, int delta){
		Polygon a = new Polygon();
		
	}
	
	public void drawGameObjects(){
		this.avatar.draw(this.viewWindow);
	}
	
	public void loadImages() throws SlickException{
		this.avatar.loadImage();
	}
	

}
