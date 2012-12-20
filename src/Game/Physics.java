package Game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * Liikuttaa kappaleita painovoiman mukaisesti. Pitaa huolen esim. Avatarin
 * lentoradasta hypynaikana. viewWindow:in liikkumisesta aiheutuva naennainen
 * liike ei kuulu tanne.
 * @author 345707
 *
 */
public class Physics {
	private Avatar avatar;
	private List<GameObject> layers;
	private Vector2f velocity;
	private Vector2f acceleration;
	
	/**
	 * viive edellisesta game-loopin update-metodin kutsumisesta
	 */
	private int delta;
	
	public static final float gravity = 1f;
	
	public Physics(Avatar avatar) {
		this.avatar = avatar;
		this.layers = new ArrayList<GameObject>();
		this.velocity = new Vector2f();
		this.acceleration = new Vector2f(0, Physics.gravity);
	}
	
	
	public void update(List<GameObject> layers, int delta){
		this.delta = delta;
		this.layers = layers;
	}
	
	public void jump(){
		
		Vector2f deltaV = new Vector2f(this.acceleration).scale(delta);
		deltaV.y -= 1000f;
		this.velocity.add(deltaV);
	}
	
	public void moveAvatar(){
		this.acceleration.y = Physics.gravity;
		
		/*fysiikasta:
		 * v = a*t
		 */
		Vector2f deltaV = new Vector2f(this.acceleration).scale(this.delta);
		this.velocity.add(deltaV);
		
		
		/*fysiikasta:
		 * s= v*t
		 */
		
		//sijainnin muutos on nopeus kertaa aika edellisesta paivityksesta
		//jaetaan 1000: lla koska delta yksikkoa ms
		float scaleValue = this.delta/1000.0f;
		Vector2f deltaLocation = new Vector2f(this.velocity).scale(scaleValue);
		//float deltaX = this.velocity.x*this.delta/1000; // jaetaan 1000:lla
		//float deltaY = this.velocity.y*this.delta/1000 ; //koska delta yksikkoa ms
		//Vector2f deltaLocation = new Vector2f(deltaX, deltaY);
		//avatarin sijaintiin lisataan sijainnin muutos
		
		
		Vector2f AvatarsLocation = this.avatar.getLocationAbs();
		AvatarsLocation.add(deltaLocation);
		this.collisionCheck(AvatarsLocation);
		
		
	}	
	
	/**
	 * tarkistaa onko uusi koordinaatti laillinen. Voidaanko avatar siirtaa?
	 * Muuttaa GameObjectin sijainnin lailliseksi jos parametri-sijainti oli 
	 * laiton
	 * 
	 */
	public void collisionCheck(Vector2f location){
		//reagoi tormaykseen vasta kun kappaleet sisakkain
	
		boolean collisionX = false;
		boolean collisionY = false;
		float x = location.x;
		float y = location.y;
		
		if (x < 0) {
			location.x = 0;
			collisionX = true;
		}
		if (y < 0) {
			location.y = 0;
			collisionY = true;
		}
		float maxX = Game.WIDTH - this.avatar.getWidth(); 
		float maxY = Game.HEIGHT - this.avatar.getHeight();
		if (x > (maxX)) {
			location.x = maxX;
			collisionX = true;
		}
		if (y > (maxY)) {
			location.y = maxY;
			collisionY = true;
		}

		if (collisionX){
			this.velocity.x = 0;
			this.acceleration.x = 0;
		}

		if (collisionY){
			this.velocity.y = 0;
			this.acceleration.y = 0; //törmäyksen jalkeen pitää pystya nopeasti
			//lahtemaan vastakkaiseen suuntaan
		}
		
		//return new Vector2f(x,y);

	}



	/*	


		//TÖRMÄYSTARKISTUS
		//reagoi tormaykseen vasta kun kappaleet sisakkain
		boolean collisionX = false;
		boolean collisionY = false;
		if (x < 0) {
			x = 0;
			collisionX = true;
		}
		if (y < 0) {
			y = 0;
			collisionY = true;
		}
		float maxX = this.screenX - this.plane.getWidth(); 
		float maxY = this.screenY - this.plane.getHeight();
		if (x > (maxX)) {
			x = maxX;
			collisionX = true;
		}
		if (y > (maxY)) {
			y = maxY;
			collisionY = true;
		}
		
		if (collisionX){
			this.velocity.x = 0;
			this.acceleration.x = 0;
		}
		
		if (collisionY){
			this.velocity.y = 0;
			this.acceleration.y = 0; //törmäyksen jalkeen pitää pystya nopeasti
			//lahtemaan vastakkaiseen suuntaan
		}
	
	}
	*/
}
