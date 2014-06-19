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

package GUI.Components.Specific.EnvironmentRepresentation;

import Environment.ZombieEpidemicEnvironment;
import Environment.ZombieEpidemicEnvironmentInformation;
import Environment.ZombieEpidemicResourcesInformation;
import SimulationConfiguration.EnvironmentConfiguration;
import SimulationConfiguration.SimulationConfiguration;
import StandardAgentFramework.RunEnvironmentEventsHandler;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * This class represents a swing component of the representation of a simulation.
 * This class extends the base environment representation class in order to ensure
 * that the information will be retrieved from an executing simulation.
 * @author Xavier
 */
public class InteractiveZombieEpidemicEnvironmentRepresentation extends ZombieEpidemicEnvironmentRepresentation {
    //Attributes
    /**
     * The simulation object.
     */
    private ZombieEpidemicEnvironment environment;
    
    /**
     * The thread used for automatic execution.
     */
    private Thread runEnvironmentThread;
    
    //Public Constructors
    public InteractiveZombieEpidemicEnvironmentRepresentation(SimulationConfiguration configuration) {
        super(configuration.getEnvironment(), null);
        
        this.environment = new ZombieEpidemicEnvironment(configuration);
        
        this.environment.addRunEnvironmentEventsHandler(new RunEnvironmentEventsHandler() {
            @Override
            public void afterRunOnePhase() {
                //Repaint the component after every phase
                InteractiveZombieEpidemicEnvironmentRepresentation.this.repaint();
            }
        });
    }
    
    //Overriden Methods
    @Override
    protected void paintComponent(Graphics g) {
        //Update the environment information 
        super.setEnvironmentInformation(this.environment.getEnvironmentInformation());
        
        //Call the parent method
        super.paintComponent(g);
    }
    
