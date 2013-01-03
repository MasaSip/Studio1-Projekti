package Game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tests.AnimationTest;



public class Game extends BasicGame {
	
	public static final float WIDTH = 900;
	public static final float HEIGHT = 550;
	
	public GameEngine gameEngine;
	
	public Game() throws SlickException{
		super("Sokeri Humala");
		this.gameEngine = new GameEngine(this);
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
		this.gameEngine.putAvatarIntoGame();
		this.gameEngine.putBottomLayerIntoGame();
	
	}

	@Override
	/**
	 * logiikan paivitus
	 * @param delta kauanko on aikaa edellisesta paivityksesta
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		this.gameEngine.updatePhysics(delta);
		Input input = gc.getInput();
		this.gameEngine.scrollView(delta);
		this.gameEngine.generateLayers();
				
		this.gameEngine.moveAvatar(input, delta);
		if (this.gameEngine.gameOver()){
			this.gameOver(gc);
		}
	
	}
	
	public void gameOver(GameContainer gc){
		// jos halutaan ikkunan menevän kiinni
		//gc.exit();
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
