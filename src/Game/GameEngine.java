package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
/**
 * Absoluuttisen koordinaatiston origo on vasen ylakulma aloitusnaytolta.
 * Absoluuttinen koordinaatisto kasvaa alasoikealle
 * @author 
 *
 */
public class GameEngine {
	 
	

	
	private View view;
	private Avatar avatar;
	private Layer bottomLayer;
	private List <Layer> layers;
	/**
	 * onko pelaaja painanut yhtaan nappia. Jos on, maailman scrollaus voidaan 
	 * aloittaaa
	 */
	private boolean scrollingOn;
	private Physics physics;
	private Random rnd;
	private Boolean extraInfo;
	
	private GamePlayState game;
	
	private final float DISTANCEBETWEENLAYERS = 100;
	/**
	 * score = paras korkeus mihin avatar on paassyt
	 */
	private int score;
	
	public GameEngine(GamePlayState game) throws SlickException {
		this.game = game;
		this.rnd = new Random();
		this.layers = new ArrayList<Layer>();
		Layer bottomLayer = new Layer("data/BottomLayer.png");
		this.bottomLayer = bottomLayer;
		this.layers.add(bottomLayer);
		
		
		this.avatar = new Avatar();
		this.physics = new Physics(this.avatar);
		this.view = new View();
		this.scrollingOn = false;
		this.extraInfo = true;
		this.score = 0;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public void convertToScore(float height){
		int score = (int) height/10;
		this.setScore(score);
	}
	
	public void updateScore(float currentHeight){
		if (currentHeight > this.avatar.getBestHeight()){
			this.avatar.setBestHeight(currentHeight);
			this.convertToScore(this.avatar.getBestHeight());
			
		}
	}
	
	public void putBottomLayerIntoGame(){
		this.bottomLayer.setLocationOnBottom();
		this.avatar.move(new Vector2f(0,-this.bottomLayer.getHeight()));
		
		
	}
	
	/**
	 * 
	 * @param scrollingOn true, jos scrollataan, false jos ei
	 */
	public void setScrollingOn(boolean scrollingOn){
		this.scrollingOn = scrollingOn;
	}
	
	public void putAvatarIntoGame() throws SlickException{
		
	
		float Xabs = (Game.WIDTH - avatar.getWidth())/2;
		float Yabs = Game.HEIGHT - avatar.getHeight();
		//Yabs +=
	
		this.avatar.setLocation(new Vector2f(Xabs, Yabs));
		this.avatar.setOnGround(true);
		
	}
	
	/**
	 * @param direction Luokassa Input mŠŠritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisestŠ
	 * liikumisesta
	 */
	public void moveAvatar(Input input, int delta){
		MovingStatus state = this.avatar.getMovingStatus();
		boolean left = input.isKeyDown(Input.KEY_LEFT);
		boolean right = input.isKeyDown(Input.KEY_RIGHT);
		//XXX: kannattaa muuttaa physicsia kŠyttŠvŠksi
		
		//mita korkeammalle avatar hyppaisi, sita kovemmin se kipittaa
		float amount = this.avatar.getSpeed()*delta;
		boolean move = false;
		if (left){
			amount = -amount;
			move = true;
		}
		
		if (right){	
			move = true;
		}
		//painetaan pelkastaan vasenta
		if (left && !right){
			if (state.equals(MovingStatus.RIGHT)){// && this.avatar.isOnGround()){
				this.avatar.decreaseJumpingBonus(70);
			}
			this.avatar.setMovingStatus(MovingStatus.LEFT);
		}
		//painetaan pelkastaan oikeaa
		if (!left && right){
			if (state.equals(MovingStatus.LEFT)){// && this.avatar.isOnGround()){
				this.avatar.decreaseJumpingBonus(70);
			}
			
			this.avatar.setMovingStatus(MovingStatus.RIGHT);
		}
		
		if (move){			
			
			this.avatar.move(new Vector2f(amount, 0f));
			this.scrollingOn = true;
			if (this.avatar.isOnGround()){				
				this.avatar.increaseJumpingBonus();
			}
		}
		else {
			this.avatar.decreaseConstantValue(1);
		}
		
		//tŠhŠn asti
		
		if (input.isKeyDown(Input.KEY_SPACE) && this.avatar.isOnGround()){
			this.scrollingOn = true;
			this.physics.jump();
		}
		
		this.physics.moveAvatar();
		
		
	}
	
	public void drawGame(Graphics g){
		for (Layer o : this.layers){
			this.view.draw(o);
		}
		this.view.draw(this.avatar);
		int score = this.getScore();
		this.view.drawScore(score, g);
		
		if (this.extraInfo){
			this.view.drawExtraInformation(this.avatar);
		}
		
		this.view.drawBackground(g);
	}
	
	public void loadImages() throws SlickException{
		this.avatar.loadImage();
		this.bottomLayer.loadImage();
		for (Layer o : this.layers){
			o.loadImage();
		}
	}
	
	/**
	 * luo lisaa laattoja kunnes ruudulle ei enaa mahdu niita. Seuraava laatta
	 * on distanceBetweenLayers attribuutin maaraaman arvon verran edellisen
	 * laatan ylapuolella
	 * @throws SlickException
	 */
	public void generateLayers() throws SlickException{
		int layersInGame = this.layers.size();
		
		Layer topLayer = this.layers.get(layersInGame -1);
		float spaceAboveTopLayer = 
				topLayer.getTopY() - this.view.getMinY();
	
		if (spaceAboveTopLayer > this.DISTANCEBETWEENLAYERS){
	
			Layer newLayer = new Layer("data/Pilvi.png");
			newLayer.loadImage();
			
			//x ja y absoluuttisessa koordinaatistossa
			
			float Xabs = rnd.nextFloat()*Game.WIDTH - newLayer.getWidth()/2;
			//float Xabs = this.view.getCenterX() - newLayer.getWidth()/2;
			float Yabs = topLayer.getTopY() 
					- this.DISTANCEBETWEENLAYERS
					- newLayer.getHeight();
			
			Vector2f locationAbs = new Vector2f(Xabs, Yabs);
			newLayer.setLocation(locationAbs);
			this.layers.add(newLayer);
			
			this.generateLayers();
		}
	}
	
	public void updatePhysics(int delta){
		this.physics.update(this.layers, delta);
	}
	
	public void updateScore(){
		float zeroHeight = 
				Game.HEIGHT 
				- this.avatar.getHeight() - this.bottomLayer.getHeight();
	
		float absHeight = this.avatar.getTopY();
		float currentHeight = zeroHeight - absHeight;
		this.updateScore(currentHeight);
		
		
	}
	
	public void scrollView(int delta){
		if (this.scrollingOn){
			this.view.scroll(delta, true);
		}
		
		float topYOnScreen = this.view.getMinY();
		float limit = this.view.getAutoScrollLimit();
		GameObject o = this.avatar;
		if (this.objectIsHigherThan(topYOnScreen, limit, o)){
			this.view.scroll(delta, false);
		}
		
	}
	
	/**
	 * tarkistetaan onko peli havitty
	 */
	public boolean gameOver(){
		
		//jos avatar on hŠvinnyt kokonaan ruudun alalaitaan
		if (this.avatar.getLocationAbs().getY() > this.view.getMaxY()){
			return true;
		}
		return false;
	}
	
	public void initView() throws SlickException{
		this.view.initFont();
		
	}
	/**
	 * 
	 * @param y -koordinaatti
	 * @param percent kuinka monta % tulisi olla y-koordinaatin ylapuolella
	 * @param o esim. Avatar
	 * @return
	 */
	public boolean objectIsHigherThan(float y, float percent, GameObject o){
		float oHeight = o.getHeight();
		float objectY = o.getTopY();
		
		//o on kokonaan rajan alapuolella
		if (objectY > y){
			return false;
		}
		
		float percentAtm = (y - objectY)/oHeight*100;
		if (percent <= percentAtm){
			return true;
		}
		return false;
	}

}
