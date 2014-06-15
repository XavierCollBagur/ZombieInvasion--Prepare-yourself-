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
 * This class contains the configuration parameters of the population of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "PopulationConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class PopulationConfiguration {
    //Attributes
    /**
     * Number of healthy humans in the initial population.
     */
    @XmlElement(name = "InitiallyHealthy")
    private int initiallyHealthy;
    
    /**
     * Number of infected humans in the initial population.
     */
    @XmlElement(name = "InitiallyInfected")
    private int initiallyInfected;
    
    /**
     * Number of zombies in the initial population.
     */
    @XmlElement(name = "InitiallyZombified")
    private int initiallyZombified;
    
    //Public Constructors
    public PopulationConfiguration(int initiallyHealthy, int initiallyInfected, int initiallyZombified) {
        this.initiallyHealthy   = initiallyHealthy;
        this.initiallyInfected  = initiallyInfected;
        this.initiallyZombified = initiallyZombified;
    }
    
    public PopulationConfiguration(PopulationConfiguration original) {
        this(original.initiallyHealthy, original.initiallyInfected, original.initiallyZombified);
    }
    
    //Necessary for XML serialization purposes
    private PopulationConfiguration() {
        this(0, 0, 0);
    }
    
    //Public Methods
    /**
     * Returns the number of healthy humans in the initial population.
     * @return the number of healthy humans
     */
    public int getInitiallyHealthy() {
        return this.initiallyHealthy;
    }

    /**
     * Returns the number of infected humans in the initial population.
     * @return the number of infected humans
     */
    public int getInitiallyInfected() {
        return this.initiallyInfected;
    }

    /**
     * Returns the number of zombies in the initial population
     * @return the number of zombies.
     */
    public int getInitiallyZombified() {
        return this.initiallyZombified;
    }

    /**
     * Returns the number of humans (healthy + infected) in the initial population.
     * @return the number of humans
     */
    public int getInitiallyHuman() {
        return this.initiallyHealthy + this.initiallyInfected;
    }
    
    /**
     * Returns the number of agents in the initial population.
     * @return the number of agents
     */
    public int getInitialPopulationCount() {
        return this.initiallyHealthy + this.initiallyInfected + this.initiallyZombified;
    }

    /**
     * Sets the number of healthy humans in the initial population.
     * @param initiallyHealthy the number of healthy humans
     */
    public void setInitiallyHealthy(int initiallyHealthy) {
        this.initiallyHealthy = initiallyHealthy;
    }

    /**
     * Sets the number of infected humans in the initial population.
     * @param initiallyInfected the number of infected humans
     */
    public void setInitiallyInfected(int initiallyInfected) {
        this.initiallyInfected = initiallyInfected;
    }

    /**
     * Sets the number of zombies in the initial population.
     * @param initiallyZombified the number of zombies
     */
    public void setInitiallyZombified(int initiallyZombified) {
        this.initiallyZombified = initiallyZombified;
    }
}
