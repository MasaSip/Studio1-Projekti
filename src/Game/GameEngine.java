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
	 
	

	private List<Integer> deltas;
	private float averageDelta;
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
	/**
	 * jos true, naytolle piirretaan ylimaaraisia tietoja kuten scrollaus 
	 * nopeus
	 */
	private Boolean extraInfo;
	
	private GamePlayState game;
	
	private final float DISTANCEBETWEENLAYERS = 100;
	/**
	 * score = paras korkeus mihin avatar on paassyt
	 */
	private int score;
	
	public GameEngine(GamePlayState game) throws SlickException {
		this.deltas = new ArrayList();
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
		this.extraInfo = false;
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
	 * Hoitaa GameEnginessa kaiken mika liittyy avatarin liikutamiseen.
	 * @param direction Luokassa Input mŠŠritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisestŠ
	 * liikumisesta
	 */
	public void moveAvatar(Input input, int delta){
		MovingStatus state = this.avatar.getMovingStatus();
		boolean left = input.isKeyDown(Input.KEY_LEFT);
		boolean right = input.isKeyDown(Input.KEY_RIGHT);
		boolean jump = input.isKeyDown(Input.KEY_SPACE) || 
			input.isKeyDown(Input.KEY_UP);
		
		
		//mita korkeammalle avatar hyppaisi, sita kovemmin se kipittaa
		float amount = this.avatar.getSpeed()*delta;
		boolean move = false;
		
		//oletuksena ollaan paikallaan. Jos nuolta painetaan, on syytŠ liikua.
		MovingStatus newDirection = MovingStatus.STATIC;
		
		//painetaan pelkastaan vasenta
		if (left && !right){
			amount = -amount;
			move = true;
			newDirection = MovingStatus.LEFT;
			if (state.equals(MovingStatus.RIGHT)){// && this.avatar.isOnGround()){
				this.avatar.changeDirection(newDirection);
				
			}
		}
		//painetaan pelkastaan oikeaa
		if (!left && right){
			move = true;
			newDirection = MovingStatus.RIGHT;
			if (state.equals(MovingStatus.LEFT)){// && this.avatar.isOnGround()){
				this.avatar.changeDirection(newDirection);		
				}
			
			//this.avatar.setMovingStatus(MovingStatus.RIGHT);//XXX
		}
		
		if (move){			
			
			this.avatar.move(new Vector2f(amount, 0f));
			
			//Maailman scrollaus voi alkaa vasta kun Avataria on liikutettu
			this.scrollingOn = true;
			//XXX onko hyva etta bonusta saa myšs ilmassa liikkumisesta?
			//if (this.avatar.isOnGround()){				
			//}
			
			this.avatar.increaseJumpingBonus(delta);
			
			this.avatar.setMovingStatus(newDirection); //XXX
		}

		if (!move || this.avatar.getLeansToWall()){
			this.avatar.decreaseBonusConstant(5*delta);
		}
		
		
		
		if (jump && this.avatar.isOnGround()){
			this.scrollingOn = true;
			this.physics.jump();
		}
		
		this.physics.moveAvatar();
		
		
	}
	
	public void drawGame(Graphics g){
		for (Layer o : this.layers){
			this.view.draw(o, g ,this.extraInfo);
			
		}
		this.view.draw(this.avatar ,g ,this.extraInfo);
		int score = this.getScore();
		this.view.drawScore(score, g);
		
		
		
		this.view.drawBonusBar(this.avatar, g);
		this.view.drawInformation(this.avatar, this.averageDelta, this.extraInfo);
	
		
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
	
	/**
	 * paivitetaan GameEnginen tietoja esim. naytetaanko ylimaaraisia tietoja
	 * vai ei
	 * @param input
	 * @param delta
	 */
	public void update(Input input, int delta){
		this.updateAverageDelta(delta);
		if (input.isKeyPressed(Input.KEY_E)){
			this.extraInfo = !this.extraInfo;
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
	
	public void updateAverageDelta(int delta){
		this.deltas.add(delta);
		int size = deltas.size();
		if (size > 1000){
			this.deltas.remove(0);
		}
		int sum = 0;
		for (int i : this.deltas){
			sum += i;
		}
		
		this.averageDelta = sum/(float)size;
	}
	
	public void scrollView(int delta){
		if (this.scrollingOn){
			this.view.scroll(delta, true);
		}
		
		float topYOnScreen = this.view.getMinY();
		float limit = this.view.getAutoScrollLimit();
		GameObject o = this.avatar;
		
		
		while (this.objectIsHigherThan(topYOnScreen, limit, o)){
			
			this.view.scroll(delta, false);
			limit = limit*1.2f;
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
		this.view.loadImages();
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
