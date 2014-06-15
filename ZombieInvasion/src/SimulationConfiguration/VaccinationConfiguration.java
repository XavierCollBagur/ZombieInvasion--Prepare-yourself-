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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains the configuration parameters of the vaccination of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "VaccinationConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class VaccinationConfiguration {
    //Attributes
    
    /**
     * Cost of a vaccination kit.
     */
    @XmlElement(name = "VaccinationKitCost")
    private int vaccinationKitCost;
    
    /**
     * Number of vaccinated healthy humans per vaccination kit.
     */
    @XmlElement(name = "VaccinatedPerVaccinationKit")
    private int vaccinatedPerVaccinationKit;

    //Public Constructors
    public VaccinationConfiguration(int vaccinationKitCost, int vaccinatedPerVaccinationKit) {
        this.vaccinationKitCost          = vaccinationKitCost;
        this.vaccinatedPerVaccinationKit = vaccinatedPerVaccinationKit;
    }
    
    public VaccinationConfiguration(VaccinationConfiguration original) {
        this(original.vaccinationKitCost, original.vaccinatedPerVaccinationKit);
    }
    
    //Necessary for XML serialization purposes
    private VaccinationConfiguration() {
        this(0, 0);
    }
    
    //Public Methods
    /**
     * Returns the cost of a vaccination kit.
     * @return the cost of a vaccination kit 
     */
    public int getVaccinationKitCost() {
        return this.vaccinationKitCost;
    }

    /**
     * Returns the number of vaccinated people per vaccination kit.
     * @return the number of vaccinated per kit
     */
    public int getVaccinatedPerVaccinationKit() {
        return this.vaccinatedPerVaccinationKit;
    }

    /**
     * Sets the cost of a vaccination kit.
     * @param vaccinationKitCost the cost
     */
    public void setVaccinationKitCost(int vaccinationKitCost) {
        this.vaccinationKitCost = vaccinationKitCost;
    }

    /**
     * Sets the numbe of vaccinated people per vaccination kit.
     * @param vaccinatedPerVaccinationKit the number of vaccinated per kit
     */
    public void setVaccinatedPerVaccinationKit(int vaccinatedPerVaccinationKit) {
        this.vaccinatedPerVaccinationKit = vaccinatedPerVaccinationKit;
    }
}
