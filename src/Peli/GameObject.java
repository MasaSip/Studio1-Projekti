package Peli;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

public class GameObject 
	implements Drawable {
			
	private Image image;
	private Point size;
	private Point locationAbs;
	private Point locationOnScreen;
	
	public GameObject(Image image, Point location) {
		this.image = image;
		this.locationAbs = location;
		
	}

	@Override
	public void draw() {
		this.image.draw
			(this.locationOnScreen.getX(), this.locationOnScreen.getCenterY());
		
	}
	
	public float getHeight(){
		return (float) this.image.getHeight();
	}
	
	public float getWidth(){
		return (float) this.image.getWidth();
	}
	


}
