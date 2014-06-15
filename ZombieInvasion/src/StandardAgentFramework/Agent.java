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

package StandardAgentFramework;

/**
 * This class represents an intelligent agent.
 * @author Xavier
 */
public interface Agent {
    
    /**
     * Returns the action that the agent must do now.
     * @param perceptions the perceptions the agent perceives now
     * @return the accion the agent will do
     */
    public Action act(Perceptions perceptions);
}
