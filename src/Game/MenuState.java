package game;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

public class MenuState extends BasicGameState {

	private int stateID;
	
	String menuText;
	UnicodeFont font;
	Image background;
	
	
	public MenuState(int stateID) {
		this.stateID = stateID;
		this.menuText = "Paina mua, jos mielit pelata";
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.initFont(30);
		this.background = new Image("data/NukkuvaHamis.png");
		
		
	}
	
	public void initFont(int size) throws SlickException{
		Font awtFont = new Font("Comic Sans MS", Font.BOLD, size);
		this.font = new UnicodeFont(awtFont);
		this.font.addAsciiGlyphs();
		ColorEffect red = new ColorEffect(java.awt.Color.red);
		this.font.getEffects().add(red);
		this.font.loadGlyphs();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		this.font.drawString(200, Game.HEIGHT - 200, this.menuText);
		g.setBackground(Color.white);
		g.drawImage(this.background, 10, 10);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			this.enterState(game, Game.GAMEPLAYSTATE);
		}
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
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
