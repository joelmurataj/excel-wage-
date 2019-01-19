package com.project.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.project.utility.Message;

@ManagedBean(name = "paymentBean")
@ViewScoped
public class PaymentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UploadedFile uploadedFile;
	private static final double TAP = 0.13;
	private static final double TAP1 = 0.23;
	private static final double KSHP = 0.017;
	private static final double KSHOQP = 0.095;
	private static final double KSHPU = 0.017;
	private static final double KSHOQPU = 0.15;
	private Workbook workbook;
	private StreamedContent fileDownload;

	public void submit() throws IOException {
		String fileName = uploadedFile.getName();
		if (getFileExtension(fileName).equals("xlsx")) {
			FileInputStream fileUpload = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fileUpload);
			Sheet sheet = workbook.getSheetAt(0);
			Map<Integer, List<String>> data = readExcel(sheet);
			if (data != null) {
				writeExcel(data, fileName);
				InputStream stream = new BufferedInputStream(new FileInputStream(fileName));
				fileDownload = new DefaultStreamedContent(stream, "application/xls", fileName);
			}
		} else {
			Message.addMessage(Message.bundle.getString("PROBLEM_TYPE_FILE"), "error");
		}

	}

	private Map<Integer, List<String>> readExcel(Sheet sheet) {
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
						Double neto = (double) 0;
						if (bruto > 0 && bruto <= 30000) {
							neto = bruto - (bruto * KSHOQP + bruto * KSHOQPU + bruto * KSHP + bruto * KSHPU);
						} else if (bruto >= 30001 && bruto <= 130000) {
							neto = bruto
									- (bruto * TAP + bruto * KSHOQP + bruto * KSHOQPU + bruto * KSHP + bruto * KSHPU);
						} else {
							neto = bruto
									- (bruto * TAP1 + bruto * KSHOQP + bruto * KSHOQPU + bruto * KSHP + bruto * KSHPU);
						}
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

	private void writeExcel(Map<Integer, List<String>> data, String filename) throws IOException {
		FileInputStream file = new FileInputStream(new File(filename));
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		XSSFSheet spreadsheet = workbook.getSheetAt(0);

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
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}
	}

	private static Double tryParse(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload) {
		this.fileDownload = fileDownload;
	}

}
