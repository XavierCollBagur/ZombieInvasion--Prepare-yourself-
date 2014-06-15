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
 * This class represents an abstract handler that can be added to an environment.
 * @author Xavier
 */
public abstract class RunEnvironmentEventsHandler {
    
    /**
     * Function that will be called before the execution of a phase of the execution
     * of an environment.
     */
    public void beforeRunOnePhase() {}
    
    
    /**
     * Function that will be called after the execution of a phase of the execution
     * of a simulation.
     */
    public void afterRunOnePhase() {}
    
    /**
     * Function that will be called when the simulation has reached a final state
     * (a finishing state).
     */
    public void whenFinalStateAchieved() {}
}
