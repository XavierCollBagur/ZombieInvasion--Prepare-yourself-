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

package SimulationConfiguration;

import Geometry.Cell;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains the configuration parameters of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "SimulationConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulationConfiguration {
    //Attributes
    
    /**
     * Configuration parameters of the environment.
     */
    @XmlElement(name = "Environment")
    private EnvironmentConfiguration environment;
    
    /**
     * Configuration parameters of the population.
     */
    @XmlElement(name = "Population")
    private PopulationConfiguration population;
    
    /**
     * Configuration parameters of the zombie epidemic.
     */
    @XmlElement(name = "ZombieEpidemic")
    private ZombieEpidemicConfiguration zombieEpidemic;
    
    /**
     * Configuration parameters of the human agents.
     */
    @XmlElement(name = "Human")
    private HumanConfiguration human;
    
    /**
     * Configuration parameters of the interaction between humans and zombies.
     */
    @XmlElement(name = "HumanZombieInteraction")
    private HumanZombieInteractionConfiguration humanZombieInteraction;
    
    /**
     * Configuration parameters of the resources.
     */
    @XmlElement(name = "ResourcesConfiguration")
    private ResourcesConfiguration resources;
   
    //Public Constructors
    public SimulationConfiguration(EnvironmentConfiguration environment,
                   PopulationConfiguration population, 
                   ZombieEpidemicConfiguration zombieEpidemic, 
                   HumanConfiguration human, 
                   HumanZombieInteractionConfiguration humanZombieInteraction,
                   ResourcesConfiguration resourcesConfiguration) {
        
        this.environment            = environment;
        this.population             = population;
        this.zombieEpidemic         = zombieEpidemic;
        this.human                  = human;
        this.humanZombieInteraction = humanZombieInteraction;
        this.resources              = resourcesConfiguration;
    }
    
    public SimulationConfiguration(int environmentRows, int environmentColumns, 
          int cellWidth, int cellHeight, int agentWidth, int agentHeight, Collection<Cell> inaccessibleCells,
          int initiallyHealthy, int initiallyInfected, int initiallyZombified, 
          int infectedLatencyPeriod, int zombieSpeed, int zombieSpeedAtRest, int zombieVisionDistance, int zombieOlfactoryDistance,
          int humanSpeed, int humanVisionDistance, 
          double winLoseZombieRatio, double killInfectZombieRatio, double killEscapeHumanRatio,
          int totalResourcesAvailable, int vaccinationKitCost, int vaccinatedPerVaccinationKit,
          int wallUnitLength, int wallUnitCost, int zombiesNeededToBreakDownAWall,
          int weaponKitCost, int armedPerWeaponKit, int bulletsPerWeapon, int bulletsNeededToKill, 
          int bulletTrajectoryDiversionDegrees, double zombieWinLoseRatioAgainstArmedHuman) {
       
        this(new EnvironmentConfiguration(environmentRows, environmentColumns, cellWidth, cellHeight, agentWidth, agentHeight, inaccessibleCells),
             new PopulationConfiguration(initiallyHealthy, initiallyInfected, initiallyZombified),
             new ZombieEpidemicConfiguration(infectedLatencyPeriod, zombieSpeed, zombieSpeedAtRest, zombieVisionDistance, zombieOlfactoryDistance),
             new HumanConfiguration(humanSpeed, humanVisionDistance),
             new HumanZombieInteractionConfiguration(winLoseZombieRatio, killInfectZombieRatio, killEscapeHumanRatio),
             new ResourcesConfiguration(totalResourcesAvailable, vaccinationKitCost, vaccinatedPerVaccinationKit, 
                                        wallUnitLength, wallUnitCost, zombiesNeededToBreakDownAWall,
                                        weaponKitCost, armedPerWeaponKit, bulletsPerWeapon, bulletsNeededToKill,
                                        bulletTrajectoryDiversionDegrees, zombieWinLoseRatioAgainstArmedHuman)
            );
    }
    
    public SimulationConfiguration(SimulationConfiguration original) {
        this(new EnvironmentConfiguration(original.environment),
             new PopulationConfiguration(original.population),
             new ZombieEpidemicConfiguration(original.zombieEpidemic),
             new HumanConfiguration(original.human),
             new HumanZombieInteractionConfiguration(original.humanZombieInteraction),
             new ResourcesConfiguration(original.resources)
            );
    }
    
    //Necessary for XML serialization purposes
    private SimulationConfiguration() {
        this(null, null, null, null, null, null);
    }
    
    //Public Methods
    /**
     * Returns the configuration parameters of the environment.
     * @return the environment configuration parameters
     */
    public EnvironmentConfiguration getEnvironment() {
        return this.environment;
    }

    /**
     * Returns the configuration parameters of the population.
     * @return the population configuration parameters
     */
    public PopulationConfiguration getPopulation() {
        return this.population;
    }

    /**
     * Returns the configuration parameters of the zombie epidemic.
     * @return the zombie epidemic configuration parameters
     */
    public ZombieEpidemicConfiguration getZombieEpidemic() {
        return this.zombieEpidemic;
    }

    
    /**
     * Returns the configuration parameters of the human agents.
     * @return the human agents configuration parameters
     */
    public HumanConfiguration getHuman() {
        return this.human;
    }

    /**
     * Returns the configuration parameters of the interaction between human and zombies.
     * @return the human/zombie interaction configuration parameters
     */
    public HumanZombieInteractionConfiguration getHumanZombieInteraction() {
        return this.humanZombieInteraction;
    }

    /**
     * Returns the configuration parameters of the resources.
     * @return the resources configuration parameters
     */
    public ResourcesConfiguration getResources() {
        return this.resources;
    }
    
    /**
     * Sets the configuration parameters of the environment.
     * @param environment the environment configuration parameters
     */
    public void setEnvironment(EnvironmentConfiguration environment) {
        this.environment = environment;
    }

    /**
     * Sets the configuration parameters of the population.
     * @param population the population configuration parameters
     */
    public void setPopulation(PopulationConfiguration population) {
        this.population = population;
    }

    /**
     * Sets the configuration parameters of the zombie epidemic.
     * @param zombieEpidemic the zombie epidemic configuration parameters
     */
    public void setZombieEpidemic(ZombieEpidemicConfiguration zombieEpidemic) {
        this.zombieEpidemic = zombieEpidemic;
    }

    /**
     * Sets the configuration parameters of the human agents.
     * @param human the human agents configuration parameters
     */
    public void setHuman(HumanConfiguration human) {
        this.human = human;
    }

    /**
     * Sets the configuration parameters of the interaction between humans and zombies.
     * @param humanZombieInteraction the human/zombie interaction configuration parameters
     */
    public void setHumanZombieInteraction(HumanZombieInteractionConfiguration humanZombieInteraction) {
        this.humanZombieInteraction = humanZombieInteraction;
    }

    /**
     * Sets the configuration parameters of the resources.
     * @param resources the resources configuration parameters
     */
    public void setResources(ResourcesConfiguration resources) {
        this.resources = resources;
    }
}
