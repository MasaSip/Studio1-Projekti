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
	private float bestHeight;
	
	
	public Avatar() throws SlickException {
		super("data/Hamis.png");
		super.setSpeed(0.8f);
		this.score = 0;
		this.bestHeight = 0;
		this.onGround = false;
		
	}
	public float getBestHeight(){
		return this.bestHeight;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void setBestHeight(float height){
		this.bestHeight = height;
	}
	
	public boolean isOnGround(){
		return this.onGround;
	}
	public void setOnGround(boolean onGround){
		this.onGround = onGround;
	}
	
	public void updateScore(float currentHeight){
		if (currentHeight > this.getBestHeight()){
			this.setBestHeight(currentHeight);
			this.convertToScore(this.getBestHeight());
			
		}
	}
	
	public void convertToScore(float height){
		int score = (int) height/10;
		this.setScore(score);
	}
	
	public void jump(){
		
	}
}
