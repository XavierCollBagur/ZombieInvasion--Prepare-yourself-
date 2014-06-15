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

import Agents.Base.BaseAgent;
import Agents.Human.HumanAgent;
import Agents.Zombie.ZombieAgent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a group agents of the zombie epidemic environment separated
 * by its type (human or zombie)
 * @author Xavier
 */
class AgentsGroup implements Iterable<BaseAgent>{
    //Attributes
    /**
     * List of the humans of the group.
     */
    private final ArrayList<HumanAgent> humans;
    
    /**
     * List of the zombies of the group.
     */
    private final ArrayList<ZombieAgent> zombies;
    
    //Attributes 
    public AgentsGroup() {
        this.humans = new ArrayList<>();
        this.zombies = new ArrayList<>();
    }
    
    //Overriden Methods
    /**
     * Returns an iterator of the agents of the group.
     * @return the iterator of the group
     */
    @Override
    public Iterator<BaseAgent> iterator() {
        return new AgentsGroupIterator(this);
    }
    
    //Public Methods
    /**
     * Returns the humans of the group.
     * @return a list of the humans of the group
     */
    public ArrayList<HumanAgent> getHumans() {
        return humans;
    }

    /**
     * Returns the zombies of the group.
     * @return a list of the zombies of the group
     */
    public ArrayList<ZombieAgent> getZombies() {
        return zombies;
    }
}
