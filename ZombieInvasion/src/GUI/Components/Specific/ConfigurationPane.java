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

package GUI.Components.Specific;

import Agents.Base.BaseInformation;
import Agents.Human.HumanHealthStatus;
import Agents.Human.HumanInformation;
import Environment.EnvironmentWall;
import Environment.ZombieEpidemicEnvironmentInformation;
import GUI.Components.General.CarouselPanel;
import GUI.Components.General.ThickSliderUI;
import GUI.Components.General.ThickTitledBorder;
import GUI.Components.Specific.EnvironmentRepresentation.Anchor;
import GUI.Components.Specific.EnvironmentRepresentation.ZombieEpidemicEnvironmentRepresentation;
import Geometry.Cell;
import SimulationConfiguration.EnvironmentConfiguration;
import SimulationConfiguration.HumanConfiguration;
import SimulationConfiguration.HumanZombieInteractionConfiguration;
import SimulationConfiguration.PopulationConfiguration;
import SimulationConfiguration.ResourcesConfiguration;
import SimulationConfiguration.SimulationConfiguration;
import SimulationConfiguration.VaccinationConfiguration;
import SimulationConfiguration.WallConfiguration;
import SimulationConfiguration.WeaponConfiguration;
import SimulationConfiguration.ZombieEpidemicConfiguration;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Xavier
 */
public class ConfigurationPane extends CarouselPanel {
    //Constants
    private final static int   PANEL_TITLE_HEIGHT           = 30;
    private final static Color PANEL_TITLE_BACKGROUND_COLOR = new Color(0x24246F),
                               PANEL_TITLE_FONT_COLOR       = Color.WHITE,
                               PANEL_BACKGROUND_COLOR       = new Color(0xFFFFD7),
                               CUSTOM_GREEN                 = new Color(0x26bc22), 
                               CUSTOM_RED                   = new Color(0xcd2529),
                               CUSTOM_YELLOW                = new Color(0xefff51);
    
    private final static int MIN_ROWS                         = 1,
                             MAX_ROWS                         = Integer.MAX_VALUE,
                             MIN_COLUMNS                      = 1,
                             MAX_COLUMNS                      = Integer.MAX_VALUE,
                             CELL_HEIGHT                      = 200,
                             MIN_HUMAN_SPEED                  = 1,
                             MAX_HUMAN_SPEED                  = CELL_HEIGHT,
                             MIN_HUMAN_VISION_DISTANCE        = CELL_HEIGHT / 2,
                             MAX_HUMAN_VISION_DISTANCE        = 5 * CELL_HEIGHT,
                             MIN_INFECTED_LATENCY_PERIOD      = 0,
                             MAX_INFECTED_LATENCY_PERIOD      = 35,
                             MIN_ZOMBIE_SPEED                 = MIN_HUMAN_SPEED,
                             MAX_ZOMBIE_SPEED                 = MAX_HUMAN_SPEED,
                             MIN_ZOMBIE_SPEED_AT_REST         = MIN_ZOMBIE_SPEED,
                             MAX_ZOMBIE_SPEED_AT_REST         = MAX_ZOMBIE_SPEED,
                             MIN_ZOMBIE_VISION_DISTANCE       = MIN_HUMAN_VISION_DISTANCE,
                             MAX_ZOMBIE_VISION_DISTANCE       = MAX_HUMAN_VISION_DISTANCE,
                             MIN_ZOMBIE_OLFACTORY_DISTANCE    = 0,
                             MAX_ZOMBIE_OLFACTORY_DISTANCE    = 3 * MAX_ZOMBIE_VISION_DISTANCE / 2,
                             MIN_TOTAL_RESOURCES              = 0,
                             MAX_TOTAL_RESOURCES              = Integer.MAX_VALUE,
                             MIN_VACCINATION_KIT_COST         = 0,
                             MAX_VACCINATION_KIT_COST         = Integer.MAX_VALUE,
                             MIN_VACCINATED_PER_KIT           = 1,
                             MAX_VACCINATED_PER_KIT           = Integer.MAX_VALUE,
                             MIN_WEAPON_KIT_COST              = 0,
                             MAX_WEAPON_KIT_COST              = Integer.MAX_VALUE,
                             MIN_ARMED_PER_KIT                = 1,
                             MAX_ARMED_PER_KIT                = Integer.MAX_VALUE,
                             MIN_BULLETS_PER_WEAPON           = 1,
                             MAX_BULLETS_PER_WEAPON           = Integer.MAX_VALUE,
                             MIN_BULLETS_TO_KILL              = 1,
                             MAX_BULLETS_TO_KILL              = Integer.MAX_VALUE,
                             WALL_UNIT_LENGTH                 = CELL_HEIGHT,
                             MIN_WALL_UNIT_COST               = 0,
                             MAX_WALL_UNIT_COST               = Integer.MAX_VALUE,
                             MIN_ZOMBIES_NEEDED_WALL          = 1,
                             MAX_ZOMBIES_NEEDED_WALL          = Integer.MAX_VALUE,
                             MIN_TOTAL_POPULATION             = 2,
                             MAX_TOTAL_POPULATION             = Integer.MAX_VALUE,
                             MIN_HUMAN                        = 1,
                             MIN_HEALTHY                      = 0;
    
    //Attributes
    private SimulationConfiguration linkedConfiguration;
    private ZombieEpidemicEnvironmentRepresentation environmentRepresentation;
    
    //Public Constructors
    public ConfigurationPane(SimulationConfiguration linkedConfiguration) {
        super();
        
        this.linkedConfiguration = linkedConfiguration;
        
        adjustConfigurationInitialValues();
        initComponents();
    }
    
