package Peli;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class Avatar extends GameObject {

	public Avatar(Point location) throws SlickException {
		super(new Image("data/A.png"), location);
	}

}
