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
 * This class contains the configuration parameters of the weapons of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "WeaponConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class WeaponConfiguration {
    //Attributes
    
    /**
     * Cost of a weapon kit.
     */
    @XmlElement(name = "WeaponKitCost")
    private int weaponKitCost;
    
    /**
     * Number of armed people per weapon kit.
     */
    @XmlElement(name = "ArmedPerWeaponKit")
    private int armedPerWeaponKit;
    
    /**
     * Number of bullets per weapon.
     */
    @XmlElement(name = "BulletsPerWeapon")
    private int bulletsPerWeapon;
    
    /**
     * Number of bullets needed to kill an agent.
     */
    @XmlElement(name = "BulletsNeededToKill")
    private int bulletsNeededToKill;
    
    /**
     * Number of maximum degrees of deviation (for each side) of the trajectory of a bullet.
     */
    @XmlElement(name = "BulletTrajectoryDiversionDegrees")
    private int bulletTrajectoryDiversionDegrees;
    
    /**
     * Chances of a zombie to win a confrontation against an armed human
     */
    @XmlElement(name = "ZombieWinLoseRatioAgainstArmedHuman")
    private double zombieWinLoseRatioAgainstArmedHuman;

    public WeaponConfiguration(int weaponKitCost, int armedPerWeaponKit, int bulletsPerWeapon,
                               int bulletsNeededToKill, int bulletTrajectoryDiversionDegrees,
                               double zombieWinLoseRatioAgainstArmedHuman) {
        
        this.weaponKitCost                       = weaponKitCost;
        this.armedPerWeaponKit                   = armedPerWeaponKit;
        this.bulletsPerWeapon                    = bulletsPerWeapon;
        this.bulletsNeededToKill                 = bulletsNeededToKill;
        this.bulletTrajectoryDiversionDegrees    = bulletTrajectoryDiversionDegrees;
        this.zombieWinLoseRatioAgainstArmedHuman = zombieWinLoseRatioAgainstArmedHuman;
    }
    
    public WeaponConfiguration(WeaponConfiguration original) {
        this(original.weaponKitCost, original.armedPerWeaponKit, original.bulletsPerWeapon,
             original.bulletsNeededToKill, original.bulletTrajectoryDiversionDegrees,
             original.zombieWinLoseRatioAgainstArmedHuman);
    }
    
    //Necessary for XML serialization purposes
    private WeaponConfiguration() {
        this(0, 0, 0, 0, 0, 0);
    }
    
    //Public Methods
    
    /**
     * Returns the cost of a weapon kit.
     * @return the cost of a weapon kit
     */
    public int getWeaponKitCost() {
        return this.weaponKitCost;
    }

    /**
     * Returns the number of armed people per weapon kit.
     * @return the number of armed people per weapon kit
     */
    public int getArmedPerWeaponKit() {
        return this.armedPerWeaponKit;
    }

    /**
     * Returns the number of bullets per weapon. 
     * @return the number of bullets per weapon
     */
    public int getBulletsPerWeapon() {
        return this.bulletsPerWeapon;
    }

    /**
     * Returns the number of bullets needed to kill an agent.
     * @return the number of bullets
     */
    public int getBulletsNeededToKill() {
        return this.bulletsNeededToKill;
    }

    /**
     * Returns the number of maximum degrees of deviation (for each side) of the trajectory of a bullet.
     * @return the number of degrees
     */
    public int getBulletTrajectoryDiversionDegrees() {
        return this.bulletTrajectoryDiversionDegrees;
    }

    /**
     * Returns the chances of a zombie to win a confrontation against an armed human.
     * @return win/lose ratio of a zombie against an armed human
     */
    public double getZombieWinLoseRatioAgainstArmedHuman() {
        return this.zombieWinLoseRatioAgainstArmedHuman;
    }

    /**
     * Sets the cost of a weapon kit.
     * @param weaponKitCost the cost of a weapon kit
     */
    public void setWeaponKitCost(int weaponKitCost) {
        this.weaponKitCost = weaponKitCost;
    }

    /**
     * Sets the number of armed people per weapon kit.
     * @param armedPerWeaponKit the number of armed per kit
     */
    public void setArmedPerWeaponKit(int armedPerWeaponKit) {
        this.armedPerWeaponKit = armedPerWeaponKit;
    }

    /**
     * Sets the number of bullets per weapon.
     * @param bulletsPerWeapon the number of bullets per weapon
     */
    public void setBulletsPerWeapon(int bulletsPerWeapon) {
        this.bulletsPerWeapon = bulletsPerWeapon;
    }

    /**
     * Sets the number of bullets needed to kill an agent.
     * @param bulletsNeededToKill the number of bullets
     */
    public void setBulletsNeededToKill(int bulletsNeededToKill) {
        this.bulletsNeededToKill = bulletsNeededToKill;
    }

    /**
     * Sets the number of maximum degrees of deviation (for each side) of the trajectory of a bullet.
     * @param bulletTrajectoryDiversionDegrees the number of degrees 
     */
    public void setBulletTrajectoryDiversionDegrees(int bulletTrajectoryDiversionDegrees) {
        this.bulletTrajectoryDiversionDegrees = bulletTrajectoryDiversionDegrees;
    }

    /**
     * Sets the chances of a zombie to win a confrontation against an armed human.
     * @param zombieWinLoseRatioAgainstArmedHuman the win/lose ratio of a zombie against an armed human
     */
    public void setZombieWinLoseRatioAgainstArmedHuman(double zombieWinLoseRatioAgainstArmedHuman) {
        this.zombieWinLoseRatioAgainstArmedHuman = zombieWinLoseRatioAgainstArmedHuman;
    }
}