    //Private functions
    private void adjustConfigurationInitialValues() {
        int numRows, numColumns, agentWidth, agentHeight, oldCellHeight, cellWidth, cellHeight,
            humanSpeed, humanVisionDistance, infectedLatencyPeriod, zombieSpeed, zombieSpeedAtRest, 
            zombieVisionDistance, zombieOlfactoryDistance, totalResources, vaccinationKitCost, vaccinatedPerKit,
            weaponKitCost, armedPerKit, bulletsPerWeapon, bulletsToKill, trajectoryDiversionDegrees,
            wallUnitLength, wallUnitCost, zombiesNeededWall, numHealthy, numInfected, numZombified;
        double agentCellWidthRatio, agentCellHeightRatio, cellWidthHeightRatio,
               zombieWinLoseRatio, zombieKillInfectRatio, humanKillEscapeRatio, zombieWinLoseRatioWeapon;
        EnvironmentConfiguration environmentConfig;
        HumanConfiguration humanConfig;
        ZombieEpidemicConfiguration zombieConfig;
        HumanZombieInteractionConfiguration humanZombieConfig;
        PopulationConfiguration populationConfig;
        ResourcesConfiguration resourcesConfig;
        VaccinationConfiguration vaccinationConfig;
        WeaponConfiguration weaponConfig;
        WallConfiguration wallConfig;
        
        environmentConfig          = this.linkedConfiguration.getEnvironment();
        humanConfig                = this.linkedConfiguration.getHuman();
        zombieConfig               = this.linkedConfiguration.getZombieEpidemic();
        humanZombieConfig          = this.linkedConfiguration.getHumanZombieInteraction();
        populationConfig           = this.linkedConfiguration.getPopulation();
        resourcesConfig            = this.linkedConfiguration.getResources();
        weaponConfig               = resourcesConfig.getWeapon();
        vaccinationConfig          = resourcesConfig.getVaccination();
        wallConfig                 = resourcesConfig.getWall();
        numRows                    = this.mantainBetween(environmentConfig.getNumberOfRows(), MIN_ROWS, MAX_ROWS);
        numColumns                 = this.mantainBetween(environmentConfig.getNumberOfColumns(), MIN_COLUMNS, MAX_COLUMNS);
        agentCellWidthRatio        = this.mantainBetween((double)environmentConfig.getAgentWidth() / environmentConfig.getCellWidth(), 0.01, 1);
        agentCellHeightRatio       = this.mantainBetween((double)environmentConfig.getAgentHeight() / environmentConfig.getCellHeight(), 0.01, 1);
        cellWidthHeightRatio       = this.mantainBetween((double)environmentConfig.getCellWidth() / environmentConfig.getCellHeight(), 0.01, 1);
        oldCellHeight              = environmentConfig.getCellHeight();
        cellHeight                 = CELL_HEIGHT;
        cellWidth                  = (int)Math.ceil(cellWidthHeightRatio * cellHeight);
        agentWidth                 = (int)Math.ceil(agentCellWidthRatio * cellWidth);
        agentHeight                = (int)Math.ceil(agentCellHeightRatio * cellHeight);
        humanSpeed                 = this.mantainBetween(humanConfig.getSpeed() * cellHeight / oldCellHeight, MIN_HUMAN_SPEED, MAX_HUMAN_SPEED);
        humanVisionDistance        = this.mantainBetween(humanConfig.getVisionDistance() * cellHeight / oldCellHeight, MIN_HUMAN_VISION_DISTANCE, MAX_HUMAN_VISION_DISTANCE);
        infectedLatencyPeriod      = this.mantainBetween(zombieConfig.getInfectedLatencyPeriod(), MIN_INFECTED_LATENCY_PERIOD, MAX_INFECTED_LATENCY_PERIOD);
        zombieSpeed                = this.mantainBetween(zombieConfig.getZombieSpeed() * cellHeight / oldCellHeight, MIN_ZOMBIE_SPEED, MAX_ZOMBIE_SPEED);
        zombieSpeedAtRest          = this.mantainBetween(zombieConfig.getZombieSpeedAtRest() * cellHeight / oldCellHeight, MIN_ZOMBIE_SPEED_AT_REST, MAX_ZOMBIE_SPEED_AT_REST);
        zombieVisionDistance       = this.mantainBetween(zombieConfig.getZombieVisionDistance() * cellHeight / oldCellHeight, MIN_ZOMBIE_VISION_DISTANCE, MAX_ZOMBIE_VISION_DISTANCE);
        zombieOlfactoryDistance    = this.mantainBetween(zombieConfig.getZombieOlfactoryDistance() * cellHeight / oldCellHeight, MIN_ZOMBIE_OLFACTORY_DISTANCE, MAX_ZOMBIE_OLFACTORY_DISTANCE);
        zombieWinLoseRatio         = this.mantainBetween(humanZombieConfig.getZombieWinLoseRatio(), 0, 1);
        zombieKillInfectRatio      = this.mantainBetween(humanZombieConfig.getZombieKillInfectRatio(), 0, 1);
        humanKillEscapeRatio       = this.mantainBetween(humanZombieConfig.getHumanKillEscapeRatio(), 0, 1);
        numHealthy                 = this.mantainBetween(populationConfig.getInitiallyHealthy(), MIN_HEALTHY, MAX_TOTAL_POPULATION);
        numInfected                = this.mantainBetween(populationConfig.getInitiallyInfected(), Math.max(0, MIN_HUMAN - numHealthy), MAX_TOTAL_POPULATION - numHealthy);
        numZombified               = this.mantainBetween(populationConfig.getInitiallyZombified(), 0, MAX_TOTAL_POPULATION - numHealthy - numInfected);
        totalResources             = this.mantainBetween(resourcesConfig.getTotalResourcesAvailable(), MIN_TOTAL_RESOURCES, MAX_TOTAL_RESOURCES);
        vaccinationKitCost         = this.mantainBetween(vaccinationConfig.getVaccinationKitCost(), MIN_VACCINATION_KIT_COST, MAX_VACCINATION_KIT_COST);
        vaccinatedPerKit           = this.mantainBetween(vaccinationConfig.getVaccinatedPerVaccinationKit(), MIN_VACCINATED_PER_KIT, MAX_VACCINATED_PER_KIT);
        weaponKitCost              = this.mantainBetween(weaponConfig.getWeaponKitCost(), MIN_WEAPON_KIT_COST, MAX_WEAPON_KIT_COST);
        armedPerKit                = this.mantainBetween(weaponConfig.getArmedPerWeaponKit(), MIN_ARMED_PER_KIT, MAX_ARMED_PER_KIT);
        bulletsPerWeapon           = this.mantainBetween(weaponConfig.getBulletsPerWeapon(), MIN_BULLETS_PER_WEAPON, MAX_BULLETS_PER_WEAPON);
        bulletsToKill              = this.mantainBetween(weaponConfig.getBulletsNeededToKill(), MIN_BULLETS_TO_KILL, MAX_BULLETS_TO_KILL);
        zombieWinLoseRatioWeapon   = this.mantainBetween(weaponConfig.getZombieWinLoseRatioAgainstArmedHuman(), 0, 1);
        trajectoryDiversionDegrees = this.mantainBetween(weaponConfig.getBulletTrajectoryDiversionDegrees(), 0, 90);
        wallUnitLength             = WALL_UNIT_LENGTH;
        wallUnitCost               = this.mantainBetween(wallConfig.getWallUnitCost(), MIN_WALL_UNIT_COST, MAX_WALL_UNIT_COST);
        zombiesNeededWall          = this.mantainBetween(wallConfig.getZombiesNeededToBreakDownAWall(), MIN_ZOMBIES_NEEDED_WALL, MAX_ZOMBIES_NEEDED_WALL);
        
        environmentConfig.setNumberOfRows(numRows);
        environmentConfig.setNumberOfColumns(numColumns);
        environmentConfig.setCellWidth(cellWidth);
        environmentConfig.setCellHeight(cellHeight);
        environmentConfig.setAgentWidth(agentWidth);
        environmentConfig.setAgentHeight(agentHeight);
        humanConfig.setSpeed(humanSpeed);
        humanConfig.setVisionDistance(humanVisionDistance);
        zombieConfig.setInfectedLatencyPeriod(infectedLatencyPeriod);
        zombieConfig.setZombieSpeed(zombieSpeed);
        zombieConfig.setZombieSpeedAtRest(zombieSpeedAtRest);
        zombieConfig.setZombieVisionDistance(zombieVisionDistance);
        zombieConfig.setZombieOlfactoryDistance(zombieOlfactoryDistance);
        humanZombieConfig.setZombieWinLoseRatio(zombieWinLoseRatio);
        humanZombieConfig.setZombieKillInfectRatio(zombieKillInfectRatio);
        humanZombieConfig.setHumanKillEscapeRatio(humanKillEscapeRatio);
        populationConfig.setInitiallyHealthy(numHealthy);
        populationConfig.setInitiallyInfected(numInfected);
        populationConfig.setInitiallyZombified(numZombified);
        resourcesConfig.setTotalResourcesAvailable(totalResources);
        vaccinationConfig.setVaccinationKitCost(vaccinationKitCost);
        vaccinationConfig.setVaccinatedPerVaccinationKit(vaccinatedPerKit);
        weaponConfig.setWeaponKitCost(weaponKitCost);
        weaponConfig.setArmedPerWeaponKit(armedPerKit);
        weaponConfig.setBulletsPerWeapon(bulletsPerWeapon);
        weaponConfig.setBulletsNeededToKill(bulletsToKill);
        weaponConfig.setZombieWinLoseRatioAgainstArmedHuman(zombieWinLoseRatioWeapon);
        weaponConfig.setBulletTrajectoryDiversionDegrees(trajectoryDiversionDegrees);
        wallConfig.setWallUnitLength(wallUnitLength);
        wallConfig.setWallUnitCost(wallUnitCost);
        wallConfig.setZombiesNeededToBreakDownAWall(zombiesNeededWall);
    }
    
    private void initComponents() {
        this.add(this.createEnvironmentPanel());
        this.add(this.createAgentsConfigurationPanel());
        this.add(this.createPopulationCountAndInteractionPanel());
        this.add(this.createResourcesConfigurationPanel());
    }
    
    private JPanel createEnvironmentPanel() {
        JPanel container, scenarioPanel, agentCellProportionPanel;
        
        container                = new JPanel(new GridBagLayout());
        scenarioPanel            = this.createScenarioPanel();
        agentCellProportionPanel = this.createAgentCellProportion();
        
        scenarioPanel.setPreferredSize(new Dimension(0, 0));
        
        container.setOpaque(false);
        container.add(scenarioPanel, this.createConstraints(GridBagConstraints.BOTH, 0, 0, 1, 1, 1));
        container.add(agentCellProportionPanel, this.createConstraints(GridBagConstraints.VERTICAL, 1, 0, 1, 0, 1));
        
        return container;
    }
    
