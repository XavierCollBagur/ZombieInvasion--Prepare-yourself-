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
import Media.Images.ImagesDirectory;
import SimulationConfiguration.SimulationConfiguration;
import SimulationConfigurationFileAdapter.SimulationConfigurationFileAdapter;
import SimulationConfigurationFileAdapter.XMLFileError;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

/**
 * This class represents the main menu of the application.
 * @author Xavier
 */
public class MainMenu extends JPanel {
    //Attributes
    /**
     * The title of the label.
     */
    private JLabel title;
    
    /**
     * The button for configure a new simulation.
     */
    private JButton newSimulationButton;
    
    /**
     * The button for configure a simulation whose values initially are retrieved
     * from a file.
     */
    private JButton newSimulationFromExistingButton;
    
    /**
     * The button for execute a simulation whose configuration is retrieved from a file.
     */
    private JButton openSimulationButton; 
            
    /**
     * The button for stopping the application.
     */
    private JButton quitButton;
    
    //Public Constructors
    public MainMenu() {
        super();
        
        initComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        this.setBackground(Color.WHITE);
    }
    
    //Private Methods
    /**
     * Creates and places all the children components.
     */
    private void initComponents() {
        //Initialize components
        this.title                           = this.createTitleLabel();
        this.newSimulationButton             = new JButton("Nova simulació");
        this.newSimulationFromExistingButton = new JButton("Nova simulació des d'existent");
        this.openSimulationButton            = new JButton("Obrir simulació");
        this.quitButton                      = new JButton("Sortir");
        
        //Set components' properties
        this.setLayout(new GridBagLayout());
        this.setMenuButtonProperties(this.newSimulationButton);
        this.setMenuButtonProperties(this.newSimulationFromExistingButton);
        this.setMenuButtonProperties(this.openSimulationButton);
        this.setMenuButtonProperties(this.quitButton);
        
        //Add buttons' listeneres
        this.newSimulationButton.addActionListener(this.createNewSimulationActionListener());
        this.newSimulationFromExistingButton.addActionListener(this.createNewSimulationFromExistingActionListener());
        this.openSimulationButton.addActionListener(this.createOpenSimulationActionListener());
        this.quitButton.addActionListener(this.createExitActionListener());
        
        //Place elements in panel
        this.add(this.title, this.createConstraints(0, 10));
        this.add(this.newSimulationButton, this.createConstraints(1, 1));
        this.add(this.newSimulationFromExistingButton, this.createConstraints(2, 1));
        this.add(this.openSimulationButton, this.createConstraints(3, 1));
        this.add(this.quitButton, this.createConstraints(4, 1));
    }
    
    /**
     * Creates the label of the title.
     * @return the label
     */
    private JLabel createTitleLabel() {
        JLabel label;
        
        label = new JLabel();
        
        label.setIcon(new ImageIcon(ImagesDirectory.APPLICATION_TITLE_IMAGE));
        
        return label;
    }
    
    /**
     * Changes values of the properties of a button of this section.
     * @param button the button
     */
    private void setMenuButtonProperties(JButton button) {
        button.setPreferredSize(new Dimension(257, 57));
        button.setMinimumSize((Dimension)button.getPreferredSize().clone());
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setFont(button.getFont().deriveFont(18f));
    }
    
    /**
     * Creates a GridBagConstraints object.
     */
    private GridBagConstraints createConstraints(int gridY, int weightY) {
        GridBagConstraints constraints;
        constraints         = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridy   = gridY;
        constraints.weightx = 1;
        constraints.weighty = weightY;
        constraints.insets  = new Insets(2, 2, 2, 2);
        return constraints;
    }
    
    /**
     * Creates the action listener of the button for open a simulation configuration section.
     * @return the action listener
     */
    private ActionListener createNewSimulationActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTabbedPane tabs;
                
                //Obtain the application's tabbed pane
                tabs = (JTabbedPane)MainMenu.this.getParent();
                
