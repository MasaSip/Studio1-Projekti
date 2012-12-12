import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Point;

public class Ohjain {
	private Point ikkunanKoko;
	private GameContainer gc;
	private int delta;
	
	private Nakyma nakyma;
	private Hahmo hahmo;
	
	public Ohjain(Nakyma nakyma, Point ikkunanKoko) {
		this.nakyma = nakyma;
		this.hahmo = new Hahmo(this.annaKeskiX(), Peli.korkeus - 100f);
		
	}
	
	/**
	 * 
	 * @return Hahmon sijainti
	 */
	public Point annaHahmonSijainti(){
		return this.hahmo.annaSijainti();
	}
	/**
	 * kertoo ohjaimelle GameContainerin ja viiveen edellisestä paivityksesta
	 * @param gc 
	 * @param delta
	 */
	public void paivita(GameContainer gc, int delta){
		this.gc = gc;
		this.delta = delta;
	}
	
	public void liikutaHahmoa(){
		Input input = gc.getInput();
		Point sijainti = this.hahmo.annaSijainti();
		float sijaintiX = sijainti.getX();
		
		if (input.isKeyDown(Input.KEY_LEFT)){
			
			sijaintiX -= 0.3f*this.delta;
			sijainti.setX(sijaintiX);
		}
		
		if (input.isKeyDown(Input.KEY_RIGHT)){

			sijaintiX += 0.3f*this.delta;
			sijainti.setX(sijaintiX);
		}
		this.hahmo.asetaSijainti(sijainti);
	}
	
	public void annaPiirtoOhjeet(){
		this.nakyma.piirraHahmo(this.hahmo.annaSijainti());
	}
	
	/**
	 * keskelle leveys suunnassa
	 * @return
	 */
	public float annaKeskiX(){
		float HahmonLeveys = this.nakyma.annaHahmonMitat().getX();
		System.out.println(HahmonLeveys);
		System.out.println(Peli.leveys);
		return (Peli.leveys - HahmonLeveys)/2;
	}
}
