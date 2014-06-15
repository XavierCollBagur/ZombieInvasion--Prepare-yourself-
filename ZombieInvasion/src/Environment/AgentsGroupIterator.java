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
import java.util.Iterator;

/**
 * AgentsGroup's iterator implementation class
 * @author Xavier
 */
class AgentsGroupIterator implements Iterator<BaseAgent>{
    //Attributes
    private Iterator<HumanAgent> humanIterator;
    private Iterator<ZombieAgent> zombieIterator;
    
    //Public Methods
    public AgentsGroupIterator(AgentsGroup agentsGroup) {
        this.humanIterator  = agentsGroup.getHumans().iterator();
        this.zombieIterator = agentsGroup.getZombies().iterator();
    }
    
    @Override
    public boolean hasNext() {
        return this.humanIterator.hasNext() || this.zombieIterator.hasNext();
    }

    @Override
    public BaseAgent next() {
        BaseAgent next;
        
        if(this.humanIterator.hasNext()) {
            next = this.humanIterator.next();
        }
        else {
            next = this.zombieIterator.next();
        }
        
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation not supported.");
    }
    
}
