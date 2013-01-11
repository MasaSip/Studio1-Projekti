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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.tests.AnimationTest;

/**
* Pelin kuvat ovat Sonja Kiiverin piirtamia. Musiikkin ovat tehneet 
* Nora Bergman ja Aapo Haapasalo.
* 
*/

public class GamePlayState extends BasicGameState {
	
	private int stateID;
	
	public GameEngine gameEngine;
	
	
	public GamePlayState(int stateID) throws SlickException{
		this.gameEngine = new GameEngine(this);
		this.stateID = stateID;
	}

	@Override
	/**
	 * grafiikan piirto
	 */
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
	
		this.gameEngine.drawGame(g);
		
		
		
		
		
	}

	@Override
	/**
	 * suoritetaan ennen game-loopin kaynnistymista.
	 */
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
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
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
		this.gameEngine.updatePhysics(delta);
		Input input = gc.getInput();
		this.gameEngine.scrollView(delta);
		this.gameEngine.generateLayers();
				
		this.gameEngine.moveAvatar(input, delta);
		if (this.gameEngine.gameOver()){
			this.gameOver(gc, game);
		}
		this.gameEngine.updateScore();
	
	}
	
	public void gameOver(GameContainer gc, StateBasedGame game) throws SlickException
	
	{
		int score = this.gameEngine.getScore();
		MenuState state = (MenuState) game.getState(Game.MENUSTATE);
		state.addScore(score);
		state.updateMenuText();
		this.gameEngine = new GameEngine(this);
		this.init(gc, game);
		this.enterState(game, Game.MENUSTATE);
		
		
	}
	
	

	@Override
	public int getID() {
	
		return this.stateID;
	}
	/**
	 * Vaihdetaan GameState himmennyksen kautta
	 * @param game
	 * @param state mihin
	 */
	public void enterState(StateBasedGame game, int state){
		Transition leave = new FadeOutTransition();
		Transition enter = new FadeInTransition();
		game.enterState(state, leave, enter);
	}

}