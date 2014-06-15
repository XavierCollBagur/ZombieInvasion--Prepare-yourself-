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

package Environment;

import Agents.Base.BaseInformation;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Collections;

/**
 * This class contains the information of the state of the environment at a given moment.
 * @author Xavier
 */
public class ZombieEpidemicEnvironmentInformation {
    //Attributes
    
    /**
     * List of the information of the alive population.
     */
    private final Collection<BaseInformation> alivePopulation;
    
    /**
     * List of the information of the dead population.
     */
    private final Collection<BaseInformation> deadPopulation;
    
    /**
     * List of the walls in the environment.
     */
    private final Collection<EnvironmentWall> walls;
    
    /**
     * List of shots in the environment.
     */
    private final Collection<Line2D> shots;

    //Public Constructors
    public ZombieEpidemicEnvironmentInformation(Collection<BaseInformation> deadPopulation, 
                                                Collection<BaseInformation> alivePopulation,
                                                Collection<EnvironmentWall> walls,
                                                Collection<Line2D> shots) {
        
        this.deadPopulation       = deadPopulation;
        this.alivePopulation      = alivePopulation;
        this.walls                = walls;
        this.shots                = shots;
    }
    
    public ZombieEpidemicEnvironmentInformation() {
        this(Collections.<BaseInformation>emptyList(), Collections.<BaseInformation>emptyList(), 
             Collections.<EnvironmentWall>emptyList(), Collections.<Line2D>emptyList());
    }
    
    //Public Methods
    /**
     * Returns a list of the dead population.
     * @return the list of dead population
     */
    public Collection<BaseInformation> getDeadPopulation() {
        return this.deadPopulation;
    }

    /**
     * Returns a list of the alive population.
     * @return the list of alive population
     */
    public Collection<BaseInformation> getAlivePopulation() {
        return this.alivePopulation;
    }

    /**
     * Returns a list of the walls in the environment.
     * @return the list of walls
     */
    public Collection<EnvironmentWall> getWalls() {
        return this.walls;
    }

    /**
     * Returns a list of shots in the environment.
     * @return the list of shots
     */
    public Collection<Line2D> getShots() {
        return this.shots;
    }
}
