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

package GUI.Components.General;

import Geometry.Triangle2D;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author Xavier
 */
public class CarouselPanel extends JPanel {
    //Constants
    private final static Color DEFAULT_ARROWS_COLOR            = Color.BLACK;
    private final static int   DEFAULT_ARROWS_PREFERRED_WIDTH  = 30,
                               DEFAULT_ARROWS_PREFERRED_HEIGHT = 30;
    
    //Attributes
    private CardLayout contentLayout;
    private JPanel contentPanel;
    private Color arrowsColor;
    private Dimension arrowsPreferredSize;
    
    //Public Constructors
    public CarouselPanel() {
        this.arrowsColor         = CarouselPanel.DEFAULT_ARROWS_COLOR;
        this.arrowsPreferredSize = new Dimension(CarouselPanel.DEFAULT_ARROWS_PREFERRED_WIDTH, 
                                                 CarouselPanel.DEFAULT_ARROWS_PREFERRED_HEIGHT);
        
        initComponents();
    }

    //Overridden Methods
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        this.contentPanel.add(comp, index);
    }

    @Override
    public void setLayout(LayoutManager mgr) {}
    
    //Public Methods
    public Color getArrowsColor() {
        return arrowsColor;
    }

    public void setArrowsColor(Color arrowsColor) {
        this.arrowsColor = arrowsColor;
    }

    public Dimension getArrowsPreferredSize() {
        return arrowsPreferredSize;
    }

    public void setArrowsPreferredSize(int width, int height) {
        this.arrowsPreferredSize.setSize(width, height);
    }
    
    public void setArrowsPreferredSize(Dimension arrowsPreferredSize) {
        this.arrowsPreferredSize.setSize(arrowsPreferredSize);
    }
    
    //Private Methods
    private void initComponents() {
        JComponent leftArrowComponent, rightArrowComponent;
        
        this.contentLayout  = new CardLayout();
        this.contentPanel   = new JPanel(contentLayout);
        leftArrowComponent  = this.createLeftArrowComponent();
        rightArrowComponent = this.createRightArrowComponent();
        
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        this.contentPanel.setOpaque(false);
        
        super.setLayout(new BorderLayout());
        super.addImpl(leftArrowComponent, BorderLayout.WEST, 0);
        super.addImpl(this.contentPanel, BorderLayout.CENTER, 1);
        super.addImpl(rightArrowComponent, BorderLayout.EAST, 2);
    }
    
    private JComponent createLeftArrowComponent() {
        JComponent component;
        
        component = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                int arrowWidth, arrowHeight, preferredWidth, preferredHeight;
                double x1, y1, x2, y2, y3, x3;
                Graphics2D g2d;
                
                g2d             = (Graphics2D)g;
                preferredWidth  = CarouselPanel.this.arrowsPreferredSize.width;
                preferredHeight = CarouselPanel.this.arrowsPreferredSize.height;
                arrowWidth      = Math.min(preferredWidth, this.getWidth());
                arrowHeight     = Math.min(preferredHeight, this.getHeight());
                x1              = (this.getWidth() + arrowWidth) / 2;
                y1              = (this.getHeight() - arrowHeight) / 2;
                x2              = (this.getWidth() + arrowWidth) / 2;
                y2              = (this.getHeight() + arrowHeight) / 2;
                x3              = (this.getWidth() - arrowWidth) / 2;
                y3              = this.getHeight() / 2;
                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CarouselPanel.this.arrowsColor);
                g2d.fill(new Triangle2D.Double(x1, y1, x2, y2, x3, y3));
            }
            
        };
        
        component.setPreferredSize(this.arrowsPreferredSize);
        
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                CarouselPanel.this.contentLayout.previous(CarouselPanel.this.contentPanel);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
 
        return component;
    }
    
    private JComponent createRightArrowComponent() {
        JComponent component;
        
        component = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                int arrowWidth, arrowHeight, preferredWidth, preferredHeight;
                double x1, y1, x2, y2, y3, x3;
                Graphics2D g2d;
                
                g2d             = (Graphics2D)g;
                preferredWidth  = CarouselPanel.this.arrowsPreferredSize.width;
                preferredHeight = CarouselPanel.this.arrowsPreferredSize.height;
                arrowWidth      = Math.min(preferredWidth, this.getWidth());
                arrowHeight     = Math.min(preferredHeight, this.getHeight());
                x1              = (this.getWidth() - arrowWidth) / 2;
                y1              = (this.getHeight() - arrowHeight) / 2;
                x2              = (this.getWidth() - arrowWidth) / 2;
                y2              = (this.getHeight() + arrowHeight) / 2;
                x3              = (this.getWidth() + arrowWidth) / 2;
                y3              = this.getHeight() / 2;
                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(CarouselPanel.this.arrowsColor);
                g2d.fill(new Triangle2D.Double(x1, y1, x2, y2, x3, y3));
            }
            
        };
        
        component.setPreferredSize(this.arrowsPreferredSize);
        
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                CarouselPanel.this.contentLayout.next(CarouselPanel.this.contentPanel);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        return component;
    }
}
