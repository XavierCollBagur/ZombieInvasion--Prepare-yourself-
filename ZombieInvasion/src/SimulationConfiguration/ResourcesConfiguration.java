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
 * This class contains the configuration parameters of the resources of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "ResourcesConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class ResourcesConfiguration {
    //Attributes
    
    /**
     * Number of total resources available initially.
     */
    @XmlElement(name = "TotalResourcesAvailable")
    private int totalResourcesAvailable;
    
    /**
     * Configuration parameters of vaccination.
     */
    @XmlElement(name = "VaccinationConfiguration")
    private VaccinationConfiguration vaccination;

    /**
     * Configuration parameters of walls. 
     */
    @XmlElement(name = "WallConfiguration")
    private WallConfiguration wall;
    
    /**
     * Configuration parameters of weapons.
     */
    @XmlElement(name = "WeaponConfiguration")
    private WeaponConfiguration weapon;
    
    //Public Constructors
    public ResourcesConfiguration(int totalResourcesAvailable, VaccinationConfiguration vaccinationConfiguration,
                                  WallConfiguration wallConfiguration, WeaponConfiguration weaponConfiguration) {
        
        this.totalResourcesAvailable = totalResourcesAvailable;
        this.vaccination             = vaccinationConfiguration;
        this.wall                    = wallConfiguration;
        this.weapon                  = weaponConfiguration;
    }

    public ResourcesConfiguration(int totalResourcesAvailable, 
                                 int vaccinationKitCost, int vaccinatedPerVaccinationKit,
                                 int wallUnitLength, int wallUnitCost, int zombiesNeededToBreakDownAWall,
                                 int weaponKitCost, int armedPerWeaponKit, int bulletsPerWeapon, int bulletsNeededToKill,
                                 int bulletTrajectoryDiversionDegrees, double zombieWinLoseRatioAgainstArmedHuman) {
        
        this(totalResourcesAvailable,
             new VaccinationConfiguration(vaccinationKitCost, vaccinatedPerVaccinationKit),
             new WallConfiguration(wallUnitLength, wallUnitCost, zombiesNeededToBreakDownAWall),
             new WeaponConfiguration(weaponKitCost, armedPerWeaponKit, bulletsPerWeapon, bulletsNeededToKill,
                                     bulletTrajectoryDiversionDegrees, zombieWinLoseRatioAgainstArmedHuman)
            );
    }
    
    public ResourcesConfiguration(ResourcesConfiguration original) {
        this(original.totalResourcesAvailable, new VaccinationConfiguration(original.vaccination),
             new WallConfiguration(original.wall), new WeaponConfiguration(original.weapon));
    }
     
    //Necessary for XML serialization purposes
    private ResourcesConfiguration() {
        this(0, null, null, null);
    }
    
    //Public Methods 

    /**
     * Returns the number of total resources available initially.
     * @return the resources available initially
     */
    public int getTotalResourcesAvailable() {
        return this.totalResourcesAvailable;
    }

    /**
     * Returns the configuration parameters of vaccination.
     * @return vaccination configuration parameters
     */
    public VaccinationConfiguration getVaccination() {
        return this.vaccination;
    }

    /**
     * Returns the configuration parameters of walls.
     * @return walls configuration parameters
     */
    public WallConfiguration getWall() {
        return this.wall;
    }

    /**
     * Returns the configuration parameters of weapons.
     * @return weapons configuration parameters
     */
    public WeaponConfiguration getWeapon() {
        return this.weapon;
    }

    /**
     * Sets the number of total resources available initially.
     * @param totalResourcesAvailable the resources available initially
     */
    public void setTotalResourcesAvailable(int totalResourcesAvailable) {
        this.totalResourcesAvailable = totalResourcesAvailable;
    }

    /**
     * Sets the configuration parameters of vaccination
     * @param vaccination vaccination configuration parameters
     */
    public void setVaccination(VaccinationConfiguration vaccination) {
        this.vaccination = vaccination;
    }

    /**
     * Sets the configuration parameters of walls.
     * @param wall wall configuration parameters
     */
    public void setWall(WallConfiguration wall) {
        this.wall = wall;
    }

    /**
     * Sets the configuration parameters of weapons.
     * @param weapon weapon configuration parameters
     */
    public void setWeapon(WeaponConfiguration weapon) {
        this.weapon = weapon;
    }
}
