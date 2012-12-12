package Peli;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;



public class Game extends BasicGame {
	
	public static final float WIDTH = 900;
	public static final float HEIGHT = 700;
	
	public GameEngine gameEngine;
	
	public Game() {
		super("Sokeri Humala");
		this.gameEngine = new GameEngine();
	}

	@Override
	/**
	 * grafiikan piirto
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
	
	}

	@Override
	/**
	 * suoritetaan ennen game-loopin kaynnistymista.
	 */
	public void init(GameContainer gc) throws SlickException {
		
	}

	@Override
	/**
	 * logiikan paivitus
	 * @param delta kauanko on aikaa edellisesta paivityksesta
	 */
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
				
		if (input.isKeyDown(Input.KEY_LEFT)){
			this.gameEngine.moveAvatar(GameEngine.LEFT, delta);
		}
		
		if (input.isKeyDown(Input.KEY_RIGHT)){
			this.gameEngine.moveAvatar(GameEngine.RIGHT, delta);
		}
	
	}
	
	public static void main(String[] args) 
		throws SlickException
	{	

		Game peli = new Game();
		AppGameContainer app = new AppGameContainer(peli);
		app.setDisplayMode(
				(int)Game.WIDTH,
				(int) Game.HEIGHT, 
				false);
		app.start();
		
	}

}
