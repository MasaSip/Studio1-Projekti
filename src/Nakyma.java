import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;


public class Nakyma {
	private Image hahmo = null;
	
	public Nakyma() throws SlickException {
		hahmo = new Image("data/A.png");
	}
	
	public void paivita(GameContainer gc, Graphics g){
		

	}
	
	public void piirraHahmo(Point sijainti){
		hahmo.draw(sijainti.getX(), sijainti.getY());
	}
	
	public Point annaHahmonMitat(){
		return new Point(hahmo.getWidth(), hahmo.getHeight());
	}
}
