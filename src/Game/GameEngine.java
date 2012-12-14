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
	 * absoluuttisen koordinaatiston arvoja. 
	 */
	private Polygon viewWindow;
	private Avatar avatar;
	private List <GameObject> layers;
	private float lastLayer;
	
	/**
	 * kuinka korkealla nakyma on lahtotasosta
	 */
	private float distanceFromGround;
	
	private float distanceBetweenLayers = 150;
	
	public GameEngine() throws SlickException {
		this.layers = new ArrayList();
		GameObject bottomLayer = new GameObject("data/BottomLayer.png");
		this.layers.add(bottomLayer);
		
		
		float[] figure = 
			{0f,0f, Game.WIDTH,0f, Game.WIDTH,Game.HEIGHT, 0f,Game.HEIGHT};
		this.viewWindow = new Polygon(figure);
		this.avatar = new Avatar();
	}
	
	public void putBottomLayerIntoGame(){
		for (GameObject o : this.layers){
			o.setLocation(Location.BOTTOM_CENTER);
			
		}
		
	}
	
	public void putAvatarIntoGame() throws SlickException{
		this.avatar.setLocation(Location.BOTTOM_CENTER);
	}
	
	/**
	 * @param direction Luokassa Input mŠŠritelty julkinen vakio
	 * @param delta kuinka paljon millisekuntteja on kulunut edellisestŠ
	 * liikumisesta
	 */
	public void moveAvatar(int direction, int delta){
		this.avatar.move(direction, delta);
		
		
	}
	
	public void drawGameObjects(){
		for (GameObject o : this.layers){
			o.draw(this.viewWindow);
		}
		this.avatar.draw(this.viewWindow);
	}
	
	public void loadImages() throws SlickException{
		this.avatar.loadImage();
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
	

}
