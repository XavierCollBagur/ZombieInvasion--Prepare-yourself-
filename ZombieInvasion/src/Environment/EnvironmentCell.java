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

import Agents.Base.AgentsUtils;
import Agents.Base.BaseAgent;
import Agents.Human.HumanAgent;
import Agents.Zombie.ZombieAgent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * This class contains the elements of an environment cell
 * @author Xavier
 */
public class EnvironmentCell {
    //Attributes
    /**
     * Group of agents located in the cell.
     */
    private final AgentsGroup agents;
    
    /**
     * List of the destructible walls that cross the cell.
     */
    private final Collection<EnvironmentWall> destructibleWalls; 
    
    //Public Constructors
    public EnvironmentCell() {
        this.agents              = new AgentsGroup();
        this.destructibleWalls   = Collections.synchronizedList(new ArrayList<EnvironmentWall>());
    }
    
    //Public Methods
    /**
     * Returns a list of the humans located in the cell.
     * @return the list of humans
     */
    public Collection<HumanAgent> getHumans() {
        return this.agents.getHumans();
    }
    
    /**
     * Returns a list of the zombies located in the cell.
     * @return the list of zombies
     */
    public Collection<ZombieAgent> getZombies() {
        return this.agents.getZombies();
    }
    
    /**
     * Returns an iterator over both humans and zombies lists
     * @return the iterator
     */
    public Iterator<BaseAgent> getAgentsIterator() {
        return this.agents.iterator();
    }
    
    /**
     * Returns a list of the destructible walls that cross the cell
     * @return the list of destructible walls
     */
    public Collection<EnvironmentWall> getDestructibleWalls() {
        return this.destructibleWalls;
    }
    
    /**
     * Returns the number of humans located in the cell
     * @return the number of humans
     */
    public int getNumberOfHumans() {
        return this.getHumans().size();
    }
    
    /**
     * Returns the number of zombies located in the cell
     * @return the number of zombies
     */
    public int getNumberOfZombies() {
        return this.getZombies().size();
    }
    
    /**
     * Returns the number of destructible walls that cross the cell
     * @return the number of destructible walls
     */
    public int getNumberOfDestructibleWalls() {
        return this.destructibleWalls.size();
    }
    
    /**
     * Add a human agent in the cell
     * @param human the human to add
     */
    public void add(HumanAgent human) {
        this.getHumans().add(human);
    }
    
    /**
     * Add a zombie agent in the cell
     * @param zombie the zombie to add
     */
    public void add(ZombieAgent zombie) {
        this.getZombies().add(zombie);
    }
    
    /**
     * Add an agent in the cell
     * @param agent the agent to add
     */
    public void add(BaseAgent agent) {
        if(AgentsUtils.isHuman(agent)) {
            this.add((HumanAgent)agent);
        }
        else {
            this.add((ZombieAgent)agent);
        }
    }
    
    /**
     * Add a destructible wall in the cell
     * @param wall the destructible wall
     */
    public void add(EnvironmentWall wall) {
        if(wall.isDestructible()) {
            this.destructibleWalls.add(wall);
        } 
    }
    
    /**
     * Remove a human in the cell.
     * @param human the human to remove
     */
    public void remove(HumanAgent human) {
        this.getHumans().remove(human);
    }
    
    /**
     * Remove a zombie in the cell.
     * @param zombie the zombie to remove
     */
    public void remove(ZombieAgent zombie) {
        this.getZombies().remove(zombie);
    }
    
    /**
     * Remove an agent in the cell.
     * @param agent the agent to remove
     */
    public void remove(BaseAgent agent) {
        if(AgentsUtils.isHuman(agent)) {
            this.remove((HumanAgent)agent);
        }
        else {
            this.remove((ZombieAgent)agent);
        }
    }
    
    /**
     * Remove a destructible wall in the cell.
     * @param wall  the wall to remove
     */
    public void remove(EnvironmentWall wall) {
        this.destructibleWalls.remove(wall);
    }
    
    /**
     * Remove all the destructible walls in the cell.
     */
    public void removeAllDestructibleWalls() {
        this.destructibleWalls.clear();
    }
}
