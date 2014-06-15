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

import Agents.Base.AgentLifeStatus;
import Agents.Base.AgentsUtils;
import Agents.Base.BaseInformation;
import Agents.Human.HumanInformation;
import Environment.EnvironmentWall;
import Environment.ZombieEpidemicEnvironmentInformation;
import Geometry.Cell;
import SimulationConfiguration.EnvironmentConfiguration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import javax.swing.JPanel;

/**
 *
 * @author Xavier
 */
public class ZombieEpidemicEnvironmentRepresentation extends JPanel{
    //Constants
    private final static Color HEALTHY_HUMAN_COLOR     = new Color(0x26bc22),//Color.GREEN,
                               INFECTED_HUMAN_COLOR    = new Color(0xefff51),//Color.YELLOW,
                               ZOMBIE_COLOR            = new Color(0xcd2529),//Color.RED,
                               GRID_LINE_COLOR         = Color.BLACK,
                               GRID_BACKGROUND_COLOR   = Color.WHITE,
                               WALL_COLOR              = Color.BLACK,
                               SHOT_LINE_COLOR         = Color.BLACK,
                               CROSS_COLOR             = Color.BLACK,
                               INACCESSIBLE_CELL_COLOR = Color.BLACK;
    
    private final static float GRID_LINE_WIDTH     = 1.1f,
                               WALL_WIDTH          = 2, 
                               SHOT_LINE_WIDTH     = 1.1f,
                               CROSS_LINES_WIDTH   = 1.1f,
                               AGENT_CONTOUR_WIDTH = 1.1f;
    
    //Attributes
    private EnvironmentConfiguration environmentConfiguration;
    private ZombieEpidemicEnvironmentInformation environmentInformation; 
    private boolean mantainAspectRatio;
    private Anchor mantainAspectRatioAnchor;
    
    //Public Constructors
    public ZombieEpidemicEnvironmentRepresentation(EnvironmentConfiguration environmentConfiguration,
                                                   ZombieEpidemicEnvironmentInformation environmentInformation) {
        int environmentWidth, environmentHeight;
        Dimension preferredSize;
      
        this.environmentConfiguration = environmentConfiguration;
        this.environmentInformation   = environmentInformation;
        this.mantainAspectRatio       = false;
        this.mantainAspectRatioAnchor = Anchor.NorthWest;
        environmentWidth              = this.environmentConfiguration.getEnvironmentWidth();
        environmentHeight             = this.environmentConfiguration.getEnvironmentHeight();
        preferredSize                 = new Dimension(environmentWidth, environmentHeight);
        
        this.setPreferredSize(preferredSize);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
    }
    
    //Overriden Methods
    @Override
    protected void paintComponent(Graphics g) {
        Collection<BaseInformation> deadPopulation, alivePopulation;
        Collection<EnvironmentWall> walls;
        Collection<Line2D> shots;
        Collection<Cell> inaccessibleCells;
        EnvironmentMeasures measures;
        Graphics2D g2d;
        
        super.paintComponent(g);
        
        g2d                    = (Graphics2D)g;
        measures               = this.getMeasures();
        deadPopulation         = this.environmentInformation.getDeadPopulation();
        alivePopulation        = this.environmentInformation.getAlivePopulation();
        walls                  = this.environmentInformation.getWalls();
        shots                  = this.environmentInformation.getShots();
        inaccessibleCells      = this.environmentConfiguration.getInaccessibleCells();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(measures.getX0(), measures.getY0());
        g2d.scale(measures.getHorizontalGrowthFactor(), measures.getVerticalGrowthFactor());
       
        this.paintGrid(g2d, inaccessibleCells);        
        this.paintPopulation(g2d, deadPopulation, alivePopulation);
        this.paintWalls(g2d, walls);
        this.paintHumanShots(g2d, shots);
    }
    
    //Public Methods
    public boolean isMantainAspectRatio() {
        return this.mantainAspectRatio;
    }
    
