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

package Main;

import GUI.MainWindow;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Xavier
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MainWindow window;
   
        //Set the look and feel of the aplication as the operating system look and feel.
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        //Show a JOptionPane with the message of all uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                JOptionPane.showMessageDialog(null, getUncaughtExceptionMessage(e), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //Create a new application window and make it visible
        window = new MainWindow();
        
        window.setVisible(true);
    }
    
    /**
     * Returns a message of the exception to show in a JOptionPane
     * @param e the exception ocurred
     * @return the message to show
     */
    private static Object getUncaughtExceptionMessage(Throwable e) {
        StringBuilder message;
        
        message = new StringBuilder();
        
        message.append("<html>");
        
        if(e.getMessage() != null) {
            //Shows the message of the exception
            message.append(e.getMessage().replace("\n", "<br/>"));
        }
        else {
            //Show the class of the exception and the stack trace
            
            message.append("Excepció no controlada: ")
                   .append(e.getClass())
                   .append("<br/>Stack trace:<br/>");

            for(StackTraceElement elem: e.getStackTrace()) {
                message.append(elem.toString()).append("<br/>");
            }
        }
        
        message.append("</html>");
        
        return new JLabel(message.toString());
    }
}
