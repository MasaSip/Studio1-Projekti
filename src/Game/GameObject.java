package game;





import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
			
	private Image image;
	private String imageLocation;
	private Vector2f locationAbs;
	
	
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
	/**
	 * 
	 * @return kuvan korkeus
	 */
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
	
	
	public void loadImage() throws SlickException{
		this.image = new Image(this.imageLocation);
	}
	
	/**
	 * 
	 * @param vector Vector2f jonka x ja y koordinaatit lisataan olion 
	 * alkuperaisiin x ja y koordinaatteihin
	 */
	public void move(Vector2f vector){
		/*
		 * XXX: turhaa
		float currentX = this.locationAbs.getX();
		float currentY = this.locationAbs.getY();
		float newX = currentX + vector.getX();
		float newY = currentY + vector.getY();
		this.locationAbs.set(newX, newY);
		 */
		this.locationAbs.add(vector);
	
		
	}
	

}
