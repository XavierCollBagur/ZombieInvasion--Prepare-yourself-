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
 *
 * @author Xavier
 */
public class InteractiveZombieEpidemicEnvironmentRepresentation extends ZombieEpidemicEnvironmentRepresentation {
    //Attributes
    private ZombieEpidemicEnvironment environment;
    private Thread runEnvironmentThread;
    
    //Public Constructors
    public InteractiveZombieEpidemicEnvironmentRepresentation(SimulationConfiguration configuration) {
        super(configuration.getEnvironment(), null);
        
        this.environment = new ZombieEpidemicEnvironment(configuration);
        
        this.environment.addRunEnvironmentEventsHandler(new RunEnvironmentEventsHandler() {
            @Override
            public void afterRunOnePhase() {
                InteractiveZombieEpidemicEnvironmentRepresentation.this.repaint();
            }
        });
    }
    
    //Overriden Methods
    @Override
    protected void paintComponent(Graphics g) {
        super.setEnvironmentInformation(this.environment.getEnvironmentInformation());
        super.paintComponent(g);
    }
    
    @Override
    public void setEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration) {
        throw new UnsupportedOperationException("Environment configuration can't be modified manually");
    }

    @Override
    public void setEnvironmentInformation(ZombieEpidemicEnvironmentInformation environmentInformation) {
        throw new UnsupportedOperationException("Environment information can't be modified manually");
    }

    //Public Methods
    public void addRunEnvironmentEventsHandler(RunEnvironmentEventsHandler handler) {
        this.environment.addRunEnvironmentEventsHandler(handler);
    }
    
    public void runEnvironmentOnePhase() {
        this.environment.runOnePhase();
        this.repaint();
    }
    
    public void runEnvironment(){
        if(this.runEnvironmentThread == null) {
            this.runEnvironmentThread = createRunEnvironmentThread();
            
            this.runEnvironmentThread.start();
        }
    }
    
    public void stopEnvironment() {
        if(this.runEnvironmentThread != null) {
            this.environment.stop();
            
            this.runEnvironmentThread = null;
        }
    }
    
    public boolean isRunning() {
        return this.runEnvironmentThread != null;
    }
    
    public boolean buyVaccinationKit() {
        return this.environment.buyVaccinationKit();
    }
    
    public int buyVaccinationKits(int units) {
        return this.environment.buyVaccinationKits(units);
    }
    
    public boolean useVaccinationKit() {
        boolean used;
        
        used = this.environment.useVaccinationKit();
        
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    public boolean buyAndUseVaccinationKit() {
        boolean used;
        
        used = this.environment.buyAndUseVaccinationKit();
        
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    public int buyWallUnits(int units) {
        return this.environment.buyWallUnits(units);
    }
    
    public boolean buyWallUnit() {
        return this.environment.buyWallUnit();
    }
    
    public double buildWall(Point2D p1, Point2D p2, boolean destructible) {
        double wallLength;
        
        wallLength = this.environment.buildWall(p1, p2, destructible);
        
        if(wallLength > 0) {
            this.repaint();
        }
        
        return wallLength;
    }
    
    public double buyAndBuildWall(Point2D p1, Point2D p2, boolean destructible) {
        double wallLength;
        
        wallLength = this.environment.buyAndBuildWall(p1, p2, destructible);
        
        if(wallLength > 0) {
            this.repaint();
        }
        
        return wallLength;
    }
    
    public int buyWeaponKits(int units) {
        return this.environment.buyWeaponKits(units);
    }
    
    public boolean buyWeaponKit() {
        return this.environment.buyWeaponKit();
    }
    
    public boolean useWeaponKit() {
        boolean used;
        
        used = this.environment.useWeaponKit();
    
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    public boolean buyAndUseWeaponKit() {
        boolean used;
        
        used = this.environment.buyAndUseWeaponKit();
    
        if(used) {
            this.repaint();
        }
        
        return used;
    }
    
    public ZombieEpidemicResourcesInformation getResourcesInformation() {
        return this.environment.getResourcesInformation();
    }
    
    //Private Methods
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
