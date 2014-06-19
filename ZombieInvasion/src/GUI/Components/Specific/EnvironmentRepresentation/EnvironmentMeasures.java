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

/**
 * This class contains necessary values to paint an environment representation.
 * @author Xavier
 */
class EnvironmentMeasures {
    //Attributes
    /**
     * The representation's X component of the top-left point
     */
    private double x0;
    
    /**
     * The representation's Y component of the top-left point
     */
    private double y0;
    
    /**
     * The representation's horizontal scale factor
     */
    private double horizontalGrowthFactor;
    
    /**
     * The representation's vertical scale factor
     */
    private double verticalGrowthFactor;
    
    //Public Constructors
    public EnvironmentMeasures(double x0, double y0, double horizontalGrowthFactor, double verticalGrowthFactor) {
        this.x0                     = x0;
        this.y0                     = y0;
        this.horizontalGrowthFactor = horizontalGrowthFactor;
        this.verticalGrowthFactor   = verticalGrowthFactor;
    }
    
    //Public Methods
    /**
     * Returns the representation's X component of the top-left point
     * @return the X component
     */
    public double getX0() {
        return x0;
    }

    /**
     * Returns the representation's Y component of the top-left point
     * @return the Y component
     */
    public double getY0() {
        return y0;
    }

    /**
     * Returns the representation's horizontal scale factor.
     * @return the scale factor
     */
    public double getHorizontalGrowthFactor() {
        return horizontalGrowthFactor;
    }
    
    /**
     * Returns the representation's vertical scale factor.
     * @return the scale factor
     */
    public double getVerticalGrowthFactor() {
        return verticalGrowthFactor;
    }
}
