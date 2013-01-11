package game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

public class MenuState extends BasicGameState {

	private int stateID;
	
	private List<Integer>lastScores;
	//kuinka monta tulosta naytetaan enimmillaan yhta aikaa
	private final int scoresMax = 5;
	
	private String menuText;
	private UnicodeFont font;
	private Image background;
	
	
	public MenuState(int stateID) {
		this.stateID = stateID;
		this.menuText = "Paina mua, jos mielit pelata";
		this.lastScores = new ArrayList<Integer>();
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
		this.showLastScores(g, new Point(800,40));
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
	
	public void addScore(int score){
		this.lastScores.add(score);
	}
	
	public void showLastScores(Graphics g, Point point){
		//tulos rivien v�linen et�isyys
		float distBetweenLines = 50f;
		
		float X = point.getX();
		float Y = point.getY();
		
		//kuinka monta on jo piirretty ruudulle
		float scoresShown = 0;
		int scores = this.lastScores.size(); 
		if (scores == 0){
			return;
		}
		
		font.drawString(X, Y, "N�in korkealle olet kivunnut");
		
		for (int i = scores-1; i >= 0 && scoresShown < this.scoresMax; i--){
		
			
			float x = X + 20;
			float y = Y + (scoresShown+1.0f) * distBetweenLines;
			String scoreTxt = Integer.toString(this.lastScores.get(i));
			font.drawString(x, y, scoreTxt);
			scoresShown++;
		}
	}
	
	public void updateMenuText(){
		this.menuText = "Hihii, uudestaan!";
	}

}
