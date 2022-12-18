package com.nut;

import jdk.internal.org.objectweb.asm.Handle;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * @author fym
 * @date 2022/12/16 11:35
 * @email 3271758240@qq.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateTableStructureTest {

    @Test
    public void test1() throws IOException {

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("这是第一段文字");

        XWPFTable table = document.createTable();
        table.setWidth("100%");

        // 第一行需要特殊处理
        XWPFTableRow oneRow = table.getRow(0);
        XWPFTableCell cell = oneRow.getCell(0);
        cell.setText("这是第一行，第一列文字！");

        // 添加第一列
        oneRow.addNewTableCell().setText("这是第一个行，第二列文字！");

        XWPFTableRow twoRow = table.createRow();
        twoRow.getCell(0).setText("这是第二行，第一列文字");
        twoRow.getCell(1).setText("这是第二行，第二列文字");


        // 创建标题
        XWPFParagraph heard = document.createParagraph();
        heard.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun heardRun = heard.createRun();
        heardRun.setBold(true);
//        heardRun.setColor("#ff0000");
        heardRun.setText("这是一个标题！");

        XWPFParagraph body = document.createParagraph();
        body.setIndentationFirstLine(400);
        XWPFRun bodyRun = body.createRun();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append("test"+i);
        }

        bodyRun.setFontFamily("宋体");
        bodyRun.setFontSize(30);

        bodyRun.setText(buffer.toString());

        FileOutputStream outputStream = new FileOutputStream("C:/Users/付意敏/Desktop/upload/test.docx");

        document.write(outputStream);


        outputStream.close();
    }


    @Test
    public void test02() throws IOException {

        int tableRow = 5;
        int tableColumn = 5;
        int currentRow = 0;
        int currentColumn = 0;
        XWPFDocument document = new XWPFDocument();

        XWPFTable table = document.createTable(5, 5);
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTString tblStyle = tblPr.addNewTblStyle();
        table.setTableAlignment(TableRowAlign.LEFT);
        tblStyle.setVal("StyleTable");

        // 获取每一行
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {

            CTTrPr ctTrPr = row.getCtRow().addNewTrPr();
            CTHeight ctHeight = ctTrPr.addNewTrHeight();
            ctHeight.setVal(BigInteger.valueOf(300));

            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTcPr ctTcPr = cell.getCTTc().addNewTcPr();
                CTVerticalJc ctVerticalJc = ctTcPr.addNewVAlign();
                ctVerticalJc.setVal(STVerticalJc.CENTER);

                CTShd ctShd = ctTcPr.addNewShd();
                ctShd.setColor("auto");
                ctShd.setVal(STShd.CLEAR);

                XWPFParagraph xwpfParagraph = null;
                int size = cell.getParagraphs().size();
                if (size == 0){
                    xwpfParagraph = cell.addParagraph();
                }else {
                    xwpfParagraph = cell.getParagraphs().get(size - 1);
                }

                XWPFRun run = xwpfParagraph.createRun();
                if (currentRow == 0){

                    run.setFontFamily("宋体");
                    run.setFontSize(18);
                    run.setColor("ff0000");
                    run.setText("header-"+(currentColumn+1));
                }else {
                    run.setColor("00bfff");
                    run.setText("body-"+(currentColumn+1));
                }
                xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);

                currentColumn++;
            }
            currentColumn = 0;
            currentRow++;
        }

        FileOutputStream outputStream = new FileOutputStream("C:/Users/付意敏/Desktop/upload/test03.docx");
        document.write(outputStream);
        outputStream.close();
    }
}
