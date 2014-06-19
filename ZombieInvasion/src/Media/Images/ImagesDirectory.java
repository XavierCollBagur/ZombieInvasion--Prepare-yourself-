/*
  This file is part of ZombiesSimulator.

  ZombiesSimulator is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  ZombiesSimulator is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with ZombiesSimulator.  If not, see <http://www.gnu.org/licenses/>.
 */

package Media.Images;

import java.awt.Image;
import java.io.IOError;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class contains image objects of the images used in the application
 * @author Xavier
 */
public class ImagesDirectory {
    public final static Image APPLICATION_ICON_IMAGE, APPLICATION_TITLE_IMAGE, 
                              GUN_IMAGE, PLAY_IMAGE, PAUSE_IMAGE, RESTART_IMAGE, 
                              VACCINE_IMAGE, WALL_IMAGE, WALL_FORBIDDEN_IMAGE;
    
    static {
        //Read all the images used in the application
        try {
            APPLICATION_ICON_IMAGE  = ImageIO.read(ImagesDirectory.class.getResource("ApplicationIcon.png"));
            APPLICATION_TITLE_IMAGE = ImageIO.read(ImagesDirectory.class.getResource("ApplicationTitle.png"));
            GUN_IMAGE               = ImageIO.read(ImagesDirectory.class.getResource("gun.png"));
            PLAY_IMAGE              = ImageIO.read(ImagesDirectory.class.getResource("play.png"));
            PAUSE_IMAGE             = ImageIO.read(ImagesDirectory.class.getResource("pause.png"));
            RESTART_IMAGE           = ImageIO.read(ImagesDirectory.class.getResource("restart.png"));
            VACCINE_IMAGE           = ImageIO.read(ImagesDirectory.class.getResource("vaccine.png"));
            WALL_IMAGE              = ImageIO.read(ImagesDirectory.class.getResource("wall.png"));
            WALL_FORBIDDEN_IMAGE    = ImageIO.read(ImagesDirectory.class.getResource("wall forbidden.png"));
        } catch (IOException ex) {
            throw new IOError(ex);
        }
    }
}
