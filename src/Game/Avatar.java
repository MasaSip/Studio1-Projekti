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
	/*
	 * taian sittenkin laittaa naa Physicsiin
	private Vector2f velocity;
	private Vector2f acceleration;
	*/
	/**
	 * true jos Avatar on laatan paalla ja voi hypata
	 */
	private boolean onGround;
	/**
	 * Taman perusteella maaritetaan pistemaara, score
	 */
	private int bestHeight;
	
	public Avatar() throws SlickException {
		super("data/Hamis.png");
		super.setSpeed(0.8f);
		this.bestHeight = 0;
		this.onGround = false;
		
		//xxx this.velocity = new Vector2f();
		//xxx this.acceleration = new Vector2f(0, Physics.gravity);
	}
	
	public int getBestHeight(){
		return this.bestHeight;
	}
	
	public void setBestHeight(int height){
		this.bestHeight = height;
	}
	
	public boolean isOnGround(){
		return this.onGround;
	}
	public void setOnGround(boolean onGround){
		this.onGround = onGround;
	}
	
	/*
	 * Physicsiin
	public Vector2f getVelocity(){
		return this.velocity;
	}
	
	public Vector2f getAcceleration(){
		return this.acceleration;
	}
	*/
	
	
	public void jump(){
		
	}
}
