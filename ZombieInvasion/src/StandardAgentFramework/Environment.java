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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents the environment of a simulation based on intelligent agents.
 * @author Xavier
 */
public abstract class Environment {
    //Attributes
    /**
     * List of handlers of the execution of the environment.
     */
    private final ArrayList<RunEnvironmentEventsHandler> eventsHandler;
    
    /**
     * Boolean flag used to stop manually the current execution of the environment.
     */
    private volatile boolean stop;
    
    //PublicConstructors
    public Environment() {
        this.eventsHandler = new ArrayList<>();
    }
    
    //Abstract Methods
    /**
     * Returns a list of the agents in the environment. 
     * @return a list of the agents.
     */
    protected abstract Collection<Agent> getAgents();
    
    /**
     * Returns the perceptions of the environment that an agent senses.
     * @param agent the agent
     * @return the perceptions sensed by the agent
     */
    protected abstract Perceptions getPerceptions(Agent agent);
    
    /**
     * Update the environment with the information of the agents.
     * @param agents list of the information of each agent in the environment
     */
    protected abstract void update(Collection<AgentOnePhaseInformation> agents);
    
    /**
     * Checks if the simulation has reached a final state (a termination state).
     * @return <code>true</code> if the simulation has finished, <code>false</code>
     * otherwise
     */
    protected abstract boolean finalStateAchieved();
    
    //Public Methods
    /**
     * Adds a new handler of the execution of the environment.
     * @param handler 
     */
    public void addRunEnvironmentEventsHandler(RunEnvironmentEventsHandler handler) {
        this.eventsHandler.add(handler);
    }
    
    /**
     * Move the environment one phase: all agents receive the perceptions that they
     * sense, they decide the what action they will do and update the environment
     * with these decisions.
     */
    public void runOnePhase() {
        Collection<AgentOnePhaseInformation> agentsInformation;
        ArrayDeque<Perceptions> agentsPerceptions;
        Collection<Agent> agents;
        Perceptions perceptions;
        AgentOnePhaseInformation agentInformation;
        Action action;
       
        agentsPerceptions = new ArrayDeque<>();
        agentsInformation = new ArrayDeque<>();
        
        //Get a list of the agents in the environment
        agents = this.getAgents();
        
        //For each agent, obtain its perceptions
        for(Agent agent: agents) {
            perceptions = this.getPerceptions(agent);
            
            agentsPerceptions.add(perceptions);
        }
        
        //Give to each agent its perceptions and save the action it decided to do 
        for(Agent agent: agents) {
            perceptions      = agentsPerceptions.poll();
            action           = agent.act(perceptions);
            agentInformation = new AgentOnePhaseInformation(agent, perceptions, action);
            
            agentsInformation.add(agentInformation);
        }
        
        //Update the environment with the information of each agent (its perceptions and its action)
        this.update(agentsInformation);
    }
    
    /**
     * Move the environment automatically until the simulation is finished or the user
     * stops it manually (with the <code>stop</code> function). 
     * @param millisBetweenPhases the minimum number of milliseconds between phases
     */
    public void run(long millisBetweenPhases) {
        long time, sleepTime;
        
        this.stop = false;
        
        while(!stop && !this.finalStateAchieved()) {
            time = - System.currentTimeMillis();
            
            //Execute the handlers before running the phase
            for(RunEnvironmentEventsHandler handler: this.eventsHandler) {
                handler.beforeRunOnePhase();
            }
            
            //Run the environment one phase
            this.runOnePhase();
            
            //Execute the handlers after running the phase
            for(RunEnvironmentEventsHandler handler: this.eventsHandler) {
                handler.afterRunOnePhase();
            }
            
            time += System.currentTimeMillis();
            
            //Sleep the thread if necessary 
            sleepTime = millisBetweenPhases - time;
                        
            if(sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {}
            }
        }
        
        //Execute handlers if necessary
        if(this.finalStateAchieved()) {
            for(RunEnvironmentEventsHandler handler: this.eventsHandler) {
                handler.whenFinalStateAchieved();
            }
        }
    }
    
    /**
     * Stop the environment execution manually.
     */
    public void stop() {
        this.stop = true;
    }
}
