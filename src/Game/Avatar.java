package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

public class Avatar extends GameObject {
	/**
	 * Mihin suuntaan Avatar liikkuu
	 */
	
	
	/**
	 * true jos Avatar on laatan paalla ja voi hypata
	 */
	private boolean onGround;
	/**
	 * score = paras korkeus mihin avatar on paassyt
	 */
	private int score;
	
	public Avatar() throws SlickException {
		super("data/Hamis.png");
		super.setSpeed(0.8f);
		this.score = 0;
		this.onGround = false;
		
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void setBestHeight(int height){
		this.score = height;
	}
	
	public boolean isOnGround(){
		return this.onGround;
	}
	public void setOnGround(boolean onGround){
		this.onGround = onGround;
	}
	
	
	
	public void jump(){
		
	}
}
