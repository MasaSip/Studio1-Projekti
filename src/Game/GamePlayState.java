package game;

import menu.MenuState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;


/**
 * Pelinakyman GameLoop pyorii taalla.
 * 
 *
 */
public class GamePlayState extends BasicGameState {
	
	private int stateID;
	
	public GameEngine gameEngine;
	
	
	
	
	
	public GamePlayState(int stateID) throws SlickException{
		this.gameEngine = new GameEngine();
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
		
		long initStart = System.currentTimeMillis();
		this.gameEngine.loadImages();
		
		this.gameEngine.putAvatarIntoGame();
		this.gameEngine.putBottomLayerIntoGame();
		this.gameEngine.putAvatarAboveBottomLayer();
		
		this.gameEngine.initView();
		
		long initEnd = System.currentTimeMillis();
		long initializingTime = initEnd - initStart;
		
		System.out.println("GamePlayState:n init-metodin suoritus kesti " + 
		initializingTime + " millisekunttia");
		
		
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
				
		if (this.gameEngine.gameOver()){
			this.gameOver(gc, game);
		}
		this.gameEngine.updateScore();
		
		this.gameEngine.update(input, delta);
		this.gameEngine.updateAverageDelta(delta);
		
		
		
	
	}
	
	public void gameOver(GameContainer gc, StateBasedGame game) throws SlickException
	
	{
		int score = this.gameEngine.getScore();
		MenuState state = (MenuState) game.getState(Game.MENUSTATE);
		state.addScore(score);
		state.updateMenuText();
		this.gameEngine = new GameEngine();
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
