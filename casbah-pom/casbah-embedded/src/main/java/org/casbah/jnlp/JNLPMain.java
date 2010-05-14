package org.casbah.jnlp;

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

}
