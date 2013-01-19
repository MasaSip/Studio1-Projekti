package menu;

import game.Game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

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
	private UnicodeFont menuFont;
	private UnicodeFont creditsFont;
	private Image background;
	
	
	public MenuState(int stateID) {
		this.stateID = stateID;
		this.menuText = "Paina mua, jos mielit pelata";
		this.lastScores = new ArrayList<Integer>();
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.initFont(35);
		this.background = new Image("data/NukkuvaHamis.png");
		
		
	}
	
	public void initFont(int size) throws SlickException{
		Font awtFont = new Font("Chalkduster", Font.BOLD, size);
		this.menuFont = new UnicodeFont(awtFont);
		this.menuFont.addAsciiGlyphs();
		java.awt.Color menuColor = new java.awt.Color(225, 0, 0);
		ColorEffect red = new ColorEffect(menuColor);
		this.menuFont.getEffects().add(red);
		this.menuFont.loadGlyphs();
		
		Font creditsAwt = new Font("Tahoma",Font.BOLD, 18);
		this.creditsFont = new UnicodeFont(creditsAwt);
		this.creditsFont.addAsciiGlyphs();
		java.awt.Color creditsColor = new java.awt.Color(53, 189, 216);
		ColorEffect effect = new ColorEffect(creditsColor);
		this.creditsFont.getEffects().add(effect);
		this.creditsFont.loadGlyphs();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		this.showLastScores(g, new Point(800,40));
		this.menuFont.drawString(200, Game.HEIGHT - 400, this.menuText);
		this.drawCredits(g);
		
		g.setBackground(Color.white);
		g.drawImage(this.background, 10, 10);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		
		boolean mouseLeft = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		boolean mouseRight = input.isMousePressed(Input.MOUSE_RIGHT_BUTTON);
		boolean enter = input.isKeyPressed(Input.KEY_ENTER);
		
		if (mouseLeft || mouseRight || enter){
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
		//tulos rivien vŠlinen etŠisyys
		float distBetweenLines = 50f;
		
		float X = point.getX();
		float Y = point.getY();
		
		//kuinka monta on jo piirretty ruudulle
		float scoresShown = 0;
		int scores = this.lastScores.size(); 
		if (scores == 0){
			return;
		}
		
		menuFont.drawString(X, Y, "NŠin korkealle olet kivunnut");
		
		for (int i = scores-1; i >= 0 && scoresShown < this.scoresMax; i--){
		
			
			float x = X + 20;
			float y = Y + (scoresShown+1.0f) * distBetweenLines;
			String scoreTxt = Integer.toString(this.lastScores.get(i));
			menuFont.drawString(x, y, scoreTxt);
			scoresShown++;
		}
	}
	
	public void updateMenuText(){
		this.menuText = "Pikku torkut tŠhŠn vŠliin. HerŠtŠ, jos haluut uusiks!";
	}
	
	public void drawCredits(Graphics g){
		int lineHeight = this.creditsFont.getLineHeight();
		float y = Game.HEIGHT -lineHeight*1.2f;
		float x = Game.WIDTH -930f;
		String credits = "OHJELMOINTI: Matti Sippola, " +
				"GRAFIIKKA: Sonja Kiiveri, " +
				"MUSIIKKI: Aapo Haapasalo ja Nora Bergman";
	
		this.creditsFont.drawString(x, y, credits);
	}
	

}
