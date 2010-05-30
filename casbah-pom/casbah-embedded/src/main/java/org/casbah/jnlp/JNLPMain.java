/*******************************************************************************
 * Copyright (C) 2010 Marco Sandrini
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.casbah.jnlp;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import winstone.Launcher;

public class JNLPMain {

	public static void main(String[] args) throws Exception {
	
		try {
			System.setSecurityManager(null);
		} catch (SecurityException se) {
			se.printStackTrace();
		}
		
		setUILookAndFeel();
		
		new JNLPConsole().setVisible(true);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					launchBrowser("http://localhost:8080/");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}, 1000);
		
		
		Launcher.main(args);
		
	}
	
    /**
     * Sets to the platform native look and feel.
     *
     * see http://javaalmanac.com/egs/javax.swing/LookFeelNative.html
     */
    public static void setUILookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }
    }
    
    private static void launchBrowser(String url) throws IOException {
    	if (Desktop.isDesktopSupported()) {
    		Desktop.getDesktop().browse(URI.create(url));
    	}
    }

}
