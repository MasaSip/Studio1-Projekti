package Game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Line;
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
	
	public static final float gravity = 1.2f;
	
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
		deltaV.y -= 700f;
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
		
		
		Vector2f oldLocation = this.avatar.getLocationAbs();
		Vector2f targetLocation = oldLocation.copy();
		targetLocation.add(deltaLocation);
		
		Vector2f finalLocation = //this.collisionCheck(targetLocation);
				this.collisionWithLayers(targetLocation);
		finalLocation = this.collisionWithWindow(this.avatar, finalLocation);
		
		this.avatar.setLocation(finalLocation);
		
		//AvatarsLocation.add(deltaLocation);
		//this.collisionCheck(AvatarsLocation);
		
		
	}	
	
	/**
	 * tormaustarkistus ikkunan reunojen kanssa.
	 * @param o kenen tormaysta tarkistetaan
	 * @param to mihin yritetaan menna
	 */
	public Vector2f collisionWithWindow(GameObject o, Vector2f to){
		//reagoi tormaykseen vasta kun kappaleet sisakkain
		
		boolean collisionX = false;
		boolean collisionY = false;
		float x = to.x;
		float y = to.y;
		
		if (x < 0) {
			x = 0;
			collisionX = true;
		}
		/*
		 * poista jos toimii ilman xxx 
		if (y < 0) {
			y = 0;
			collisionY = true;
		}
		 */
		float maxX = Game.WIDTH - o.getWidth(); 
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
			return to;
		}
		
		//ollaan menossa alapain
		
		
		
		
		for (GameObject o : this.layers){
			
			//luodaan jana johon voi tormata, vaakajana
			Line collisionLine = this.getCollisionLine(o);
			
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
			
			
			
			
			
			if (to.y > o.getTopY()){


				
				Line layerLine = new Line(o.getLeftTop(), o.getRightTop());

				Vector2f intersectPoint = motionLine.intersect(layerLine);
				//tästä jatkuu
				/*
				 *get point palautta 4 pistettä jotka ympäröivät kuvion
					float[] layerPoints = layerLine.getPoints();
					float[] motionPoints = motionLine.getPoints();
					for (int i=0 ; i < layerPoints.length; i++){
						for (int j = 0; j < motionPoints.length ; j++)

						System.out.println(i);
					}
				 */
			}


		}


		
		return new Vector2f(x,y);
	}
	
	/**
	 * 
	 * @param o
	 * @return jana kuvan ylätasosta
	 */
	public Line getCollisionLine(GameObject o){
		Vector2f startPoint = o.getLeftTop();
		Vector2f endPoint = o.getRightTop();
		return new Line(startPoint, endPoint);
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


	
}
