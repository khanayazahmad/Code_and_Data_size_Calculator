package com.btpb.service;

import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class Utils {
	
	public static HashMap<String, HashMap<String, Boolean>> getFeatureList(String fwPath) {
		HashMap<String, HashMap<String, Boolean>> featureList = new HashMap<String, HashMap<String, Boolean>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath + "\\platform\\pseudo\\export\\bt_le_feature_config.h")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			for(int i = 0; i<lines.length; i++){
				
				if(lines[i].contains("Macros")){
					
					String categoryName = lines[i].replaceAll("\\*", "").replaceAll("/", "").trim();
					featureList.put(categoryName, new HashMap<String, Boolean>());
					
					while(++i<lines.length && !(lines[i].contains("Macros"))){
						if(lines[i].contains("#define")){
							String featureName;
							if(lines[i].trim().startsWith("/")){
								featureName = lines[i].replaceAll("\\*", "").replaceAll("/", "").replaceAll("#define", "").trim();
								featureList.get(categoryName).put(featureName, false);
							}else{
								featureName = lines[i].replaceAll("#define", "").trim();
								featureList.get(categoryName).put(featureName, true);
							}
						}
					}
					i--;
					
				}
			}
			
			return featureList;
		} catch (IOException e) {
			
			e.printStackTrace();
			return new HashMap<String, HashMap<String, Boolean>>();
		}
		

	}
	
	public static Boolean setFeatureList(String fwPath, HashMap<String, HashMap<String, Boolean>> featureList) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath + "\\platform\\pseudo\\export\\bt_le_feature_config.h")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			for(int i = 0; i<lines.length; i++){
				
				if(lines[i].contains("Macros")){
					
					String categoryName = lines[i].replaceAll("\\*", "").replaceAll("/", "").trim();
					if(!featureList.containsKey(categoryName))
						continue;
					
					while(++i<lines.length && !(lines[i].contains("Macros"))){
						if(lines[i].contains("#define")){
							String featureName;
							if(lines[i].trim().startsWith("/")){
								featureName = lines[i].replaceAll("\\*", "").replaceAll("/", "").replaceAll("#define", "").trim();
								if(featureList.get(categoryName).containsKey(featureName)&&featureList.get(categoryName).get(featureName)){
									
									lines[i] = lines[i].replaceAll("\\*", "").replaceAll("/", "").trim();
								}
							}else{
								featureName = lines[i].replaceAll("#define", "").trim();
								if(featureList.get(categoryName).containsKey(featureName)&&(!featureList.get(categoryName).get(featureName))){
									
									lines[i] = "//"+lines[i];
								}
							}
						}
					}
					i--;
					
				}
			}
			
			FileWriter fw = new FileWriter(new File(fwPath + "\\platform\\pseudo\\export\\bt_le_feature_config.h"));
			fw.write(String.join("\r\n",lines));
			fw.close();
			
			return true;
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		

	}
	
	public static HashMap<String, Integer> collectValues(String fwPath){
		HashMap<String, Integer> values = new HashMap<String, Integer>();
		values.put("Code", 0);
		values.put("RO Data", 0);
		values.put("Code Size", 0);
		values.put("ZI Data", 0);
		values.put("ZI Data OS", 0);
		values.put("Data Size", 0);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath + "\\build\\library\\btc_5.0_fw_8c.txt")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			
			for(int i = 0; i<lines.length; i++){
				
				if(lines[i].contains("TOTAL")){
					String num[] = lines[i].split(" ");
					int count = 0;
					
					for(int j=0; j<num.length; j++){
						
						if(!num[j].equals("")){
							count ++;
							if(count == 1){
								values.put("Code", Integer.parseInt(num[j]));
							}else if(count == 2){
								values.put("RO Data", Integer.parseInt(num[j]));
							}else if(count == 4){
								values.put("ZI Data", Integer.parseInt(num[j]));
								break;
							}
						}
						
					}
					break;
				}
				
			}
			br.close();
			
			br = new BufferedReader(new FileReader(new File(fwPath + "\\build\\library\\os_8c.txt")));
			o = br.lines().toArray();
			lines = Arrays.copyOf(o, o.length, String[].class);
			
			for(int i = 0; i<lines.length; i++){
				
				if(lines[i].contains("TOTAL")){
					String num[] = lines[i].split(" ");
					int count = 0;
					
					for(int j=0; j<num.length; j++){
						
						if(!num[j].equals("")){
							count ++;
							if(count == 4){
								values.put("ZI Data OS", Integer.parseInt(num[j]));
								break;
							}
						}
						
					}
					break;
				}
				
			}
			
			br.close();
			return values;
			
		} catch (IOException e) {
			
			//e.printStackTrace();
			return values;
		}
	
	}
	
	
	public static Boolean runAllCombo(String catName, HashMap<String, HashMap<String, Boolean>> featureList, String fwPath){
		
		HashMap<String, HashMap<String, Boolean>> featureListTemp = new HashMap<String, HashMap<String, Boolean>>();
		for(String key:featureList.keySet()){
			HashMap<String, Boolean> tmp = new HashMap<String, Boolean>();
			for(String key2:featureList.get(key).keySet()){
				tmp.put(key2, featureList.get(key).get(key2));
			}
			
			featureListTemp.put(key, tmp);
		}
		
		JFrame status = new JFrame("Progress");
		status.setBackground(Color.decode("#C9D6E3"));
		status.setSize(350, 140);
		
		JPanel p= new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		JLabel label = new JLabel("Configuring combinations...");
		label.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 18));
		
		JProgressBar jb = new JProgressBar(0, 100);
		jb.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 24));
		jb.setBounds(40, 40, 250, 30);
		jb.setValue(0);
		jb.setStringPainted(true);
		jb.setVisible(true);
		
		p.add(label);
		p.add(jb);
		status.add(p);
		((JPanel) status.getContentPane()).setOpaque(false);
		status.setResizable(false);
		status.setLocationRelativeTo(null);
		status.setUndecorated(true);
		status.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		status.pack();
		status.setVisible(true);
		

		Object o[] = featureList.get(catName).keySet().toArray();
		String fList[] = Arrays.copyOf(o, o.length, String[].class);
		HashMap<String,Integer> info = new HashMap<String,Integer>();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Code Data Size");
		XSSFRow row = sheet.createRow(0);
		
	    CellStyle style = workbook.createCellStyle();

		

		String maxBin = "";
		int i;
		for(i=0; i<fList.length; i++){
			maxBin+="1";

			row.createCell(i).setCellValue(fList[i]);
		}
		
	    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    
	    Font font = workbook.createFont();
            font.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
            font.setBold(true);
            
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFont(font);
            
            CellStyle s = workbook.createCellStyle();
			Font f = workbook.createFont();
			f.setBold(true);
			s.setFont(f);

		row.createCell(i++).setCellValue("Build");
		row.createCell(i++).setCellValue("Code");
		row.createCell(i++).setCellValue("RO Data");
		row.createCell(i++).setCellValue("Code Size(KB)");
		row.createCell(i++).setCellValue("ZI Data");
		row.createCell(i++).setCellValue("ZI Data OS");
		row.createCell(i++).setCellValue("Data Size(KB)");
		
		jb.setValue(5);
		status.pack();
		status.setVisible(true);
		
		int maxNum = Integer.parseInt(maxBin, 2);
		XSSFRow rows[] = new XSSFRow[maxNum];
		for(i=0; i<maxNum; i++){
			
			jb.setValue(5+(int)(((double)i/maxNum)*90));
			label.setText("Calculating size for "+(i+1)+"/"+maxNum +" Combinations of "+catName);
			status.pack();
			status.setVisible(true);
			
			rows[i] = sheet.createRow(i+1);
			String bin = Integer.toBinaryString(i+1);
			for(int j=bin.length();j<fList.length;j++)
				bin="0"+bin;
			
			for(int j=0;j<bin.length();j++){
				
				if(bin.charAt(j)=='1'){
					featureList.get(catName).put(fList[j], true);
					rows[i].createCell(j).setCellValue("enabled");
					rows[i].getCell(j).setCellStyle(s);
				}else{
					featureList.get(catName).put(fList[j], false);
					rows[i].createCell(j).setCellValue("disabled");
				}
			}
				setFeatureList(fwPath, featureList);
				
				label.setText("Calculating size for "+(i+1)+"/"+maxNum +" Combinations of "+catName);
				status.pack();
				status.setVisible(true);
				
				info = runSelectedCombo(fwPath);
				
				
				
				int j=bin.length();
				if(checkValues(info, fwPath).equals(true)){
					rows[i].createCell(j++,CellType.STRING).setCellValue("Success");
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("Code"));
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("RO Data"));
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(((double)info.get("Code Size")/1024));
					rows[i].getCell(j-1).setCellStyle(s);
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("ZI Data"));
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("ZI Data OS"));
					rows[i].createCell(j++,CellType.NUMERIC).setCellValue(((double)info.get("Data Size")/1024));
					rows[i].getCell(j-1).setCellStyle(s);
				}else{
					
					File log= new File(fwPath+"\\build\\scripts\\llf_release_build_pseudo.log");
					File logNewPath = new File(fwPath+"\\CDS_pseudo_build_logs");
					logNewPath.mkdirs();
					File logNew = new File(fwPath+"\\CDS_pseudo_build_logs\\llf_release_build_pseudo_"+catName.replaceAll(" ", "_")+"_"+i+".log");
					try {
						Files.copy(log.toPath(),logNew.toPath(),StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					CreationHelper createHelper = workbook.getCreationHelper();
				      XSSFCellStyle hlinkstyle = workbook.createCellStyle();
				      XSSFFont hlinkfont = workbook.createFont();
				      hlinkfont.setUnderline(XSSFFont.U_SINGLE);
				      hlinkfont.setColor(IndexedColors.BLUE.getIndex());
				      hlinkstyle.setFont(hlinkfont);
					
					rows[i].createCell(j++,CellType.STRING).setCellValue("Error");
					rows[i].getCell(j-1).setCellStyle(s);
					
					rows[i].createCell(j++).setCellValue("Go to Log File..");

				      XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.FILE);
				      link.setAddress(logNew.toURI().toString());
				      rows[i].getCell(j-1).setHyperlink(link);
				      rows[i].getCell(j-1).setCellStyle(hlinkstyle);
						rows[i].createCell(j++);
						rows[i].createCell(j++);
						rows[i].createCell(j++);
						rows[i].createCell(j++);
						rows[i].createCell(j++);
					sheet.addMergedRegion(new CellRangeAddress(i+1,i+1,j-6,j-1));
				}
				
				
	
			}
			
		row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(style));
		row.cellIterator().forEachRemaining(cell -> sheet.autoSizeColumn(cell.getColumnIndex()));
		row.setRowStyle(style);
			
		
		
		try {
			label.setText("Opening excel report...");
			jb.setValue(99);
			status.pack();
			status.setVisible(true);
			long timestamp =System.currentTimeMillis();
			FileOutputStream opStream = new FileOutputStream(new File(fwPath+"\\CodeSizeDataSize_"+catName.replaceAll(" ", "_")+"_"+timestamp+"_"+".xlsx"));
            workbook.write(opStream);
            workbook.close();
            opStream.close();

			Desktop.getDesktop().open(new File(fwPath+"\\CodeSizeDataSize_"+catName.replaceAll(" ", "_")+"_"+timestamp+"_"+".xlsx"));
			jb.setValue(100);
			status.pack();
			status.setVisible(true);
			
			status.setVisible(false);
			setFeatureList(fwPath, featureListTemp);
			return true;
			
		} catch (IOException e) {
			
			e.printStackTrace();
			setFeatureList(fwPath, featureListTemp);
			return false;
		}
		
		

		
	}
	
	public static HashMap<String, Integer> runSelectedCombo(String fwPath){
		
		
		HashMap<String, Integer> values = collectValues(fwPath);
		try {
			
			ProcessBuilder pb = new ProcessBuilder("cmd.exe","/C \"cd "+fwPath+"\\build\\scripts & clean_build.bat & llf_release_build_pseudo.bat &"
					+ " cd "+fwPath+"\\build\\library & armar --zt btc_5.0_fw.lib > btc_5.0_fw_8c.txt & armar --zt os.lib > os_8c.txt\"");
		   
			pb.directory(new File(fwPath+"\\build\\scripts"));
			
			Process tr = pb.start();
			BufferedReader stdOut=new BufferedReader(new InputStreamReader(tr.getInputStream()));
			
			String s;
			while((s=stdOut.readLine())!=null){
				System.out.println(s);
			}
			values = collectValues(fwPath);
			
			
			
			values.put("Code Size", values.get("Code")+values.get("RO Data"));
			values.put("Data Size", values.get("ZI Data")+values.get("ZI Data OS"));						
			return values;
			
		} catch (IOException e) {

			e.printStackTrace();
			return values;
		}
		
		
	}
	
	public static Boolean checkValues(HashMap<String, Integer> values, String fwPath){
		
		for(String key : values.keySet()){
			if(values.get(key) == 0){
				return false;
			}
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath+"\\build\\scripts\\llf_release_build_pseudo.log")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			
			if(lines[lines.length-1].contains("Error")){
				br.close();
				return false;
			}
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		return true;
		
	}

	public static boolean setConfig(HashMap<String, Integer> config, String fwPath) {
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath + "\\platform\\pseudo\\config\\bt_config_defines.h")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			
			for(String key: config.keySet()){
				
				if(key.equals("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED")||key.equals("LL_MAX_NO_OF_ADV_SETS_SUPPORTED")){
					for(int i = 0; i <lines.length ; i++){
						
						if(lines[i].contains(key) && lines[i].contains("#define")){
							lines[i] = "#define " + key + "               " + config.get(key);
							break;
						}

					}
				}
				
				
			}
			
			FileWriter fw = new FileWriter(new File(fwPath + "\\platform\\pseudo\\config\\bt_config_defines.h"));
			fw.write(String.join("\r\n",lines));
			fw.close();
			
			 br = new BufferedReader(new FileReader(new File(fwPath + "\\llf\\ll\\include\\ll_scanner.h")));
				o = br.lines().toArray();
				lines = Arrays.copyOf(o, o.length, String[].class);
				br.close();
				
				for(String key: config.keySet()){
					
					if(key.equals("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED")){
						for(int i = 0; i <lines.length ; i++){
							
							if(lines[i].contains(key) && lines[i].contains("#define")){
								lines[i] = "#define " + key + "               " + config.get(key);
								break;
							}

						}
					}
					
					
				}
				
				fw = new FileWriter(new File(fwPath + "\\llf\\ll\\include\\ll_scanner.h"));
				fw.write(String.join("\r\n",lines));
				fw.close();
				
				
				 br = new BufferedReader(new FileReader(new File(fwPath + "\\llf\\hci\\include\\lec_hci_le_spec_derivative_defines.h")));
					o = br.lines().toArray();
					lines = Arrays.copyOf(o, o.length, String[].class);
					br.close();
					
					for(String key: config.keySet()){
						
						
						if(key.equals("LLH_NUM_CHAIN_PDU_SUPPORTED")||key.equals("EXT_ADV_MAX_DATA_LEN_SUPPORT")){
							for(int i = 0; i <lines.length ; i++){
								
								if(lines[i].contains(key) && lines[i].contains("#define")){
									lines[i] = "#define " + key + "               " + config.get(key);
									break;
								}

							}
						}
						
						
					}
					
					fw = new FileWriter(new File(fwPath +  "\\llf\\hci\\include\\lec_hci_le_spec_derivative_defines.h"));
					fw.write(String.join("\r\n",lines));
					fw.close();
			
				
			
			return true;
			
		} catch (IOException e) {
		
			e.printStackTrace();
			
			return false;
		}
		
		
		
	}

	public static HashMap<String, Integer> getConfigParams(String fwPath) {
		
		HashMap<String, Integer> config = new HashMap<String, Integer>();
		config.put("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED", 8);
		config.put("LL_MAX_NO_OF_ADV_SETS_SUPPORTED", 4);
		config.put("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED", 2);
		config.put("LLH_NUM_CHAIN_PDU_SUPPORTED", 6);
		config.put("EXT_ADV_MAX_DATA_LEN_SUPPORT", 1650);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fwPath + "\\platform\\pseudo\\config\\bt_config_defines.h")));
			Object o[] = br.lines().toArray();
			String lines[] = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			
			for(int i=0; i<lines.length; i++){
				
				if(lines[i].contains("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED") && lines[i].contains("#define")){
					
					config.put("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED", Integer.parseInt(lines[i].trim().split(" ")[lines[i].trim().split(" ").length-1]));
					break;
				}
				
			}
			
			for(int i=0; i<lines.length; i++){
				
				if(lines[i].contains("LL_MAX_NO_OF_ADV_SETS_SUPPORTED") && lines[i].contains("#define")){
					
					config.put("LL_MAX_NO_OF_ADV_SETS_SUPPORTED", Integer.parseInt(lines[i].trim().split(" ")[lines[i].trim().split(" ").length-1]));
					break;
				}
				
			}
			
			br = new BufferedReader(new FileReader(new File(fwPath + "\\llf\\hci\\include\\lec_hci_le_spec_derivative_defines.h")));
			o = br.lines().toArray();
			lines = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			
			for(int i=0; i<lines.length; i++){
				
				if(lines[i].contains("LLH_NUM_CHAIN_PDU_SUPPORTED") && lines[i].contains("#define")){
					
					config.put("LLH_NUM_CHAIN_PDU_SUPPORTED", Integer.parseInt(lines[i].trim().split(" ")[lines[i].trim().split(" ").length-1]));
					break;
				}
				
			}
			
			for(int i=0; i<lines.length; i++){
				
				if(lines[i].contains("EXT_ADV_MAX_DATA_LEN_SUPPORT") && lines[i].contains("#define")){
					
					config.put("EXT_ADV_MAX_DATA_LEN_SUPPORT", Integer.parseInt(lines[i].trim().split(" ")[lines[i].trim().split(" ").length-1]));
					break;
				}
				
			}
			
			br = new BufferedReader(new FileReader(new File(fwPath + "\\llf\\ll\\include\\ll_scanner.h")));
			o = br.lines().toArray();
			lines = Arrays.copyOf(o, o.length, String[].class);
			br.close();
			
			for(int i=0; i<lines.length; i++){
				
				if(lines[i].contains("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED") && lines[i].contains("#define")){
					
					config.put("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED", Integer.parseInt(lines[i].trim().split(" ")[lines[i].trim().split(" ").length-1]));
					break;
				}
				
			}
			
			return config;
			
		} catch (IOException e) {
			
			return config;
			
		}
		
		
		
	}

	public static void runConfigCombo(String cat, HashMap<String, Integer> config, String fwPath) {
		
		HashMap<String, Integer> configTmp = new HashMap<String, Integer>();
		for(String key:config.keySet()){
			configTmp.put(key, config.get(key));
		}
		HashMap<String,Integer> info = new HashMap<String,Integer>();
		
		JFrame status = new JFrame("Progress");
		status.setBackground(Color.decode("#C9D6E3"));
		status.setSize(350, 140);
		
		JPanel p= new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
		JLabel label = new JLabel("Configuring combinations...");
		label.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 18));
		
		JProgressBar jb = new JProgressBar(0, 100);
		jb.setFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 24));
		jb.setBounds(40, 40, 250, 30);
		jb.setValue(0);
		jb.setStringPainted(true);
		jb.setVisible(true);
		
		p.add(label);
		p.add(jb);
		status.add(p);
		((JPanel) status.getContentPane()).setOpaque(false);
		status.setResizable(false);
		status.setLocationRelativeTo(null);
		status.setUndecorated(true);
		status.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		status.pack();
		status.setVisible(true);
		
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Code Data Size");
		XSSFRow row = sheet.createRow(0);
		
	    CellStyle style = workbook.createCellStyle();
	    
	    int i=0;
	    for(String param: config.keySet()){
	    	
	    	row.createCell(i++).setCellValue(param);
	    	
	    }
	    
	    
	    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	    
	    Font font = workbook.createFont();
            font.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
            font.setBold(true);
            
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFont(font);
            
            CellStyle s = workbook.createCellStyle();
			Font f = workbook.createFont();
			f.setBold(true);
			s.setFont(f);

		row.createCell(i++).setCellValue("Build");
		row.createCell(i++).setCellValue("Code");
		row.createCell(i++).setCellValue("RO Data");
		row.createCell(i++).setCellValue("Code Size(KB)");
		row.createCell(i++).setCellValue("ZI Data");
		row.createCell(i++).setCellValue("ZI Data OS");
		row.createCell(i++).setCellValue("Data Size(KB)");
	    
		jb.setValue(5);
		status.pack();
		status.setVisible(true);
		
		int maxNum = configTmp.get("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED")
						*configTmp.get("LL_MAX_NO_OF_ADV_SETS_SUPPORTED")
							*configTmp.get("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED")
								*configTmp.get("LLH_NUM_CHAIN_PDU_SUPPORTED");
		
		XSSFRow rows[] = new XSSFRow[maxNum];
		i=0;
		for(int con = 1; con <= configTmp.get("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED"); con++){
			for(int adv = 1; adv <= configTmp.get("LL_MAX_NO_OF_ADV_SETS_SUPPORTED"); adv++){
				for(int sync = 1; sync <= configTmp.get("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED"); sync++){
					for(int chain = 1; chain <= configTmp.get("LLH_NUM_CHAIN_PDU_SUPPORTED"); chain ++){
						
						jb.setValue(5+(int)(((double)(i+1)/maxNum)*90));
						label.setText("Calculating size for "+(i+1)+"/"+maxNum +" Combinations of Configuration Parameters");
						status.pack();
						status.setVisible(true);
						
						config.put("LL_MAX_NO_OF_CONNECTIONS_SUPPORTED", con);
						config.put("LL_MAX_NO_OF_ADV_SETS_SUPPORTED", adv);
						config.put("LL_MAX_NO_OF_SYNC_ENGINE_SUPPORTED", sync);
						config.put("LLH_NUM_CHAIN_PDU_SUPPORTED", chain);
						config.put("EXT_ADV_MAX_DATA_LEN_SUPPORT", 1650);
						
						setConfig(config, fwPath);
						
						label.setText("Calculating size for "+(i+1)+"/"+maxNum +" Combinations of Configuration Parameters");
						status.pack();
						status.setVisible(true);
						
						info = runSelectedCombo(fwPath);
						
						int j = 0;
						
						rows[i] = sheet.createRow(i+1);
						
						rows[i].createCell(j++).setCellValue(""+con);
						rows[i].createCell(j++).setCellValue(""+adv);
						rows[i].createCell(j++).setCellValue(""+sync);
						rows[i].createCell(j++).setCellValue(""+chain);
						rows[i].createCell(j++).setCellValue(""+configTmp.get("EXT_ADV_MAX_DATA_LEN_SUPPORT"));
						
						
						
						if(checkValues(info, fwPath).equals(true)){
							rows[i].createCell(j++,CellType.STRING).setCellValue("Success");
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("Code"));
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("RO Data"));
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(((double)info.get("Code Size")/1024));
							rows[i].getCell(j-1).setCellStyle(s);
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("ZI Data"));
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(info.get("ZI Data OS"));
							rows[i].createCell(j++,CellType.NUMERIC).setCellValue(((double)info.get("Data Size")/1024));
							rows[i].getCell(j-1).setCellStyle(s);
						}else{
							
							File log= new File(fwPath+"\\build\\scripts\\llf_release_build_pseudo.log");
							File logNewPath = new File(fwPath+"\\CDS_pseudo_build_logs");
							logNewPath.mkdirs();
							File logNew = new File(fwPath+"\\CDS_pseudo_build_logs\\llf_release_build_pseudo_"+"ConfigParams"+"_"+i+".log");
							try {
								Files.copy(log.toPath(),logNew.toPath(),StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							
							CreationHelper createHelper = workbook.getCreationHelper();
						      XSSFCellStyle hlinkstyle = workbook.createCellStyle();
						      XSSFFont hlinkfont = workbook.createFont();
						      hlinkfont.setUnderline(XSSFFont.U_SINGLE);
						      hlinkfont.setColor(IndexedColors.BLUE.getIndex());
						      hlinkstyle.setFont(hlinkfont);
							
							rows[i].createCell(j++,CellType.STRING).setCellValue("Error");
							rows[i].getCell(j-1).setCellStyle(s);
							
							rows[i].createCell(j++).setCellValue("Go to Log File..");

						      XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.FILE);
						      link.setAddress(logNew.toURI().toString());
						      rows[i].getCell(j-1).setHyperlink(link);
						      rows[i].getCell(j-1).setCellStyle(hlinkstyle);
								rows[i].createCell(j++);
								rows[i].createCell(j++);
								rows[i].createCell(j++);
								rows[i].createCell(j++);
								rows[i].createCell(j++);
							sheet.addMergedRegion(new CellRangeAddress(i+1,i+1,j-6,j-1));
						}
						
						i++;
					}
				}
			}
		}
		
		row.cellIterator().forEachRemaining(cell -> cell.setCellStyle(style));
		row.cellIterator().forEachRemaining(cell -> sheet.autoSizeColumn(cell.getColumnIndex()));
		row.setRowStyle(style);
			
		
		
		try {
			label.setText("Opening excel report...");
			jb.setValue(99);
			status.pack();
			status.setVisible(true);
			long timestamp =System.currentTimeMillis();
			FileOutputStream opStream = new FileOutputStream(new File(fwPath+"\\CodeSizeDataSize_"+"ConfigParams"+"_"+timestamp+"_"+".xlsx"));
            workbook.write(opStream);
            workbook.close();
            opStream.close();

			Desktop.getDesktop().open(new File(fwPath+"\\CodeSizeDataSize_"+"ConfigParams"+"_"+timestamp+"_"+".xlsx"));
			jb.setValue(100);
			status.pack();
			status.setVisible(true);
			
			status.setVisible(false);
			setConfig(configTmp, fwPath);
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			setConfig(configTmp, fwPath);
			
		}
		
		
	}


	

}
