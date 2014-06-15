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
import Agents.Zombie.ZombieInformation;
import Environment.EnvironmentWall;
import Environment.ZombieEpidemicEnvironmentInformation;
import GUI.Components.Specific.EnvironmentRepresentation.ZombieEpidemicEnvironmentRepresentation;
import Geometry.Cell;
import Geometry.GeometryUtils;
import Geometry.Triangle2D;
import Geometry.Vector2D;
import SimulationConfiguration.EnvironmentConfiguration;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Xavier
 */
public class DiversionDegreesRepresentation extends ZombieEpidemicEnvironmentRepresentation {
    //Constants
    private final static int    CELL_HEIGHT  = 400,
                                CELL_WIDTH   = CELL_HEIGHT,
                                AGENT_HEIGHT = (int)(CELL_HEIGHT / 4.0),
                                AGENT_WIDTH  = AGENT_HEIGHT;
    private final static double ZOMBIE_X     = CELL_WIDTH / 2.0, 
                                ZOMBIE_Y     = AGENT_HEIGHT / 2.0, 
                                HUMAN_X      = ZOMBIE_X, 
                                HUMAN_Y      = CELL_HEIGHT - AGENT_HEIGHT / 2.0;
    private final static Color  ANGLE_AREA_COLOR = new Color(0xFF, 0x7C, 0x00, 0x99);
    
    //Attributes
    private int diversionDegrees;
    
    //Public Constructors
    public DiversionDegreesRepresentation(int diversionDegrees) {
        super(createEnvironmentConfiguration(), createEnvironmentInformation());
        this.diversionDegrees = diversionDegrees;
    }
    
    //Overridden Methods

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d;
        
        g2d = (Graphics2D)g;
        
        super.paintComponent(g2d);
        this.paintAngle(g2d);
    }
    
    //Public Methods
    public int getDiversionDegrees() {
        return this.diversionDegrees;
    }

    public void setDiversionDegrees(int diversionDegrees) {
        this.diversionDegrees = diversionDegrees;
        this.repaint();
    }
    
    //Private Methods
    private void paintAngle(Graphics2D g2d) {
        double angleLineX1, angleLineY1, angleLineX2, angleLineY2, lineMagnitude,
               diversionRadians;
        Line2D angleLine1, angleLine2;
        Vector2D angleLineVector;
        Triangle2D area1;
        Rectangle2D area2;
        
        diversionRadians = Math.toRadians(this.diversionDegrees);
        angleLineVector  = new Vector2D(ZOMBIE_X - HUMAN_X, ZOMBIE_Y - HUMAN_Y);
        
        angleLineVector.setMagnitude(Math.hypot(CELL_WIDTH, CELL_HEIGHT));
        angleLineVector.rotate(diversionRadians);
        
        angleLineX1   = HUMAN_X;
        angleLineY1   = HUMAN_Y;
        angleLineX2   = angleLineX1 + angleLineVector.getDirectionX();
        angleLineY2   = angleLineY1 + angleLineVector.getDirectionY();
        angleLine1    = GeometryUtils.clipLine(0, 0, CELL_WIDTH, CELL_HEIGHT, angleLineX1, angleLineY1, angleLineX2, angleLineY2);
        lineMagnitude = Math.hypot(angleLine1.getX2() - angleLine1.getX1(), angleLine1.getY2() - angleLine1.getY1());
        
        angleLineVector.setMagnitude(lineMagnitude);
        angleLineVector.rotate(- 2 * diversionRadians);
        
        angleLineX2 = angleLineX1 + angleLineVector.getDirectionX();
        angleLineY2 = angleLineY1 + angleLineVector.getDirectionY();
        angleLine2  = new Line2D.Double(angleLineX1, angleLineY1, angleLineX2, angleLineY2);
        area1       = new Triangle2D.Double(HUMAN_X, HUMAN_Y, angleLine1.getX2(), angleLine1.getY2(),
                                           angleLine2.getX2(), angleLine2.getY2());
        area2       = new Rectangle2D.Double(0, 0, CELL_WIDTH, angleLine1.getY2());
        
        g2d.setColor(ANGLE_AREA_COLOR);
        g2d.fill(area1);
        g2d.fill(area2);
        
        g2d.setColor(Color.BLACK);
        g2d.draw(angleLine1);
        g2d.draw(angleLine2);
        
        
    }
    
    //Private Static Methods
    private static EnvironmentConfiguration createEnvironmentConfiguration() {
        EnvironmentConfiguration config;
        
        config = new EnvironmentConfiguration(1, 1, CELL_WIDTH, CELL_HEIGHT, 
                                              AGENT_WIDTH, AGENT_HEIGHT, Collections.<Cell>emptyList());
        
        return config;
    }
    
    private static ZombieEpidemicEnvironmentInformation createEnvironmentInformation() {
        ZombieEpidemicEnvironmentInformation info;
        ZombieInformation zombie;
        HumanInformation human;
        
        info   = new ZombieEpidemicEnvironmentInformation(Collections.<BaseInformation>emptyList(), new ArrayList<BaseInformation>(), Collections.<EnvironmentWall>emptyList(), Collections.<Line2D>emptyList());
        zombie = new ZombieInformation(ZOMBIE_X, ZOMBIE_Y);
        human  = new HumanInformation(HumanHealthStatus.Healthy, HUMAN_X, HUMAN_Y, 0, false, 0);
        
        info.getAlivePopulation().add(zombie);
        info.getAlivePopulation().add(human);
        
        return info;
    }
}
