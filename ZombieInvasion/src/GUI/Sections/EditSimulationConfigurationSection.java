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

package GUI.Sections;

import GUI.Components.General.ButtonTabComponent;
import GUI.Components.Specific.ConfigurationPane;
import SimulationConfiguration.SimulationConfiguration;
import SimulationConfigurationFileAdapter.SimulationConfigurationFileAdapter;
import SimulationConfigurationFileAdapter.XMLFileError;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

/**
 * This class represents the simulation configuration section of the application.
 * @author Xavier
 */
public class EditSimulationConfigurationSection extends JPanel {
    //Constants
    private final static String DEFAULT_SIMULATION_NAME = "Nova simulació",
                                DEFAULT_RUN_SIMULATION_NAME = "Execució simulació";
    //Attributes
    /**
     * The configuration object.
     */
    private SimulationConfiguration configuration;
    
    /**
     * The name of the simulation.
     */
    private String simulationName;
    
    /**
     * The label of the name's simulation.
     */
    private JLabel simulationNameLabel;
    
    /**
     * The configuration panel of the configuration object.
     */
    private ConfigurationPane configurationPane;
    
    /**
     * The button to run the simulation.
     */
    private JButton runSimulationButton;
    
    /**
     * The button to save the simulation in a file.
     */
    private JButton saveSimulationButton;
    
    //Public Constructors
    public EditSimulationConfigurationSection(SimulationConfiguration configuration, String simulationName) {
        this.configuration  = configuration;
        this.simulationName = simulationName;
        
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.initComponents();
    }
    
    public EditSimulationConfigurationSection() {
        this(getDefaultConfiguration(), DEFAULT_SIMULATION_NAME);
    }
    
    //Private Methods
    
