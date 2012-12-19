package Game;





import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;

public class GameObject {
			
	private Image image;
	private String imageLocation;
	private Point locationAbs;
	private float speed;
	
	/**
	 * 
	 * @param imageLocation lahdetiedoston sijainti
	 */
	public GameObject(String imageLocation) {
		this.imageLocation = imageLocation;
		
	}
	
	public void setSpeed(float s){
		this.speed = s;
	}
	public float getSpeed(){
		return this.speed;
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
	
	public float getMinYabs(){
		return this.getLocationAbs().getMinY();
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
	public void setLocationOnBottom(){
			
		float Xabs = (Game.WIDTH - this.getWidth())/2;
		float Yabs = Game.HEIGHT - this.getHeight();
		
		this.locationAbs = new Point(Xabs, Yabs);
	}
	
	
	public void setLocation(Point locationAbs){
		this.locationAbs = locationAbs;
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
	
	/**
	 * 
	 * @param vector Point jonka x ja y koordinaatit lisataan olion 
	 * alkuperaisiin x ja y koordinaatteihin
	 */
	public void move(Point vector){
		float currentX = this.locationAbs.getX();
		float currentY = this.locationAbs.getY();
		float newX = currentX + vector.getX();
		float newY = currentY + vector.getY();
		
		this.locationAbs.setLocation(newX, newY);
	}

}
