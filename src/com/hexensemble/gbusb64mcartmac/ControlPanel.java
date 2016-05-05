package com.hexensemble.gbusb64mcartmac;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

/**
 * Represents the control panel.
 * 
 * @author HexEnsemble
 * @author www.hexensemble.com
 * @version 1.0.0
 * @since 1.0.0
 */
public class ControlPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel window;

	private JPanel buttonsHigh;
	private JPanel buttonsLow;
	private Choice setBank;
	private JButton writeROM;
	private JButton readROM;
	private JButton writeSRAM;
	private JButton readSRAM;
	private JButton refresh;
	private JButton about;
	private JButton quit;

	private JScrollPane console;
	private JTextArea consoleText;

	/**
	 * Creates the control panel.
	 */
	public ControlPanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		window = new JPanel();
		window.setLayout(new BorderLayout());
		setTitle(Main.TITLE);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);

		create();
		actions();

		setVisible(true);
	}

	private void create() {
		buttonsHigh = new JPanel();
		buttonsHigh.setLayout(new FlowLayout());
		buttonsHigh.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		buttonsLow = new JPanel();
		buttonsLow.setLayout(new FlowLayout());
		setBank = new Choice();
		setBank.add("Bank 1");
		setBank.add("Bank 2");
		buttonsHigh.add(setBank);
		writeROM = new JButton("Write ROM to cart");
		buttonsHigh.add(writeROM);
		readROM = new JButton("Read ROM to file");
		buttonsHigh.add(readROM);
		refresh = new JButton("Refresh");
		buttonsHigh.add(refresh);
		writeSRAM = new JButton("Write SAVE to cart");
		buttonsLow.add(writeSRAM);
		readSRAM = new JButton("Read SAVE to file");
		buttonsLow.add(readSRAM);
		about = new JButton("About");
		buttonsLow.add(about);
		quit = new JButton("Quit");
		buttonsLow.add(quit);

		console = new JScrollPane();
		console.setPreferredSize(new Dimension(600, 450));
		consoleText = new JTextArea();
		consoleText.setBorder(BorderFactory.createCompoundBorder(consoleText.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		consoleText.setEditable(false);
		console.setViewportView(consoleText);

		window.add(buttonsHigh, BorderLayout.NORTH);
		window.add(buttonsLow, BorderLayout.CENTER);
		window.add(console, BorderLayout.SOUTH);

		execCommand("--title --verbose");
	}

	private void actions() {
		writeROM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = openFile(".gb", "ROM file (*.gb)");

				if (file != null) {
					String bank;
					if (setBank.getSelectedItem().equals("Bank 1")) {
						bank = "1";
					} else {
						bank = "2";
					}

					String[] options = new String[2];
					options[0] = new String("OK");
					options[1] = new String("Cancel");
					int confirm = JOptionPane.showOptionDialog(window,
							"This will write the ROM:\n" + file + "\n" + "to Bank " + bank + ".\n"
									+ "Are you sure you wish to proceed?",
							"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
					if (confirm == 0) {
						String path = formatPath(file);

						execCommand("--read " + path + " --bank " + bank + " --verbose");
					}
				}
			}
		});

		readROM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = saveFile(".gb", "ROM file (*.gb)");

				if (file != null) {
					String bank;
					if (setBank.getSelectedItem().equals("Bank 1")) {
						bank = "1";
					} else {
						bank = "2";
					}

					String[] options = new String[2];
					options[0] = new String("OK");
					options[1] = new String("Cancel");
					int confirm = JOptionPane.showOptionDialog(window,
							"This will read the ROM from Bank " + bank + " to:\n" + file + "\n"
									+ "Are you sure you wish to proceed?",
							"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
					if (confirm == 0) {
						String path = formatPath(file);

						execCommand("--read " + path + " --bank " + bank + " --verbose");
					}
				}
			}
		});

		writeSRAM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = openFile(".sav", "SAVE file (*.sav)");

				if (file != null) {
					String[] options = new String[2];
					options[0] = new String("OK");
					options[1] = new String("Cancel");
					int confirm = JOptionPane.showOptionDialog(window,
							"This will write the SAVE:\n" + file + "\n" + "to SRAM.\n"
									+ "Are you sure you wish to proceed?",
							"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
					if (confirm == 0) {
						String path = formatPath(file);

						execCommand("--save " + path + " --verbose");
					}
				}
			}
		});

		readSRAM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String file = saveFile(".sav", "SAVE file (*.sav)");

				if (file != null) {
					String[] options = new String[2];
					options[0] = new String("OK");
					options[1] = new String("Cancel");
					int confirm = JOptionPane.showOptionDialog(window,
							"This will read the SAVE from SRAM to:\n" + file + "\n"
									+ "Are you sure you wish to proceed?",
							"WARNING!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 1);
					if (confirm == 0) {
						String path = formatPath(file);

						execCommand("--read " + path + " --verbose");
					}
				}
			}
		});

		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				execCommand("--title --verbose");
			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String emsFlasher = "This program uses EMS Flasher by Mike Ryan.\n" + "www.lacklustre.net";

				consoleText.setText(Main.TITLE + "\n" + "by " + Main.AUTHOR + "\n" + "\n" + Main.VERSION + "\n"
						+ Main.DATE + "\n" + "\n" + Main.WEB + "\n" + "\n" + emsFlasher);
			}
		});

		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	private String openFile(final String fileExtension, final String fileDescription) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open File");
		fileChooser.setApproveButtonText("Open");
		fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(fileExtension);
				}
			}

			@Override
			public String getDescription() {
				return fileDescription;
			}
		});
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(false);

		if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
			String file = fileChooser.getSelectedFile().toString();
			return file;
		} else {
			return null;
		}
	}

	private String saveFile(final String fileExtension, final String fileDescription) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save File");
		fileChooser.setApproveButtonText("Save");
		fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().toLowerCase().endsWith(fileExtension);
				}
			}

			@Override
			public String getDescription() {
				return fileDescription;
			}
		});
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(false);

		if (fileChooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
			String file = fileChooser.getSelectedFile().toString();
			return file;
		} else {
			return null;
		}
	}

	private String formatPath(String s) {
		String s2 = s.replaceAll("\\s", "\\\\ ");
		String s3 = s2.replaceAll("\\(", "\\\\(");
		String s4 = s3.replaceAll("\\)", "\\\\)");
		String s5 = s4.replaceAll("\\,", "\\\\,");
		return s5;
	}

	private void execCommand(String command) {
		writeROM.setEnabled(false);
		writeROM.paintImmediately(buttonsHigh.getBounds());
		readROM.setEnabled(false);
		readROM.paintImmediately(buttonsHigh.getBounds());
		writeSRAM.setEnabled(false);
		writeSRAM.paintImmediately(buttonsHigh.getBounds());
		readSRAM.setEnabled(false);
		readSRAM.paintImmediately(buttonsHigh.getBounds());
		refresh.setEnabled(false);
		refresh.paintImmediately(buttonsHigh.getBounds());
		quit.setEnabled(false);
		quit.paintImmediately(buttonsHigh.getBounds());
		consoleText.setText("Working, please wait. Do not unplug cartridge!");
		consoleText.paintImmediately(console.getViewportBorderBounds());

		Processor processor = new Processor(command);
		Thread thread = new Thread(processor);
		thread.start();
		while (thread.isAlive()) {

		}

		writeROM.setEnabled(true);
		readROM.setEnabled(true);
		writeSRAM.setEnabled(true);
		readSRAM.setEnabled(true);
		refresh.setEnabled(true);
		quit.setEnabled(true);
		consoleText.setText(processor.getOutput());
	}

}