    @Override
    public void setEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration) {
        //Throw an exception (the envornment configuration can't be set from outside of this class)
        throw new UnsupportedOperationException("Environment configuration can't be modified manually");
    }

    @Override
    public void setEnvironmentInformation(ZombieEpidemicEnvironmentInformation environmentInformation) {
        //Throw an exception (the envornment information can't be set from outside of this class)
        throw new UnsupportedOperationException("Environment information can't be modified manually");
    }

    //Public Methods
    /**
     * Adds a new handler of the execution of the environment.
     * @param handler 
     */
    public void addRunEnvironmentEventsHandler(RunEnvironmentEventsHandler handler) {
        this.environment.addRunEnvironmentEventsHandler(handler);
    }
    
    /**
     * Move the environment one phase: all agents receive the perceptions that they
     * sense, they decide the what action they will do and update the environment
     * with these decisions.
     */
    public void runEnvironmentOnePhase() {
        this.environment.runOnePhase();
        this.repaint();
    }
    
    /**
     * Run the environment automatically. If the environment is currently executing,
     * this method won't do anything.
     */
    public void runEnvironment(){
        if(this.runEnvironmentThread == null) {
            //Create a new thread and start it
            this.runEnvironmentThread = createRunEnvironmentThread();
            
            this.runEnvironmentThread.start();
        }
    }
    
    /**
     * Stop the automatical execution of the environment. If the environment is 
     * currently stopped, this method won't do anything.
     */
    public void stopEnvironment() {
        if(this.runEnvironmentThread != null) {
            this.environment.stop();
            
            this.runEnvironmentThread = null;
        }
    }
    
    /**
     * Checks if the environment is currently executing
     * @return <code>true</code> if the environment is currently executing, 
     * <code>false</code> otherwise
     */
    public boolean isRunning() {
        return this.runEnvironmentThread != null;
    }
    
    /**
     * Buy one vaccination kit (if there are enough resources available).
     * @return <code>true</code> if the kit has been bought, <code>false</code> otherwise
     */
    public boolean buyVaccinationKit() {
        return this.environment.buyVaccinationKit();
    }
    
    /**
     * Buys a number of vaccination kits (if there are enough resources available).
     * @param units the number of vaccination kits
     * @return the number of vaccination kits bought
     */
    public int buyVaccinationKits(int units) {
        return this.environment.buyVaccinationKits(units);
    }
    
    /**
     * Buy one vaccination kit (if there are enough resources available).
     * @return <code>true</code> if the kit has been bought, <code>false</code> otherwise
     */
    public boolean useVaccinationKit() {
        boolean used;
        
        used = this.environment.useVaccinationKit();
        
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    /**
     * Buys one vaccination kit and uses it
     * @return <code>true</code> if a kit has been bought and used, <code>false</code> otherwise
     */
    public boolean buyAndUseVaccinationKit() {
        boolean used;
        
        used = this.environment.buyAndUseVaccinationKit();
        
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    /**
     * Buy a number of wall units (if there are enough resources)
     * @param units the number of walls
     * @return the number of wall units bought
     */
    public int buyWallUnits(int units) {
        return this.environment.buyWallUnits(units);
    }
    
    /**
     * Buys one wall unit.
     * @return <code>true</code> if the wall unit has been bought, <code>false</code> otherwise
     */
    public boolean buyWallUnit() {
        return this.environment.buyWallUnit();
    }
    
    /**
     * Builds a wall between the two ponts (if there's enough wall length available).
     * @param p1 the start point of the wall
     * @param p2 the end point of the wall
     * @param destructible boolean value indicating if the wall is destructible or not
     * @return the length of the wall built
     */
    public double buildWall(Point2D p1, Point2D p2, boolean destructible) {
        double wallLength;
        
        wallLength = this.environment.buildWall(p1, p2, destructible);
        
        if(wallLength > 0) {
            this.repaint();
        }
        
        return wallLength;
    }
    
    /**
     * Buys enough wall length to build a wall and builds it.
     * @param p1 the start point of the wall
     * @param p2 the end point of the wall
     * @param destructible boolean value indicating if the wall is destructible or not
     * @return the length of the wall built
     */
    public double buyAndBuildWall(Point2D p1, Point2D p2, boolean destructible) {
        double wallLength;
        
        wallLength = this.environment.buyAndBuildWall(p1, p2, destructible);
        
        if(wallLength > 0) {
            this.repaint();
        }
        
        return wallLength;
    }
    
    /**
     * Buys a number of weapon kits.
     * @param units the number of kits
     * @return the number of weapon kits bought
     */
    public int buyWeaponKits(int units) {
        return this.environment.buyWeaponKits(units);
    }
    
    /**
     * Buys one weapon kit.
     * @return <code>true</code> if the kit has been bought, <code>false</code> otherwise
     */
    public boolean buyWeaponKit() {
        return this.environment.buyWeaponKit();
    }
    
    /**
     * Uses one weapon kit
     * @return <code>true</code> if the kit has been used, <code>false</code> otherwise
     */
    public boolean useWeaponKit() {
        boolean used;
        
        used = this.environment.useWeaponKit();
    
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    /**
     * Buys one weapon kit and uses it.
     * @return <code>true</code> if a kit has been bought and used, <code>false</code> otherwise
     */
    public boolean buyAndUseWeaponKit() {
        boolean used;
        
        used = this.environment.buyAndUseWeaponKit();
    
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    /**
     * Returns information about the current resources available
     * @return the information about the resources available
     */
    public ZombieEpidemicResourcesInformation getResourcesInformation() {
        return this.environment.getResourcesInformation();
    }
    
    //Private Methods
    /**
     * Create a new thread to execute the environment.
     * @return the thread
     */
    private Thread createRunEnvironmentThread() {
        Thread thread;
        
        thread = new Thread("Run Environment Thread") {
            @Override
            public void run() {
                InteractiveZombieEpidemicEnvironmentRepresentation.this.environment.run(100);
            }
        };
        
        thread.setDaemon(true);
        
        
        return thread;
    }
}
