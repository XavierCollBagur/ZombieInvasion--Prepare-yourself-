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
 * This class contains the configuration parameters of the zombie epidemic of a simulation.
 * @author Xavier
 */

@XmlRootElement(name = "ZombieEpidemicConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class ZombieEpidemicConfiguration {
    //Attributes
    
    /**
     * Number of phases needed for infected humans to become zombies.
     */
    @XmlElement(name = "InfectedLatencyPeriod")
    private int infectedLatencyPeriod;
    
    /**
     * Speed of zombies.
     */
    @XmlElement(name = "ZombieSpeed")
    private int zombieSpeed;
    
    /**
     * Speed of zombies when they doesn't see any human.
     */
    @XmlElement(name = "ZombieSpeedAtRest")
    private int zombieSpeedAtRest;
    
    /**
     * Maximum distance of vision of zombies.
     */
    @XmlElement(name = "ZombieVisionDistance")
    private int zombieVisionDistance;
    
    /**
     * Maximum distance of smell of zombies.
     */
    @XmlElement(name = "ZombieOlfactoryDistance")
    private int zombieOlfactoryDistance;
    
    //Public Constructors
    public ZombieEpidemicConfiguration(int infectedLatencyPeriod, int zombieSpeed, 
                                       int zombieSpeedAtRest, int zombieVisionDistance,
                                       int zombieOlfactoryDistance) {
        
        this.infectedLatencyPeriod   = infectedLatencyPeriod;
        this.zombieSpeed             = zombieSpeed;
        this.zombieSpeedAtRest       = zombieSpeedAtRest;
        this.zombieVisionDistance    = zombieVisionDistance;
        this.zombieOlfactoryDistance = zombieOlfactoryDistance;
    }
    
    public ZombieEpidemicConfiguration(ZombieEpidemicConfiguration original) {
        this(original.infectedLatencyPeriod, original.zombieSpeed, original.zombieSpeedAtRest,
             original.zombieVisionDistance, original.zombieOlfactoryDistance);
    }

    //Necessary for XML serialization purposes
    public ZombieEpidemicConfiguration() {
        this(0, 0, 0, 0, 0);
    }
    
    //Public Method
    /**
     * Returns the number of phases needed for infected humans to become zombies.
     * @return the number of phases
     */
    public int getInfectedLatencyPeriod() {
        return infectedLatencyPeriod;
    }

    /**
     * Returns the speed of zombies.
     * @return the speed
     */
    public int getZombieSpeed() {
        return zombieSpeed;
    }

    /**
     * Returns the speed of zombies when they doesn't see any human.
     * @return the speed at rest
     */
    public int getZombieSpeedAtRest() {
        return zombieSpeedAtRest;
    }

    /**
     * Returns the maximum distance of vision of zombies.
     * @return the maximum vision distance
     */
    public int getZombieVisionDistance() {
        return zombieVisionDistance;
    }
    
    /**
     * Returns the maximum distance of smell of zombies.
     * @return the maximum olfactory distance
     */
    public int getZombieOlfactoryDistance() {
        return zombieOlfactoryDistance;
    }

    /**
     * Sets the number of phases needed for infected humans to become zombies.
     * @param infectedLatencyPeriod the number of phases 
     */
    public void setInfectedLatencyPeriod(int infectedLatencyPeriod) {
        this.infectedLatencyPeriod = infectedLatencyPeriod;
    }

    /**
     * Sets the speed of zombies.
     * @param zombieSpeed the speed
     */
    public void setZombieSpeed(int zombieSpeed) {
        this.zombieSpeed = zombieSpeed;
    }

    /**
     * Sets the speed of zombies when they doesn't see any human.
     * @param zombieSpeedAtRest the speed
     */
    public void setZombieSpeedAtRest(int zombieSpeedAtRest) {
        this.zombieSpeedAtRest = zombieSpeedAtRest;
    }

    /**
     * Sets the maximum distance of vision of zombies.
     * @param zombieVisionDistance the vision distance
     */
    public void setZombieVisionDistance(int zombieVisionDistance) {
        this.zombieVisionDistance = zombieVisionDistance;
    }

    /**
     * Sets the maximum distance of smell of zombies.
     * @param zombieOlfactoryDistance the olfactory distance
     */
    public void setZombieOlfactoryDistance(int zombieOlfactoryDistance) {
        this.zombieOlfactoryDistance = zombieOlfactoryDistance;
    }
}
