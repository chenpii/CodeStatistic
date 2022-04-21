package com.codestatistic.bean;

import java.io.*;

/**
 * svn差异文件
 *
 * @author chenpi
 * @create 2022-04-21 8:42
 */
public class SvnFile extends File {
    private Integer addLines = countAddLines(super.getPath());
    private Integer deleteLines = countDeleteLines(super.getPath());
    private Integer netAddLines = countNetAddLines();

    public SvnFile(String pathname) throws IOException {
        super(pathname);
    }

    public Integer getAddLines() {
        return addLines;
    }

    public Integer getDeleteLines() {
        return deleteLines;
    }

    public Integer getNetAddLines() {
        return netAddLines;
    }

    private Integer countAddLines(String pathname) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(new File(pathname)));
        String line;
        int count = 0;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("+") && !line.startsWith("+++") && (line.length() != 1)) {
                count++;
            }
        }
        r.close();
        return count;

    }

    private Integer countDeleteLines(String pathname) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(new File(pathname)));
        String line;
        int count = 0;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("-") && !line.startsWith("---") && (line.length() != 1)) {
                count++;
            }
        }
        r.close();
        return count;
    }

    private Integer countNetAddLines() {
        return this.addLines - this.deleteLines;
    }
}
