package game;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
/**
 * 
 * 
 */
public class Layer extends GameObject {
	
	/**
	 * Kuinka paljon kuvakkeen yl�laidasta on tyhj�� tilaa, johon ei voi 
	 * t�rm�t�?
	 */
	private float freeSpace = 10;
	
	public Layer(String imageReference) {
		super(imageReference);
		
	}
	/**
	 * 
	 * @param layer
	 * @return jana kuvan yl�tasosta
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
