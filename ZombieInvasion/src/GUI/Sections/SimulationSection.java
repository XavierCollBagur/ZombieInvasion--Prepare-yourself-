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
 *
 * @author Xavier
 */
public class SimulationSection extends JPanel {
    //Constants
    private final static Color AVAILABLE_RESOURCES_COLOR     = new Color(0x5CB85C),
                               NOT_AVAILABLE_RESOURCES_COLOR = Color.RED;
    
    //Attributes
    private final SimulationConfiguration configuration;
    private boolean inWallConstructionMode;
    private Point2D lastEnvironmentPoint;
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

    public void stop() {
        this.environmentRepresentation.stopEnvironment();
    }
    
    //Private Methods
    private void initComponents(SimulationConfiguration config) {
        JPanel optionsPanel;
        
        this.setLayout(new BorderLayout());
        
        optionsPanel                   = this.createOptionsPanel(config.getResources());
        this.environmentRepresentation = createEnvironmentRepresentation(config);
        
        this.add(optionsPanel, BorderLayout.WEST);
        this.add(this.environmentRepresentation, BorderLayout.CENTER);
    }
    private JPanel createOptionsPanel(ResourcesConfiguration configuration) {
        final int vaccinatedPerVaccinationKit, armedPerWeaponKit, vaccinationKitCost, 
                  weaponKitCost, wallUnitCost, totalResourcesAvailable;
        String vaccineText, weaponText, wallText;
        JPanel optionsPanel;
        GridBagConstraints constraints;
        JSeparator verticalSeparator, horizontalSeparator;
         
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
        
        this.reproductionButton.addActionListener(this.createPlayPauseEnvironmentActionListener());
        this.restartButton.addActionListener(this.createRestartEnvironmentActionListener());
        this.buyVaccinesButton.addActionListener(this.createBuyVaccinationKitActionListener());
        this.buyWeaponsButton.addActionListener(this.createBuyWeaponKitActionListener());
        this.wallModeButton.addActionListener(this.createWallModeActionListener());
        
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
    
    private JButton createOptionButton(String buttonText, Image buttonImage) {
        JButton optionButton;
  
        optionButton = new JButton(new ImageIcon(buttonImage));
        
        optionButton.setPreferredSize(new Dimension(60, 60));
        optionButton.setMinimumSize(new Dimension(60, 60));
        optionButton.setMargin(new Insets(0, 0, 0, 0));
        optionButton.setToolTipText(buttonText);
        
        return optionButton;
    }
    
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
    
    private GridBagConstraints createOptionButtonConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints        = this.createOptionElementConstraints(gridX, gridY); 
        constraints.insets = new Insets(20, 5, 0, 5);
        
        return constraints;
    }
    
    private GridBagConstraints createOptionLabelConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints        = this.createOptionElementConstraints(gridX, gridY); 
        constraints.insets = new Insets(0, 5, 0, 5);
        
