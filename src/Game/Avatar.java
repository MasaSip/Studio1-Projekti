package game;

import org.newdawn.slick.SlickException;

public class Avatar extends GameObject {
	
	
	/**
	 * true jos Avatar on laatan paalla ja voi hypata
	 */
	private boolean onGround;
	
	/**
	 * Paras korkeus, jonka Avatar on saavuttanut
	 */
	private float bestHeight;
	
	/**
	 * Nopeus vaakasuunnassa. Tämän päälle lisätään mahdollinen jumpingBonus 
	 */
	private final float basicSpeed = 0.5f;
	
	/**
	 * Hypyn nopeusvektori = basicJump + jumpingBonus
	 */
	private final float basicJump = 850f;
	
	/**
	 * Hypyn kokonaisvoima = basicJump + jumpingBonus
	 */
	private float jumpingBonus;
	
	/**
	 * Tätä suurempaa arvoa ei jumpingBonus voi saada
	 */
	private final float maxBonus = 700f;
	
	/**
	 * Mikä on avatarin vaakaliikkeen tila tällähetkellä.
	 */
	private MovingStatus movingStatus;
	
	/**
	 * jumpingBonusta ei voida kasvattaa esim. jos maailmanraja vasemmalla tai 
	 * oikealla estää liikkumisen. Tälläin leansToWall = true
	 */
	private boolean leansToWall;
	
	public Avatar() throws SlickException {
		super("data/Hamis.png");
		//XXX this.setBasicSpeed(0.5f);
		this.bestHeight = 0;
		this.onGround = false;
		//XXX this.basicJump = 850f;
		this.jumpingBonus = 0f;
		//XXX this.maxBonus = 700f;
		this.movingStatus = MovingStatus.STATIC;
		this.leansToWall = false;
		
	}
	
	public void setLeansToWall(boolean on){
		this.leansToWall = on;
	}
	
	
	public boolean getLeansToWall(){
		return this.leansToWall;
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
	 * @param percent kuinka monta prosenttia otetaan pois jumpingBonuksesta
	 */
	public void decreaseBonusPercent(float percent){
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
	public void decreaseBonusConstant(float constant){
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
	/*
	 * XXX
	public void setBasicSpeed(float s){
		this.basicSpeed = s;
	}
	 */
	
	/**
	 * 
	 * @return kokonaisnopeus koostuu perus nopeudesta ja JumpingBonuksesta
	 */
	public float getSpeed(){
		return this.basicSpeed + 1.2f*this.getJumpingBonus()/1000;
	}
	
	public void increaseJumpingBonus(int delta){
		if (this.leansToWall){
			return;
		}
		
		float tuningConstant = 4.6f;
		
		this.jumpingBonus += 3*delta/tuningConstant;
		
		if (this.jumpingBonus > this.maxBonus/3){
			this.jumpingBonus +=1*delta/tuningConstant;
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
	
	public void changeDirection(MovingStatus newStatus){
		this.decreaseBonusPercent(70);
		this.decreaseBonusConstant(this.maxBonus/8);
		this.setMovingStatus(newStatus);
	}
	
	
}
