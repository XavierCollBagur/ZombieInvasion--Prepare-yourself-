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

package SimulationConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains the configuration parameters of human agents of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "HumanConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class HumanConfiguration {
    //Attributes
    /**
     * Speed of humans.
     */
    @XmlElement(name = "Speed")
    private int speed;
    
    /**
     * Maximum distance of vision of humans.
     */
    @XmlElement(name = "VisionDistance")
    private int visionDistance;
    
    //Public Constructors
    public HumanConfiguration(int speed, int visionDistance) {
        this.speed          = speed;
        this.visionDistance = visionDistance;
    }
    
    public HumanConfiguration(HumanConfiguration original) {
        this(original.speed, original.visionDistance);
    }
    
    //Necessary for XML serialization purposes
    private HumanConfiguration() {
        this(0, 0);
    }
    
    //Public Methods
    /**
     * Returns the speed of the humans.
     * @return the speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Returns the maximum distance of vision of humans.
     * @return the vision distance
     */
    public int getVisionDistance() {
        return this.visionDistance;
    }

    /**
     * Sets the speed of humans.
     * @param speed the speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Sets the maximum distance of vision of humans.
     * @param visionDistance the vision distance
     */
    public void setVisionDistance(int visionDistance) {
        this.visionDistance = visionDistance;
    }
}
