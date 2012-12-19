package Game;

import java.util.List;
import java.util.ArrayList;


import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Polygon;
/**
 * Absoluuttisen koordinaatiston origo on vasen ylakulma aloitusnaytolta.
 * Absoluuttinen koordinaatisto kasvaa alasoikealle
 * @author 
 *
 */
public class GameEngine {
	 
	
	/**
	 * Tietaa mika osa pelista tulee nayttaa ruudulle. Polygonin sijainti
	 * absoluuttisen koordinaatiston arvoja. Pelin edetessa viewWindow liikkuu
	 * ylemmas eli sen y-koordinaatit pienenevat. Koska viewWindow liikkuu 
	 * ylospain, nayttaa kuin kaikki valuisi itsestaan alaspain.
	 */
	private Polygon viewWindow;
	private Avatar avatar;
	private GameObject bottomLayer;
	private List <GameObject> layers;
	private float lastLayer;
	private Physics physics;
	
	/**
	 * kuinka korkealla nakyma on lahtotasosta
	 */
	private float distanceFromGround;
	
	private float distanceBetweenLayers = 150;
	
	public GameEngine() throws SlickException {
		this.layers = new ArrayList();
		GameObject bottomLayer = new GameObject("data/BottomLayer.png");
		this.bottomLayer = bottomLayer;
		this.layers.add(bottomLayer);
		
		float[] figure = 
			{0f,0f, Game.WIDTH,0f, Game.WIDTH,Game.HEIGHT, 0f,Game.HEIGHT};
		this.viewWindow = new Polygon(figure);
		this.avatar = new Avatar();
		physics = new Physics(this.avatar);
	}
	
	public void putBottomLayerIntoGame(){
		this.bottomLayer.setLocationOnBottom();
		this.avatar.move(new Point(0,-this.bottomLayer.getHeight()));
		
		
	}
	
	public void putAvatarIntoGame() throws SlickException{
		
	
		float Xabs = (Game.WIDTH - avatar.getWidth())/2;
		float Yabs = Game.HEIGHT - avatar.getHeight();
		//Yabs +=
	
		this.avatar.setLocation(new Point(Xabs, Yabs));
		this.avatar.setOnGround(true);
		
	}
	
	/**
	 * @param direction Luokassa Input mŠŠritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisestŠ
	 * liikumisesta
	 */
	public void moveAvatar(Input input, int delta){
		
		//xxx kannattaa muuttaa physicsia kŠyttŠvŠksi
		float amount = this.avatar.getSpeed()*delta;
		boolean move = false;
		if (input.isKeyDown(Input.KEY_LEFT)){
			amount = -amount;
			move = true;
		}
		
		if (input.isKeyDown(Input.KEY_RIGHT)){
			move = true;
		}
		if (move){			
			this.avatar.move(new Point(amount, 0f));
		}
		
		//tŠhŠn asti
		
		if (input.isKeyPressed(Input.KEY_SPACE) && this.avatar.isOnGround()){
			
			this.physics.jump();
		}
		
		this.physics.moveAvatar();
		
		
	}
	
	public void drawGameObjects(){
		for (GameObject o : this.layers){
			o.draw(this.viewWindow);
		}
		this.avatar.draw(this.viewWindow);
	}
	
	public void loadImages() throws SlickException{
		this.avatar.loadImage();
		this.bottomLayer.loadImage();
		for (GameObject o : this.layers){
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
		
		GameObject topLayer = this.layers.get(layersInGame -1);
		float spaceAboveTopLayer = 
				topLayer.getMinYabs() - this.viewWindow.getMinY();
	
		if (spaceAboveTopLayer > this.distanceBetweenLayers){
	
			GameObject newLayer = new GameObject("data/Layer.png");
			newLayer.loadImage();
			
			//x ja y absoluuttisessa koordinaatistossa
			float Xabs = this.viewWindow.getCenterX() - newLayer.getWidth()/2;
			float Yabs = topLayer.getMinYabs() 
					- this.distanceBetweenLayers
					- newLayer.getHeight();
			
			Point locationAbs = new Point(Xabs, Yabs);
			newLayer.setLocation(locationAbs);
			this.layers.add(newLayer);
			
			this.generateLayers();
		}
	}
	
	public void updatePhysics(int delta){
		this.physics.update(this.layers, delta);
	}
	

}
