package com.project.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.project.service.ExcelService;
import com.project.utility.Message;

@ManagedBean(name = "paymentBean")
@ViewScoped
public class PaymentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UploadedFile uploadedFile;
	private Workbook workbook;
	private StreamedContent fileDownload;
	
	@ManagedProperty(value = "#{excelService}")
	ExcelService excelService;

	public void submit() throws IOException {
		String fileName = uploadedFile.getName();
		if (excelService.getFileExtension(fileName).equals("xlsx")) {
			FileInputStream fileUpload = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fileUpload);
			Sheet sheet = workbook.getSheetAt(0);
			Map<Integer, List<String>> data = excelService.readExcel(sheet);
			if (data != null) {
				excelService.writeExcel(data, fileName);
				InputStream stream = new BufferedInputStream(new FileInputStream(fileName));
				fileDownload = new DefaultStreamedContent(stream, "application/xls", fileName);
			}
		} else {
			Message.addMessage(Message.bundle.getString("PROBLEM_TYPE_FILE"), "error");
		}

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

	public ExcelService getExcelService() {
		return excelService;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}
	
}
