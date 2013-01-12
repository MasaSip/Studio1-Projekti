package game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

/**
 * 
 * Liikuttaa kappaleita painovoiman mukaisesti. Pitaa huolen esim. Avatarin
 * lentoradasta hypynaikana. viewWindow:in liikkumisesta aiheutuva naennainen
 * liike ei kuulu tanne.
 * 
 *
 */
public class Physics {
	private Avatar avatar;
	private List<Layer> layers;
	private Vector2f velocity;
	private Vector2f acceleration;
	/**
	 * vähintään tän verran Avatarin leveydesta nakyy kokoajan. Sivuttais 
	 * tormays tapahtuu vasta ruudun ulkopuolella.
	 */
	private final float minVisible = 40;
	
	/**
	 * viive edellisesta game-loopin update-metodin kutsumisesta
	 */
	private int delta;
	
	public static final float gravity = 1.2f;
	
	public Physics(Avatar avatar) {
		this.avatar = avatar;
		this.layers = new ArrayList<Layer>();
		this.velocity = new Vector2f();
		this.acceleration = new Vector2f(0, Physics.gravity);
	}
	
	
	public void update(List<Layer> layers, int delta){
		this.delta = delta;
		this.layers = layers;
	}
	
	public void jump(){
		//hyppyrajoitus toimii tällä xxx 
		if (this.avatar.isOnGround()){
			
		Vector2f deltaV = new Vector2f(this.acceleration).scale(delta);
		deltaV.y -= this.avatar.getJumpingPower();
		this.velocity.add(deltaV);
		}
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
		
		
		
		Vector2f oldLocation = this.avatar.getLocationAbs();
		Vector2f targetLocation = oldLocation.copy();
		targetLocation.add(deltaLocation);
		
		Vector2f finalLocation = //this.collisionCheck(targetLocation);
				this.collisionWithLayers(targetLocation);
		finalLocation = this.collisionWithWindow(this.avatar, finalLocation);
		
		this.avatar.setLocation(finalLocation);
		
		
		
		
	}	
	
	/**
	 * tormaustarkistus ikkunan reunojen kanssa.
	 * @param o kenen tormaysta tarkistetaan
	 * @param to mihin yritetaan menna
	 */
	public Vector2f collisionWithWindow(GameObject o, Vector2f to){
		//reagoi tormaykseen vasta kun kappaleet sisakkain
		
		float width = this.avatar.getWidth();
		//kuinka paljon Avatarista voi enimmillaan menna ruudun sivun taakse
		float invisibleMax = this.getPercentageValue(this.minVisible, width);
		
		boolean collisionX = false;
		boolean collisionY = false;
		float x = to.x;
		float y = to.y;
		
		if (x < -invisibleMax) {
			x = -invisibleMax;
			collisionX = true;
		}
		
		float maxX = Game.WIDTH - o.getWidth() + invisibleMax; 
		float maxY = Game.HEIGHT - o.getHeight();
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
			this.stopAvatarsMovement();
			
		}
		
		return new Vector2f(x,y);

	}
	
	
	public Vector2f collisionWithLayers(Vector2f to){
		Vector2f from = this.avatar.getLeftTop();
		Vector2f leftBottom = this.avatar.getLeftBottom();
		//motion on liikevektori
		Vector2f fromNegate = from.negate();
		Vector2f toCopy = to.copy();
		Vector2f motion = toCopy.add(fromNegate); 
		//System.out.println(motion); //xxx sivuttais liikettä ei huomioida
		
		//java avatarin pohjasta vasemmalta oikealle
		Line bottomLine =
				new Line(leftBottom, 
						this.avatar.getRightBottom());
		//avatarin pohjan y-koordinaatti alussa
		float bottomY1 = from.y + this.avatar.getHeight();
		//avatarin pohja y-koordinaatti lopussa
		float bottomY2 = to.y + this.avatar.getHeight();
		
		//jana, joka kuvaa avatarin vasemman alakulman liikkeen
		Line motionLine = new Line(leftBottom, leftBottom.copy().add(motion));
		
		//naita muutetaan jos tormataan, naista tulee lopullinen sijainti
		float x = to.x;
		float y = to.y;
		
		//ollaan menossa ylöspäin. Koordinaatisto kasvaa alas
		if (to.y < from.y){
			this.avatar.setOnGround(false);
			return to;
			
		}
		
		//ollaan menossa alapain
		
		
		
		
		for (Layer layer : this.layers){
			
			//luodaan jana johon voi tormata, vaakajana
			Line collisionLine = layer.getCollisionLine();
			
			float avatarsWidth = this.avatar.getWidth();
			
			if (avatarsWidth > collisionLine.getWidth()){
				System.err.println("Physics-luokka: tilannetta jossa avatar on " +
						"leveämpi kuin pohjalaatta ei ole otettu vielä " +
						"huomioon");
			}
			//layerin ylatason y-koordinaatti
			float layerY = collisionLine.getY();
			
			
			
			boolean collisionPossible = 
					bottomY1 <= layerY && layerY <= bottomY2;
			if (collisionPossible){
				
				
			
				//Avatarin pohjan vasemman alakulman x-koordinaatti sijainnissa
				//jossa Avatar saattaisi tormata layeriin
				float collisionX = this.getXfromY(layerY, motionLine);
				float X1 = collisionX;
				float X2 = collisionX + bottomLine.getWidth();
				boolean collisionTrue = X2 > collisionLine.getX1() && X1 < 
					collisionLine.getX2();
				
				if (collisionTrue){
					x = collisionX;
					y = layerY - this.avatar.getHeight();
					this.stopAvatarsMovement();
				}
				
			}



		}

		Vector2f finalLocation = new Vector2f(x,y);
		
		this.avatar.setOnGround(!finalLocation.equals(to));
		
		return finalLocation;
	}
	
	
	
	/**
	 * Suoran yhtälö, sijoita y, saat x:n
	 * @param y sijoita y-koordinaatti
	 * @param line sisältää kaksi pistetta, jotka maarittavat suoran
	 * @return saat x-koordinaatin
	 */
	public float getXfromY(float y, Line line){
		/*
		 * kulmakerroin k = (y2-y1)/(x2-x1)
		 * suoran yhtalo: y - y1 = k(x-x1)
		 * joten x = 1/k * (y-y1) + x1
		 *  <=> x = (x2-x1)/(y2-y1) * (y-y1) + x1
		 */
		
		float x1 = line.getX1();
		float x2 = line.getX2();
		float y1 = line.getY1();
		float y2 = line.getY2();
		
		if (y2 == y1){
			System.err.println("Physics-luokassa yritetään jakaa nollalla");
			return (x2-x1)/2;
		}
		return (x2-x1)/(y2-y1) * (y-y1) + x1;
	}
	
	public void stopAvatarsMovement(){
		this.velocity.y = 0;
		this.acceleration.y = 0; //törmäyksen jalkeen pitää pystya nopeasti
		//lahtemaan vastakkaiseen suuntaan
	}
	
	/**
	 * 
	 * @param percent prosenttia
	 * @param a
	 * @return p % a:sta
	 */
	public float getPercentageValue(float percent, float a){
		return percent/100.0f*a;
	}

	
}
