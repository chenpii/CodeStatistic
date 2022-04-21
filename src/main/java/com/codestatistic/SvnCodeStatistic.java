package com.codestatistic;

import com.codestatistic.bean.ExcelStatisticFile;
import com.codestatistic.bean.LineChar_CLS;
import com.codestatistic.bean.SvnFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.io.*;

/**
 * @author chenpi
 * @create 2022-04-21 8:42
 */
public class SvnCodeStatistic {
    public static void main(String[] args) throws IOException {
        SvnFile svn_file = new SvnFile("svn_file");
        String excelfile = "code_statistic.xls";

        Integer addLines = svn_file.getAddLines();
        Integer deleteLines = svn_file.getDeleteLines();
        Integer netAddLines = svn_file.getNetAddLines();
        System.out.println("新增行数：" + addLines);
        System.out.println("删除行数：" + deleteLines);
        System.out.println("净增行数：" + netAddLines);

        //保存数据到excel
        ExcelStatisticFile excelStatisticFile = new ExcelStatisticFile(excelfile);
        excelStatisticFile.apppendDateToExcel(excelfile, addLines, deleteLines, netAddLines);

        //从excel数据生成图表
        LineChar_CLS lineChar_cls = new LineChar_CLS();
        CategoryDataset dataset = lineChar_cls.creatDataset(excelStatisticFile);
        JFreeChart chart = lineChar_cls.createChart(dataset);
        lineChar_cls.saveAsJPG(chart, "./chart.jpg", 600, 400);

    }

}
