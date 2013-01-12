package game;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
/**
 * 
 * Layer luokalle vois olla käyttöä
 *	tai luokka LayerLine extends Line?
 */
public class Layer extends GameObject {
	
	/*
	 * XXX: jos freeSpace on jotain muuta kuin 0, peli lakka toimimasta 
	 */
	private float freeSpace = 0;
	
	public Layer(String imageLocation) {
		super(imageLocation);
		
	}
	/**
	 * 
	 * @param layer
	 * @return jana kuvan ylätasosta
	 */
	public Line getCollisionLine(){
		Vector2f startPoint = this.getLeftTop().copy();
		Vector2f endPoint = this.getRightTop().copy();
		
		startPoint.y += this.freeSpace;
		endPoint.y += this.freeSpace;
		Line collisionLine = new Line(startPoint, endPoint);
		
		
		return collisionLine;
	}
	
	
}
