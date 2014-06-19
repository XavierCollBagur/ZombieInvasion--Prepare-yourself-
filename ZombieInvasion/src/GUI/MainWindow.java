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

package GUI;

import GUI.Sections.MainMenu;
import Media.Images.ImagesDirectory;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Frame of the application. It contains a tabbed pane which has the main menu tab
 * by default.
 * @author Xavier
 */
public class MainWindow  extends JFrame {
    //Attributes
    private JTabbedPane tabs;
    
    //Public Constructors
    public MainWindow() {
        super("Zombie Invasion: Prepare yourself!");
        
        initComponents();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700, 600);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        
        try {
            this.setIconImage(ImagesDirectory.APPLICATION_ICON_IMAGE);
        } catch (Exception ex) {}
    }
    
    //Private Methods
    /**
     * Adds a tabbed pane with the application's main menu to the window.
     */
    private void initComponents() {
        Container windowContainer;
        MainMenu mainMenu;
       
        windowContainer        = this.getContentPane();
        this.tabs              = new JTabbedPane();
        mainMenu               = new MainMenu();
        
        this.tabs.addTab("Menú principal", mainMenu);
        windowContainer.add(tabs, BorderLayout.CENTER);
    }
}
