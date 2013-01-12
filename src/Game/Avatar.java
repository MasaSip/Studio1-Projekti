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
	private float bestHeight;
	
	private float basicSpeed;
	
	/**
	 * hypyn kokonaisvoima = basicJump + jumpingBonus
	 */
	private float basicJump;
	
	/**
	 * hypyn kokonaisvoima = basicJump + jumpingBonus
	 */
	private float jumpingBonus;
	private float maxBonus;
	private MovingStatus movingStatus;
	
	public Avatar() throws SlickException {
		super("data/Hamis.png");
		this.setBasicSpeed(0.5f);
		this.bestHeight = 0;
		this.onGround = false;
		this.basicJump = 800f;
		this.jumpingBonus = 0f;
		this.maxBonus = 700f;
		this.movingStatus = MovingStatus.STATIC;
		
	}
	
	public MovingStatus getMovingStatus(){
		return this.movingStatus;
	}
	
	public void setMovingStatus(MovingStatus status){
		this.movingStatus = status;
	}
	
	
	public void setJumpingBonus(float power){
		if (power < 0){
			power = 0;
		}
		this.jumpingBonus = power;
	}
	
	/**
	 * 
	 * @param percent kuinka monta prosenttia otetaan pois
	 */
	public void decreaseJumpingBonus(float percent){
		float bonus = this.getJumpingBonus();
		if (percent > 100){
			percent = 100.0f;
		}
		
		if (percent < 0.0f){
			percent = 0.0f;
		}
		float decreasedBonus = (100.0f - percent)/100*bonus;
		this.setJumpingBonus(decreasedBonus);
		
	}
	
	/**
	 * vahennestaan jumpingBonusta vakiomaara
	 * @param constant
	 */
	public void decreaseConstantValue(float constant){
		float bonus = this.getJumpingBonus();
		bonus -= constant;
		this.setJumpingBonus(bonus);
	}
	
	public float getJumpingBonus(){
		return this.jumpingBonus;
	}
	
	public float getJumpingPower(){
		return this.jumpingBonus + this.basicJump;
	}
	
	public void setBasicSpeed(float s){
		this.basicSpeed = s;
	}
	
	/**
	 * 
	 * @return kokonaisnopeus koostuu perus nopeudesta ja JumpingBonuksesta
	 */
	public float getSpeed(){
		return this.basicSpeed + this.getJumpingBonus()/1000;
	}
	
	public void increaseJumpingBonus(){
		this.jumpingBonus += 3;
		if (this.jumpingBonus > this.maxBonus/3){
			this.jumpingBonus +=2;
		}
		if (this.getJumpingBonus() > this.maxBonus){
			this.jumpingBonus = this.maxBonus;
		}
	}
	
	public float getBestHeight(){
		return this.bestHeight;
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
	
	
	
	public void jump(){
		
	}
}