        return constraints;
    }
    
    private GridBagConstraints createOptionElementConstraints(int gridX, int gridY) {
        GridBagConstraints constraints;
        
        constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        
        return constraints;
    }
    
    private ActionListener createPlayPauseEnvironmentActionListener() {
        return new ActionListener() {
            boolean isRunning = false;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton reproductionButton;
                JLabel reproductionLabel;
                InteractiveZombieEpidemicEnvironmentRepresentation environmentRepresentation;
                
                reproductionButton        = SimulationSection.this.reproductionButton;
                reproductionLabel         = SimulationSection.this.reproductionLabel;
                environmentRepresentation = SimulationSection.this.environmentRepresentation;
                this.isRunning            = !this.isRunning;
                
                if(this.isRunning) {
                    reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PAUSE_IMAGE));
                    reproductionLabel.setText("Pausa");
                    environmentRepresentation.runEnvironment();
                }
                else {
                    reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PLAY_IMAGE));
                    reproductionLabel.setText("Reprodueix");
                    environmentRepresentation.stopEnvironment();
                }
                
            }
        };
    }
    
    private ActionListener createRestartEnvironmentActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationSection thisPanel;
                
                thisPanel = SimulationSection.this;
                
                if(thisPanel.environmentRepresentation.isRunning()) {
                    thisPanel.reproductionButton.doClick();
                }
                
                thisPanel.remove(thisPanel.environmentRepresentation);
                
                thisPanel.environmentRepresentation = thisPanel.createEnvironmentRepresentation(thisPanel.configuration);
                
                thisPanel.add(thisPanel.environmentRepresentation, BorderLayout.CENTER);
                
                thisPanel.updateResourcesInformation();
                
                if(thisPanel.inWallConstructionMode) {
                    thisPanel.wallModeButton.doClick();
                }
                
                thisPanel.validate();
            }
        };
    }
    
    private ActionListener createBuyVaccinationKitActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean bought;
                
                bought = SimulationSection.this.environmentRepresentation.buyAndUseVaccinationKit();
                
                if(bought) {
                    SimulationSection.this.updateResourcesInformation();
                }
            }
        };
    }
    
    private ActionListener createBuyWeaponKitActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean bought;
                
                bought = SimulationSection.this.environmentRepresentation.buyAndUseWeaponKit();
                
                if(bought) {
                    SimulationSection.this.updateResourcesInformation();
                }
            }
        };
    }
    
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
                    lastEnvironmentPoint = null;
                    buttonImage          = ImagesDirectory.WALL_FORBIDDEN_IMAGE;
                    labelText            = "Sortir mode construcció (-" + wallUnitCost +  "€/metre)";
                }
                else {
                    buttonImage = ImagesDirectory.WALL_IMAGE;
                    labelText   = "Entrar mode construcció (-" + wallUnitCost +  "€/metre)";
                }
                
                wallModeButton.setIcon(new ImageIcon(buttonImage));
                wallModeButton.setToolTipText(labelText);
                wallModeLabel.setText("<html><center>" + labelText + "</center></html>");
            }
        };
    }
    
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
        
        this.totalResourcesAvailableLabel.setText(resourcesAvailable + "€");
        this.buyVaccinesButton.setEnabled(canBuyVaccinationKit);
        this.buyWeaponsButton.setEnabled(canBuyWeaponKit);
        this.wallModeButton.setEnabled(canBuyWallUnit);
        
        if(!canBuyVaccinationKit && !canBuyWeaponKit && !canBuyWallUnit) {
            if(this.inWallConstructionMode) {
                this.wallModeButton.doClick();
            }
            
            this.totalResourcesAvailableLabel.setForeground(NOT_AVAILABLE_RESOURCES_COLOR);
        }
        else {
            this.totalResourcesAvailableLabel.setForeground(AVAILABLE_RESOURCES_COLOR);
        }
    }
    
    private InteractiveZombieEpidemicEnvironmentRepresentation createEnvironmentRepresentation(SimulationConfiguration config) {
        InteractiveZombieEpidemicEnvironmentRepresentation environment;
        
        environment = new InteractiveZombieEpidemicEnvironmentRepresentation(config);
        
        environment.setMantainAspectRatio(true);
        environment.setMantainAspectRatioAnchor(Anchor.Center);
        environment.addMouseListener(this.createClickEnvironmentMouseListener());
        environment.addRunEnvironmentEventsHandler(new RunEnvironmentEventsHandler() {
            @Override
            public void whenFinalStateAchieved() {
                SimulationSection.this.reproductionButton.setIcon(new ImageIcon(ImagesDirectory.PLAY_IMAGE));
                SimulationSection.this.reproductionLabel.setText("Reprodueix");
            }
        });
        
        return environment;
    }
    
    private MouseListener createClickEnvironmentMouseListener() {
        return new MouseAdapter() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
                InteractiveZombieEpidemicEnvironmentRepresentation environmentRepresentation;
                Point2D environmentPoint;
                
                if(SimulationSection.this.inWallConstructionMode) {
                    switch(e.getButton()) {
                        case MouseEvent.BUTTON1:
                            environmentRepresentation = SimulationSection.this.environmentRepresentation;
                            environmentPoint          = environmentRepresentation.getEnvironmentPoint(e.getPoint());

                            if(environmentPoint != null) {
                                
                                if(lastEnvironmentPoint != null) {
                                    environmentRepresentation.buyAndBuildWall(lastEnvironmentPoint, environmentPoint, true);
                                }

                                lastEnvironmentPoint = environmentPoint;
                                
                                SimulationSection.this.updateResourcesInformation();
                            }
                            break;
                        case MouseEvent.BUTTON3:
                            lastEnvironmentPoint = null;
                    }
                }
            }
        };
    }
}
