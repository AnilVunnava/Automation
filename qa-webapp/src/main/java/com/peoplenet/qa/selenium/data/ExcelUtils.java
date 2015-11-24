package com.peoplenet.qa.selenium.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 *
 * This Class ExcelUtils is used to parse excel spreadsheet and
 * store that data into SuiteConfig and AccessConfig Models
 *
 */
public class ExcelUtils {

	private static Sheet dataWSheet;
	private static Sheet accessWSheet;
	private static Workbook ExcelWBook;
	private static Cell Cell;
	private static Row Row;
	public static String sheet;
	public static int initRows = 0;
	private static final Logger log = Logger.getLogger("SeleniumTest");

	// This method is to set the File path and to open the Excel file, Pass
	// Excel Path and Sheetname as Arguments to this method

	public static void setExcelFile(String path, String dataFile,
			String accessFile) throws Exception {
		try {
			// Open the Excel file
			File file = new File(path);
			if (file.exists()) {
				ExcelWBook = new XSSFWorkbook(new FileInputStream(path));

			} else if (path.startsWith("SuiteConfig")) {
				String excelFile = new File(".") + "SuiteConfig.xlsx";
				log.info("Excel File Path : " + new File(".") + "SuiteConfig.xlsx");
				ExcelWBook = new XSSFWorkbook(new FileInputStream(excelFile));

			} else {
				ExcelWBook = new SXSSFWorkbook();
			}

			// Access the required test data sheet

			if (dataFile.equals("none")) {
				dataFile = ExcelWBook.getSheetName(0);
			}
			sheet = dataFile;
			dataWSheet = ExcelWBook.getSheet(sheet);
			accessWSheet = ExcelWBook.getSheet(accessFile);
			if (dataWSheet == null) {
				dataWSheet = ExcelWBook.createSheet(sheet);
			}
		} catch (Exception e) {
			throw (e);
		}
	}

	public static void setExcelFile(InputStream stream, String sheetName)
			throws Exception {
		try {
			// Access the required test data sheet
			ExcelWBook = new SXSSFWorkbook(new XSSFWorkbook(stream));
			if (sheetName.equals("none")) {
				sheetName = ExcelWBook.getSheetName(0);
			}
			sheet = sheetName;
			dataWSheet = ExcelWBook.getSheet(sheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * @return List of SuiteConfig model parsed from Excel Spread sheet
	 */
	public static List<SuiteConfig> loadSuiteData() {
		List<SuiteConfig> excelData = new ArrayList<SuiteConfig>();
		for (Row row : dataWSheet) {
			SuiteConfig data = new SuiteConfig();
			for (Cell cell : row) {
				String cellData = null;
				if (!cell.getStringCellValue().isEmpty()) {
					cellData = cell.getStringCellValue();
				}
				switch (cell.getColumnIndex()) {
				case 0:
					data.setScript(cellData);
				case 1:
					data.setGroup(cellData);
				case 2:
					data.setProject(cellData);
				case 3:
					data.setInput(cellData);
				case 4:
					data.setBrowser(cellData);
				case 5:
					data.setStatus(cellData);
				case 6:
					data.setDescription(cellData);
				}
			}
			if (data.getScript() != null) {
				if (!data.getScript().equals("Script"))
					excelData.add(data);
			}
		}
		return excelData;
	}

	public static List<AccessConfig> loadSuiteAccessData() {
		List<AccessConfig> excelData = new ArrayList<AccessConfig>();
		for (Row row : accessWSheet) {
			AccessConfig data = new AccessConfig();
			for (Cell cell : row) {
				String cellData = null;
				if (!cell.getStringCellValue().isEmpty()) {
					cellData = cell.getStringCellValue();
				}
				switch (cell.getColumnIndex()) {
				case 0:
					data.setScript(cellData);
				case 1:
					data.setURL(cellData);
				case 2:
					data.setLogin(cellData);
				case 3:
					data.setPasword(cellData);
				}
			}
			if (data.getScript() != null) {
				if (!data.getScript().equals("Script"))
					excelData.add(data);
			}
		}
		return excelData;
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	public static void setCellData(String Result, int RowNum, int ColNum,
			String testData) throws Exception {
		try {
			Row = dataWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum,
					org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(testData);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}
}