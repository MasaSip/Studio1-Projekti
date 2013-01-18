package game;

import menu.MenuState;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;



public class Game extends StateBasedGame {
	
	public static final int GAMEPLAYSTATE = -1;
	public static final int MENUSTATE = 0;
	public static final float WIDTH = 1400;
	public static final float HEIGHT = 750;
	
	public Game() {
		super("Hämiksen Päiväunet");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		
		//StateBasedGame käy kutsumassa näiden init-metodia
		this.addState(new GamePlayState(GAMEPLAYSTATE));
		this.addState(new MenuState(MENUSTATE));
	}
	
	public static void main(String[] args) 
			throws SlickException
		{	

		Game game = new Game();
		AppGameContainer app = new AppGameContainer(game);

		app.setDisplayMode(
				/*
				 * 
					800,
					600, 
					true)
				 */
				(int)Game.WIDTH,
				(int) Game.HEIGHT, 
				false);

		app.setTargetFrameRate(50);
		app.setShowFPS(false);
		app.setVSync(true);
		//Musiikki soi jatkuvasti riippumatta alotetaanko peli alusta.
		Music music = new Music("data/Cajon_Party.ogg");
		music.loop(1.0f, 1f);
		
		app.start();
	
		}

}
