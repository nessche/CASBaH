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

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JNLPConsole extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JTextArea textArea = new JTextArea();

	public JNLPConsole() throws HeadlessException, IOException {
		super("CASBaH Console");

		JScrollPane pane = new JScrollPane(textArea);
		pane.setMinimumSize(new Dimension(400, 150));
		pane.setPreferredSize(new Dimension(400, 150));
		add(pane);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);

		PipedOutputStream out = new PipedOutputStream();
		PrintStream pout = new PrintStream(out);
		System.setErr(pout);
		System.setOut(pout);

		// this thread sets the text to JTextArea
		final BufferedReader in = new BufferedReader(new InputStreamReader(
				new PipedInputStream(out)));
		new Thread() {
			public void run() {
				try {
					while (true) {
						String line;
						while ((line = in.readLine()) != null) {
							final String text = line;
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									textArea.append(text + '\n');
									scrollDown();
								}
							});
						}
					}
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		}.start();

		pack();
	}

	/**
	 * Forces the scroll of text area.
	 */
	private void scrollDown() {
		int pos = textArea.getDocument().getEndPosition().getOffset();
		textArea.getCaret().setDot(pos);
		textArea.requestFocus();
	}
}
