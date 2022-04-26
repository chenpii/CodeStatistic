package com.codestatistic;

import com.codestatistic.bean.ExcelStatisticFile;
import com.codestatistic.bean.SvnFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
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
        CategoryDataset dataset = creatDataset(excelStatisticFile);
        JFreeChart chart = createChart(dataset);
        saveAsJPG(chart, "./linechart.jpg", 800, 600);

    }

    public static CategoryDataset creatDataset(ExcelStatisticFile excelFile) throws IOException {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // keys
        String series1 = "add";
        String series2 = "delete";
        String series3 = "netadd";

        FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new HSSFWorkbook(fis);
        Sheet linesSheet = workbook.getSheet("lines");
        int lastRowNum = linesSheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            Row row = linesSheet.getRow(i);

//            row.getCell(0).setCellValue(Cell.CELL_TYPE_NUMERIC);
            String dateCellValue = row.getCell(0).getStringCellValue();

//            row.getCell(1).setCellValue(Cell.CELL_TYPE_NUMERIC);
            double addLineCellValue = row.getCell(1).getNumericCellValue();

//            row.getCell(2).setCellValue(Cell.CELL_TYPE_NUMERIC);
            double deleteLineCellValue = row.getCell(2).getNumericCellValue();

//            row.getCell(3).setCellValue(Cell.CELL_TYPE_NUMERIC);
            double netaddLineCellValue = row.getCell(3).getNumericCellValue();

//            System.out.println("Date:" + dateCellValue + ",add:" + addLineCellValue + ",delete:" + deleteLineCellValue + ",netadd:" + netaddLineCellValue);

            dataset.setValue(addLineCellValue, series1, dateCellValue);
            dataset.setValue(deleteLineCellValue, series2, dateCellValue);
            dataset.setValue(netaddLineCellValue, series3, dateCellValue);
        }
        fis.close();
        return dataset;
    }

    //根据dataset创建Jfreechart对象
    public static JFreeChart createChart(CategoryDataset dataset) {
        System.out.println("生成图表...");
        JFreeChart chart = ChartFactory.createLineChart(
                "Daily Code Line Statistics",//标题
                "Date",//横坐标标签
                "Lines",//纵坐标标签
                dataset,
                PlotOrientation.VERTICAL,//垂直
                true,//显示说明
                true,//显示工具提示
                false
        );
        //背景色
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(SystemColor.WHITE);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = categoryPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.GREEN);//第1条线为绿色
        renderer.setSeriesPaint(1, Color.RED);//第2条线为红色
        renderer.setSeriesPaint(2, Color.BLUE);//第3条线为蓝色

        categoryPlot.setBackgroundPaint(Color.lightGray);
        categoryPlot.setDomainGridlinesVisible(true);
        categoryPlot.setRangeGridlinesVisible(true);
        categoryPlot.setDomainGridlinePaint(Color.white);//设置横坐标网格线
        categoryPlot.setRangeGridlinePaint(Color.white);//设置纵坐标网格线

        CategoryAxis categoryAxis = categoryPlot.getDomainAxis();
        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 45度倾斜


        return chart;
    }

    // 保存成jpg
    public static void saveAsJPG(JFreeChart chart, String outputPath,
                                 int weight, int height) {
        System.out.println("导出成" + outputPath);
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
//            ChartUtilities.writeChartAsJPEG(out, chart, 600, 400);
            ChartUtilities.writeChartAsJPEG(out, chart, weight, height);

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
