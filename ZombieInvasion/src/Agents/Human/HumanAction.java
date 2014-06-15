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

import Agents.Base.ActionType;
import Agents.Base.BaseAction;

/**
 * This class contains all the methods to define an allowed human action
 * @author Xavier
 */
public class HumanAction extends BaseAction {
    //Public Constructors
    public HumanAction() {
        super();
    }
  
    //Public Methods
    /**
     * Set the action to shooting in a direction
     * @param directionX the X component of the direction
     * @param directionY the Y component of the direction
     */
    public void setShoot(double directionX, double directionY) {
        this.actionType = ActionType.Shoot;
        
        this.direction.setDirection(directionX, directionY);
    }
}
