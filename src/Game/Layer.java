package game;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
/**
 * 
 * 
 */
public class Layer extends GameObject {
	
	/**
	 * Kuinka paljon kuvakkeen ylälaidasta on tyhjää tilaa, johon ei voi 
	 * törmätä?
	 */
	private float freeSpace = 10;
	
	public Layer(String imageReference) {
		super(imageReference);
		
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