                //Create a new tab with a new simulation configuration section and put it
                //in the tabbed pane
                tabs.add("Configuració nova simulació", new EditSimulationConfigurationSection());
                tabs.setTabComponentAt(tabs.getTabCount() - 1, new ButtonTabComponent(tabs));
                tabs.setSelectedIndex(tabs.getTabCount() - 1);
            }
        };
    }
    
    /**
     * Creates the action listener of the button for open a simulation configuration section
     * with the initial vaules retrieved from a file.
     * @return the action listener
     */
    private ActionListener createNewSimulationFromExistingActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationConfiguration config;
                SimulationConfigurationFileAdapter fileAdapter;
                JTabbedPane tabs;
                File selectedFile;
                String fileName;
                
                //Get the file to open
                selectedFile = getFile();
               
                if(selectedFile != null) {
                    //Get the configuration from the file and create a new tab with
                    //a new simulation configuration section
                    try {  
                        fileAdapter = new SimulationConfigurationFileAdapter();    
                        config      = fileAdapter.read(new File(selectedFile.getAbsolutePath()));
                        tabs        = (JTabbedPane)MainMenu.this.getParent();
                        fileName    = selectedFile.getName();

                        tabs.add("Configuració \"" + fileName + "\"", new EditSimulationConfigurationSection(config, fileName));
                        tabs.setTabComponentAt(tabs.getTabCount() - 1, new ButtonTabComponent(tabs));
                        tabs.setSelectedIndex(tabs.getTabCount() - 1);
                    } 
                    catch (XMLFileError ex) {
                        JOptionPane.showMessageDialog(MainMenu.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (JAXBException ex) {
                        JOptionPane.showMessageDialog(MainMenu.this, "Excepció no controlada: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
    }
    
    /**
     * Creates the action listener of the button for executing a simulation whose
     * parameters are retrieved from a file.
     * @return the action listener
     */
    private ActionListener createOpenSimulationActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationConfiguration config;
                SimulationConfigurationFileAdapter fileAdapter;
                JTabbedPane tabs;
                File selectedFile;
                String fileName;
                ButtonTabComponent tabComponent;
                final SimulationSection simulationSection;
                
                //Get the file to open
                selectedFile = getFile();
                
                if(selectedFile != null) {
                    //Get the configuration from the file and create a new tab with
                    //a new simulation execution section. Add a listener when the tab 
                    //is closed to stop the execution of the simulation
                    try {  

                        fileAdapter       = new SimulationConfigurationFileAdapter();    
                        config            = fileAdapter.read(new File(selectedFile.getAbsolutePath()));
                        tabs              = (JTabbedPane)MainMenu.this.getParent();
                        fileName          = selectedFile.getName();
                        simulationSection = new SimulationSection(new SimulationConfiguration(config));
                        tabComponent      = new ButtonTabComponent(tabs);

                        tabs.add("Execució \"" + fileName + "\"", simulationSection);
                        tabs.setTabComponentAt(tabs.getTabCount() - 1, tabComponent);
                        tabs.setSelectedIndex(tabs.getTabCount() - 1);
                        tabComponent.addActionListenerOnCloseTabButton(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                simulationSection.stop();
                            }
                        });
                    } 
                    catch (XMLFileError ex) {
                        JOptionPane.showMessageDialog(MainMenu.this, "El fitxer XML no té el format correcte.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (JAXBException ex) {
                        JOptionPane.showMessageDialog(MainMenu.this, "Excepció no controlada: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
    }
    
    /**
     * Opens a file chooser for opening XML files.
     * @return the file selected
     */
    private File getFile() {
        int fileChooserValue;
        JFileChooser fileChooser;
        File selectedFile;
        
        fileChooser  = new JFileChooser();
        selectedFile = null;
        
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));

        fileChooserValue = fileChooser.showOpenDialog(MainMenu.this);
        
        if(fileChooserValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
      
        return selectedFile;
    }
    
    /**
     * Creates the action litener of the button for stopping the simulation.
     * @return the action listener
     */
    private ActionListener createExitActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
    }
}
