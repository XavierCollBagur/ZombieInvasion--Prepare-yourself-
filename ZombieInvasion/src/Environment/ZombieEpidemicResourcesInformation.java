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

/**
 * This class contains the information of the available resources at a given moment.
 * @author Xavier
 */
public class ZombieEpidemicResourcesInformation {
    //Attributes
    
    /**
     * Number of resources available.
     */
    private final int totalResourcesAvailable;
    
    /**
     * Number of vaccination kits available.
     */
    private final int vaccinationKitsAvailable;
    
    /**
     * Wall length available.
     */
    private final int wallLengthAvailable;
    
    /**
     * Number of weapon kits available.
     */
    private final int weaponKitsAvailable;
    
    
    //Public Methods
    public ZombieEpidemicResourcesInformation(int totalResourcesAvailable, int vaccinationKitsAvailable,
                                              int wallLengthAvailable, int weaponKitsAvailable) {
        this.totalResourcesAvailable  = totalResourcesAvailable;
        this.vaccinationKitsAvailable = vaccinationKitsAvailable;
        this.wallLengthAvailable      = wallLengthAvailable;
        this.weaponKitsAvailable      = weaponKitsAvailable;
    }

    //Public Methods
    /**
     * Returns the number of resources available.
     * @return the number of resources
     */
    public int getTotalResourcesAvailable() {
        return this.totalResourcesAvailable;
    }

    /**
     * Returns the number of vaccination kits available.
     * @return the number of vaccination kits
     */
    public int getVaccinationKitsAvailable() {
        return this.vaccinationKitsAvailable;
    }
    
    /**
     * Returns the wall length available.
     * @return the wall length
     */
    public int getWallLengthAvailable() {
        return this.wallLengthAvailable;
    }
    
    /**
     * Returns the number of weapon kits available.
     * @return the number of weapon kits
     */
    public int getWeaponKitsAvailable() {
        return this.weaponKitsAvailable;
    }
}
