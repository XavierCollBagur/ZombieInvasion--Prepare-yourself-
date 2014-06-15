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
 * This class represents all the information that the environment has in a phase of
 * the simulation: the agent object, its perceptions and the action decided.
 * @author Xavier
 */
public class AgentOnePhaseInformation {
    //Attributes
    /**
     * The agent.
     */
    private final Agent agent;
    
    /**
     * The perceptions sensed by the agent in the current phase.
     */
    private final Perceptions perceptions;
    
    /**
     * The action decided by the agent in the current phase. 
     */
    private final Action action;
    
    //Public Constructors
    public AgentOnePhaseInformation(Agent agent, Perceptions perceptions, Action action) {
        this.agent       = agent;
        this.perceptions = perceptions;
        this.action      = action;
    }    
    
    //Public Methods
    
    /**
     * Returns the agent.
     * @return the agent
     */
    public Agent getAgent() {
        return this.agent;
    }

    /**
     * Returns the perceptions sensed by the agent in the current phase.
     * @return the perceptions
     */
    public Perceptions getPerceptions() {
        return this.perceptions;
    }

    /**
     * Returns the action decided by the agent in the current phase. 
     * @return the action
     */
    public Action getAction() {
        return this.action;
    }
    
}
