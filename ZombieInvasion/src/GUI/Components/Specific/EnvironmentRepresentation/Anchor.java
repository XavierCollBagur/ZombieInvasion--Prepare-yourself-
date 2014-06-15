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
public enum Anchor {
    Center,
    CenterEast,
    NorthCenter,
    NorthEast,
    NorthWest,
    SouthCenter,
    SouthEast,
    SouthWest,
    CenterWest;
    
    public boolean isHorizontalCentered() {
        return this == Center || this == NorthCenter || this == SouthCenter;
    }
    
    public boolean isVerticalCentered() {
        return this == Center || this == CenterEast || this == CenterWest;
    }
    
    public boolean hasEastComponent() {
        return this == CenterEast || this == NorthEast || this == SouthEast;
    }
    
    public boolean hasNorthComponent() {
        return this == NorthCenter || this == NorthEast || this == NorthWest;
    }
    
    public boolean hasSouthComponent() {
        return this == SouthCenter || this == SouthEast || this == SouthWest;
    }
    
    public boolean hasWestComponent() {
        return this == CenterWest || this == NorthWest || this == SouthWest;
    }
    
}
