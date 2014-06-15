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

package Geometry;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class describes a triangle defined by its 3 vertexes 
 * @author Xavier
 */
public abstract class Triangle2D implements Shape {
    //Attributes
    /**
     * Internal representation of the triangle
     */
    protected Path2D internalRepresentation;
    
    //Overridden Methods
    @Override
    public Rectangle getBounds() {
        return this.internalRepresentation.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return this.internalRepresentation.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return this.internalRepresentation.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return this.internalRepresentation.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return this.internalRepresentation.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return this.internalRepresentation.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return this.internalRepresentation.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return this.internalRepresentation.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return this.internalRepresentation.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return this.internalRepresentation.getPathIterator(at, flatness);
    }
 
    
    //Implementation classes
    
    /**
     * This class defines a triangle in float coordinates.
     */
    public static class Float extends Triangle2D {
        
        public Float(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.internalRepresentation = new Path2D.Float();
         
            this.internalRepresentation.moveTo(x1, y1);
            this.internalRepresentation.lineTo(x2, y2);
            this.internalRepresentation.lineTo(x3, y3);
            this.internalRepresentation.closePath();
        }
    }
    
    /**
     * This class defines a triangle in double coordinates.
     */
    public static class Double extends Triangle2D {
        
        public Double(double x1, double y1, double x2, double y2, double x3, double y3) {
            this.internalRepresentation = new Path2D.Double();
            
            this.internalRepresentation.moveTo(x1, y1);
            this.internalRepresentation.lineTo(x2, y2);
            this.internalRepresentation.lineTo(x3, y3);
            this.internalRepresentation.closePath();
        }
    }
}
