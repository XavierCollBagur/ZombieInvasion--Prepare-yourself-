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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class contains a util methods to solve geometry problems
 * @author Xavier
 */
public class GeometryUtils {
    //Private Constants
    private static int INSIDE = 0, // 0000
                       LEFT   = 1, // 0001
                       RIGHT  = 2, // 0010
                       BOTTOM = 4, // 0100
                       TOP    = 8; // 1000
    
    //Public Static Methods
    /**
     * Returns the nearest point to a reference point.
     * @param refPointX the X component of the reference point
     * @param refPointY the Y component of the reference point
     * @param points the list of points
     * @return the nearest point in the list
     */
    public static Point2D getNearestPoint(double refPointX, double refPointY, Collection<Point2D> points) {
        Point2D nearestPoint;
        double minSquaredDistance, squaredDistance;
        
        nearestPoint       = null;
        minSquaredDistance = Double.POSITIVE_INFINITY;
        
        for(Point2D point: points) {
            squaredDistance = Point2D.distanceSq(refPointX, refPointY, point.getX(), point.getY());
            
            if(squaredDistance < minSquaredDistance) {
                minSquaredDistance = squaredDistance;
                nearestPoint       = point;
            }
        }
        
        return nearestPoint;
    }
    
    /**
     * Returns the nearest point to a reference point.
     * @param refPoint the reference point
     * @param points the list of points
     * @return the nearest point in the list
     */
    public static Point2D getNearestPoint(Point2D refPoint, Collection<Point2D> points) {
        
        return GeometryUtils.getNearestPoint(refPoint.getX(), refPoint.getY(), points);
    }
    
    /**
     * Returns the nearest point of an INFINITE line to a reference point.
     * @param pointX the X component of the reference point
     * @param pointY the Y component of the reference point
     * @param lineX1 the X component of the first given point of the line
     * @param lineY1 the Y component of the first given point of the line
     * @param lineX2 the X component of the second given point of the line
     * @param lineY2 the Y component of the second given point of the line
     * @return the nearest point of the line
     */
    public static Point2D getNearestPointInLine(double pointX, double pointY, 
                                                double lineX1, double lineY1, double lineX2, double lineY2) {
        double k;
        
        k = ((lineY2 - lineY1) * (pointX - lineX1) - (lineX2 - lineX1) * (pointY - lineY1)) 
            / ((lineY2 - lineY1) * (lineY2 - lineY1) + (lineX2 - lineX1) * (lineX2 - lineX1));
                
        return new Point2D.Double(pointX - k * (lineY2 - lineY1), pointY + k * (lineX2 - lineX1));
    }
    
    /**
     * Returns the nearest point of an INFINITE line to a reference point.
     * @param point the reference point
     * @param line a line segment of the infinite line
     * @return the nearest point of the line
     */
    public static Point2D getNearestPointInLine(Point2D point, Line2D line) {
        
        return GeometryUtils.getNearestPointInLine(point.getX(), point.getY(), 
                                                   line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
    
    /**
     * Returns a list of the cells of an infinite grid crossed by a line segment
     * @param lineX1 the X component of the start point of the segment
     * @param lineY1 the Y component of the start point of the segment
     * @param lineX2 the X component of the end point of the segment
     * @param lineY2 the Y component of the end point of the segment
     * @param cellWidth cell's width
     * @param cellHeight cell's height
     * @return the list of the cells crossed by the segment
     */
    public static Collection<Cell> getGridCellsCrossedByLine(double lineX1, double lineY1, double lineX2, double lineY2,
                                                             double cellWidth, double cellHeight) {
        double dx, dy, yRef, slope;
        int minRow, minColumn, maxRow, maxColumn, xStep, yStep, row, column, 
            currentRow, currentColumn, numRows, numColumns;
        Collection<Cell> cells = new ArrayList<>();
        
        dx         = lineX2 - lineX1;
        dy         = lineY2 - lineY1;
        minRow     = (int)(lineY1 / cellHeight);
        minColumn  = (int)(lineX1 / cellWidth);
        maxRow     = (int)(lineY2 / cellHeight);
        maxColumn  = (int)(lineX2 / cellWidth);
        numRows    = Math.abs(maxRow - minRow) + 1;
        numColumns = Math.abs(maxColumn - minColumn) + 1;
        row        = minRow;
        column     = minColumn;
        xStep      = dx > 0 ? 1 : -1;
        yStep      = dy > 0 ? 1 : -1;
        
        if(numColumns >= numRows) {
            slope = dy / dx;
            
            if(minColumn < maxColumn) {
                yRef = lineY1 + slope * ((minColumn + 1) * cellWidth - lineX1); 
            }
            else {
                yRef = lineY1 + slope * (minColumn * cellWidth - lineX1);
            }
            
            for(int i = 0; i < numColumns - 1 ; i++) {
                currentRow = (int)(yRef / cellHeight);
                
                while(row != currentRow) {
                    cells.add(new Cell(row, column));
                    row += yStep;
                }
                cells.add(new Cell(row, column));
                
                column += xStep;
                yRef   += slope * (xStep * cellWidth);
            }
            
            while(row != maxRow) {
                cells.add(new Cell(row, column));
                row += yStep;
            }
            cells.add(new Cell(row, column));
            
        }
        else {
            double slopeInverse, xRef;
            
            slopeInverse = dx / dy;
            
            if(minRow < maxRow) {
                xRef = lineX1 + slopeInverse * ((minRow + 1) * cellHeight - lineY1); 
            }
            else {
                xRef = lineX1 + slopeInverse * (minRow * cellHeight - lineY1);
            }
            
            for(int i = 0; i < numRows - 1 ; i++) {
                currentColumn = (int)(xRef / cellWidth);
                
                while(column != currentColumn) {
                    cells.add(new Cell(row, column));
                    column += xStep;
                }
                cells.add(new Cell(row, column));
                
                row  += yStep;
                xRef += slopeInverse * (yStep * cellHeight);
            }
            
            while(column != maxColumn) {
                cells.add(new Cell(row, column));
                column += xStep;
            }
            cells.add(new Cell(row, column));
        }
        
        return cells;
    }
    
    /**
     * Returns a list of the cells of an infinite grid crossed by a line segment
     * @param line the line segment
     * @param cellWidth cell's width
     * @param cellHeight cell's height
     * @return a list of the cells crossed by the segment
     */
    public static Collection<Cell> getGridCellsCrossedByLine(Line2D line, double cellWidth, double cellHeight) { 
        
        return GeometryUtils.getGridCellsCrossedByLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), 
                                                       cellWidth, cellHeight);
    }
    
