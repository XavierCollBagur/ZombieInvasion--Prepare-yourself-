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

import Environment.ZombieEpidemicResourcesInformation;
import GUI.Components.Specific.EnvironmentRepresentation.Anchor;
import GUI.Components.Specific.EnvironmentRepresentation.InteractiveZombieEpidemicEnvironmentRepresentation;
import Media.Images.ImagesDirectory;
import SimulationConfiguration.ResourcesConfiguration;
import SimulationConfiguration.SimulationConfiguration;
import StandardAgentFramework.RunEnvironmentEventsHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * This class represents the simulation execution section of the application.
 * @author Xavier
 */
public class SimulationSection extends JPanel {
    //Constants
    private final static Color AVAILABLE_RESOURCES_COLOR     = new Color(0x5CB85C),
                               NOT_AVAILABLE_RESOURCES_COLOR = Color.RED;
    
    //Attributes
    /**
     * The configuration object with the values used in the execution.
     */
    private final SimulationConfiguration configuration;
    
    /**
     * Boolean value indicating if the wall construction mode is active.
     */
    private boolean inWallConstructionMode;
    
    /**
     * Last point of the simulation's environment that the user clicked with the 
     * wall construction mode active.
     */
    private Point2D lastEnvironmentPoint;
    
    /**
     * The component of the simulation.
     */
    private InteractiveZombieEpidemicEnvironmentRepresentation environmentRepresentation;
    
    private JLabel reproductionLabel, restartLabel, buyVaccinesLabel, buyWeaponsLabel, wallModeLabel, 
                   totalResourcesAvailableLabel;
    private JButton reproductionButton, restartButton, 
                    buyVaccinesButton, buyWeaponsButton, wallModeButton;
    
    //Public Constructors
    public SimulationSection(SimulationConfiguration config) {
        super();
        
        this.configuration          = config;
        this.inWallConstructionMode = false;
        this.lastEnvironmentPoint   = null;
        
        initComponents(config);
    }

    /**
     * Stops the current execution of the simulation.
     */
    public void stop() {
        this.environmentRepresentation.stopEnvironment();
    }
    
    //Private Methods
    /**
     * Creates and adds into the container the simulation component and the options panel.
     * @param config the configuration object
     */
    private void initComponents(SimulationConfiguration config) {
        JPanel optionsPanel;
        
        this.setLayout(new BorderLayout());
        
        optionsPanel                   = this.createOptionsPanel(config.getResources());
        this.environmentRepresentation = createEnvironmentRepresentation(config);
        
        this.add(optionsPanel, BorderLayout.WEST);
        this.add(this.environmentRepresentation, BorderLayout.CENTER);
    }
    
