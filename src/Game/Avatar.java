package Game;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

public class Avatar extends GameObject {
	/**
	 * Mihin suuntaan Avatar liikkuu
	 */
	//xxx t��t� ei k�ytet� viel� miss��n
	private Vector2f direction;
	
	public Avatar() throws SlickException {
		super("data/A.png");
		super.setSpeed(1f);
		this.direction = new Vector2f();
	}
	
	/**
	 * 
	 * @param direction Luokassa Input m��ritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisest�
	 * liikumisesta
	 */
	public void move(int direction, int delta){
		float amount = super.getSpeed()*delta;
		if (direction == Input.KEY_LEFT){
			amount = -amount;
		}
		
		super.move(new Point(amount, 0f));
		
	}
	
	public void jump(){
		
	}
}
