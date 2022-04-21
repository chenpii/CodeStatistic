package com.codestatistic.bean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * excel文件
 *
 * @author chenpi
 * @create 2022-04-21 13:44
 */
public class ExcelStatisticFile extends File {

    public ExcelStatisticFile(String pathname) {
        super(pathname);
    }

    public void apppendDateToExcel(String excelfile, Integer addlines, Integer deletelines, Integer netaddlines) throws IOException {
        File file = new File(excelfile);
        if (!(file.exists())) {
            createExcel(excelfile);
        }
        System.out.println("追加数据至" + excelfile);
        FileInputStream fis = new FileInputStream(excelfile);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet linesSheet = workbook.getSheet("lines");
        int RowNum = linesSheet.getLastRowNum() + 1;
        Row row = linesSheet.createRow(RowNum);

        //日期为前一天
        row.createCell(0).setCellValue(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
        row.createCell(1).setCellValue(addlines);
        row.createCell(2).setCellValue(deletelines);
        row.createCell(3).setCellValue(netaddlines);

        FileOutputStream fos = new FileOutputStream(excelfile);
        workbook.write(fos);
        fos.close();


    }

    public void createExcel(String excelfile) throws IOException {
//        FileInputStream fis = new FileInputStream(excelfile);
        System.out.println(excelfile + "不存在，自动创建");
        //1.创建工作簿
        Workbook workbook = new HSSFWorkbook();
        //2.创建工作表
        Sheet linesSheet = workbook.createSheet("lines");
        //3.初始化表头
        Row title = linesSheet.createRow(0);
        Cell datetitle = title.createCell(0);
        datetitle.setCellValue("Date");
        Cell addlinetitle = title.createCell(1);
        addlinetitle.setCellValue("AddLines");
        Cell deletelinestitle = title.createCell(2);
        deletelinestitle.setCellValue("DeleteLines");
        Cell netaddlinestitle = title.createCell(3);
        netaddlinestitle.setCellValue("NetAddLines");

        FileOutputStream fos = new FileOutputStream(excelfile);
        workbook.write(fos);
        fos.close();

    }
}
