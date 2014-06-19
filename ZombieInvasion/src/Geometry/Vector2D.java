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

/**
 * This class represents a vector in 2D coordinates
 * @author Xavier
 */
public class Vector2D {
    //Attributes
    /**
     * X component of the vector.
     */
    private double directionX;
    
    /**
     * Y component of the vector.
     */
    private double directionY;
    
    //Public Constructors
    public Vector2D(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }
    
    public Vector2D(Vector2D other) {
        this(other.directionX, other.directionY);
    }
    
    //Public Methods
    /**
     * Returns the X component of the vector.
     * @return the X component
     */
    public double getDirectionX() {
        return this.directionX;
    }

    /**
     * Returns the Y component of the vector.
     * @return the Y component
     */
    public double getDirectionY() {
        return this.directionY;
    }
    
    /**
     * Returns the magnitude of the vector.
     * @return the magintude
     */
    public double getMagnitude() {
        return Math.sqrt(this.getSquaredMagnitude());
    }
    
    /**
     * Returns the squared magnitude of the vector.
     * @return the squared magniture
     */
    public double getSquaredMagnitude() {
        return this.directionX * this.directionX + this.directionY * this.directionY;
    }
    
    /**
     * Returns the dot product of the vector with another.
     * @param other the other vector
     * @return the dot product
     */
    public double getDotProduct(Vector2D other) {
        return this.directionX * other.directionX + this.directionY * other.directionY;
    }
    
    /**
     * Returns the angle between the vector and another
     * @param other the other vector
     * @return the angle (in radians)
     */
    public double getAngle(Vector2D other) {
        double dotProduct, mySquaredMagnitude, otherSquaredMagnitude;
        
        dotProduct            = this.getDotProduct(other);
        mySquaredMagnitude    = this.getSquaredMagnitude();
        otherSquaredMagnitude = other.getSquaredMagnitude();
        
        return Math.acos(dotProduct / Math.sqrt(mySquaredMagnitude * otherSquaredMagnitude));
    }

    /**
     * Sets the X component of the vector.
     * @param directionX the X component
     */
    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    /**
     * Sets the Y component of the vector.
     * @param directionY the Y component
     */
    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }
    
    /**
     * Sets both X and Y components of the vector
     * @param directionX the X component
     * @param directionY the Y component
     */
    public void setDirection(double directionX, double directionY) {
        this.setDirectionX(directionX);
        this.setDirectionY(directionY);
    }
    
    /**
     * Changes the X and Y component in order to obtain a new magnitude without
     * change the direction of the vector.
     * @param magnitude the new magnitude of the vector
     */
    public void setMagnitude(double magnitude) {
        double oldMagnitude;
        
        oldMagnitude    = this.getMagnitude();
        this.directionX = (this.directionX / oldMagnitude) * magnitude;
        this.directionY = (this.directionY / oldMagnitude) * magnitude;
    }
    
    /**
     * Rotate the vector an angle
     * @param radians the angle to rotate (in radians)
     */
    public void rotate(double radians) {
        double newX, newY, cos, sin;
        
        cos = Math.cos(radians);
        sin = Math.sin(radians);
        
        newX = this.directionX * cos - this.directionY * sin;
        newY = this.directionY * cos + this.directionX * sin;
        
        this.setDirection(newX, newY);
    }
    
    /**
     * Reflects the vector against another
     * @param reflectionVector the other vector
     */
    public void reflect(Vector2D reflectionVector) {
        double dotProduct, reflVectorMagn;
        
        dotProduct       = this.getDotProduct(reflectionVector);
        reflVectorMagn   = reflectionVector.getMagnitude();
        this.directionX -=  2 * dotProduct * reflectionVector.directionX / reflVectorMagn;
        this.directionY -=  2 * dotProduct * reflectionVector.directionY / reflVectorMagn; 
    }
}
    

