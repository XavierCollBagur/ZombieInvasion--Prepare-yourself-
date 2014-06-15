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
 *
 * @author Xavier
 */
class EnvironmentMeasures {
    //Attributes
    private double x0, y0, horizontalGrowthFactor, verticalGrowthFactor;
    
    //Public Constructors
    public EnvironmentMeasures(double x0, double y0, double horizontalGrowthFactor, double verticalGrowthFactor) {
        this.x0                     = x0;
        this.y0                     = y0;
        this.horizontalGrowthFactor = horizontalGrowthFactor;
        this.verticalGrowthFactor   = verticalGrowthFactor;
    }
    
    //Public Methods
    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    public double getHorizontalGrowthFactor() {
        return horizontalGrowthFactor;
    }

    public double getVerticalGrowthFactor() {
        return verticalGrowthFactor;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public void setHorizontalGrowthFactor(double horizontalGrowthFactor) {
        this.horizontalGrowthFactor = horizontalGrowthFactor;
    }

    public void setVerticalGrowthFactor(double verticalGrowthFactor) {
        this.verticalGrowthFactor = verticalGrowthFactor;
    }
}