    /**
     * Creates the panel with the options buttons.
     * @param configuration the configuraiton object
     * @return the panel
     */
    private JPanel createOptionsPanel(ResourcesConfiguration configuration) {
        final int vaccinatedPerVaccinationKit, armedPerWeaponKit, vaccinationKitCost, 
                  weaponKitCost, wallUnitCost, totalResourcesAvailable;
        String vaccineText, weaponText, wallText;
        JPanel optionsPanel;
        GridBagConstraints constraints;
        JSeparator verticalSeparator, horizontalSeparator;
         
        //Create the components
        totalResourcesAvailable           = configuration.getTotalResourcesAvailable();
        vaccinatedPerVaccinationKit       = configuration.getVaccination().getVaccinatedPerVaccinationKit();
        armedPerWeaponKit                 = configuration.getWeapon().getArmedPerWeaponKit();
        vaccinationKitCost                = configuration.getVaccination().getVaccinationKitCost();
        weaponKitCost                     = configuration.getWeapon().getWeaponKitCost();
        wallUnitCost                      = configuration.getWall().getWallUnitCost();
        vaccineText                       = "Vacuna " + vaccinatedPerVaccinationKit + " persones (-" + vaccinationKitCost +  "€)";
        weaponText                        = "Arma " + armedPerWeaponKit + " persones (-" + weaponKitCost +  "€)";
        wallText                          = "Entrar mode construcció (-" + wallUnitCost +  "€/metre)";
        optionsPanel                      = new JPanel(new GridBagLayout());
        this.totalResourcesAvailableLabel = this.createTotalResourcesAvailableLabel(totalResourcesAvailable); 
        this.reproductionButton           = this.createOptionButton("Reprodueix", ImagesDirectory.PLAY_IMAGE);
        this.reproductionLabel            = this.createOptionLabel("Reprodueix");
        this.restartButton                = this.createOptionButton("Reinicia", ImagesDirectory.RESTART_IMAGE);
        this.restartLabel                 = this.createOptionLabel("Reinicia");
        this.buyVaccinesButton            = this.createOptionButton(vaccineText, ImagesDirectory.VACCINE_IMAGE);
        this.buyVaccinesLabel             = this.createOptionLabel(vaccineText);
        this.buyWeaponsButton             = this.createOptionButton(weaponText, ImagesDirectory.GUN_IMAGE);
        this.buyWeaponsLabel              = this.createOptionLabel(weaponText);
        this.wallModeButton               = this.createOptionButton(wallText, ImagesDirectory.WALL_IMAGE);
        this.wallModeLabel                = this.createOptionLabel(wallText);
        verticalSeparator                 = new JSeparator(SwingConstants.VERTICAL);
        horizontalSeparator               = new JSeparator(SwingConstants.HORIZONTAL);
        
        //Add listeners to the components
        this.reproductionButton.addActionListener(this.createPlayPauseEnvironmentActionListener());
        this.restartButton.addActionListener(this.createRestartEnvironmentActionListener());
        this.buyVaccinesButton.addActionListener(this.createBuyVaccinationKitActionListener());
        this.buyWeaponsButton.addActionListener(this.createBuyWeaponKitActionListener());
        this.wallModeButton.addActionListener(this.createWallModeActionListener());
        
        //Add the components into the container
        optionsPanel.add(this.reproductionButton, this.createOptionButtonConstraints(0, 0));
        optionsPanel.add(this.reproductionLabel, this.createOptionLabelConstraints(0, 1));
        
        optionsPanel.add(this.restartButton, this.createOptionButtonConstraints(1, 0));
        optionsPanel.add(this.restartLabel, this.createOptionLabelConstraints(1, 1));
        
        constraints            = new GridBagConstraints();
        constraints.gridx      = 0;
        constraints.gridy      = 2;
        constraints.gridwidth  = 3;
        constraints.fill       = GridBagConstraints.HORIZONTAL;
        optionsPanel.add(horizontalSeparator, constraints);
        
        constraints           = new GridBagConstraints();
        constraints.gridx     = 0;
        constraints.gridy     = 3;
        constraints.gridwidth = 3;
        constraints.anchor    = GridBagConstraints.CENTER;
        constraints.insets    = new Insets(25, 0, 0, 0);
        optionsPanel.add(this.totalResourcesAvailableLabel, constraints);
        
        optionsPanel.add(this.buyVaccinesButton, this.createOptionButtonConstraints(0, 4));
        optionsPanel.add(this.buyVaccinesLabel, this.createOptionLabelConstraints(0, 5));
        
        optionsPanel.add(this.buyWeaponsButton, this.createOptionButtonConstraints(1, 4));
        optionsPanel.add(this.buyWeaponsLabel, this.createOptionLabelConstraints(1, 5));
        
        optionsPanel.add(this.wallModeButton, this.createOptionButtonConstraints(2, 4));
        optionsPanel.add(this.wallModeLabel, this.createOptionLabelConstraints(2, 5));
        
        constraints            = new GridBagConstraints();
        constraints.gridx      = 3;
        constraints.gridy      = 0;
        constraints.gridheight = 8;
        constraints.fill       = GridBagConstraints.VERTICAL;
        optionsPanel.add(verticalSeparator, constraints);
        
        
        constraints            = new GridBagConstraints();
        constraints.gridx      = 0;
        constraints.gridy      = 7;
        constraints.weighty    = 1;
        optionsPanel.add(new JLabel(), constraints);
        
        
        return optionsPanel;
    }
    
    /**
     * Creates the label of the number of resources available.
     * @param totalResourcesAvailable the number of resources avalable
     * @return the label
     */
    private JLabel createTotalResourcesAvailableLabel(int totalResourcesAvailable) {
        JLabel label;
        Dimension textSize;
        FontMetrics labelFontMetrics;
        
        label = new JLabel(totalResourcesAvailable + "€");
        
        label.setFont(label.getFont().deriveFont(Font.BOLD, 30));
        
        labelFontMetrics = label.getFontMetrics(label.getFont());
        textSize         = new Dimension(labelFontMetrics.stringWidth(label.getText()), 
                                         labelFontMetrics.getHeight());   
        
        label.setForeground(AVAILABLE_RESOURCES_COLOR);
        label.setPreferredSize(textSize);
        label.setMinimumSize(textSize);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        
        return label;
    }
    
    /**
     * Creates a button of the options panel.
     * @param buttonText the text of the action of the button
     * @param buttonImage the image to display in the button
     * @return the button
     */
    private JButton createOptionButton(String buttonText, Image buttonImage) {
        JButton optionButton;
  
        optionButton = new JButton(new ImageIcon(buttonImage));
        
        optionButton.setPreferredSize(new Dimension(60, 60));
        optionButton.setMinimumSize(new Dimension(60, 60));
        optionButton.setMargin(new Insets(0, 0, 0, 0));
        optionButton.setToolTipText(buttonText);
        
        return optionButton;
    }
    
