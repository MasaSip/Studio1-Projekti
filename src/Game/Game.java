package game;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.tests.AnimationTest;

/**
* Pelin kuvat ovat Sonja Kiiverin piirtamia. Musiikkin ovat tehneet 
* Nora Bergman ja Aapo Haapasalo.
* 
*/

public class Game extends BasicGame {
	
	public static final float WIDTH = 1600;
	public static final float HEIGHT = 650;
	
	public GameEngine gameEngine;
	
	
	public Game() throws SlickException{
		super("Tyttel� Peli");
		this.gameEngine = new GameEngine(this);
	}

	@Override
	/**
	 * grafiikan piirto
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
	
		this.gameEngine.drawGame(g);
		
		
		
		
		
	}

	@Override
	/**
	 * suoritetaan ennen game-loopin kaynnistymista.
	 */
	public void init(GameContainer gc) throws SlickException {
		this.gameEngine.loadImages();
		this.gameEngine.putAvatarIntoGame();
		this.gameEngine.putBottomLayerIntoGame();
		
		this.gameEngine.initView();

		
		
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
		this.gameEngine.updateScore();
	
	}
	
	public void gameOver(GameContainer gc) throws SlickException
	
	{
		
		this.gameEngine = new GameEngine(this);
		this.init(gc);
		
		
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
		
		app.setShowFPS(false);
		
		//Musiikki soi jatkuvasti riippumatta alotetaanko peli alusta.
		Music music = new Music("data/Cajon_Party.wav");
		music.loop(1.0f, 40f);
		
		
		app.start();
		
	}

}
