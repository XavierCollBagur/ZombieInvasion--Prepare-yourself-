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
 * This class contains the configuration parameters of the interaction between
 * humans and zombies of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "HumanZombieConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class HumanZombieInteractionConfiguration {
    //Attributes
    /**
     * Chances of a zombie to win a confrontation against a human.
     */
    @XmlElement(name = "ZombieWinLoseRatio")
    private double zombieWinLoseRatio;
    
    /**
     * Chances of a zombie to kill a human (if he has won the confrontation).
     */
    @XmlElement(name = "ZombieKillInfectRatio")
    private double zombieKillInfectRatio;
    
    /**
     * Chances of a human to kill a zombie (if he has won the confrontation).
     */
    @XmlElement(name = "HumanKillEscapeRatio")
    private double humanKillEscapeRatio;
    
    //Public Constructors

    public HumanZombieInteractionConfiguration(double zombieWinLoseRatio, 
                    double zombieKillInfectRatio, double humanKillEscapeRatio) {
        
        this.zombieWinLoseRatio    = zombieWinLoseRatio;
        this.zombieKillInfectRatio = zombieKillInfectRatio;
        this.humanKillEscapeRatio  = humanKillEscapeRatio;
    }

    public HumanZombieInteractionConfiguration(HumanZombieInteractionConfiguration original) {
        this(original.zombieWinLoseRatio, original.zombieKillInfectRatio, original.humanKillEscapeRatio);
    }
    
    //Necessary for XML serialization purposes
    private HumanZombieInteractionConfiguration() {
        this(0, 0, 0);
    }
    
    //Public Methods
    /**
     * Returns the chances of a zombie to win a confrontation against a human.
     * @return the win/lose ratio of a zombie against a human
     */
    public double getZombieWinLoseRatio() {
        return this.zombieWinLoseRatio;
    }

    /**
     * Returns the chances of a zombie to kill a human (if he has won the confrontation).
     * @return the kill/infect ratio of a zombie when he wins a confrontation
     */
    public double getZombieKillInfectRatio() {
        return this.zombieKillInfectRatio;
    }

    /**
     * Returns the chances of a human to kill a zombie (if he has won the confrontation).
     * @return the kill/escape ratio of a human when he wins a confrontation 
     */
    public double getHumanKillEscapeRatio() {
        return this.humanKillEscapeRatio;
    }

    /**
     * Sets the chances of a zombie to win a confrontation against a human.
     * @param zombieWinLoseRatio the win/lose ratio of a zombie against a human
     */
    public void setZombieWinLoseRatio(double zombieWinLoseRatio) {
        this.zombieWinLoseRatio = zombieWinLoseRatio;
    }

    /**
     * Sets the chances of a zombie to kill a human (if he has won the confrontation).
     * @param zombieKillInfectRatio the kill/infect ratio of a zombie when he wins a confrontation
     */
    public void setZombieKillInfectRatio(double zombieKillInfectRatio) {
        this.zombieKillInfectRatio = zombieKillInfectRatio;
    }

    /**
     * Sets the chances of a human to kill a zombie (if he has won the confrontation).
     * @param humanKillEscapeRatio the kill/infect ratio of a zombie when he wins a confrontation
     */
    public void setHumanKillEscapeRatio(double humanKillEscapeRatio) {
        this.humanKillEscapeRatio = humanKillEscapeRatio;
    }
}
