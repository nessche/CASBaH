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
