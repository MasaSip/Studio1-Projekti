package Game;





import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
			
	private Image image;
	private String imageLocation;
	private Vector2f locationAbs;
	private float speed;
	
	/**
	 * 
	 * @param imageLocation lahdetiedoston sijainti
	 */
	public GameObject(String imageLocation) {
		this.imageLocation = imageLocation;
		
	}
	
	public Image getImage(){
		return this.image;
	}
	
	public void setSpeed(float s){
		this.speed = s;
	}
	public float getSpeed(){
		return this.speed;
	}

	/*
	 * xxx pois jos toimii ilman 
	public void draw(View view) {
		Vector2f locationOnScreen = this.getLocationOnScreen(viewWindow);
		float XonScreen = locationOnScreen.getX();
		float YonScreen = locationOnScreen.getY();
		
		this.image.draw
			(XonScreen, YonScreen);
		
	}
	 */
	
	/**
	 * 
	 * @return kuvakkeen vasen yläkulma
	 */
	public Vector2f getLocationAbs(){
		
		return this.locationAbs;
	}
	
	/**
	 * 
	 * @return Kuvakkeen korkein y-koordinaatti
	 */
	public float getTopY(){
		return this.getLocationAbs().getY();
	}
	
	public float getHeight(){
		return (float) this.image.getHeight();
	}
	
	public float getWidth(){
		return (float) this.image.getWidth();
	}
	/**
	 * 
	 * @return Kuvakkeen vasen ylakulma
	 */
	public Vector2f getLeftTop(){
		return this.locationAbs.copy();
	}
	
	public Vector2f getLeftBottom(){
		float x = this.getLocationAbs().getX();
		float y = this.getLocationAbs().getY() + this.getHeight();
		return new Vector2f(x,y);
	}
	
	/**
	 * 
	 * @return Kuvakkeen oikea ylakulma
	 */
	public Vector2f getRightTop(){
		Vector2f fromRightToLeft = new Vector2f(this.getWidth(),0f);
		return this.getLeftTop().add(fromRightToLeft);
	}
	/**
	 * 
	 * @return kuvakkeen oikea alakulma
	 */
	public Vector2f getRightBottom(){
		float x = this.getLocationAbs().getX() + this.getWidth();
		float y = this.getLocationAbs().getY() + this.getHeight();
		return new Vector2f(x,y);
	}
	
	/**
	 * 
	 * @param location mihin halutaan sijoittaa
	 */
	public void setLocationOnBottom(){
			
		float Xabs = (Game.WIDTH - this.getWidth())/2;
		float Yabs = Game.HEIGHT - this.getHeight();
		
		this.locationAbs = new Vector2f(Xabs, Yabs);
	}
	
	
	public void setLocation(Vector2f locationAbs){
		this.locationAbs = locationAbs;
	}
	
	/*voi poistaa jos peli toimii xxx
	public Vector2f getLocationOnScreen(View view){
		float X = this.getLocationAbs().getX();
		float Y = this.getLocationAbs().getY();
		
		float viewScrolled = this.v
		 * float heightFromGround = Game.HEIGHT - viewWindow.getMaxY();
		 
		
		float YonScreen = Y + heightFromGround;
		
		return new Vector2f(X, YonScreen);
	}
	*/
	
	public void loadImage() throws SlickException{
		this.image = new Image(this.imageLocation);
	}
	
	/**
	 * 
	 * @param vector Vector2f jonka x ja y koordinaatit lisataan olion 
	 * alkuperaisiin x ja y koordinaatteihin
	 */
	public void move(Vector2f vector){
		float currentX = this.locationAbs.getX();
		float currentY = this.locationAbs.getY();
		float newX = currentX + vector.getX();
		float newY = currentY + vector.getY();
		
		this.locationAbs.set(newX, newY);
		
	}
	

}
