package com.codestatistic.bean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import java.io.*;

/**
 * 代码行线形统计图
 * LineChar_CLS(CodeLinesStatistic)
 *
 * @author chenpi
 * @create 2022-04-21 15:13
 */
public class LineChar_CLS {


    //通过excel创建数据集dataset
    public CategoryDataset creatDataset(ExcelStatisticFile excelFile) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // keys
        String series1 = "新增代码行";
        String series2 = "删除代码行";
        String series3 = "净增代码行";

        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet linesSheet = workbook.getSheet("lines");
        int lastRowNum = linesSheet.getLastRowNum();
        for (int i = 1; i < lastRowNum; i++) {
            Row row = linesSheet.getRow(i);
            row.getCell(0).setCellValue(Cell.CELL_TYPE_NUMERIC);
            dataset.setValue(row.getCell(1).getNumericCellValue(), series1, row.getCell(0).getDateCellValue());
            dataset.setValue(row.getCell(2).getNumericCellValue(), series2, row.getCell(0).getDateCellValue());
            dataset.setValue(row.getCell(3).getNumericCellValue(), series3, row.getCell(0).getDateCellValue());
        }
        fis.close();
        return dataset;
    }

    //根据dataset创建Jfreechart对象
    public JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Daily Code Line Statistics",//标题
                "Date",//横坐标标签
                "Lines",//纵坐标标签
                dataset,
                PlotOrientation.VERTICAL,//垂直
                true,//显示说明
                true,//显示工具提示
                false
        );
        return chart;
    }
    // 保存成jpg
    public void saveAsJPG(JFreeChart chart, String outputPath,
                                 int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 保存为PNG
            // ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
            // 保存为JPEG
            ChartUtilities.writeChartAsJPEG(out, chart, 600, 400);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }
}