    private JPanel createScenarioPanel() {
        int initRows, initColumns;
        JPanel panel;
        JLabel rowsLabel, columnsLabel, inaccessibleCellsLabel;
        final JSpinner rowsSpinner, columnsSpinner; 
        Dimension minimumSize;
        
        initRows                  = this.linkedConfiguration.getEnvironment().getNumberOfRows();
        initColumns               = this.linkedConfiguration.getEnvironment().getNumberOfColumns();
        panel                     = this.createTitledPanel("ESCENARI");
        rowsLabel                 = this.createLabel("Files", 15);
        columnsLabel              = this.createLabel("Columnes", 15);
        inaccessibleCellsLabel    = this.createLabel("Cel·les innaccessibles", 15);
        rowsSpinner               = new JSpinner(new SpinnerNumberModel(initRows, MIN_ROWS, MAX_ROWS, 1));
        columnsSpinner            = new JSpinner(new SpinnerNumberModel(initColumns, MIN_COLUMNS, MAX_COLUMNS, 1));
        environmentRepresentation = new ZombieEpidemicEnvironmentRepresentation(linkedConfiguration.getEnvironment(), 
                                                                                new ZombieEpidemicEnvironmentInformation());
        minimumSize               = columnsLabel.getMinimumSize();
        
        rowsLabel.setMinimumSize(minimumSize);
        columnsLabel.setMinimumSize(minimumSize);
        rowsSpinner.setMinimumSize(minimumSize);
        columnsSpinner.setMinimumSize(minimumSize);
        
        environmentRepresentation.setMantainAspectRatio(true);
        environmentRepresentation.setMantainAspectRatioAnchor(Anchor.Center);
        environmentRepresentation.setOpaque(false);
        
        panel.setLayout(new GridBagLayout());
        panel.add(rowsLabel, this.createConstraints(0, 0));
        panel.add(columnsLabel, this.createConstraints(1, 0));
        panel.add(new JLabel(), this.createConstraints(2, 0, 1, 20));
        panel.add(rowsSpinner, this.createConstraints(0, 1));
        panel.add(columnsSpinner, this.createConstraints(1, 1));
        panel.add(inaccessibleCellsLabel, this.createConstraints(0, 2, 3, 1));
        panel.add(environmentRepresentation, this.createConstraints(GridBagConstraints.BOTH, 0, 3, 3, 1, 1));
        
        rowsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                final int oldRows, newRows, columns;
                Cell cell;
                Set inaccessibleCells;
                
                columns = linkedConfiguration.getEnvironment().getNumberOfColumns();
                oldRows = linkedConfiguration.getEnvironment().getNumberOfRows();
                newRows = (int)rowsSpinner.getValue();
                
                linkedConfiguration.getEnvironment().setNumberOfRows(newRows);
                
                if(oldRows > newRows) {
                    cell              = new Cell(0, 0);
                    inaccessibleCells = linkedConfiguration.getEnvironment().getInaccessibleCells();
                    
                    for(int row = newRows; row < oldRows; row++) {
                        for(int column = 0; column < columns; column++) {
                            cell.set(row, column);
                            inaccessibleCells.remove(cell);
                        }
                    }
                }
                
                environmentRepresentation.repaint();
            }
        });
        
        columnsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                final int oldColumns, newColumns, rows;
                Cell cell;
                Set inaccessibleCells;
                
                rows       = linkedConfiguration.getEnvironment().getNumberOfRows();
                oldColumns = linkedConfiguration.getEnvironment().getNumberOfColumns();
                newColumns = (int)columnsSpinner.getValue();
                
                linkedConfiguration.getEnvironment().setNumberOfColumns(newColumns);
                
                if(oldColumns > newColumns) {
                    cell              = new Cell(0, 0);
                    inaccessibleCells = linkedConfiguration.getEnvironment().getInaccessibleCells();
                    
                    for(int column = newColumns; column < oldColumns; column++) {
                        for(int row = 0; row < rows; row++) {
                            cell.set(row, column);
                            inaccessibleCells.remove(cell);
                        }
                    }
                }
                
                environmentRepresentation.repaint();
            }
        });
        
        environmentRepresentation.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e) {
                Cell cell;
                Set<Cell> inaccessibleCells;
                boolean alreadyInserted;
                
                cell = environmentRepresentation.getEnvironmentCell(e.getPoint());
                
                if(cell != null) { 
                    inaccessibleCells = linkedConfiguration.getEnvironment().getInaccessibleCells();
                    alreadyInserted   = !inaccessibleCells.add(cell);
                    
                    if(alreadyInserted) {
                        inaccessibleCells.remove(cell);
                    }
                    
                    environmentRepresentation.repaint();
                }
            }
        });
        
        return panel;
    }   
    
    private JPanel createAgentCellProportion() {
        final int initialAgentWidth, initialAgentHeight, initialCellWidth, initialCellHeight,
                  agentCellWidthPercentage, agentCellHeightPercentage, cellWidthHeightPercentage;
        JPanel panel;
        JLabel cellWidthHeightLabel, agentCellWidthLabel, agentCellHeightLabel;
        final JSlider cellWidthHeightSlider, agentCellWidthSlider, agentCellHeightSlider;
        final ZombieEpidemicEnvironmentRepresentation agentCellRepresentation;
        
        initialAgentWidth         = this.linkedConfiguration.getEnvironment().getAgentWidth();
        initialAgentHeight        = this.linkedConfiguration.getEnvironment().getAgentHeight();
        initialCellWidth          = this.linkedConfiguration.getEnvironment().getCellWidth();
        initialCellHeight         = this.linkedConfiguration.getEnvironment().getCellHeight();
        agentCellWidthPercentage  = (int)Math.ceil(((double)initialAgentWidth / initialCellWidth) * 100);
        agentCellHeightPercentage = (int)Math.ceil(((double)initialAgentHeight / initialCellHeight) * 100);
        cellWidthHeightPercentage = (int)Math.ceil(((double)initialCellWidth / initialCellHeight) * 100);
        panel                     = this.createTitledPanel("Relació agent/cel·la");
        cellWidthHeightLabel      = this.createLabel("Percentatge ample/alt cel·la", 15);
        agentCellWidthLabel       = this.createLabel("Percentatge ample agent/cel·la", 15);
        agentCellHeightLabel      = this.createLabel("Percentatge alt agent/cel·la", 15);
        cellWidthHeightSlider     = this.createPercentageSlider(Math.min(cellWidthHeightPercentage, 100));
        agentCellWidthSlider      = this.createPercentageSlider(Math.min(agentCellWidthPercentage, 100));
        agentCellHeightSlider     = this.createPercentageSlider(Math.min(agentCellHeightPercentage, 100));
        agentCellRepresentation   = this.createAgentCellRepresentation();
        
        panel.setLayout(new GridBagLayout());
        panel.add(cellWidthHeightLabel, this.createConstraints(0, 0));
        panel.add(cellWidthHeightSlider, this.createConstraints(0, 1));
        panel.add(agentCellWidthLabel, this.createConstraints(0, 2));
        panel.add(agentCellWidthSlider, this.createConstraints(0, 3));
        panel.add(agentCellHeightLabel, this.createConstraints(0, 4));
        panel.add(agentCellHeightSlider, this.createConstraints(0, 5));
        panel.add(agentCellRepresentation, this.createConstraints(GridBagConstraints.BOTH, 0, 6, 6, 1, 1));
        
        cellWidthHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                EnvironmentConfiguration config;
                BaseInformation agentInfo;
                int newCellWidth, newAgentWidth;
                double cellProportion, agentProportion;
                
                config          = agentCellRepresentation.getEnvironmentConfiguration();
                agentInfo       = agentCellRepresentation.getEnvironmentInformation().getAlivePopulation().iterator().next(); 
                cellProportion  = (double)cellWidthHeightSlider.getValue() / 100;  
                newCellWidth    = (int)Math.ceil(cellProportion * config.getCellHeight());
                agentProportion = (double)agentCellWidthSlider.getValue() / 100;  
                newAgentWidth   = (int)Math.ceil(agentProportion * newCellWidth);
                
                config.setCellWidth(newCellWidth);
                config.setAgentWidth(newAgentWidth);
                linkedConfiguration.getEnvironment().setCellWidth(newCellWidth);
                linkedConfiguration.getEnvironment().setAgentWidth(newAgentWidth);
                agentInfo.setPosition(config.getAgentMinX(), config.getAgentMaxY());
                agentCellRepresentation.repaint();
                environmentRepresentation.repaint();
            }
        });
        
        agentCellWidthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                EnvironmentConfiguration config;
                BaseInformation agentInfo;
                int newAgentWidth;
                double proportion;
                
                config        = agentCellRepresentation.getEnvironmentConfiguration();
                agentInfo     = agentCellRepresentation.getEnvironmentInformation().getAlivePopulation().iterator().next(); 
                proportion    = (double)agentCellWidthSlider.getValue() / 100;  
                newAgentWidth = (int)Math.ceil(proportion * config.getCellWidth());
                
                config.setAgentWidth(newAgentWidth);
                linkedConfiguration.getEnvironment().setAgentWidth(newAgentWidth);
                agentInfo.setPosition(config.getAgentMinX(), config.getAgentMaxY());
                agentCellRepresentation.repaint();
            }
        });
        
        agentCellHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                EnvironmentConfiguration config;
                BaseInformation agentInfo;
                int newAgentHeight;
                double proportion;
                
                config         = agentCellRepresentation.getEnvironmentConfiguration();
                agentInfo      = agentCellRepresentation.getEnvironmentInformation().getAlivePopulation().iterator().next(); 
                proportion     = (double)agentCellHeightSlider.getValue() / 100;  
                newAgentHeight = (int)Math.ceil(proportion * config.getCellHeight());
                
                config.setAgentHeight(newAgentHeight);
                linkedConfiguration.getEnvironment().setAgentHeight(newAgentHeight);
                agentInfo.setPosition(config.getAgentMinX(), config.getAgentMaxY());
                agentCellRepresentation.repaint();
            }
        });
        
        return panel;
    }
    
    private JSlider createPercentageSlider(int value) {
        JSlider slider;
        Hashtable<Integer, JLabel> labelTable;
        
        slider     = new JSlider(1, 100, value);
        labelTable = new Hashtable<>();
        
        labelTable.put(1, createLabel("1%", 13));
        
        for(int percentage = 25; percentage <= 100; percentage += 25) {
            labelTable.put(percentage, createLabel(percentage + "%", 11));
        }
        
        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setOpaque(false);
        
        return slider;
    }
    
    private ZombieEpidemicEnvironmentRepresentation createAgentCellRepresentation() {
        ZombieEpidemicEnvironmentRepresentation representation;
        EnvironmentConfiguration config;
        ZombieEpidemicEnvironmentInformation info;
        HumanInformation agentInfo;
        
        config         = new EnvironmentConfiguration(this.linkedConfiguration.getEnvironment());
        agentInfo      = new HumanInformation(HumanHealthStatus.Healthy, 0, 0, 0, false, 0);
        info           = new ZombieEpidemicEnvironmentInformation(Collections.<BaseInformation>emptyList(), new ArrayList<BaseInformation>(), Collections.<EnvironmentWall>emptyList(), Collections.<Line2D>emptyList());
        representation = new ZombieEpidemicEnvironmentRepresentation(config, info);
        
        config.setNumberOfRows(1);
        config.setNumberOfColumns(1);
        config.getInaccessibleCells().clear();
        agentInfo.setPosition(config.getAgentMinX(), config.getAgentMaxY());
        info.getAlivePopulation().add(agentInfo);
        representation.setMantainAspectRatio(true);
        representation.setMantainAspectRatioAnchor(Anchor.Center);
        representation.setOpaque(false);
        
        return representation;
    }
    
    private JPanel createPopulationCountAndInteractionPanel() {
        JPanel container, initialPopulationPanel,zombieHumanInteractionPanel;
        
        container                   = new JPanel(new GridBagLayout());
        initialPopulationPanel      = this.createInitialPopulationPanel();
        zombieHumanInteractionPanel = this.createZombieHumanInteractionPanel();
        
        container.setOpaque(false);
        container.add(initialPopulationPanel, this.createConstraints(GridBagConstraints.BOTH, 0, 0, 1, 1, 1));
        container.add(zombieHumanInteractionPanel, this.createConstraints(GridBagConstraints.BOTH, 1, 0, 1, 1, 1));
        
        return container;
    }
    
    private JPanel createInitialPopulationPanel() {
        final int totalPopulation, initiallyHealthy, initiallyHuman;
        JPanel panel;
        final JSpinner totalPopulationSpinner;
        JLabel totalPopulationLabel, humanZombieLabel, healthyInfectedLabel;
        final JSlider humanZombieSlider, healthyInfectedSlider;
        
        initiallyHealthy       = this.linkedConfiguration.getPopulation().getInitiallyHealthy();
        initiallyHuman         = this.linkedConfiguration.getPopulation().getInitiallyHuman();
        totalPopulation        = this.linkedConfiguration.getPopulation().getInitialPopulationCount();
        panel                  = createTitledPanel("POBLACIÓ INICIAL");
        totalPopulationLabel   = createLabel("Població total" , 15);
        humanZombieLabel       = createLabel("Proporció humans/zombis", 15);
        healthyInfectedLabel   = createLabel("Proporció sans/infectats", 15);
        totalPopulationSpinner = new JSpinner(new SpinnerNumberModel(totalPopulation, MIN_TOTAL_POPULATION, MAX_TOTAL_POPULATION, 10));
        humanZombieSlider      = createThickSlider(initiallyHuman, MIN_HUMAN, totalPopulation, CUSTOM_GREEN, CUSTOM_RED, "Humans", "Zombis", "");
        healthyInfectedSlider  = createThickSlider(initiallyHealthy, MIN_HEALTHY, humanZombieSlider.getValue(), CUSTOM_GREEN, CUSTOM_YELLOW, "Sans", "Infectats", "");
        
        panel.setLayout(new GridBagLayout());
        panel.add(totalPopulationLabel, this.createConstraints(0, 0));
        panel.add(totalPopulationSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 1, 1, 1, 0));
        panel.add(humanZombieLabel, this.createConstraints(0, 2));
        panel.add(humanZombieSlider, this.createConstraints(0, 3));
        panel.add(healthyInfectedLabel, this.createConstraints(0, 4));
        panel.add(healthyInfectedSlider, this.createConstraints(0, 5));
        panel.add(new JLabel(), this.createConstraints(GridBagConstraints.NONE, 0, 6, 1, 0, 1));
        
        totalPopulationSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int totalPopulation;
                
                totalPopulation = (int)(totalPopulationSpinner.getValue());
                
                changeThickSliderMaximum(humanZombieSlider, totalPopulation);
            }
        });
        
        humanZombieSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int humanPopulation, zombifiedPopulation;
                
                humanPopulation = humanZombieSlider.getValue();
                zombifiedPopulation = humanZombieSlider.getMaximum() - humanPopulation;
                
                changeThickSliderMaximum(healthyInfectedSlider, humanPopulation);
                linkedConfiguration.getPopulation().setInitiallyZombified(zombifiedPopulation);
                
            }
        });
        
        healthyInfectedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int healthyPopulation, infectedPopulation;
                
                healthyPopulation  = healthyInfectedSlider.getValue();
                infectedPopulation = healthyInfectedSlider.getMaximum() - healthyPopulation;
                
                linkedConfiguration.getPopulation().setInitiallyHealthy(healthyPopulation);
                linkedConfiguration.getPopulation().setInitiallyInfected(infectedPopulation);
            }
        });
        
        return panel;
    }
    
    private void changeThickSliderMaximum(JSlider slider, int newMax) {
        JLabel maxLabel;
        
        maxLabel = (JLabel)slider.getLabelTable().remove(slider.getMaximum());
        
        slider.getLabelTable().put(newMax, maxLabel);
        slider.setMaximum(newMax);
    }
    
    private JPanel createHumanConfigurationPanel() {
        final int speed, visionDistance;
        JPanel panel;
        JLabel speedLabel, visionDistanceLabel;
        final JSlider speedSlider, visionDistanceSlider;

        speed                = this.linkedConfiguration.getHuman().getSpeed();
        visionDistance       = this.linkedConfiguration.getHuman().getVisionDistance();
        panel                = this.createTitledPanel("HUMÀ");
        speedLabel           = this.createLabel("Velocitat", 15);
        visionDistanceLabel  = this.createLabel("Capacitat de visió", 15);
        speedSlider          = this.createMinMaxSlider(speed, MIN_HUMAN_SPEED, MAX_HUMAN_SPEED, "Baixa", "Alta");//this.createTopLabelsSlider(speed, MIN_HUMAN_SPEED, MAX_HUMAN_SPEED, new JLabel("Baixa"), new JLabel("Alta"), new JLabel("Humà"));
        visionDistanceSlider = this.createMinMaxSlider(visionDistance, MIN_HUMAN_VISION_DISTANCE, MAX_HUMAN_VISION_DISTANCE, "Baixa", "Alta");//this.createTopLabelsSlider(visionDistance, MIN_HUMAN_VISION_DISTANCE, MAX_HUMAN_VISION_DISTANCE, new JLabel("Baixa"), new JLabel("Alta"), new JLabel("Humà"));
        
        panel.setLayout(new GridBagLayout());
        panel.add(speedLabel, this.createConstraints(0, 0));
        panel.add(speedSlider, this.createConstraints(0, 1));
        panel.add(visionDistanceLabel, this.createConstraints(0, 2));
        panel.add(visionDistanceSlider, this.createConstraints(0, 3));
        panel.add(new JLabel(), this.createConstraints(0, 4, 1, 1, 1));
        
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int speed;
                
                speed = speedSlider.getValue();
                
                linkedConfiguration.getHuman().setSpeed(speed);
            }
        });
        
        visionDistanceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int visionDistance;
                
                visionDistance = visionDistanceSlider.getValue();
                
                linkedConfiguration.getHuman().setVisionDistance(visionDistance);
            }
        });
        
        return panel;
    }
    
    private JPanel createZombieConfigurationPanel() {
        final int latencyPeriod, speed, speedAtRest, visionDistance, olfactoryDistance;
        JPanel panel;
        JLabel latencyPeriodLabel, speedLabel, speedAtRestLabel, visionDistanceLabel, olfactoryDistanceLabel;
        final JSlider latencyPeriodSlider, speedSlider, speedAtRestSlider, visionDistanceSlider, olfactoryDistanceSlider;
        
        latencyPeriod           = this.linkedConfiguration.getZombieEpidemic().getInfectedLatencyPeriod();
        speed                   = this.linkedConfiguration.getZombieEpidemic().getZombieSpeed();
        speedAtRest             = this.linkedConfiguration.getZombieEpidemic().getZombieSpeedAtRest();
        visionDistance          = this.linkedConfiguration.getZombieEpidemic().getZombieVisionDistance();
        olfactoryDistance       = this.linkedConfiguration.getZombieEpidemic().getZombieOlfactoryDistance();
        panel                   = this.createTitledPanel("ZOMBI");
        latencyPeriodLabel      = this.createLabel("Temps per a transformar-se", 15);
        speedLabel              = this.createLabel("Velocitat", 15);
        speedAtRestLabel        = this.createLabel("Velocitat quan no veu humans", 15);
        visionDistanceLabel     = this.createLabel("Capacitat de visió", 15);
        olfactoryDistanceLabel  = this.createLabel("Capacitat olfactiva", 15);
        latencyPeriodSlider     = this.createMinMaxSlider(latencyPeriod, MIN_INFECTED_LATENCY_PERIOD, MAX_INFECTED_LATENCY_PERIOD, "Instantani", "Llarg");//this.createTopLabelsSlider(latencyPeriod, MIN_INFECTED_LATENCY_PERIOD, MAX_INFECTED_LATENCY_PERIOD, new JLabel("Instantani"), new JLabel("Llarg"), new JLabel("Zombi"));
        speedSlider             = this.createMinMaxSlider(speed, MIN_ZOMBIE_SPEED, MAX_ZOMBIE_SPEED, "Baixa", "Alta");//this.createTopLabelsSlider(speed, MIN_ZOMBIE_SPEED, MAX_ZOMBIE_SPEED, new JLabel("Baixa"), new JLabel("Alta"), new JLabel("Zombi"));
        speedAtRestSlider       = this.createMinMaxSlider(speedAtRest, MIN_ZOMBIE_SPEED_AT_REST, MAX_ZOMBIE_SPEED_AT_REST, "Baixa", "Alta");//this.createTopLabelsSlider(speedAtRest, MIN_ZOMBIE_SPEED_AT_REST, MAX_ZOMBIE_SPEED_AT_REST, new JLabel("Nul·la"), new JLabel("Alta"), new JLabel("Zombi"));
        visionDistanceSlider    = this.createMinMaxSlider(visionDistance, MIN_ZOMBIE_VISION_DISTANCE, MAX_ZOMBIE_VISION_DISTANCE, "Baixa", "Alta");//this.createTopLabelsSlider(visionDistance, MIN_ZOMBIE_VISION_DISTANCE, MAX_ZOMBIE_VISION_DISTANCE, new JLabel("Baixa"), new JLabel("Alta"), new JLabel("Zombi"));
        olfactoryDistanceSlider = this.createMinMaxSlider(olfactoryDistance, MIN_ZOMBIE_OLFACTORY_DISTANCE, MAX_ZOMBIE_OLFACTORY_DISTANCE, "Nul·la", "Alta");//this.createTopLabelsSlider(olfactoryDistance, MIN_ZOMBIE_OLFACTORY_DISTANCE, MAX_ZOMBIE_OLFACTORY_DISTANCE, new JLabel("Nul·la"), new JLabel("Alta"), new JLabel("Zombi"));
        
        panel.setLayout(new GridBagLayout());
        panel.add(latencyPeriodLabel, this.createConstraints(0, 0));
        panel.add(latencyPeriodSlider, this.createConstraints(0, 1));
        panel.add(speedLabel, this.createConstraints(0, 2));
        panel.add(speedSlider, this.createConstraints(0, 3));
        panel.add(speedAtRestLabel, this.createConstraints(0, 4));
        panel.add(speedAtRestSlider, this.createConstraints(0, 5));
        panel.add(visionDistanceLabel, this.createConstraints(0, 6));
        panel.add(visionDistanceSlider, this.createConstraints(0, 7));
        panel.add(olfactoryDistanceLabel, this.createConstraints(0, 8));
        panel.add(olfactoryDistanceSlider, this.createConstraints(0, 9));
        panel.add(new JLabel(), this.createConstraints(0, 10, 1, 1, 1));
        
        latencyPeriodSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int latencyPeriod;
                
                latencyPeriod = latencyPeriodSlider.getValue();
                
                linkedConfiguration.getZombieEpidemic().setInfectedLatencyPeriod(latencyPeriod);
            }
        });
        
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int speed;
                
                speed = speedSlider.getValue();
                
                linkedConfiguration.getZombieEpidemic().setZombieSpeed(speed);
            }
        });
        
        speedAtRestSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int speed;
                
                speed = speedAtRestSlider.getValue();
                
                linkedConfiguration.getZombieEpidemic().setZombieSpeedAtRest(speed);
            }
        });
        
        visionDistanceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int visionDistance;
                
                visionDistance = visionDistanceSlider.getValue();
                
                linkedConfiguration.getZombieEpidemic().setZombieVisionDistance(visionDistance);
            }
        });
        
        olfactoryDistanceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int olfactoryDistance;
                
                olfactoryDistance = olfactoryDistanceSlider.getValue();
                
                linkedConfiguration.getZombieEpidemic().setZombieOlfactoryDistance(olfactoryDistance);
            }
        });
        
        return panel;
    }
    
    private JPanel createZombieHumanInteractionPanel() {
        int zombieWinLosePercentage, zombieWinLosePercentageWhenArmed, 
            zombieKillInfectPercentage, humanKillEscapePercentage;
        JPanel panel;
        JLabel humanWinLoseLabel, humanWinLoseWhenArmedLabel, zombieKillInfectLabel, humanKillEscapeLabel;
        final JSlider humanWinLoseSlider, humanWinLoseWhenArmedSlider, zombieKillInfectSlider, humanKillEscapeSlider;
        
        zombieWinLosePercentage          = (int)(this.linkedConfiguration.getHumanZombieInteraction().getZombieWinLoseRatio() * 100);
        zombieWinLosePercentageWhenArmed = (int)(this.linkedConfiguration.getResources().getWeapon().getZombieWinLoseRatioAgainstArmedHuman() * 100);
        zombieKillInfectPercentage       = (int)(this.linkedConfiguration.getHumanZombieInteraction().getZombieKillInfectRatio() * 100);
        humanKillEscapePercentage        = (int)(this.linkedConfiguration.getHumanZombieInteraction().getHumanKillEscapeRatio() * 100);
        panel                            = this.createTitledPanel("ZOMBI VS. HUMÀ");
        humanWinLoseLabel                = this.createLabel("Qui té més probabilitats de guanyar?", 15);
        humanWinLoseWhenArmedLabel       = this.createLabel("I si l'humà va armat?", 15);
        zombieKillInfectLabel            = this.createLabel("Si guanya el zombi, que passa?", 15);
        humanKillEscapeLabel             = this.createLabel("Si guanya, l'humà, que passa?", 15);
        humanWinLoseSlider               = this.createThickSlider(100 - zombieWinLosePercentage, 0, 100, CUSTOM_GREEN, CUSTOM_RED, "Humà", "Zombi", "%");
        humanWinLoseWhenArmedSlider      = this.createThickSlider(100 - zombieWinLosePercentageWhenArmed, 0, 100, CUSTOM_GREEN, CUSTOM_RED, "Humà", "Zombi", "%");
        zombieKillInfectSlider           = this.createThickSlider(zombieKillInfectPercentage, 0, 100, Color.BLACK, CUSTOM_YELLOW, "L'humà mor", "L'humà s'infecta", "%");
        humanKillEscapeSlider            = this.createThickSlider(humanKillEscapePercentage, 0, 100, Color.BLACK, CUSTOM_GREEN, "El zombi mor", "L'humà escapa", "%");
        
        panel.setLayout(new GridBagLayout());
        panel.add(humanWinLoseLabel, this.createConstraints(0, 0));
        panel.add(humanWinLoseSlider, this.createConstraints(0, 1));
        panel.add(humanWinLoseWhenArmedLabel, this.createConstraints(0, 2));
        panel.add(humanWinLoseWhenArmedSlider, this.createConstraints(0, 3));
        panel.add(zombieKillInfectLabel, this.createConstraints(0, 4));
        panel.add(zombieKillInfectSlider, this.createConstraints(0, 5));
        panel.add(humanKillEscapeLabel, this.createConstraints(0, 6));
        panel.add(humanKillEscapeSlider, this.createConstraints(0, 7));
        panel.add(new JLabel(), this.createConstraints(GridBagConstraints.NONE, 0, 8, 1, 0, 1));
        
        humanWinLoseSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double zombieWinLoseRatio;
                
                zombieWinLoseRatio = (100d - humanWinLoseSlider.getValue()) / 100;
                
                linkedConfiguration.getHumanZombieInteraction().setZombieWinLoseRatio(zombieWinLoseRatio);
            }
        });
        
        humanWinLoseWhenArmedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double zombieWinLoseRatio;
                
                zombieWinLoseRatio = (100d - humanWinLoseWhenArmedSlider.getValue()) / 100;
                
                linkedConfiguration.getResources().getWeapon().setZombieWinLoseRatioAgainstArmedHuman(zombieWinLoseRatio);
            }
        });
        
        zombieKillInfectSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double zombieKillInfectRatio;
                
                zombieKillInfectRatio = zombieKillInfectSlider.getValue() / 100d;
                
                linkedConfiguration.getHumanZombieInteraction().setZombieKillInfectRatio(zombieKillInfectRatio);
            }
        });
        
        humanKillEscapeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double humanKillEscape;
                
                humanKillEscape = humanKillEscapeSlider.getValue() / 100d;
                
                linkedConfiguration.getHumanZombieInteraction().setHumanKillEscapeRatio(humanKillEscape);
            }
        });
        
        return panel;
    }
    
    private JPanel createAgentsConfigurationPanel() {
        JPanel container, humanConfigurationPanel, 
               zombieConfigurationPanel;
        
        container                   = new JPanel(new GridBagLayout());
        humanConfigurationPanel     = this.createHumanConfigurationPanel();
        zombieConfigurationPanel    = this.createZombieConfigurationPanel();
        
        humanConfigurationPanel.setPreferredSize(zombieConfigurationPanel.getPreferredSize());
        humanConfigurationPanel.setMinimumSize(zombieConfigurationPanel.getMinimumSize());
        
        container.setOpaque(false);
        container.add(humanConfigurationPanel, this.createConstraints(GridBagConstraints.BOTH, 0, 0, 1, 1, 1));
        container.add(zombieConfigurationPanel, this.createConstraints(GridBagConstraints.BOTH, 1, 0, 1, 1, 1));
        
        return container;
    }
    
    private JPanel createResourcesConfigurationPanel() {
        JPanel container, totalResourcesPanel, vaccinationConfigurationPanel, 
               wallConfigurationPanel, weaponConfigurationPanel;
        
        container                     = new JPanel(new GridBagLayout());
        totalResourcesPanel           = this.createTotalResourcesPanel();
        vaccinationConfigurationPanel = this.createVaccinationConfigurationPanel();
        wallConfigurationPanel        = this.createWallConfigurationPanel();
        weaponConfigurationPanel      = this.createWeaponConfigurationPanel();
        
        container.setOpaque(false);
        container.add(totalResourcesPanel, this.createConstraints(GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0, 0, 1, 1, 1, 1));
        container.add(vaccinationConfigurationPanel, this.createConstraints(GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 1, 1, 1, 1, 1));
        container.add(wallConfigurationPanel, this.createConstraints(GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 0, 2, 1, 1, 1, 1));
        container.add(weaponConfigurationPanel, this.createConstraints(GridBagConstraints.BOTH, 1, 0, 1, 3, 1, 1));

        return container;
    }
    
    private JPanel createTotalResourcesPanel() {
        final int resourcesAvailable;
        JPanel panel;
        JLabel totalResourcesLabel;
        final JSpinner totalResourcesSpinner;
        
        resourcesAvailable    = this.linkedConfiguration.getResources().getTotalResourcesAvailable();
        panel                 = this.createTitledPanel("DINERS DISPONIBLES", 0, 0, 5, 0);
        totalResourcesLabel   = this.createLabel("Diners disponibles inicialment", 15);
        totalResourcesSpinner = new JSpinner(new SpinnerNumberModel(resourcesAvailable, MIN_TOTAL_RESOURCES, MAX_TOTAL_RESOURCES, 1));
        
        panel.setLayout(new GridBagLayout());
        panel.add(totalResourcesLabel, this.createConstraints(0, 0));
        panel.add(totalResourcesSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 1, 1, 1, 1));
        
        totalResourcesSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int totalResources;
                
                totalResources = (int)totalResourcesSpinner.getValue();
                
                linkedConfiguration.getResources().setTotalResourcesAvailable(totalResources);
            }
        });
        
        return panel;
    }
    
    private JPanel createVaccinationConfigurationPanel() {
        final int vaccinationKitCost, vaccinatedPerKit;
        JPanel panel;
        JLabel vaccinationKitCostLabel, vaccinatedPerKitLabel;
        final JSpinner vaccinationKitCostSpinner, vaccinatedPerKitSpinner;
        
        vaccinationKitCost        = this.linkedConfiguration.getResources().getVaccination().getVaccinationKitCost();
        vaccinatedPerKit          = this.linkedConfiguration.getResources().getVaccination().getVaccinatedPerVaccinationKit();
        panel                     = this.createTitledPanel("VACUNACIÓ", 0, 0, 5, 0);
        vaccinationKitCostLabel   = this.createLabel("Cost d'un kit de vacunació (€)", 15);
        vaccinatedPerKitLabel     = this.createLabel("Número de vacunats per kit", 15);
        vaccinationKitCostSpinner = new JSpinner(new SpinnerNumberModel(vaccinationKitCost, MIN_VACCINATION_KIT_COST, MAX_VACCINATION_KIT_COST, 1));
        vaccinatedPerKitSpinner   = new JSpinner(new SpinnerNumberModel(vaccinatedPerKit, MIN_VACCINATED_PER_KIT, MAX_VACCINATED_PER_KIT, 1));
        
        panel.setLayout(new GridBagLayout());
        panel.add(vaccinationKitCostLabel, this.createConstraints(0, 0));
        panel.add(vaccinationKitCostSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 1, 1, 1, 1));
        panel.add(vaccinatedPerKitLabel, this.createConstraints(0, 2));
        panel.add(vaccinatedPerKitSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 3, 1, 1, 1));
        
        vaccinationKitCostSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int cost;
                
                cost = (int)vaccinationKitCostSpinner.getValue();
                
                linkedConfiguration.getResources().getVaccination().setVaccinationKitCost(cost);
            }
        });
        
        vaccinatedPerKitSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int vaccinated;
                
                vaccinated = (int)vaccinatedPerKitSpinner.getValue();
                
                linkedConfiguration.getResources().getVaccination().setVaccinatedPerVaccinationKit(vaccinated);
            }
        });
        
        return panel;
    }
    
    private JPanel createWallConfigurationPanel() {
        final int wallUnitCost, zombiesNeeded;
        JPanel panel;
        JLabel wallUnitCostLabel, zombiesNeededLabel;
        final JSpinner wallUnitCostSpinner, zombiesNeededSpinner;
        
        wallUnitCost         = this.linkedConfiguration.getResources().getWall().getWallUnitCost();
        zombiesNeeded        = this.linkedConfiguration.getResources().getWall().getZombiesNeededToBreakDownAWall();
        panel                = this.createTitledPanel("CONSTRUCCIÓ DE PARETS", 0, 0, 5, 0);
        wallUnitCostLabel    = this.createLabel("Cost d'un metre de paret (€)", 15);
        zombiesNeededLabel   = this.createLabel("Zombis necessaris per a destruir una paret", 15);
        wallUnitCostSpinner  = new JSpinner(new SpinnerNumberModel(wallUnitCost, MIN_WALL_UNIT_COST, MAX_WALL_UNIT_COST, 1));
        zombiesNeededSpinner = new JSpinner(new SpinnerNumberModel(zombiesNeeded, MIN_ZOMBIES_NEEDED_WALL, MAX_ZOMBIES_NEEDED_WALL, 1));
        
        panel.setLayout(new GridBagLayout());
        panel.add(wallUnitCostLabel, this.createConstraints(0, 0));
        panel.add(wallUnitCostSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 1, 1, 1, 1));
        panel.add(zombiesNeededLabel, this.createConstraints(0, 2));
        panel.add(zombiesNeededSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 3, 1, 1, 1));
        
        wallUnitCostSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int cost;
                
                cost = (int)wallUnitCostSpinner.getValue();
                
                linkedConfiguration.getResources().getWall().setWallUnitCost(cost);
            }
        });
        
        zombiesNeededSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int numZombies;
                
                numZombies = (int)zombiesNeededSpinner.getValue();
                
                linkedConfiguration.getResources().getWall().setZombiesNeededToBreakDownAWall(numZombies);
            }
        });
        
        return panel;
    }
    
    private JPanel createWeaponConfigurationPanel() {
        final int weaponKitCost, armedPerKit, bulletsPerWeapon, 
                  bulletsToKill, trajectoryDiversionDegrees;
        JPanel panel;
        JLabel weaponKitCostLabel, armedPerKitLabel, bulletsPerWeaponLabel, 
               bulletsToKillLabel, trajectoryDiversionLabel;
        final JSpinner weaponKitCostSpinner, armedPerKitSpinner, bulletsPerWeaponSpinner, 
                       bulletsToKillSpinner, trajectoryDiversionSpinner;
        final DiversionDegreesRepresentation diversionRepresentation;
        
        weaponKitCost              = this.linkedConfiguration.getResources().getWeapon().getWeaponKitCost();
        armedPerKit                = this.linkedConfiguration.getResources().getWeapon().getArmedPerWeaponKit();
        bulletsPerWeapon           = this.linkedConfiguration.getResources().getWeapon().getBulletsPerWeapon();
        bulletsToKill              = this.linkedConfiguration.getResources().getWeapon().getBulletsNeededToKill();
        trajectoryDiversionDegrees = this.linkedConfiguration.getResources().getWeapon().getBulletTrajectoryDiversionDegrees();
        panel                      = this.createTitledPanel("ARMES");
        weaponKitCostLabel         = this.createLabel("Cost d'un kit d'armes (€)", 15);
        armedPerKitLabel           = this.createLabel("Número d'armats per kit", 15);
        bulletsPerWeaponLabel      = this.createLabel("Bales per arma", 15);
        bulletsToKillLabel         = this.createLabel("Bales necessàries per a matar", 15);
        trajectoryDiversionLabel   = this.createLabel("Graus possibles de desviació de la bala (0º - 180º)", 15);
        weaponKitCostSpinner       = new JSpinner(new SpinnerNumberModel(weaponKitCost, MIN_WEAPON_KIT_COST, MAX_WEAPON_KIT_COST, 1));
        armedPerKitSpinner         = new JSpinner(new SpinnerNumberModel(armedPerKit, MIN_ARMED_PER_KIT, MAX_ARMED_PER_KIT, 1));
        bulletsPerWeaponSpinner    = new JSpinner(new SpinnerNumberModel(bulletsPerWeapon, MIN_BULLETS_PER_WEAPON, MAX_BULLETS_PER_WEAPON, 1));
        bulletsToKillSpinner       = new JSpinner(new SpinnerNumberModel(bulletsToKill, MIN_BULLETS_TO_KILL, MAX_BULLETS_TO_KILL, 1));
        trajectoryDiversionSpinner = new JSpinner(new SpinnerNumberModel(2 * trajectoryDiversionDegrees, 0, 180, 2));
        diversionRepresentation    = new DiversionDegreesRepresentation(trajectoryDiversionDegrees);
        
        diversionRepresentation.setMantainAspectRatio(true);
        diversionRepresentation.setMantainAspectRatioAnchor(Anchor.Center);
        diversionRepresentation.setOpaque(false);
        
        panel.setLayout(new GridBagLayout());
        panel.add(weaponKitCostLabel, this.createConstraints(0, 0));
        panel.add(weaponKitCostSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 1, 1, 1, 0));
        panel.add(armedPerKitLabel, this.createConstraints(0, 2));
        panel.add(armedPerKitSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 3, 1, 1, 0));
        panel.add(bulletsPerWeaponLabel, this.createConstraints(0, 4));
        panel.add(bulletsPerWeaponSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 5, 1, 1, 0));
        panel.add(bulletsToKillLabel, this.createConstraints(0, 6));
        panel.add(bulletsToKillSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 7, 1, 1, 0));
        panel.add(trajectoryDiversionLabel, this.createConstraints(0, 8));
        panel.add(trajectoryDiversionSpinner, this.createConstraints(GridBagConstraints.NONE, 0, 9, 1, 1, 0));
        panel.add(diversionRepresentation, this.createConstraints(GridBagConstraints.BOTH, 0, 10, 1, 1, 1));
        
        trajectoryDiversionSpinner.setPreferredSize(weaponKitCostSpinner.getPreferredSize());
        trajectoryDiversionSpinner.setMinimumSize(weaponKitCostSpinner.getMinimumSize());
        
        weaponKitCostSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int cost;
                
                cost = (int)weaponKitCostSpinner.getValue();
                
                linkedConfiguration.getResources().getWeapon().setWeaponKitCost(cost);
            }
        });
        
        armedPerKitSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int numArmed;
                
                numArmed = (int)armedPerKitSpinner.getValue();
                
                linkedConfiguration.getResources().getWeapon().setArmedPerWeaponKit(numArmed);
            }
        });
        
        bulletsPerWeaponSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int numBullets;
                
                numBullets = (int)bulletsPerWeaponSpinner.getValue();
                
                linkedConfiguration.getResources().getWeapon().setBulletsPerWeapon(numBullets);
            }
        });
        
        bulletsToKillSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int bulletsToKill;
                
                bulletsToKill = (int)bulletsToKillSpinner.getValue();
                
                linkedConfiguration.getResources().getWeapon().setBulletsNeededToKill(bulletsToKill);
            }
        });
        
        trajectoryDiversionSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int diversionDegrees;
                
                diversionDegrees = (int)trajectoryDiversionSpinner.getValue() / 2;
                
                linkedConfiguration.getResources().getWeapon().setBulletTrajectoryDiversionDegrees(diversionDegrees);
                diversionRepresentation.setDiversionDegrees(diversionDegrees);
            }
        });
        
        return panel;
    }
    
    private JPanel createTitledPanel(String title, int top, int left, int bottom, int right) {
        JPanel panel;
        ThickTitledBorder titledBorder;
        Border insetsBorder;
        
        panel        = new JPanel();
        titledBorder = new ThickTitledBorder(title, PANEL_TITLE_HEIGHT, PANEL_TITLE_BACKGROUND_COLOR, PANEL_TITLE_FONT_COLOR);
        insetsBorder = BorderFactory.createEmptyBorder(top, left, bottom, right);
        
        panel.setBorder(BorderFactory.createCompoundBorder(titledBorder, insetsBorder));

        panel.setBackground(PANEL_BACKGROUND_COLOR);
        
        return panel;
    }
    
    private JPanel createTitledPanel(String title) {
        return this.createTitledPanel(title, 0, 0, 0, 0);
    }
    
    private JLabel createLabel(String text, float fontSize) {
        JLabel label;
        
        label = new JLabel(text);
        
        label.setFont(label.getFont().deriveFont(Font.BOLD, fontSize));
        label.setToolTipText(label.getText());
        
        return label;
    }
    
