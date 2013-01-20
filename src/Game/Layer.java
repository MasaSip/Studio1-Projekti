package game;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
/**
 * 
 * Tasoon voi tromata vain ylhaalta pain.
 */
public class Layer extends GameObject {
	
	/**
	 * Kuinka paljon kuvakkeen ylälaidasta on tyhjää tilaa, johon ei voi 
	 * törmätä?
	 */
	private float freeSpaceY ;
	private float freeSpaceX;
	private int layerType;
	
	public static final int BOTTOMLAYER = -1;
	public static final int GROUND = -2;
	public static final int GRASS = -3;
	public static final int CLOUD = -4;
	
	
	
	public Layer(int layer) {
		super(Layer.getImageReference(layer));
		this.layerType = layer;
		this.freeSpaceX = this.getFreeSpaceX(layer);
		this.freeSpaceY = this.getFreeSpaceY(layer);
		
	}
	/**
	 * 
	 * @param layer
	 * @return jana kuvan ylätasosta
	 */
	public Line getCollisionLine(){
		Vector2f startPoint = this.getLeftTop().copy();
		Vector2f endPoint = this.getRightTop().copy();
		
		startPoint.y += this.freeSpaceY;
		endPoint.y += this.freeSpaceY;
		
		startPoint.x += this.freeSpaceX;
		endPoint.x -= this.freeSpaceX;
		
		Line collisionLine = new Line(startPoint, endPoint);
		
		
		
		return collisionLine;
	}
	
	public static String getImageReference(int layer){
		if (layer == Layer.BOTTOMLAYER){
			return "data/MetsaMaa.png";
		}
		
		if (layer == Layer.GROUND){
			return "data/KurpitsaMaa.png";
		}
		
		if (layer == Layer.GRASS){
			return "data/Nurmikko.png";
		}
		else {
			return "data/Pilvi.png";
		}
	}
	
	public float getFreeSpaceX(int layer){
		if (layer == Layer.BOTTOMLAYER) return 0f;
		if (layer == Layer.GROUND) return 70f;
		if (layer == Layer.GRASS) return 90f;
		//vain pilvi on jäljellä
		else return 60f;
	}
	
	public float getFreeSpaceY(int layer){
		if (layer == Layer.BOTTOMLAYER) return 380f;
		if (layer == Layer.GROUND) return 70f;
		if (layer == Layer.GRASS) return 120f;
		//vain pilvi on jäljellä
		else return 10f;
	}
	
	public int getLayerType(){
		return this.layerType;
	}
	
}
