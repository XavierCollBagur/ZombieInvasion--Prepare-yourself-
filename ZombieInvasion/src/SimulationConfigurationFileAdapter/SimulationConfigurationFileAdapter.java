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

package SimulationConfigurationFileAdapter;

import SimulationConfiguration.SimulationConfiguration;
import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

/**
 * This class allows you to write a <code>SimulationConfiguration</code> object
 * in a XML file and vice versa.
 * @author Xavier
 */
public class SimulationConfigurationFileAdapter {
    //Attributes
    /**
     * XML writer.
     */
    private Marshaller marshaller;
    
    /**
     * XML reader.
     */
    private Unmarshaller unmarshaller;
    
    //Public Constructors
    public SimulationConfigurationFileAdapter() throws JAXBException {
        JAXBContext context;
        
        context           = JAXBContext.newInstance(SimulationConfiguration.class);
        this.marshaller   = context.createMarshaller();
        this.unmarshaller = context.createUnmarshaller();
        
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }
    
    /**
     * Writes a <code>SimulationConfiguration</code> object in a XML file.
     * @param config the <code>SimulationConfiguration</code> object.
     * @param filePath the path of the file
     * @throws JAXBException thrown if a problem occurs during the writing of the file
     */
    public void write(SimulationConfiguration config, String filePath) throws JAXBException {
        File file;
        
        file = new File(filePath);
        
        marshaller.marshal(config, file);
    }
    
    /**
     * Reads a <code>SimulationConfiguration</code> object from an input stream 
     * of a valid XML file.
     * @param in the input stream
     * @return the <code>SimulationConfiguration</code> object.
     * @throws XMLFileError thrown if the XML file is not valid or doesn't has all configuration fields
     * @throws JAXBException thrown if another problem occurs during the reading of the file
     */
    public SimulationConfiguration read(InputStream in) throws XMLFileError, JAXBException {
        SimulationConfiguration config;

        config = null;
        
        try {
            config = (SimulationConfiguration)unmarshaller.unmarshal(in);
            
            if(this.hasNullElements(config)) {
                throw new XMLFileError("Error al intentar llegir el fitxer. " + 
                                       "Falten camps de configuració.");
            }
            
        }catch(UnmarshalException ue) {
            throw new XMLFileError("Error al intentar llegir el fitxer. " + 
                                   "Comprovi que no hi ha cap error de sintaxi XML i que les etiquetes tenen el nom correcte.");
        }
        return config;
    }
    public SimulationConfiguration read(File file) throws JAXBException, XMLFileError {
        SimulationConfiguration config;

        config = null;
        
        try {
            config = (SimulationConfiguration)unmarshaller.unmarshal(file);
            
            if(this.hasNullElements(config)) {
                throw new XMLFileError("Error al intentar llegir el fitxer. " + 
                                       "Falten camps de configuració.");
        
            }
        }catch(UnmarshalException ue) {
            throw new XMLFileError("Error al intentar llegir el fitxer. " + 
                                   "Comprovi que no hi ha cap error de sintaxi XML i que les etiquetes tenen el nom correcte.");
        }
        return config;
    }
    
    //Private Methods
    private boolean hasNullElements(SimulationConfiguration config) {
        return config.getEnvironment() == null || config.getHuman() == null 
               || config.getHumanZombieInteraction() == null || config.getPopulation() == null
               || config.getResources() == null || config.getZombieEpidemic() == null
               || config.getResources().getVaccination() == null || config.getResources().getWall() == null
               || config.getResources().getWeapon() == null;
    }
}