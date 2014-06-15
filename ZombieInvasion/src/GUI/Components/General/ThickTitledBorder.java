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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author Xavier
 */
public class ThickTitledBorder extends AbstractBorder {
    //Attributes
    private Color backgroundColor, fontColor;
    private int titleHeight;
    String title;

    public ThickTitledBorder(String title, int titleHeight, Color backgroundColor, Color fontColor) {
        this.backgroundColor = backgroundColor;
        this.fontColor       = fontColor;
        this.titleHeight     = titleHeight;
        this.title           = title;
    }
    
    //Public Constructors
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d;
        FontMetrics fm;
        
        g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, titleHeight - 10));
      
        fm = g2d.getFontMetrics();
        
        g2d.setColor(this.backgroundColor);
        g2d.fillRect(x, y, width, this.titleHeight);
        
        g2d.setColor(this.fontColor);
        g2d.drawString(this.title, x + 5, y + (this.titleHeight - fm.getHeight()) / 2 + fm.getAscent());
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width - 1, this.titleHeight);
        g2d.drawRect(x, y + this.titleHeight, width - 1, height - this.titleHeight - 1);
        
    }
    
    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(this.titleHeight, 1, 1, 1);
        
        return insets;
    }
    
}
