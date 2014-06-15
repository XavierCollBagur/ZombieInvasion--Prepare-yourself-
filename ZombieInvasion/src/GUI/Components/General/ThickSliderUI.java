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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author Xavier
 */
public class ThickSliderUI extends BasicSliderUI {
    //Attributes
    private Color thumbColor, leftColor, rightColor, linesColor;
    
    //Public Constructors
    public ThickSliderUI(JSlider b, Color thumbColor, Color leftColor, 
                          Color rightColor, Color linesColor) {
        super(b);
        
        this.thumbColor = thumbColor;
        this.leftColor  = leftColor;
        this.rightColor = rightColor;
        this.linesColor = linesColor;
    }
    
    public ThickSliderUI(JSlider b) {
        this(b, Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK);
    }

    //Overridden Methods
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        this.calculateGeometry();
        super.paint(g, c);
    }
    
    @Override
    protected void paintHorizontalLabel( Graphics g, int value, Component label ) {
        label.setSize(label.getPreferredSize());
        super.paintHorizontalLabel(g, value, label);
    }
    
    @Override
    protected Dimension getThumbSize() {
        return new Dimension(30, 30);
    }

    @Override
    public void paintTrack(Graphics g) {
        double trackPart1X, trackPart1Y, trackPart1Width, trackPart1Height, 
               trackPart2X, trackPart2Y, trackPart2Width, trackPart2Height,
               trackEnd1X, trackEnd1Y, trackEnd2X, trackEnd2Y;
        Color trackPart1Color, trackPart2Color;
        Graphics2D g2d;
        Dimension thumbSize;
        Ellipse2D.Double trackEnd;
        Rectangle2D.Double trackPart;
        
        g2d       = (Graphics2D) g;
        trackEnd  = new Ellipse2D.Double();
        thumbSize = this.getThumbSize();
        trackPart = new Rectangle2D.Double();
        
        switch(this.slider.getOrientation()) {
            case SwingConstants.HORIZONTAL:
                trackPart1X      = this.trackRect.x;
                trackPart1Y      = this.trackRect.y + 1;
                trackPart1Width  = this.thumbRect.x + this.thumbRect.width / 2 - trackPart1X + 1;
                trackPart1Height = this.trackRect.height - 1;
                trackPart2X      = this.thumbRect.x + this.thumbRect.width / 2 + 1;
                trackPart2Y      = this.trackRect.y + 1;
                trackPart2Width  = this.trackRect.getMaxX() - trackPart2X + 1;
                trackPart2Height = this.trackRect.height - 1;
                trackEnd1X       = this.trackRect.x - thumbSize.width / 2 + 1;
                trackEnd1Y       = this.trackRect.y + 1;
                trackEnd2X       = this.trackRect.getMaxX() - 1 - thumbSize.width / 2 + 1;
                trackEnd2Y       = this.trackRect.y + 1;
                
                if(!this.slider.getInverted()) {
                    trackPart1Color = this.leftColor;
                    trackPart2Color = this.rightColor;
                }
                else {
                    trackPart1Color = this.rightColor;
                    trackPart2Color = this.leftColor;
                }
                break;
            default://case SwingConstants.VERTICAL:
                trackPart1X      = this.trackRect.x + 1;
                trackPart1Y      = this.trackRect.y;
                trackPart1Width  = this.trackRect.width - 1;
                trackPart1Height = this.thumbRect.y  + this.thumbRect.height / 2 - trackPart1Y + 1;
                trackPart2X      = this.trackRect.x + 1;
                trackPart2Y      = this.thumbRect.y + this.thumbRect.height / 2 + 1;
                trackPart2Width  = this.trackRect.width - 1;
                trackPart2Height = this.trackRect.getMaxY() - trackPart2Y + 1;
                trackEnd1X       = trackRect.x + 1;
                trackEnd1Y       = trackRect.y  - thumbSize.height / 2 + 1;
                trackEnd2X       = trackRect.x + 1;
                trackEnd2Y       = trackRect.getMaxY() - 1 - thumbSize.height / 2 + 1;
                
                if(!this.slider.getInverted()) {
                    trackPart1Color = this.rightColor;
                    trackPart2Color = this.leftColor;
                }
                else {
                    trackPart1Color = this.leftColor;
                    trackPart2Color = this.rightColor;
                }
        }
        
        if(this.linesColor != null) {
            g2d.setColor(this.linesColor);
            g2d.draw(this.trackRect);
            trackEnd.setFrame(trackEnd1X - 1, trackEnd1Y - 1, thumbSize.width, thumbSize.height);
            g2d.draw(trackEnd);
            trackEnd.setFrame(trackEnd2X - 1, trackEnd2Y - 1, thumbSize.width, thumbSize.height);
            g2d.draw(trackEnd);
        }
        
        //Paint the first part of the track
        g2d.setColor(trackPart1Color);
        trackEnd.setFrame(trackEnd1X, trackEnd1Y, thumbSize.width - 1, thumbSize.height - 1);
        g2d.fill(trackEnd);
        trackPart.setFrame(trackPart1X, trackPart1Y, trackPart1Width, trackPart1Height);
        g2d.fill(trackPart);
        
        //Paint the second part of the track
        g2d.setColor(trackPart2Color);
        trackPart.setFrame(trackPart2X, trackPart2Y, trackPart2Width, trackPart2Height);
        g2d.fill(trackPart);
        trackEnd.setFrame(trackEnd2X, trackEnd2Y, thumbSize.width - 1, thumbSize.height - 1);
        g2d.fill(trackEnd);
        
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d;
        Ellipse2D.Double thumb;
        
        g2d   = (Graphics2D) g;
        thumb = new Ellipse2D.Double(this.thumbRect.x, this.thumbRect.y, 
                                     this.thumbRect.width, this.thumbRect.height);
        
        g2d.setColor(this.thumbColor);
        g2d.fill(thumb);
        
        if(this.linesColor != null) {
            g2d.setColor(this.linesColor);
            g2d.draw(thumb);
        }
    }
    
    //Public Methods
    public Color getThumbColor() {
        return thumbColor;
    }

    public Color getLeftColor() {
        return leftColor;
    }

    public Color getRightColor() {
        return rightColor;
    }

    public Color getLinesColor() {
        return linesColor;
    }

    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
    }

    public void setLeftColor(Color leftColor) {
        this.leftColor = leftColor;
    }

    public void setRightColor(Color rightColor) {
        this.rightColor = rightColor;
    }

    public void setLinesColor(Color linesColor) {
        this.linesColor = linesColor;
    }
}