package com.btpb.view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.btpb.service.Utils;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7797790260367820439L;

	HashMap<String, HashMap<String, Boolean>> featureList;
	HashMap<String, Integer> config;
	int dx;
	int dy;
	int x;
	int y;

	public Main() {

		super("CDS Calculator 1.0.1");
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
		setBackground(Color.decode("#C9D6E3"));

		setSize(500, 500);

		featureList = new HashMap<String, HashMap<String, Boolean>>();
		config = new HashMap<String, Integer>();

		JPanel content = new JPanel();
		content.setLayout(new GridBagLayout());
		content.setBackground(Color.decode("#edd9c0"));
		content.setOpaque(false);
		JLabel fwCodeBaseLabel = new JLabel("Path of Firmware Code Base: ");
		fwCodeBaseLabel.setFont(new Font("Calibri", Font.PLAIN, 16));

		JTextField fwCodeBaseTxt = new JTextField(15);
		fwCodeBaseTxt.setFont(new Font("Calibri", Font.PLAIN, 16));
		JButton open2 = new JButton("...");
		open2.setForeground(Color.decode("#46483A"));
		open2.setBackground(Color.decode("#a8b6bf"));
		open2.setMinimumSize(getMinimumSize());
		open2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				c.setCurrentDirectory(new File(fwCodeBaseTxt.getText()));
				int rVal = c.showOpenDialog(Main.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					fwCodeBaseTxt.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					fwCodeBaseTxt.setText("");

				}

			}
		});

		JButton load = new JButton("Load Features");
		load.setForeground(Color.decode("#46483A"));
		load.setBackground(Color.decode("#a8b6bf"));
		load.setMinimumSize(getMinimumSize());
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (fwCodeBaseTxt.getText().equals("")) {
					JOptionPane.showMessageDialog(new JFrame(), "Please select a firmware directory", "Dialog",
							JOptionPane.ERROR_MESSAGE);
				} else {

					featureList = Utils.getFeatureList(fwCodeBaseTxt.getText());
					config = Utils.getConfigParams(fwCodeBaseTxt.getText());

					if (featureList.isEmpty()) {
						JOptionPane.showMessageDialog(new JFrame(), "Please select a correct firmware directory",
								"Dialog", JOptionPane.ERROR_MESSAGE);
					} else {
						JFrame manageFeatures = new JFrame("Manage Features and Parameters");
						manageFeatures.setBackground(Color.decode("#f2f2f2"));
						manageFeatures.setSize(1300, 900);

						JPanel con = new JPanel();
						con.setLayout(new GridBagLayout());
						con.setBackground(Color.decode("#edd9c0"));

						con.setOpaque(false);

						HashMap<String, HashMap<String, JCheckBox>> featureBoxes = new HashMap<String, HashMap<String, JCheckBox>>();

						GridBagConstraints g = new GridBagConstraints();
						x = 0;
						y = 0;
						g.fill = GridBagConstraints.HORIZONTAL;
						g.gridx = x;
						g.gridy = y++;
						JLabel space2 = new JLabel(" ");
						con.add(space2, g);

						JLabel noCon = new JLabel("  Number of Connections Supported:    ");
						noCon.setFont(new Font("Calibri", Font.PLAIN, 14));
						String consup[] = { "1", "2", "3", "4", "5", "6", "7", "8" };
						JComboBox<String> cb1 = new JComboBox<String>(consup);
						cb1.setFont(new Font("Calibri", Font.PLAIN, 14));
						cb1.setSelectedItem(config.get("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED") + "");

						JLabel noAdvSets = new JLabel(" Number of Advertising Sets Supported:");
						noAdvSets.setFont(new Font("Calibri", Font.PLAIN, 14));
						String advsup[] = { "1", "2", "3", "4" };
						JComboBox<String> cb2 = new JComboBox<String>(advsup);
						cb2.setFont(new Font("Calibri", Font.PLAIN, 14));
						cb2.setSelectedItem(config.get("LL_MAX_NO_OF_ADV_SETS_SUPPORTED") + "");

						JLabel noSync = new JLabel(" Number of Syncs Supported:           ");
						noSync.setFont(new Font("Calibri", Font.PLAIN, 14));
						String syncsup[] = { "1", "2" };
						JComboBox<String> cb3 = new JComboBox<String>(syncsup);
						cb3.setFont(new Font("Calibri", Font.PLAIN, 14));
						cb3.setSelectedItem(config.get("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED") + "");

						g.gridx = x++;
						g.gridy = y;
						con.add(noCon, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						g.gridx = x++;
						g.gridy = y;
						con.add(noAdvSets, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						g.gridx = x++;
						g.gridy = y;
						con.add(noSync, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						x = 0;
						y++;

						g.gridx = x++;
						g.gridy = y;
						con.add(cb1, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						g.gridx = x++;
						g.gridy = y;
						con.add(cb2, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						g.gridx = x++;
						g.gridy = y;
						con.add(cb3, g);

						g.gridx = x++;
						con.add(new JLabel(" "), g);

						x = 0;
						y++;

						g.gridy = y++;
						con.add(new JLabel(" "), g);

						JLabel advChain = new JLabel("  Number of Chains Supported:    ");
						advChain.setFont(new Font("Calibri", Font.PLAIN, 14));

						JLabel dataLen = new JLabel("  Maximum Data Length Supported:    ");
						advChain.setFont(new Font("Calibri", Font.PLAIN, 14));

						JTextField len = new JTextField();
						len.setFont(new Font("Calibri", Font.PLAIN, 14));
						len.setText(config.get("EXT_ADV_MAX_DATA_LEN_SUPPORT") + "");

						String chain[] = { "1", "2", "3", "4", "5", "6" };
						JComboBox<String> chainb = new JComboBox<String>(chain);
						chainb.setFont(new Font("Calibri", Font.PLAIN, 14));
						chainb.setSelectedItem(config.get("LLH_NUM_CHAIN_PDU_SUPPORTED") + "");

						dataLen.setText("  Maximum Data Length Supported (max = " + 1650 + " ):    ");

						g.gridy = y++;
						g.gridx = 1;
						con.add(advChain, g);

						g.gridx = 3;
						con.add(dataLen, g);

						g.gridx = 1;
						g.gridy = y++;
						con.add(chainb, g);

						g.gridx = 3;
						con.add(len, g);

						x = 0;
						g.gridx = x;
						g.gridy = y++;
						con.add(new JLabel(" "), g);

						JButton setConfig = new JButton("Configure Parameters");
						setConfig.setFont(new Font("Calibri", Font.PLAIN, 14));
						setConfig.setBackground(Color.decode("#a8b6bf"));
						setConfig.setForeground(Color.decode("#46483A"));
						setConfig.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {

								int max = 1650;

								try {

									if (Integer.parseInt(len.getText()) > max || Integer.parseInt(len.getText()) < 0) {
										JOptionPane.showMessageDialog(new JFrame(),
												"Error: Please enter a number less than " + max + " and greater than 0",
												"Error", JOptionPane.ERROR_MESSAGE);

										return;
									}

								} catch (Exception e) {
									JOptionPane.showMessageDialog(new JFrame(), "Error: Please enter a valid number.",
											"Error", JOptionPane.ERROR_MESSAGE);
									return;
								}

								config.put("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED",
										Integer.parseInt((String) cb1.getSelectedItem()));
								config.put("LL_MAX_NO_OF_ADV_SETS_SUPPORTED",
										Integer.parseInt((String) cb2.getSelectedItem()));
								config.put("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED",
										Integer.parseInt((String) cb3.getSelectedItem()));
								config.put("LLH_NUM_CHAIN_PDU_SUPPORTED",
										Integer.parseInt((String) chainb.getSelectedItem()));
								config.put("EXT_ADV_MAX_DATA_LEN_SUPPORT", Integer.parseInt((String) len.getText()));

								if (Utils.setConfig(config, fwCodeBaseTxt.getText())) {
									JOptionPane.showMessageDialog(new JFrame(), "Parameters set successfully", "Dialog",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(new JFrame(), "Error: Parameters could not be set",
											"Error", JOptionPane.ERROR_MESSAGE);

								}

							}

						});

						g.gridx = x;
						g.gridy = y++;
						g.fill = GridBagConstraints.HORIZONTAL;
						g.gridwidth = 41;
						con.add(setConfig, g);

						g.gridy = y++;
						con.add(new JLabel(" "), g);

						for (String cat : featureList.keySet()) {
							if (!(cat.trim().startsWith("4.") || cat.trim().startsWith("5."))) {
								continue;
							}
							JLabel category = new JLabel("  " + cat + ":");
							category.setFont(new Font("Calibri", Font.PLAIN, 14));
							featureBoxes.put(cat, new HashMap<String, JCheckBox>());
							g.fill = GridBagConstraints.HORIZONTAL;
							g.gridx = x;
							g.gridy = y;
							g.gridwidth = 1;
							con.add(category, g);
							y++;

							for (String fet : featureList.get(cat).keySet()) {

								featureBoxes.get(cat).put(fet, new JCheckBox(fet));
								featureBoxes.get(cat).get(fet).setSelected(featureList.get(cat).get(fet));
								g.fill = GridBagConstraints.HORIZONTAL;

								g.gridx = x++;
								g.gridy = y;
								g.gridwidth = 1;
								con.add(featureBoxes.get(cat).get(fet), g);
								if (x > 3) {
									y++;
									x = 0;
								}
							}
							x = 0;
							y++;
							g.fill = GridBagConstraints.HORIZONTAL;
							g.gridx = x;
							g.gridy = y;
							g.gridwidth = 1;
							JLabel space1 = new JLabel(" ");
							con.add(space1, g);
							y++;
						}

						JButton set = new JButton("Set Features");
						set.setFont(new Font("Calibri", Font.PLAIN, 14));
						set.setBackground(Color.decode("#a8b6bf"));
						set.setForeground(Color.decode("#46483A"));
						set.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {

								for (String cat : featureBoxes.keySet()) {
									for (String fet : featureBoxes.get(cat).keySet()) {

										featureList.get(cat).put(fet, featureBoxes.get(cat).get(fet).isSelected());

									}
								}

								if (Utils.setFeatureList(fwCodeBaseTxt.getText(), featureList)) {
									JOptionPane.showMessageDialog(new JFrame(), "Features set successfully", "Dialog",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showMessageDialog(new JFrame(), "Error: Features could not be set",
											"Dialog", JOptionPane.ERROR_MESSAGE);

								}

							}
						});

						HashMap<String, JLabel> catg = new HashMap<String, JLabel>();
						HashMap<String, JLabel> spaces = new HashMap<String, JLabel>();
						JButton show = new JButton("Show More..");
						show.setFont(new Font("Calibri", Font.PLAIN, 14));
						show.setBackground(Color.decode("#a8b6bf"));
						show.setForeground(Color.decode("#46483A"));
						show.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {

								if (show.getText().equals("Show More..")) {

									for (String cat : featureList.keySet()) {
										if ((cat.trim().startsWith("4.") || cat.trim().startsWith("5."))) {
											continue;
										}
										catg.put(cat, new JLabel("  " + cat + ":"));
										catg.get(cat).setFont(new Font("Calibri", Font.PLAIN, 14));
										featureBoxes.put(cat, new HashMap<String, JCheckBox>());
										g.fill = GridBagConstraints.HORIZONTAL;
										g.gridx = x;
										g.gridy = y;
										g.gridwidth = 1;
										catg.get(cat).setSize(10, 10);
										con.add(catg.get(cat), g);
										y++;

										for (String fet : featureList.get(cat).keySet()) {

											featureBoxes.get(cat).put(fet, new JCheckBox(fet));
											featureBoxes.get(cat).get(fet).setSelected(featureList.get(cat).get(fet));
											g.fill = GridBagConstraints.HORIZONTAL;

											g.gridx = x++;
											g.gridy = y;
											g.gridwidth = 1;
											con.add(featureBoxes.get(cat).get(fet), g);
											if (x > 3) {
												y++;
												x = 0;
											}
										}
										x = 0;
										y++;
										g.fill = GridBagConstraints.HORIZONTAL;
										g.gridx = x;
										g.gridy = y;
										g.gridwidth = 1;
										JLabel space1 = new JLabel(" ");
										spaces.put(cat, space1);
										con.add(spaces.get(cat), g);
										y++;
									}
									con.remove(show);
									con.remove(set);
									g.gridx = x;
									g.gridy = ++y;
									g.fill = GridBagConstraints.HORIZONTAL;
									g.gridwidth = 41;
									con.add(show, g);
									g.gridy = ++y;
									JLabel space1 = new JLabel(" ");
									con.add(space1, g);

									g.gridx = x;
									g.gridy = ++y;
									g.fill = GridBagConstraints.HORIZONTAL;
									g.gridwidth = 41;
									con.add(set, g);
									show.setText("Show Less..");
									manageFeatures.pack();
									manageFeatures.setVisible(true);

								} else {

									for (String cat : featureList.keySet()) {
										if ((cat.trim().startsWith("4.") || cat.trim().startsWith("5."))) {
											continue;
										}

										con.remove(catg.get(cat));
										con.remove(spaces.get(cat));

										for (String fet : featureList.get(cat).keySet()) {

											con.remove(featureBoxes.get(cat).get(fet));

										}

									}
									show.setText("Show More..");
									manageFeatures.pack();
									manageFeatures.setVisible(true);
								}

							}

						});

						g.gridx = x;
						g.gridy = y;
						g.fill = GridBagConstraints.HORIZONTAL;
						g.gridwidth = 41;
						con.add(show, g);
						g.gridy = ++y;
						JLabel space1 = new JLabel(" ");
						con.add(space1, g);

						g.gridx = x;
						g.gridy = ++y;
						g.fill = GridBagConstraints.HORIZONTAL;
						g.gridwidth = 41;
						con.add(set, g);

						manageFeatures.setLayout(new GridBagLayout());
						manageFeatures.setResizable(false);

						manageFeatures.add(con);

						((JPanel) manageFeatures.getContentPane()).setOpaque(false);
						manageFeatures.setLocationRelativeTo(null);
						manageFeatures.pack();
						manageFeatures.setVisible(true);
					}
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		content.add(fwCodeBaseLabel, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 40;
		content.add(fwCodeBaseTxt, gbc);
		gbc.gridwidth = 1;
		gbc.gridx = 40;
		gbc.gridy = 1;
		content.add(open2, gbc);
		gbc.gridx = 42;
		gbc.gridy = 1;
		content.add(load, gbc);

		dx = 0;
		dy = 3;
		HashMap<String, JCheckBox> categories = new HashMap<String, JCheckBox>();

		String runType[] = { "Selected Combination", "All Combinations" };
		JComboBox<String> cb = new JComboBox<String>(runType);
		cb.setFont(new Font("Calibri", Font.PLAIN, 16));
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		content.add(cb, gbc);
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (cb.getSelectedItem().equals("All Combinations")) {
					if (featureList.isEmpty()) {
						if (fwCodeBaseTxt.getText().equals("")) {
							JOptionPane.showMessageDialog(new JFrame(), "Please select a firmware directory", "Dialog",
									JOptionPane.ERROR_MESSAGE);
							cb.setSelectedIndex(0);
							return;
						} else {

							featureList = Utils.getFeatureList(fwCodeBaseTxt.getText());
							config = Utils.getConfigParams(fwCodeBaseTxt.getText());
							if (featureList.isEmpty()) {
								JOptionPane.showMessageDialog(new JFrame(),
										"Please select a correct firmware directory", "Dialog",
										JOptionPane.ERROR_MESSAGE);
								cb.setSelectedIndex(0);
								return;
							}
						}
					}

					for (String cat : featureList.keySet()) {
						categories.put(cat, new JCheckBox(cat));
					}

					categories.put("Configuration Parameters", new JCheckBox("Configuration Parameters"));

					dx = 0;
					for (String cat : categories.keySet()) {
						gbc.fill = GridBagConstraints.HORIZONTAL;

						gbc.gridx = dx;
						gbc.gridy = ++dy;
						categories.get(cat).setFont(new Font("Calibri", Font.PLAIN, 14));
						content.add(categories.get(cat), gbc);

					}
					setVisible(true);
				} else {
					for (String cat : categories.keySet()) {

						dx = 0;
						content.remove(categories.get(cat));

					}
					setVisible(true);
				}

			}
		});

		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = ++dx;
		gbc.gridy = --dy;
		JButton run = new JButton("Calculate");
		run.setFont(new Font("Calibri", Font.PLAIN, 16));
		run.setBackground(Color.decode("#a8b6bf"));
		run.setForeground(Color.decode("#46483A"));
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (featureList.isEmpty()) {
					if (fwCodeBaseTxt.getText().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(), "Please select a firmware directory", "Dialog",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else {

						featureList = Utils.getFeatureList(fwCodeBaseTxt.getText());
						config = Utils.getConfigParams(fwCodeBaseTxt.getText());
						if (featureList.isEmpty()) {
							JOptionPane.showMessageDialog(new JFrame(), "Please select a correct firmware directory",
									"Dialog", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}

				Runnable runner = new Runnable() {
					public synchronized void run() {

						run.setText("Calculating..");
						run.setEnabled(false);
						fwCodeBaseTxt.setEditable(false);

						if (cb.getSelectedItem().equals("All Combinations")) {

							for (String cat : categories.keySet()) {
								if (categories.get(cat).isSelected()) {
									if (cat.equals("Configuration Parameters")) {

										Utils.runConfigCombo(cat, config, fwCodeBaseTxt.getText());

									} else {
										Utils.runAllCombo(cat, featureList, fwCodeBaseTxt.getText());
									}
								}
							}

						} else {
							HashMap<String, Integer> values = Utils.runSelectedCombo(fwCodeBaseTxt.getText());
							String column1[] = { "Code", "RO Data", "Code Size (KB)" };

							String data1[][] = { { "" + values.get("Code"), "" + values.get("RO Data"),
									"" + ((double) values.get("Code Size") / 1024) } };
							String column2[] = { "ZI Data", "ZI Data OS", "Data Size (KB)" };

							String data2[][] = { { "" + values.get("ZI Data"), "" + values.get("ZI Data OS"),
									"" + ((double) values.get("Data Size") / 1024) } };

							if (Utils.checkValues(values, fwCodeBaseTxt.getText()).equals(false)) {

								JOptionPane.showMessageDialog(new JFrame(),
										"Build Error: Please check the log file. Disable the KDS tool if it is still active.",
										"Error", JOptionPane.ERROR_MESSAGE);
								try {

									Desktop.getDesktop().open(new File(fwCodeBaseTxt.getText()
											+ "\\build\\scripts\\llf_release_build_pseudo.log"));
								} catch (IOException e) {

									e.printStackTrace();
								}

							} else {
								JFrame result = new JFrame("Result");
								result.setBackground(Color.decode("#C9D6E3"));
								result.setSize(350, 140);

								JTable t1 = new JTable(data1, column1);
								t1.setFont(new Font("Calibri", Font.PLAIN, 18));
								t1.getTableHeader().setFont(new Font("Calibri", Font.PLAIN, 16));
								JTable t2 = new JTable(data2, column2);
								t2.setFont(new Font("Calibri", Font.PLAIN, 18));
								t2.getTableHeader().setFont(new Font("Calibri", Font.PLAIN, 16));
								JPanel p = new JPanel();
								p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

								p.add(t1.getTableHeader());

								p.add(t1);

								p.add(t2.getTableHeader());

								p.add(t2);

								result.add(new JScrollPane(p));
								((JPanel) result.getContentPane()).setOpaque(false);
								result.setResizable(false);
								result.setLocationRelativeTo(null);

								result.setVisible(true);
							}

						}

						run.setText("Calculate");
						run.setEnabled(true);
						fwCodeBaseTxt.setEditable(true);
						setVisible(true);

					}
				};
				Thread t = new Thread(runner, "Code Executer");
				t.start();

			}

		});
		content.add(run, gbc);

		setResizable(false);
		setLayout(new GridBagLayout());
		add(content);
		((JPanel) getContentPane()).setOpaque(false);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}

}