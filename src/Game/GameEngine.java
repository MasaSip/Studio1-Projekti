package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	 
	
	/**
	 * Tietaa mika osa pelista tulee nayttaa ruudulle. Polygonin sijainti
	 * absoluuttisen koordinaatiston arvoja. Pelin edetessa viewWindow liikkuu
	 * ylemmas eli sen y-koordinaatit pienenevat. Koska viewWindow liikkuu 
	 * ylospain, nayttaa kuin kaikki valuisi itsestaan alaspain.
	 */
	
	private View view;
	private Avatar avatar;
	private GameObject bottomLayer;
	private List <GameObject> layers;
	private float lastLayer;
	private Physics physics;
	private Random rnd;
	
	/**
	 * kuinka korkealla nakyma on lahtotasosta
	 */
	private float distanceFromGround;
	
	private float distanceBetweenLayers = 150;
	
	public GameEngine() throws SlickException {
		this.rnd = new Random();
		this.layers = new ArrayList();
		GameObject bottomLayer = new GameObject("data/BottomLayer.png");
		this.bottomLayer = bottomLayer;
		this.layers.add(bottomLayer);
		
		
		this.avatar = new Avatar();
		this.physics = new Physics(this.avatar);
		this.view = new View();
	}
	
	public void putBottomLayerIntoGame(){
		this.bottomLayer.setLocationOnBottom();
		this.avatar.move(new Vector2f(0,-this.bottomLayer.getHeight()));
		
		
	}
	
	public void putAvatarIntoGame() throws SlickException{
		
	
		float Xabs = (Game.WIDTH - avatar.getWidth())/2;
		float Yabs = Game.HEIGHT - avatar.getHeight();
		//Yabs +=
	
		this.avatar.setLocation(new Vector2f(Xabs, Yabs));
		this.avatar.setOnGround(true);
		
	}
	
	/**
	 * @param direction Luokassa Input m��ritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisest�
	 * liikumisesta
	 */
	public void moveAvatar(Input input, int delta){
		
		//xxx kannattaa muuttaa physicsia k�ytt�v�ksi
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
			this.avatar.move(new Vector2f(amount, 0f));
		}
		
		//t�h�n asti
		
		if (input.isKeyPressed(Input.KEY_SPACE) && this.avatar.isOnGround()){
			
			this.physics.jump();
		}
		
		this.physics.moveAvatar();
		
		
	}
	
	public void drawGameObjects(){
		for (GameObject o : this.layers){
			this.view.draw(o);
		}
		this.view.draw(this.avatar);
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
				topLayer.getTopY() - this.view.getMinY();
	
		if (spaceAboveTopLayer > this.distanceBetweenLayers){
	
			GameObject newLayer = new GameObject("data/Layer.png");
			newLayer.loadImage();
			
			//x ja y absoluuttisessa koordinaatistossa
			
			float Xabs = rnd.nextFloat()*Game.WIDTH - newLayer.getWidth()/2;
			//float Xabs = this.view.getCenterX() - newLayer.getWidth()/2;
			float Yabs = topLayer.getTopY() 
					- this.distanceBetweenLayers
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
	
	public void scrollView(int delta){
		this.view.scroll(delta);
	}
	

}
