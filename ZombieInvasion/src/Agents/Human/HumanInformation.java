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

import Agents.Base.BaseInformation;

/**
 * Environment's internal information of a human agent 
 * @author Xavier
 */
public class HumanInformation extends BaseInformation {
    //Attributes
    /**
     * Health status of the human.
     */
    private HumanHealthStatus healthStatus;
    
    /**
     * Number of phases that remain the human to become zombie.
     */
    private int phasesToZombify;
    
    /**
     * Number of bullets that the human has.
     */
    private int bullets;
    
    /**
     * Boolean value indicating if the human is vaccinated.
     */
    private boolean vaccinated;

    //Public Constructors
    public HumanInformation(HumanHealthStatus healthStatus, double x, double y, int phasesToZombify, 
                            boolean vaccinated, int bullets) {
        super(x, y);
        this.healthStatus    = healthStatus;
        this.phasesToZombify = phasesToZombify;
        this.vaccinated      = vaccinated;
        this.bullets         = bullets;
    }
    
    public HumanInformation(BaseInformation baseInformation, HumanHealthStatus healthStatus, int phasesToZombify,
                            boolean vaccinated, int bullets) {
        super(baseInformation);
        this.healthStatus    = healthStatus;
        this.phasesToZombify = phasesToZombify;
        this.vaccinated      = vaccinated;
        this.bullets         = bullets;
    }
    
    public HumanInformation(HumanInformation humanInformation) {
        super(humanInformation);
        this.healthStatus    = humanInformation.healthStatus;
        this.phasesToZombify = humanInformation.phasesToZombify;
        this.vaccinated      = humanInformation.vaccinated;
        this.bullets         = humanInformation.bullets;
    }
    
    //Public Methods 
    /**
     * Returns the human's health status.
     * @return the human's health status 
     */
    public HumanHealthStatus getHealthStatus() {
        return this.healthStatus;
    }
    
    /**
     * Returns the number of phases that remain the human to become zombie
     * @return the number of phases remaining
     */
    public int getPhasesToZombify() {
        return this.phasesToZombify;
    }
    
    /**
     * Check if the human is vaccinated
     * @return <code>true</code> if the human is vaccinated, <code>false</code>
     * otherwise
     */
    public boolean isVaccinated() {
        return this.vaccinated;
    }
    
    /**
     * Returns the number of bullet that the human has.
     * @return the number of bullets
     */
    public int getBullets() {
        return this.bullets;
    }
    
    /**
     * Set the current human's health status
     * @param healthStatus the human's health status.
     */
    public void setHealthStatus(HumanHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }
    
    /**
     * Indicate if the human is vaccinated or not.
     * @param vaccinated the boolean value indicating if the human is vaccinated
     */
    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }
    
    /**
     * Set the number of bullets that the human has.
     * @param bullets the number of bullets
     */
    public void setBullets(int bullets) {
        this.bullets = bullets;
    }
    
    /**
     * Decrement one unit the number of the phases that remain the human to become zombie.
     */
    public void decrementOnePhaseToZombify() {
        this.phasesToZombify--;
        
    }
    
    /**
     * Decrement one unit the number of bullets that the human has.
     */
    public void decrementOneBullet() {
        this.bullets--;
    }
}
