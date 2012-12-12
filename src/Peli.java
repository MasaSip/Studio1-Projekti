import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;


public class Peli extends BasicGame {
	private Image avatar = null;
	private Nakyma nakyma;
	private Ohjain ohjain;
	private Malli malli;
	public static final float leveys = 900;
	public static final float korkeus = 700;
	
	
	public Peli() {
		super("Sokeri Humala");
		
	}

	@Override
	/**
	 * grafiikan piirto
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		//this.nakyma.paivita(gc, g);
		this.ohjain.annaPiirtoOhjeet();
		

	}

	@Override
	/**
	 * suoritetaan ennen game-loopin kaynnistymista.
	 */
	public void init(GameContainer gc) throws SlickException {
		this.nakyma = new Nakyma();
		this.ohjain = 
				new Ohjain(this.nakyma, new Point(Peli.leveys, Peli.korkeus));
		this.malli = new Malli();
	

	}

	@Override
	/**
	 * logiikan paivitus
	 * @param delta kauanko on aikaa edellisesta paivityksesta
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		this.ohjain.paivita(gc, delta);
		this.ohjain.liikutaHahmoa();
	}
	
	public static void main(String[] args) 
		throws SlickException
	{
		Peli peli = new Peli();
		AppGameContainer app = new AppGameContainer(peli);
		app.setDisplayMode(
				(int)Peli.leveys,
				(int) Peli.korkeus, 
				false);
		app.start();
		
	}

}
