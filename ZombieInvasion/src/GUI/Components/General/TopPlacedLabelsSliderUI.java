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

import java.awt.Dimension;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author Xavier
 */
public class TopPlacedLabelsSliderUI extends BasicSliderUI {
    //Public Constructors
    public TopPlacedLabelsSliderUI(JSlider b) {
        super(b);
    }
    
    //Overridden Methods
    @Override
    protected Dimension getThumbSize() {
        Dimension size = new Dimension();

        if ( slider.getOrientation() == JSlider.VERTICAL ) {
            size.width = 20;
            size.height = 5;
        }
        else {
            size.width = 5;
            size.height = 20;
        }

        return size;
    }
    
    @Override
    protected void calculateGeometry() {
        calculateFocusRect();
        calculateContentRect();
        calculateThumbSize();
        calculateTrackBuffer();
        
        if(slider.getPaintLabels() && slider.getOrientation() == JSlider.HORIZONTAL) {
            //Place thie calculation of the labels' rectangle before the track's rectangle
            this.calculateLabelRect(); 
            this.calculateTrackRect();
            calculateTickRect();
        }
        else {
            super.calculateTrackRect();
            calculateTickRect();
            super.calculateLabelRect();
        }
        
        calculateThumbLocation();
    }
    
    @Override
    protected void calculateLabelRect() {
        int centerSpacing; // used to center sliders added using BorderLayout.CENTER (bug 4275631)
        
        centerSpacing = thumbRect.height;
        if ( slider.getPaintTicks() ) {
            centerSpacing += getTickLength();
        }
        if ( slider.getPaintLabels() ) {
            centerSpacing += getHeightOfTallestLabel();
        }
        labelRect.x = contentRect.x;
        labelRect.y = contentRect.y + (contentRect.height - centerSpacing - 1)/2;
        labelRect.width = contentRect.width;
        labelRect.height = getHeightOfTallestLabel();
    }
    
    @Override
    protected void calculateTrackRect() {
        trackRect.x = contentRect.x + trackBuffer;
        trackRect.y = labelRect.y + labelRect.height;
        trackRect.width = contentRect.width - (trackBuffer * 2);
        trackRect.height = thumbRect.height;
    }
}