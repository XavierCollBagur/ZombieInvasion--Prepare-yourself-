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

package Agents.Base;

import Agents.Human.HumanAction;
import Agents.Human.HumanAgent;
import Agents.Human.HumanInformation;
import Agents.Human.HumanPerceptions;
import Agents.Zombie.ZombieAction;
import Agents.Zombie.ZombieAgent;
import Agents.Zombie.ZombieInformation;
import Agents.Zombie.ZombiePerceptions;
import StandardAgentFramework.Agent;

/**
 * Provides a set of statics methods related to zombie epidemic agents
 * @author Xavier
 */
public class AgentsUtils {
    //Public Static Methods
    
    /**
     * Check if an <code>Agent</code> object is an instance of <code>HumanAgent</code> class
     * @param agent an <code>Agent</code> instance
     * @return <code>true</code> if the <code>Agent</code> object is an instance of 
     * <code>HumanAgent</code> class, <code>false</code> otherwise
     */
    public static boolean isHuman(Agent agent) {
        return agent instanceof HumanAgent;
    }
    
    /**
     * Check if an <code>Agent</code> object is an instance of <code>ZombieAgent</code> class
     * @param agent an <code>Agent</code> instance
     * @return <code>true</code> if the <code>Agent</code> object is an instance of 
     * <code>ZombieAgent</code> class, <code>false</code> otherwise
     */
    public static boolean isZombie(Agent agent) {
        return agent instanceof ZombieAgent;
    }
    
    /**
     * Check if a <code>BasePerceptions</code> object is an instance of <code>HumanPerceptions</code> class
     * @param perceptions a <code>BasePerceptions</code> instance
     * @return <code>true</code> if the <code>BasePerceptions</code> object is an instance of 
     * <code>HumanPerceptions</code> class, <code>false</code> otherwise
     */
    public static boolean isHumanPerceptions(BasePerceptions perceptions) {
        return perceptions instanceof HumanPerceptions;
    }
    
    /**
     * Check if a <code>BasePerceptions</code> object is an instance of <code>ZombiePerceptions</code> class
     * @param perceptions a <code>BasePerceptions</code> instance
     * @return <code>true</code> if the <code>BasePerceptions</code> object is an instance of 
     * <code>ZombiePerceptions</code> class, <code>false</code> otherwise
     */
    public static boolean isZombiePerceptions(BasePerceptions perceptions) {
        return perceptions instanceof ZombiePerceptions;
    }
    
    /**
     * Check if a <code>BaseAction</code> object is an instance of <code>HumanAction</code> class
     * @param action a <code>BaseAction</code> instance
     * @return <code>true</code> if the <code>BaseAction</code> object is an instance of 
     * <code>HumanAction</code> class, <code>false</code> otherwise
     */
    public static boolean isHumanAction(BaseAction action) {
        return action instanceof HumanAction;
    }
    
    /**
     * Check if a <code>BaseAction</code> object is an instance of <code>ZombieAction</code> class
     * @param action a <code>BaseAction</code> instance
     * @return <code>true</code> if the <code>BaseAction</code> object is an instance of 
     * <code>ZombieAction</code> class, <code>false</code> otherwise
     */
    public static boolean isZombieAction(BaseAction action) {
        return action instanceof ZombieAction;
    }
    
    /**
     * Check if a <code>BaseInformation</code> object is an instance of <code>HumanInformation</code> class
     * @param information a <code>BaseInformation</code> instance
     * @return <code>true</code> if the <code>BaseInformation</code> object is an instance of 
     * <code>HumanInformation</code> class, <code>false</code> otherwise
     */
    public static boolean isHumanInformation(BaseInformation information) {
        return information instanceof HumanInformation;
    }
    
    /**
     * Check if a <code>BaseInformation</code> object is an instance of <code>ZombieInformation</code> class
     * @param information a <code>BaseInformation</code> instance
     * @return <code>true</code> if the <code>BaseInformation</code> object is an instance of 
     * <code>ZombieInformation</code> class, <code>false</code> otherwise
     */
    public static boolean isZombieInformation(BaseInformation information) {
        return information instanceof ZombieInformation;
    }
}
