package com.project.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.project.service.ExcelService;
import com.project.utility.Message;
import com.project.utility.Taxes;

@Service("excelService")
public class ExcelServiceImpl implements ExcelService {

	private XSSFWorkbook workbookXSSF;

	@SuppressWarnings({ "deprecation" })
	@Override
	public Map<Integer, List<String>> readExcel(Sheet sheet) {
		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		boolean problem = false;

		for (Row row : sheet) {
			data.put(i, new ArrayList<String>());
			for (Cell cell : row) {

				switch (cell.getCellTypeEnum()) {
				case STRING:
					data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
					break;
				case NUMERIC:
					if (!DateUtil.isCellDateFormatted(cell) && cell.getColumnIndex() == 4) {
						data.get(i).add(cell.getNumericCellValue() + "");
						Double bruto = (Double) cell.getNumericCellValue();
						Double neto =calculateNeto(bruto);
						data.get(i).add(neto + "");
					}
					break;
				case BOOLEAN:
					problem = true;
					break;
				case FORMULA:
					problem = true;
					break;
				default:
					problem = true;
				}
			}
			if (problem || data.get(i).size() > 6) {
				Message.addMessage(Message.bundle.getString("PROBLEM_WITH_FILE"), "error");
				return null;
			}
			i++;
		}
		return data;
	}

	@Override
	public void writeExcel(Map<Integer, List<String>> data, String filename) throws IOException {
		FileInputStream file = new FileInputStream(new File(filename));
		workbookXSSF = new XSSFWorkbook(file);

		XSSFSheet spreadsheet = workbookXSSF.getSheetAt(0);

		XSSFRow row;
		Set<Integer> keyId = data.keySet();
		int rowid = 0;
		for (int key : keyId) {
			row = spreadsheet.createRow(rowid++);
			List<String> info = data.get(key);
			int cellid = 0;
			for (String obj : info) {
				Cell cell = row.createCell(cellid++);
				if (tryParse(obj) != null) {
					double number = tryParse(obj);
					cell.setCellValue(number);
				} else {
					cell.setCellValue((String) obj);
				}
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(filename));
			workbookXSSF.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}

	}

	@Override
	public Double tryParse(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public double calculateNeto(double bruto) {
		double neto;
		if (bruto > 0 && bruto <= 30000) {
			neto = bruto - (bruto * Taxes.KSHOQP + bruto * Taxes.KSHOQPU + bruto * Taxes.KSHP + bruto * Taxes.KSHPU);
		} else if (bruto >= 30001 && bruto <= 130000) {
			neto = bruto
					- (bruto * Taxes.TAP + bruto * Taxes.KSHOQP + bruto * Taxes.KSHOQPU + bruto * Taxes.KSHP + bruto * Taxes.KSHPU);
		} else {
			neto = bruto
					- (bruto * Taxes.TAP1 + bruto * Taxes.KSHOQP + bruto * Taxes.KSHOQPU + bruto * Taxes.KSHP + bruto * Taxes.KSHPU);
		}
		return neto;
	}

	@Override
	public String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

}
