package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class GameObject {
			
	private Image image;
	private String imageLocation;
	private Point locationAbs;
	
	public GameObject(String imageLocation) {
		this.imageLocation = imageLocation;
		
		
		
	}

	
	public void draw(Polygon viewWindow) {
		Point locationOnScreen = this.getLocationOnScreen(viewWindow);
		float XonScreen = locationOnScreen.getX();
		float YonScreen = locationOnScreen.getY();
		
		this.image.draw
			(XonScreen, YonScreen);
		
	}
	
	public Point getLocationAbs(){
		
		return this.locationAbs;
	}
	
	public float getHeight(){
		return (float) this.image.getHeight();
	}
	
	public float getWidth(){
		return (float) this.image.getWidth();
	}
	/**
	 * 
	 * @param location mihin halutaan sijoittaa
	 */
	public void setLocation(Location location){
		float Xabs = 0;
		float Yabs = 0;
		if (location == null){
			this.locationAbs = null;
			return;
		}
		if (location.equals(Location.BOTTOM_CENTER)){
			Xabs = (Game.WIDTH - this.getWidth())/2;
			Yabs = Game.HEIGHT - this.getHeight();
		
		}
		this.locationAbs = new Point(Xabs, Yabs);
	}
	
	public Point getLocationOnScreen(Polygon viewWindow){
		float X = this.getLocationAbs().getX();
		float Y = this.getLocationAbs().getY();
		
		float heightFromGround = Game.HEIGHT - viewWindow.getMaxY();
		
		float YonScreen = Y + heightFromGround;
		
		return new Point(X, YonScreen);
	}
	
	public void loadImage() throws SlickException{
		this.image = new Image(this.imageLocation);
	}

}