    public Anchor getMantainAspectRatioAnchor() {
        return this.mantainAspectRatioAnchor; 
    }

    public EnvironmentConfiguration getEnvironmentConfiguration() {
        return this.environmentConfiguration;
    }

    public ZombieEpidemicEnvironmentInformation getEnvironmentInformation() {
        return this.environmentInformation;
    }
   
    public void setMantainAspectRatio(boolean mantainAspectRatio) {
        this.mantainAspectRatio = mantainAspectRatio;
    }

    public void setMantainAspectRatioAnchor(Anchor mantainAspectRatioAnchor) {
        this.mantainAspectRatioAnchor = mantainAspectRatioAnchor;
    }

    public void setEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
    }

    public void setEnvironmentInformation(ZombieEpidemicEnvironmentInformation environmentInformation) {
        this.environmentInformation = environmentInformation;
    }
    
    public Point2D getEnvironmentPoint(Point2D environmentRepresentationPoint) {
        double environmentPointX, environmentPointY;
        Point2D environmentPoint;
        Rectangle2D drawingRectangle;
        EnvironmentMeasures measures;
        
        environmentPoint = null;
        measures         = this.getMeasures();
        drawingRectangle = this.getDrawingRectangle();
        
        if(drawingRectangle.contains(environmentRepresentationPoint)) {
            environmentPointX = (environmentRepresentationPoint.getX() - drawingRectangle.getMinX()) / measures.getHorizontalGrowthFactor();
            environmentPointY = (environmentRepresentationPoint.getY() - drawingRectangle.getMinY()) / measures.getVerticalGrowthFactor();
            environmentPoint  = new Point2D.Double(environmentPointX, environmentPointY);
        }
        
        return environmentPoint;
    }
    
    public Cell getEnvironmentCell(Point2D environmentRepresentationPoint) {
        final int cellWidth, cellHeight, row, column;
        Point2D environmentPoint;
        Cell cell;
        
        environmentPoint = this.getEnvironmentPoint(environmentRepresentationPoint);
        cell             = null;
        
        if(environmentPoint != null) {
            cellWidth  = this.environmentConfiguration.getCellWidth();
            cellHeight = this.environmentConfiguration.getCellHeight();
            row        = (int)(environmentPoint.getY() / cellHeight);
            column     = (int)(environmentPoint.getX() / cellWidth);
            cell       = new Cell(row, column);
        }
        
        return cell;
    }
    
    //Private Methods
    private EnvironmentMeasures getMeasures() {
        int minEnvironmentWidth, minEnvironmentHeight;
        double environmentWidth, environmentHeight, 
               x0, y0, horizontalGrowthFactor, verticalGrowthFactor;
        Rectangle2D drawingRectangle;
        
        minEnvironmentWidth    = this.environmentConfiguration.getEnvironmentWidth();
        minEnvironmentHeight   = this.environmentConfiguration.getEnvironmentHeight();
        drawingRectangle       = this.getDrawingRectangle();
        environmentWidth       = drawingRectangle.getWidth();
        x0                     = drawingRectangle.getMinX();
        y0                     = drawingRectangle.getMinY();
        environmentHeight      = drawingRectangle.getHeight();
        horizontalGrowthFactor = environmentWidth / minEnvironmentWidth;
        verticalGrowthFactor   = environmentHeight / minEnvironmentHeight;
        
        return new EnvironmentMeasures(x0, y0, horizontalGrowthFactor, verticalGrowthFactor);
    }
    
    private Rectangle2D getDrawingRectangle() {
        double x, y, width, height, environmentWidth, environmentHeight, 
               newWidth, newHeight, aspectRatio;
        
        x               = WALL_WIDTH;
        y               = WALL_WIDTH;
        width           = this.getWidth() - 2 * WALL_WIDTH;
        height          = this.getHeight() - 2 * WALL_WIDTH;
        
        if(mantainAspectRatio) {
            environmentWidth  = this.environmentConfiguration.getEnvironmentWidth();
            environmentHeight = this.environmentConfiguration.getEnvironmentHeight();
            aspectRatio       = (double)environmentWidth / environmentHeight;
            newWidth          = height * aspectRatio;
            
            if(newWidth <= width) {
                if(this.mantainAspectRatioAnchor.isHorizontalCentered()) {
                    x += (width - newWidth) / 2;
                }
                else if(this.mantainAspectRatioAnchor.hasEastComponent()) {
                    x += width - newWidth;
                }
                
                width = newWidth;
            }
            else {
                newHeight = width / aspectRatio;
                if(this.mantainAspectRatioAnchor.isVerticalCentered()) {
                    y += (height - newHeight) / 2;
                }
                else if(this.mantainAspectRatioAnchor.hasSouthComponent()) {
                    y += height - newHeight;
                }
                
                height = newHeight;
            }
        }
        
        return new Rectangle2D.Double(x, y, width, height);
    }
    
    private void paintGrid(Graphics2D g2d, Collection<Cell> inaccessibleCells) {
        final int rows, columns, cellWidth, cellHeight, environmentWidth, environmentHeight;
        final double xMin, yMin, xMax, yMax;
        double x, y;
        Line2D line;
        Rectangle2D rectangle;
        
        rows              = this.environmentConfiguration.getNumberOfRows();
        columns           = this.environmentConfiguration.getNumberOfColumns();
        cellWidth         = this.environmentConfiguration.getCellWidth();
        cellHeight        = this.environmentConfiguration.getCellHeight();
        environmentWidth  = this.environmentConfiguration.getEnvironmentWidth();
        environmentHeight = this.environmentConfiguration.getEnvironmentHeight();
        xMin              = 0;
        yMin              = 0;
        xMax              = environmentWidth - 1;
        yMax              = environmentHeight - 1;
        line              = new Line2D.Double();
        rectangle         = new Rectangle2D.Double();
        
        //Draw background
        rectangle.setFrame(0, 0, environmentWidth, environmentHeight);
        g2d.setColor(GRID_BACKGROUND_COLOR);
        g2d.fill(rectangle);
        
        //Draw vertical lines
        g2d.setColor(GRID_LINE_COLOR);
        g2d.setStroke(new BasicStroke((float)(GRID_LINE_WIDTH / g2d.getTransform().getScaleX())));
        
        for(int i = 0; i < columns; i++) {
            x = xMin + cellWidth * i;
            
            line.setLine(x, yMin, x, yMax);
            g2d.draw(line);
        }
        
        x = xMin + cellWidth * columns - 0.1;
        line.setLine(x, yMin, x, yMax);
        g2d.draw(line);
        
        //Draw horizontal lines
        for(int i = 0; i < rows; i++) {
            y = yMin + cellHeight * i;
            
            line.setLine(xMin, y, xMax, y);
            g2d.draw(line);
        }
        
        y = yMin + cellHeight * rows - 0.1;
        line.setLine(xMin, y, xMax, y);
        g2d.draw(line);
        
        //Paint inaccessible cells
        g2d.setColor(INACCESSIBLE_CELL_COLOR);
        
        for(Cell cell: inaccessibleCells) {
            x = xMin + cell.getColumn() * cellWidth;
            y = yMin + cell.getRow() * cellHeight;
            
            rectangle.setFrame(x, y, cellWidth, cellHeight);
            g2d.fill(rectangle);
        }
    }
    
    private void paintPopulation(Graphics2D g2d, Collection<BaseInformation> deadPopulation, 
                                 Collection<BaseInformation> alivePopulation) {
        
        final double agentWidth, agentHeight;
        
        agentWidth  = this.environmentConfiguration.getAgentWidth();
        agentHeight = this.environmentConfiguration.getAgentHeight();
        
        g2d.setFont(new Font(Font.SERIF,Font.PLAIN, (int)agentHeight));
        
        paintPopulation(g2d, deadPopulation, agentWidth, agentHeight);
        
        paintPopulation(g2d, alivePopulation, agentWidth, agentHeight);
    }
    
     private void paintPopulation(Graphics2D g2d, Collection<BaseInformation> population,
                                  double agentWidth, double agentHeight) {
        
         Color color;
         HumanInformation human;
         String text;
         
         for(BaseInformation information: population) {
            text = "";
            if(AgentsUtils.isHumanInformation(information)) {
                human = (HumanInformation)information;
                
                switch(human.getHealthStatus()) {
                    case Healthy:
                        color = HEALTHY_HUMAN_COLOR;
                        break;
                    default://case Infected:
                        color = INFECTED_HUMAN_COLOR;
                }
                
                if(human.isVaccinated()) {
                    text += "V";
                }
                
                if(human.getBullets() > 0) {
                    text += "A";
                }
            }
            else {
                color = ZOMBIE_COLOR;
            }
            
            paintAgent(g2d, information, color, text, agentWidth, agentHeight);
        }
     }
    
    private void paintAgent(Graphics2D g2d, BaseInformation agentInformation, Color agentColor,
                            String text, double agentWidth, double agentHeight) {
        
        double x, y, textX, textY;
        Point2D position;
        AgentLifeStatus lifeStatus;
        Ellipse2D.Double ellipse;
        FontMetrics fm;
        
        lifeStatus = agentInformation.getLifeStatus();
        position   = agentInformation.getPosition();
        x          = position.getX() - agentWidth / 2;
        y          = position.getY() - agentHeight / 2;
        ellipse    = new Ellipse2D.Double(x, y, agentWidth, agentHeight);
        
        g2d.setColor(agentColor);
        g2d.fill(ellipse);
        g2d.setStroke(new BasicStroke((float)(AGENT_CONTOUR_WIDTH / g2d.getTransform().getScaleX())));
        g2d.setColor(Color.BLACK);
        g2d.draw(ellipse);
        
        if(!text.equals("")) {
            fm    = g2d.getFontMetrics();
            textX = x + agentWidth / 2 - fm.stringWidth(text) / 2;
            textY = y + (agentHeight - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, (float)textX, (float)textY);
        }
        if(lifeStatus == AgentLifeStatus.Dead){
            this.paintCross(g2d, x, y, agentWidth, agentHeight);
        }
    }
    
    private void paintCross(Graphics2D g2d, double x, double y, double width, double height) {
        double x0, y0, x1, y1;
        Line2D.Double line;
        
        g2d.setColor(CROSS_COLOR);
        g2d.setStroke(new BasicStroke((float)(CROSS_LINES_WIDTH / g2d.getTransform().getScaleX())));
        
        x0   = x;
        y0   = y;
        x1   = x + width;
        y1   = y + height;
        line = new Line2D.Double(x0, y0, x1, y1);
        
        g2d.draw(line);
        
        x0 = x;
        y0 = y + height;
        x1 = x + width;
        y1 = y;
        
        line.setLine(x0, y0, x1, y1);
        g2d.draw(line);
    }

    private void paintWalls(Graphics2D g2d, Collection<EnvironmentWall> walls) {
        paintLines(g2d, walls, WALL_WIDTH, WALL_COLOR);
    }

    private void paintHumanShots(Graphics2D g2d, Collection<Line2D> shots) {
        paintLines(g2d, shots, SHOT_LINE_WIDTH, SHOT_LINE_COLOR);
    }
    
    private void paintLines(Graphics2D g2d, Collection<? extends Line2D> lines,
                            float strokeWidth, Color linesColor) {
        
        g2d.setColor(linesColor);
        g2d.setStroke(new BasicStroke((float)(strokeWidth / g2d.getTransform().getScaleX())));
        
        for(Line2D line: lines) {
            g2d.draw(line);
        }
    }
}
