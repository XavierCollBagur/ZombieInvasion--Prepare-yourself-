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

package Agents.Human;

import Agents.Base.BasePerceptions;
import StandardAgentFramework.Perceptions;

/**
 * This class represents the set of perceptions a human agent perceives.
 * @author Xavier
 */
public class HumanPerceptions extends BasePerceptions implements Perceptions{
    //Attributes
    
    /**
     * Boolean value indicating if the human is vaccinated.
     */
    private final boolean vaccinated;
    
    /**
     * Number of bullets that the human has.
     */
    private final int bullets;
    
    //Public Constructors
    public HumanPerceptions(boolean vaccinated, int bullets) {
        super();
        
        this.vaccinated = vaccinated;
        this.bullets    = bullets;
    }
    
    //Public Methods
    
    /**
     * Check if the human is vaccinated
     * @return <code>true</code> if the human is vaccinated, false otherwise
     */
    public boolean isVaccinated() {
        return this.vaccinated;
    }
    
    /**
     * Returns the number of bullets that the human has.
     * @return the number of bullets
     */
    public int getBullets() {
        return this.bullets;
    }
}
