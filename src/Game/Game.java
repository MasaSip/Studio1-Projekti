package game;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;



public class Game extends StateBasedGame {
	
	public static final int GAMEPLAYSTATE = -1;
	public static final int MENUSTATE = 0;
	public static final float WIDTH = 1600;
	public static final float HEIGHT = 650;
	
	public Game() {
		super("Tyttel� Peli");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		//StateBasedGame k�y kutsumassa n�iden init-metodia
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
			
			
			app.setShowFPS(false);
			
			//Musiikki soi jatkuvasti riippumatta alotetaanko peli alusta.
			Music music = new Music("data/Cajon_Party.wav");
			music.loop(1.0f, 40f);
			
			
			app.start();
			
		}

}