    /**
     * Returns the line that is obtained by clipping a line segment within the limits of a rectangle.
     * @param rectangleXMin the leftmost X coordinate within the rectangle
     * @param rectangleYMin the uppermost Y coordinate within the rectangle
     * @param rectangleXMax the rightmost X coordinate within the rectangle
     * @param rectangleYMax the lowermost Y coordinate within the rectangle
     * @param lineX1 the X component of the start point of the segment
     * @param lineY1 the Y component of the start point of the segment
     * @param lineX2 the X component of the end point of the segment
     * @param lineY2 the Y component of the end point of the segment
     * @return the clipped line
     */
    public static Line2D clipLine(double rectangleXMin, double rectangleYMin, double rectangleXMax, double rectangleYMax,
                                  double lineX1, double lineY1, double lineX2, double lineY2) {
        //Cohen-Sutherland algorithm implementation
        Line2D clippedLine;
        int point1Code, point2Code, pointCode;
        double x, y;
        boolean accept;
       
        point1Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX1, lineY1);
        point2Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX2, lineY2);
        accept     = false;
        
        x = -1;
        y = -1;
        
        while(true) {
            if((point1Code | point2Code) == 0) {
                accept = true;
                break;
            }
            else if((point1Code & point2Code) != 0){
                break;
            }
            else {
                if(point1Code != 0) {
                    pointCode = point1Code;
                }
                else {
                    pointCode = point2Code;
                }
                
                if ((pointCode & TOP) != 0) {          // point is above the clip rectangle
                    x = lineX1 + (lineX2  - lineX1) * (rectangleYMax - lineY1) / (lineY2 - lineY1);
                    y = rectangleYMax;
                } 
                else if ((pointCode & BOTTOM) != 0) { // point is below the clip rectangle
                    x = lineX1 + (lineX2  - lineX1) * (rectangleYMin - lineY1) / (lineY2 - lineY1);
                    y = rectangleYMin;
                } 
                else if ((pointCode & RIGHT) != 0) {  // point is to the right of clip rectangle
                    x = rectangleXMax;
                    y = lineY1 + (lineY2 - lineY1) * (rectangleXMax - lineX1) / (lineX2  - lineX1);
                } 
                else if ((pointCode & LEFT) != 0) {   // point is to the left of clip rectangle
                    x = rectangleXMin;
                    y = lineY1 + (lineY2 - lineY1) * (rectangleXMin - lineX1) / (lineX2  - lineX1);
                }
                
                if(pointCode == point1Code) {
                    lineX1     = x;
                    lineY1     = y;
                    point1Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX1, lineY1);
                }
                else {
                    lineX2     = x;
                    lineY2     = y;
                    point2Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX2, lineY2);
                }
            }
        }
        
        if(accept) {
            clippedLine = new Line2D.Double(lineX1, lineY1, lineX2, lineY2);
        }
        else {
            clippedLine = null;
        }
        
        return clippedLine;
    }
    /** 
     * Returns the line that is obtained by clipping a line segment within the limits of a rectangle.
     * @param rectangle the rectangle
     * @param line the original line
     * @return the clipped line
     */
    public static Line2D clipLine(Rectangle2D rectangle, Line2D line) {
        
        return GeometryUtils.clipLine(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), 
                                      line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
    
    /**
     * Checks if a line crosses a rectangle.
     * @param rectangleXMin the leftmost X coordinate within the rectangle
     * @param rectangleYMin the uppermost Y coordinate within the rectangle
     * @param rectangleXMax the rightmost X coordinate within the rectangle
     * @param rectangleYMax the lowermost Y coordinate within the rectangle
     * @param lineX1 the X component of the start point of the segment
     * @param lineY1 the Y component of the start point of the segment
     * @param lineX2 the X component of the end point of the segment
     * @param lineY2 the Y component of the end point of the segment
     * @return <code>true</code> if the line crosses the rectangle, <code>false</code> otherwise
     */
    public static boolean lineCrossesRectangle(double rectangleXMin, double rectangleYMin, double rectangleXMax, double rectangleYMax,
                                               double lineX1, double lineY1, double lineX2, double lineY2) {
        //Based on Cohen-Sutherland algorithm (its only difference is that this method
        //doesn't create a new Line2D object)
        int point1Code, point2Code, pointCode;
        double x, y;
        boolean cross;
       
        point1Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX1, lineY1);
        point2Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX2, lineY2);
        cross      = false;
        
        x = -1;
        y = -1;
        
        while(true) {
            if((point1Code | point2Code) == 0) {
                cross = true;
                break;
            }
            else if((point1Code & point2Code) != 0){
                break;
            }
            else {
                if(point1Code != 0) {
                    pointCode = point1Code;
                }
                else {
                    pointCode = point2Code;
                }
                
                if ((pointCode & TOP) != 0) {          // point is above the clip rectangle
                    x = lineX1 + (lineX2  - lineX1) * (rectangleYMax - lineY1) / (lineY2 - lineY1);
                    y = rectangleYMax;
                } 
                else if ((pointCode & BOTTOM) != 0) { // point is below the clip rectangle
                    x = lineX1 + (lineX2  - lineX1) * (rectangleYMin - lineY1) / (lineY2 - lineY1);
                    y = rectangleYMin;
                } 
                else if ((pointCode & RIGHT) != 0) {  // point is to the right of clip rectangle
                    x = rectangleXMax;
                    y = lineY1 + (lineY2 - lineY1) * (rectangleXMax - lineX1) / (lineX2  - lineX1);
                } 
                else if ((pointCode & LEFT) != 0) {   // point is to the left of clip rectangle
                    x = rectangleXMin;
                    y = lineY1 + (lineY2 - lineY1) * (rectangleXMin - lineX1) / (lineX2  - lineX1);
                }
                
                if(pointCode == point1Code) {
                    lineX1     = x;
                    lineY1     = y;
                    point1Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX1, lineY1);
                }
                else {
                    lineX2     = x;
                    lineY2     = y;
                    point2Code = GeometryUtils.getCohenSutherlandPointCode(rectangleXMin, rectangleYMin, rectangleXMax, rectangleYMax, lineX2, lineY2);
                }
            }
        }
        
        return cross;
    }
    
    /**
     * Checks if a line crosses a rectangle.
     * @param rectangle the rectangle
     * @param line the line
     * @return <code>true</code> if the line crosses the rectangle, <code>false</code> otherwise
     */
    public static boolean lineCrossesRectangle(Rectangle2D rectangle, Line2D line) {
        
        return GeometryUtils.lineCrossesRectangle(rectangle.getMinX(), rectangle.getMinY(), rectangle.getMaxX(), rectangle.getMaxY(), 
                                                  line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
    
    //Private Static Methods
    /**
     * Returns the point code used in the Cohen-Sutherland line clipping algorithm.
     */
    private static int getCohenSutherlandPointCode(double rectangleXMin, double rectangleYMin, double rectangleXMax, double rectangleYMax,
                                                   double pointX, double pointY) {
        int code;
        
        code = INSIDE;          
 
	if (pointX < rectangleXMin) {
            code |= LEFT;
        }
        else if (pointX > rectangleXMax) {
            code |= RIGHT;
        }
        
        if (pointY < rectangleYMin) {         
            code |= BOTTOM;
        }
        else if (pointY > rectangleYMax) {     
            code |= TOP;
        }
        
	return code;
    }
}
