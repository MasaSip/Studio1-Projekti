package Peli;

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
	 * Tietaa mika osa pelista tuli nayttaa ruudulle
	 */
	private Polygon viewWindowAbsXY;
	private Avatar avatar;
	
	public GameEngine() {
		float[] figure = 
			{0f,0f, Game.WIDTH,0f, Game.WIDTH,Game.HEIGHT, 0f,Game.HEIGHT};
		this.viewWindowAbsXY = new Polygon(figure);
		this.avatar = new Avatar();
	}
	
	public void moveAvatar(int direction, int delta){
		Polygon a = new Polygon();
		a.
	}
	
	/**
	 * 
	 * @param gameObject mita haluat sijoittaa
	 * @param location mihin haluat sijoitaa peliOlion
	 * @return Point johon GameObject tulisi piirtaa jotta se olisi ruudulla
	 * halutussa paikassa
	 */
	public Point calcLocation(GameObject gameObject, Location location){
		if (gameObject == null) return null;

		if (location == Location.BOTTOM){
			float Y = this.getBottomYonSreen() - gameObject.getHeight();
			float X = (Game.WIDTH + gameObject.getWidth())/2;
			return new Point(X,Y);
			
			
		}
		return null;
	}
	
	/**
	 * 
	 * @return Y-koordinaatti absoluuttisessa koordinaatistossa
	 */
	public float getBottomYonSreen(){
		return this.viewWindowAbsXY.getMaxY();
	}
	
	

}
