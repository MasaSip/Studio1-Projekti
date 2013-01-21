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
 *  
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
	
	
	private final float DISTANCEBETWEENLAYERS = 230;
	/**
	 * score = paras korkeus mihin avatar on paassyt
	 */
	private int score;
	
	public GameEngine() throws SlickException {
		this.deltas = new ArrayList<Integer>();
		this.rnd = new Random();
		this.layers = new ArrayList<Layer>();
		Layer bottomLayer = new Layer(Layer.BOTTOMLAYER);
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
	
	public int convertToScore(float height){
		int score = (int) height/10;
		return score;
	}
	
	/**
	 * Alin laatta asetetaan keskelle pohjalle.
	 */
	public void putBottomLayerIntoGame(){
		this.bottomLayer.setLocationOnBottom();
	}
	
	
	/**
	 * Avatar asetetaan alimman laatan paalle.
	 */
	public void putAvatarAboveBottomLayer(){
		float y = this.bottomLayer.getCollisionLine().getMinY();
		this.avatar.getLocationAbs().y = y - this.avatar.getHeight();
	}
	
	/**
	 * 
	 * @param scrollingOn true, jos scrollataan, false jos ei
	 */
	public void setScrollingOn(boolean scrollingOn){
		this.scrollingOn = scrollingOn;
	}
	
	
	/**
	 * Avatar asetetaan alimman tason paalle
	 * @throws SlickException
	 */
	public void putAvatarIntoGame() throws SlickException{

		this.avatar.setLocationOnBottom();
		this.avatar.setOnGround(true);
		
	}
	
	
	/**
	 * Kaikki inputin kasittely tapahtuu taalla. Lahettaa komentoja eteenpain
	 * @param input
	 * @param delta viive
	 */
	public void update(Input input, int delta){
		MovingStatus newStatus = MovingStatus.STATIC;
		boolean left = input.isKeyDown(Input.KEY_LEFT);
		boolean right = input.isKeyDown(Input.KEY_RIGHT);
		boolean jump = input.isKeyDown(Input.KEY_SPACE) || 
				input.isKeyDown(Input.KEY_UP);
		boolean onGround = this.avatar.isOnGround();


		if (jump && onGround){
			this.scrollingOn = true;
			this.physics.jump();
		}

		if (input.isKeyPressed(Input.KEY_E)){
			this.extraInfo = !this.extraInfo;
		}

		if (left && !right){
			newStatus = MovingStatus.LEFT;
		}
		if (!left && right){
			newStatus = MovingStatus.RIGHT;
		}



		this.moveAvatar(newStatus, delta);


	}
	/**
	 * kopeloi avatarin MovinStatusta ja jumpinBonusta. Valittaa hyppykasky
	 * physicsille
	 * @param newStatus
	 * @param delta
	 */
	public void moveAvatar(MovingStatus newStatus, int delta){

		boolean leansToWall = this.avatar.getLeansToWall();
		float amount = this.avatar.getSpeed()*delta;
		MovingStatus oldStatus = this.avatar.getMovingStatus();
		//jos ei liiku, taman verran otetaan movingStatusta pois
		float decreaseAmount = 5f*delta;

		this.physics.moveAvatar();

		if (leansToWall){
			this.avatar.decreaseBonusConstant(decreaseAmount);
		}

		if (newStatus.equals(MovingStatus.STATIC)){
			this.avatar.decreaseBonusConstant(decreaseAmount);
			return;
		}

		this.avatar.increaseJumpingBonus(delta);

		if (newStatus.equals(MovingStatus.LEFT)){
			amount = -amount;
		}

		if (oldStatus.isOppositeTo(newStatus)){
			this.avatar.changeDirection(newStatus);	
		}
		this.avatar.setMovingStatus(newStatus);

		this.avatar.move(new Vector2f(amount, 0f));

	}

		
		
	/**
	 * Kaskee View:ia piirtamaan tarpeelliset asiat naytolle
	 * @param g
	 */
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
	
	/**
	 * Ladataan kuvat.
	 * @throws SlickException
	 */
	public void loadImages() throws SlickException{
		this.avatar.loadImage();
		this.bottomLayer.loadImage();
		for (Layer o : this.layers){
			o.loadImage();
		}
	}
	
	/**
	 * Luo lisaa laattoja kunnes ruudulle ei enaa mahdu lisaa. Seuraavan 
	 * laattan tormaysviiva on DISTANCEBETWEENLAYERS verran edellisen 
	 * ylapuolella
	 * @throws SlickException
	 */
	public void generateLayers() throws SlickException{
		int layersInGame = this.layers.size();
		
		Layer topLayer = this.layers.get(layersInGame -1);
		float spaceAboveTopLayer = 
				topLayer.getTopY() - this.view.getMinY();
	
		if (spaceAboveTopLayer > 0f){
			
			int layerType = this.chooseLayerType();
			Layer newLayer = new Layer(layerType);
			newLayer.loadImage();
			
			//x ja y absoluuttisessa koordinaatistossa
			float Xabs = rnd.nextFloat()*Game.WIDTH - newLayer.getWidth()/2;
			
			float Yabs = topLayer.getCollisionLine().getMinY() 
					- this.DISTANCEBETWEENLAYERS
					- newLayer.getFreeSpaceY(newLayer.getLayerType());
			
			Vector2f locationAbs = new Vector2f(Xabs, Yabs);
			newLayer.setLocation(locationAbs);
			this.layers.add(newLayer);
			
			this.generateLayers();
		}
	}
	
	/**
	 * 
	 * @return Tason tyyppiä vastaavan julkisen vakion. Riippuu sitä, kuinka 
	 * suuri on Avatarin paras korkeus
	 */
	protected int chooseLayerType(){
		float height = this.avatar.getBestHeight();
		if (height < 1000) return Layer.GROUND;
		if (height < 8000) return Layer.GRASS;
		else return Layer.CLOUD;
	}
	
	/**
	 * Kerto Physics -oliolle päivitysviiveen ja pelimaailmassa olevat tasot.
	 * @param delta viive edellisestä päivityksestä millisekuntteina
	 */
	public void updatePhysics(int delta){
		this.physics.update(this.layers, delta);
	}
	
	
	/**
	 * Paivittaa Avatarin parhaan korkeuden, jos tehtiin ennatys. Laskee 
	 * GameEngine :lle scoren Avatarin parhaankorkeuden perusteella.
	 */
	public void updateScore(){
		float currentHeight = this.getCurrentHeight();
		
		if (currentHeight > this.avatar.getBestHeight()){
			this.avatar.setBestHeight(currentHeight);
			this.score = this.convertToScore(this.avatar.getBestHeight());	
		}
	}


	/**
	 * 
	 * @return kuinka monta pikseli Avatar on kivunnut lahtotasosta
	 */
	public float getCurrentHeight(){
		float zeroHeight = 
				this.bottomLayer.getCollisionLine().getMinY()
				- this.avatar.getHeight();
				
	
		float absHeight = this.avatar.getTopY();
		float currentHeight = zeroHeight - absHeight;
		return currentHeight;
		
		
	}
	
	/**
	 * Paivitaan viimeaikaisten viiveiden keskiarvo
	 * @param delta viive
	 */
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
	
	/**
	 * Nakymaa scrollataan jatkuvasti, jos peli on alkanut. Lisä scrollaus 
	 * suoritetaan, jos Avatar on lahella ruudun ylalaitaa
	 * @param delta viive
	 */
	public void scrollView(int delta){
		if (this.scrollingOn){
			this.view.scroll(delta, true);
		}
		
		float topYOnScreen = this.view.getMinY();
		float limit = this.view.getAutoScrollLimit();
		GameObject o = this.avatar;
		
		
		while (this.objectIsHigherThan(topYOnScreen, limit, o)){
			
			this.view.scroll(delta, false);
			
			// if -else rakenteen luvut ovat mielivaltaisesti valittuja.
			if (limit < 10){
				limit += 20;
			}
			else {				
				limit = limit*1.2f;
			}
		}
		
	}
	
	/**
	 * tarkistetaan onko peli havitty
	 */
	public boolean gameOver(){
		
		//jos avatar on hävinnyt kokonaan ruudun alalaitaan
		if (this.avatar.getLocationAbs().getY() > this.view.getMaxY()){
			return true;
		}
		return false;
	}
	
	/**
	 * Ladataan kuvat ja fontit.
	 * @throws SlickException
	 */
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
		
		float percentAtm = (y - objectY)/oHeight*100;
		if (percent <= percentAtm){
			return true;
		}
		return false;
	}
	
	

}