    /**
     * Creates a label describing the action of a option button.
     * @param labelText the text of the label
     * @return the label
     */
    private JLabel createOptionLabel(String labelText) {
        JLabel optionLabel;
        
        optionLabel = new JLabel("<html><center>" + labelText + "</center></html>");
        
        optionLabel.setPreferredSize(new Dimension(60, 50));
        optionLabel.setMinimumSize(new Dimension(60, 50));
        optionLabel.setHorizontalAlignment(JLabel.CENTER);
        optionLabel.setVerticalAlignment(JLabel.TOP);
        optionLabel.setFont(optionLabel.getFont().deriveFont(10f));
        
        return optionLabel;
    }
    
    /**
     * Create a GridBagConstraints object for placing a option button.
     * @param gridX the column of the button
     * @param gridY the row of the button
     * @return the GridBagConstraints object
     */
    private GridBagConstraints createOptionButtonConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints        = this.createOptionElementConstraints(gridX, gridY); 
        constraints.insets = new Insets(20, 5, 0, 5);
        
        return constraints;
    }
    
    /**
     * Create a GridBagConstraints object for placing a option label.
     * @param gridX the column of the label
     * @param gridY the row of the label
     * @return the GridBagConstraints object
     */
    private GridBagConstraints createOptionLabelConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints        = this.createOptionElementConstraints(gridX, gridY); 
        constraints.insets = new Insets(0, 5, 0, 5);
        
        return constraints;
    }
    
    /**
     * General function to create a GridBagConstraints object for placing a option element.
     * @param gridX the column of the element
     * @param gridY the row of the element
     * @return the GridBagConstraints object
     */
    private GridBagConstraints createOptionElementConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        
        return constraints;
    }
    
    /**
     * Creates the listener of the reproduction button.
     * @return the listener
     */
    private ActionListener createPlayPauseEnvironmentActionListener() {
        return new ActionListener() {
            boolean isRunning = false;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                this.isRunning = !this.isRunning;
                
                if(this.isRunning) {
                    //Starts the execution
                    reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PAUSE_IMAGE));
                    reproductionLabel.setText("Pausa");
                    environmentRepresentation.runEnvironment();
                }
                else {
                    //Stops the execution
                    reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PLAY_IMAGE));
                    reproductionLabel.setText("Reprodueix");
                    environmentRepresentation.stopEnvironment();
                }
                
            }
        };
    }
    
    /**
     * Creates the listener of the restart button
     * @return the listener
     */
    private ActionListener createRestartEnvironmentActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationSection thisPanel;
                
                thisPanel = SimulationSection.this;
                
                if(thisPanel.environmentRepresentation.isRunning()) {
                    //Before restarting the simulation, it has to be stopped
                    thisPanel.reproductionButton.doClick();
                }
                
                //Remove the simulation component
                thisPanel.remove(thisPanel.environmentRepresentation);
                
                //Create a new simulation component and add into the container
                thisPanel.environmentRepresentation = thisPanel.createEnvironmentRepresentation(thisPanel.configuration);
                
                thisPanel.add(thisPanel.environmentRepresentation, BorderLayout.CENTER);
                
                //Update the option panel state
                thisPanel.updateResourcesInformation();
                
                if(thisPanel.inWallConstructionMode) {
                    thisPanel.wallModeButton.doClick();
                }
                
                thisPanel.validate();
            }
        };
    }
    
    /**
     * Creates the listener of the button for buy and use vaccines.
     * @return the listener
     */
    private ActionListener createBuyVaccinationKitActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean bought;
                
                //Buy and use a vaccination kit
                bought = SimulationSection.this.environmentRepresentation.buyAndUseVaccinationKit();
                
                if(bought) {
                    //Update the options panel
                    SimulationSection.this.updateResourcesInformation();
                }
            }
        };
    }
    
    /**
     * Creates the listener of the button for buy and use weapons.
     * @return the listener
     */
    private ActionListener createBuyWeaponKitActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean bought;
                
                //Buy and use a weaponkit
                bought = SimulationSection.this.environmentRepresentation.buyAndUseWeaponKit();
                
                if(bought) {
                    //Update the options panel
                    SimulationSection.this.updateResourcesInformation();
                }
            }
        };
    }
    
    /**
     * Create the listener of the button for activating and deactivating the construction mode.
     * @return the listener
     */
    private ActionListener createWallModeActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int wallUnitCost;
                String labelText;
                Image buttonImage;
                
                inWallConstructionMode = !inWallConstructionMode;
                wallUnitCost           = configuration.getResources().getWall().getWallUnitCost();
                
                if(inWallConstructionMode) {
                    //Enter the wall construction mode
                    lastEnvironmentPoint = null;
                    buttonImage          = ImagesDirectory.WALL_FORBIDDEN_IMAGE;
                    labelText            = "Sortir mode construcció (-" + wallUnitCost +  "€/metre)";
                }
                else {
                    //Exit the wall construction mode
                    buttonImage = ImagesDirectory.WALL_IMAGE;
                    labelText   = "Entrar mode construcció (-" + wallUnitCost +  "€/metre)";
                }
                
                //Update the construction mode button and text according the actived mode
                wallModeButton.setIcon(new ImageIcon(buttonImage));
                wallModeButton.setToolTipText(labelText);
                wallModeLabel.setText("<html><center>" + labelText + "</center></html>");
            }
        };
    }
    
    /**
     * Update the resources options.
     */
    private void updateResourcesInformation() {
        final int vaccinationKitCost, weaponKitCost, wallUnitCost, resourcesAvailable;
        boolean canBuyVaccinationKit, canBuyWeaponKit, canBuyWallUnit;
        ZombieEpidemicResourcesInformation resourcesInformation;
        
        vaccinationKitCost   = this.configuration.getResources().getVaccination().getVaccinationKitCost();
        weaponKitCost        = this.configuration.getResources().getWeapon().getWeaponKitCost();
        wallUnitCost         = this.configuration.getResources().getWall().getWallUnitCost();
        resourcesInformation = this.environmentRepresentation.getResourcesInformation();
        resourcesAvailable   = resourcesInformation.getTotalResourcesAvailable();
        canBuyVaccinationKit = resourcesAvailable >= vaccinationKitCost;
        canBuyWeaponKit      = resourcesAvailable >= weaponKitCost;
        canBuyWallUnit       = resourcesAvailable >= wallUnitCost;
        
        //Update the total resources available and disable buttons of resources that can't be bought
        this.totalResourcesAvailableLabel.setText(resourcesAvailable + "€");
        this.buyVaccinesButton.setEnabled(canBuyVaccinationKit);
        this.buyWeaponsButton.setEnabled(canBuyWeaponKit);
        this.wallModeButton.setEnabled(canBuyWallUnit);
        
        if(!canBuyVaccinationKit && !canBuyWeaponKit && !canBuyWallUnit) {
            //The user can't buy any resource
            
            if(this.inWallConstructionMode) {
                //Exit the wall construction mode
                this.wallModeButton.doClick();
            }
            
            this.totalResourcesAvailableLabel.setForeground(NOT_AVAILABLE_RESOURCES_COLOR);
        }
        else {
            this.totalResourcesAvailableLabel.setForeground(AVAILABLE_RESOURCES_COLOR);
        }
    }
    
    /**
     * Creates a simulation component
     * @param config the configuration object of the simulation
     * @return the component
     */
    private InteractiveZombieEpidemicEnvironmentRepresentation createEnvironmentRepresentation(SimulationConfiguration config) {
        InteractiveZombieEpidemicEnvironmentRepresentation environment;
        
        environment = new InteractiveZombieEpidemicEnvironmentRepresentation(config);
        
        environment.setMantainAspectRatio(true);
        environment.setMantainAspectRatioAnchor(Anchor.Center);
        environment.addMouseListener(this.createClickEnvironmentMouseListener());
        environment.addRunEnvironmentEventsHandler(new RunEnvironmentEventsHandler() {
            @Override
            public void whenFinalStateAchieved() {
                //Change the reproduction button to pause state
                SimulationSection.this.reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PLAY_IMAGE));
                SimulationSection.this.reproductionLabel.setText("Reprodueix");
            }
        });
        
        return environment;
    }
    
    /**
     * Create the mouse listener for clicks in the environment component 
     * @return the listener
     */
    private MouseListener createClickEnvironmentMouseListener() {
        return new MouseAdapter() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
                Point2D environmentPoint;
                
                if(SimulationSection.this.inWallConstructionMode) {
                    switch(e.getButton()) {
                        case MouseEvent.BUTTON1: //Left button
                            //Retrieve the point of the environment clicked
                            environmentPoint = environmentRepresentation.getEnvironmentPoint(e.getPoint());

                            if(environmentPoint != null) {
                                
                                if(lastEnvironmentPoint != null) {
                                    //Build a wall between the last point and the new point
                                    environmentRepresentation.buyAndBuildWall(lastEnvironmentPoint, environmentPoint, true);
                                }

                                lastEnvironmentPoint = environmentPoint;
                                
                                SimulationSection.this.updateResourcesInformation();
                            }
                            break;
                        case MouseEvent.BUTTON3:
                            //Remove the last environment point
                            lastEnvironmentPoint = null;
                    }
                }
            }
        };
    }
}