//    private JSlider createTopLabelsSlider(int value, int minValue, int maxValue, JLabel leftLabel, JLabel rightLabel,
//                                          JLabel thumbLabel) {
//        
//        JSlider slider;
//        TopPlacedLabelsSliderUI sliderUI;
//        Hashtable<Integer, JLabel> labels;
//        
//        slider   = new JSlider(minValue, maxValue, value);
//        sliderUI = new TopPlacedLabelsSliderUI(slider);
//        labels   = new Hashtable<>();
//        
//        labels.put(slider.getMinimum(), leftLabel);
//        labels.put(slider.getMaximum(), rightLabel);
//        labels.put(mantainBetween(slider.getValue(), slider.getMinimum() + 1, slider.getMaximum() - 1), thumbLabel);
//        
//        slider.setOpaque(false);
//        slider.setLabelTable(labels);
//        slider.setPaintLabels(true);
//        slider.setUI(sliderUI);
//        slider.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                JLabel thumbLabel;
//                JSlider slider;
//                Dictionary<Integer, JLabel> labels;
//                int oldValue;
//                
//                slider     = (JSlider)e.getSource();
//                
//                if(slider.getValue() != slider.getMinimum() && slider.getValue() != slider.getMaximum()) {
//                    labels     = slider.getLabelTable();
//                    oldValue   = -1;
//
//                    for(Enumeration<Integer> labelsKeys = labels.keys(); labelsKeys.hasMoreElements(); ) {
//                        oldValue = labelsKeys.nextElement();
//
//                        if(oldValue != slider.getMinimum() && oldValue != slider.getMaximum()) {
//                            break;
//                        }
//                    }
//
//                    if(oldValue != -1) {
//                        thumbLabel = labels.remove(oldValue);
//
//                        labels.put(slider.getValue(), thumbLabel);
//                    }
//
//                    slider.repaint();
//                }
//            }
//        
//        });
//        
//        return slider;
//    }
    
    private JSlider createMinMaxSlider(int value, int minValue, int maxValue, String minText, String maxText) {
        JSlider slider;
        Hashtable<Integer, JLabel> labels;
        
        slider   = new JSlider(minValue, maxValue, value);
        labels   = new Hashtable<>();
        
        labels.put(slider.getMinimum(), createLabel(minText, 11));
        labels.put(slider.getMaximum(), createLabel(maxText, 11));
        
        slider.setOpaque(false);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        
        return slider;
    }
    
    private JSlider createThickSlider(int value, int minValue, int maxValue, Color leftColor, Color rightColor, 
                                      final String leftText, final String rightText, final String valueUnit) {
        JSlider slider;
        ThickSliderUI sliderUI;
        Hashtable<Integer, JLabel> labels;
        
        slider   = new JSlider(minValue, maxValue, value);
        sliderUI = new ThickSliderUI(slider, Color.WHITE, leftColor, rightColor, Color.BLACK);
        labels   = new Hashtable<>();
        
        labels.put(slider.getMinimum(), createLabel(leftText + " (" + slider.getValue() + valueUnit + ")", 11));
        labels.put(slider.getMaximum(), createLabel(rightText + " (" + (maxValue - slider.getValue()) + valueUnit + ")", 11));
        
        slider.setOpaque(false);
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        slider.setUI(sliderUI);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JLabel leftLabel, rightLabel;
                JSlider slider;
                Dictionary<Integer, JLabel> labels;
                
                slider      = (JSlider)e.getSource();
                labels      = slider.getLabelTable();
                leftLabel  = (JLabel)labels.get(slider.getMinimum());
                rightLabel = (JLabel)labels.get(slider.getMaximum());
                
                leftLabel.setText(leftText + " (" + slider.getValue() + valueUnit + ")");
                rightLabel.setText(rightText + " (" + (slider.getMaximum() - slider.getValue()) + valueUnit + ")");
                slider.repaint();
            }
        
        });
        
        return slider;
    }
    
    private GridBagConstraints createConstraints(int anchor, int fill, int gridX, int gridY, int gridWidth,
                                                 int gridHeight, double weightX, double weightY) {
        GridBagConstraints constraints;
        
        constraints            = new GridBagConstraints();
        constraints.anchor     = anchor;
        constraints.fill       = fill;
        constraints.gridx      = gridX;
        constraints.gridy      = gridY;
        constraints.gridwidth  = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.weightx    = weightX;
        constraints.weighty    = weightY;
        constraints.insets     = new Insets(2, 10, 2, 10);
        
        return constraints;
    }
    
    private GridBagConstraints createConstraints(int fill, int gridX, int gridY, int gridWidth,
                                                 int gridHeight, double weightX, double weightY) {
        
        return this.createConstraints(GridBagConstraints.WEST, fill, gridX, gridY, gridWidth, gridHeight, weightX, weightY);
    }
    
    private GridBagConstraints createConstraints(int fill, int gridX, int gridY, int gridWidth,
                                                 double weightX, double weightY) {
        
        return this.createConstraints(fill, gridX, gridY, gridWidth, 1, weightX, weightY);
    }
    
    private GridBagConstraints createConstraints(int gridX, int gridY, int gridWidth, double weightX, 
                                                 double weightY) {
        
        return this.createConstraints(GridBagConstraints.HORIZONTAL, gridX, gridY, gridWidth, weightX, weightY);
    }
    
    private GridBagConstraints createConstraints(int gridX, int gridY, int gridWidth, double weightX) {
        return this.createConstraints(gridX, gridY, gridWidth, weightX, 0);
    }
    
    private GridBagConstraints createConstraints(int gridX, int gridY, int gridWidth) {
        return this.createConstraints(gridX, gridY, gridWidth, 1);
    }
    
    private GridBagConstraints createConstraints(int gridX, int gridY) {
        return this.createConstraints(gridX, gridY, 1);
    }
    
    private int mantainBetween(int value, int min, int max) {
        if(value < min) {
            value = min;
        }
        if(value > max) {
            value = max;
        }
        
        return value;
    }
    
    private double mantainBetween(double value, double min, double max) {
        if(value < min) {
            value = min;
        }
        if(value > max) {
            value = max;
        }
        
        return value;
    }
}
