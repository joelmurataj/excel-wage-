package com.project.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelService {

	public Map<Integer, List<String>> readExcel(Sheet sheet);
	public void writeExcel(Map<Integer, List<String>> data, String filename) throws IOException;
	public Double tryParse(String text);
	public double calculateNeto(double bruto);
	public String getFileExtension(String fileName);
	
}