    /**
     * Creates the section title, the configuration panel and the buttons row 
     * and adds them in the section.
     */
    private void initComponents() {
         JComponent northComponent, centralComponent, southComponent;
         
         northComponent   = this.createNorthComponent();
         centralComponent = this.createCentralComponent();
         southComponent   = this.createSouthComponent();
         
         this.setLayout(new BorderLayout());
         this.add(northComponent, BorderLayout.NORTH);
         this.add(centralComponent, BorderLayout.CENTER);
         this.add(southComponent, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the north component of the section: a lable with the title.
     * @return the component
     */
    private JComponent createNorthComponent(){
        Font labelFont;
        
        this.simulationNameLabel = new JLabel(this.createSimulationNameLabelText());
        labelFont                = this.simulationNameLabel.getFont().deriveFont(Font.BOLD, 40);
        
        this.simulationNameLabel.setFont(labelFont);
        
        return this.simulationNameLabel;
    }
    
    /**
     * Creates a label with the title of the simulation.
     * @return the label
     */
    private String createSimulationNameLabelText() {
        String labelName;
        
        if(this.simulationName.equals(DEFAULT_SIMULATION_NAME)) {
            labelName = DEFAULT_SIMULATION_NAME;
        }
        else {
            labelName = "Configuració \"" + this.simulationName + "\"";
        }
        
        return labelName;
    }

    /**
     * Creates the central component of the section: the configuration panel
     * @return the central component
     */
    private JComponent createCentralComponent() {
        this.configurationPane = new ConfigurationPane(this.configuration);

        this.configurationPane.setOpaque(false);
        this.configurationPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        return this.configurationPane;
    }
    
    /**
     * Creates the south component of the section: the row with the section's buttons
     * @return the south component
     */
    private JComponent createSouthComponent() {
        JPanel component;
        
        component                 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        this.runSimulationButton  = createComponentButton("Executa simulació");
        this.saveSimulationButton = createComponentButton("Guarda simulació");
        
        this.runSimulationButton.addActionListener(this.createRunSimulationActionListener());
        this.saveSimulationButton.addActionListener(this.createSaveSimulationActionListener());
        
        component.setOpaque(false);
        component.add(this.saveSimulationButton);
        component.add(this.runSimulationButton);
        
        return component;
    }
    
    /**
     * Create a button of the section.
     * @param text the text of the button
     * @return the button
     */
    private JButton createComponentButton(String text) {
        JButton button;
        
        button = new JButton(text);
        
        button.setFont(button.getFont().deriveFont(12f));
        button.setPreferredSize(new Dimension(140, 40));
        
        return button;
    }
    
    /**
     * Creates the listener of the button for running the simulation.
     * @return the listener
     */
    private ActionListener createRunSimulationActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ButtonTabComponent tabComponent;
                final SimulationSection simulationSection;
                JTabbedPane tabs;
                String tabName;
                
                //Get the tabbed pane and create a simulation section with a copy of the 
                //configuration parameters
                tabs                   = getTabs();
                simulationSection      = new SimulationSection(new SimulationConfiguration(configuration));
                tabComponent           = new ButtonTabComponent(tabs);

                //Get the name of the tab
                if(simulationName.equals(DEFAULT_SIMULATION_NAME)) {
                    tabName = DEFAULT_RUN_SIMULATION_NAME;
                }
                else {
                    tabName = "Execució \"" + simulationName + "\"";
                }
                
                //Create the tab with the simulation section and places it in the 
                //tabbed pane. Add a listener when the tab is closed to stop the
                //execution of the simulation
                tabs.add(tabName, simulationSection);
                tabs.setTabComponentAt(tabs.getTabCount() - 1, tabComponent);
                tabs.setSelectedIndex(tabs.getTabCount() - 1);
                tabComponent.addActionListenerOnCloseTabButton(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        simulationSection.stop();
                    }
                });
            }
        };
    }
    
    /**
     * Creates the listener of the button for saving the simulation configuration in a file.
     * @return the listener
     */
    private ActionListener createSaveSimulationActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser;
                int fileChooserValue;
                String filePath, fileName;
                SimulationConfigurationFileAdapter fileAdapter;
                File selectedFile;
                
                //Create a file chooser for saving a file and shows it
                fileChooser = new JFileChooser();
                
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
                
                fileChooserValue = fileChooser.showSaveDialog(EditSimulationConfigurationSection.this);
                if(fileChooserValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        //Get the name of the selected file
                        selectedFile = fileChooser.getSelectedFile();
                        fileName     = selectedFile.getName();
                        filePath     = selectedFile.getAbsolutePath();
                        fileAdapter  = new SimulationConfigurationFileAdapter();
                        
                        //Force the file to has XML extension
                        if(!hasXMLExtension(filePath)) {
                            filePath += ".xml";
                            fileName += ".xml";
                        }
                        
                        //Writes the simulation configuration in the file
                        fileAdapter.write(configuration, filePath);
                    
                        //Update the name of the simulation with the name of the file
                        simulationName = fileName;
                        
                        simulationNameLabel.setText(createSimulationNameLabelText());
                        getTabs().setTitleAt(getTabs().indexOfComponent(EditSimulationConfigurationSection.this), createSimulationNameLabelText());
                    } catch (JAXBException ex) {
                        JOptionPane.showMessageDialog(EditSimulationConfigurationSection.this, "Excepció no controlada:\n" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }            
                }
            }
            
            private boolean hasXMLExtension(String fileName) {
                return fileName.endsWith(".xml");
            }
        };
    }
    
    /**
     * Returns the tabbed pane of the application
     * @return the tabbed pane
     */
    private JTabbedPane getTabs() {
        return (JTabbedPane)this.getParent();
    }
    
    //Private Static Methods
    /**
     * Creates a configuration object from the default configuration file
     * @return the SimulationConfiguration object
     */
    private static SimulationConfiguration getDefaultConfiguration() {
        SimulationConfigurationFileAdapter fa;
        SimulationConfiguration configuration;
        InputStream defaultConfigurationStream;
        
        defaultConfigurationStream = EditSimulationConfigurationSection.class.getResourceAsStream("/SimulationConfiguration/DefaultSimulationConfiguration.xml");
        
        try {
            fa            = new SimulationConfigurationFileAdapter();
            configuration = fa.read(defaultConfigurationStream);
        } 
        catch (JAXBException | XMLFileError ex) {
            configuration = null;
        }
        
        return configuration;
    }
}
