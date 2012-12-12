import org.newdawn.slick.geom.Point;


public class Hahmo {
	private Point sijainti;
	
	public Hahmo(float x, float y) {
		
		this.sijainti = new Point(x, y);
	}
	
	/**
	 * liikuttaa kohdetta
	 * @param x neg. oikealle, pos. vasemmalle
	 * @param y neg ylos, pos alas
	 */
	public void siirra(float x, float y){
		Point wanha = this.sijainti;
		
		this.sijainti.setX(wanha.getX() + x);
		this.sijainti.setY(wanha.getY() + y);
	}
	
	public Point annaSijainti(){
		return this.sijainti;
	}
	
	public void asetaSijainti(Point sijainti){
		this.sijainti = sijainti;
	}

}
