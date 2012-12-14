package Game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;



public class Game extends BasicGame {
	
	public static final float WIDTH = 900;
	public static final float HEIGHT = 600;
	
	public GameEngine gameEngine;
	
	public Game() throws SlickException{
		super("Sokeri Humala");
		this.gameEngine = new GameEngine();
	}

	@Override
	/**
	 * grafiikan piirto
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
	
		this.gameEngine.drawGameObjects();
		
	}

	@Override
	/**
	 * suoritetaan ennen game-loopin kaynnistymista.
	 */
	public void init(GameContainer gc) throws SlickException {
		this.gameEngine.loadImages();
		this.gameEngine.putBottomLayerIntoGame();
		this.gameEngine.putAvatarIntoGame();
	}

	@Override
	/**
	 * logiikan paivitus
	 * @param delta kauanko on aikaa edellisesta paivityksesta
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		this.gameEngine.generateLayers();
				
		this.gameEngine.moveAvatar(input, delta);
		
	
	}
	
	public static void main(String[] args) 
		throws SlickException
	{	

		Game game = new Game();
		AppGameContainer app = new AppGameContainer(game);
		app.setDisplayMode(
				(int)Game.WIDTH,
				(int) Game.HEIGHT, 
				false);
		app.start();
		
	}

}
