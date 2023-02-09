package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.example.model.Table1;
import org.example.model.Table2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        /* Чтение файла xml*/
        File file = new File("Задание.xml");

        Scanner scanner;

        try {
            scanner=new Scanner(file);
        } catch (Exception e) {
            System.out.println("Open file error: "+e.toString());
            return;
        }
        /* Запись данных из файла в массив*/
        List<String> lines=new ArrayList<>();

        while (scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }

        /* Счетчик кол-ва таблиц*/
        short numberTable=0;
        /* Создание двух таблиц*/
        Table1 table1=new Table1();
        Table2 table2=new Table2();
        /* Проверка на вхождение в тело table*/
        boolean checkTable =false;
        boolean checkParaPara=false;
        List<String> textAtrb=new ArrayList<>();

        /* По строчный обход массива*/
        for (String line:lines) {
            if (line.length() == 0) continue; /* Условие чтобы не проверять пустые строки*/
            /* Условие поиска имени таблицы*/
            if (line.contains("title")){
                String str=line.substring(line.indexOf("<title>")+7,line.indexOf("</title>"));
                if (str.contains("Вспомогательное")){
                    table1.setTitle(str);
                    table1.setNumberTable((short) 1);
                    numberTable=1;
                }
                if (str.contains("Расходные")){
                    table2.setTitle(str);
                    table2.setNumberTable((short) 2);
                    numberTable=2;
                }
            }
            /* Подтверждение нахождения в теле table*/
            if (line.contains("<table")){
                checkTable = true;
            }
            if (line.contains("</table>")){
                checkTable =false;
            }
            /* Проверка наличие контента для таблицы 1 */
            if (checkTable && numberTable==1) {
                table1.setColumns(line);
            }

            /* Проверка наличие контента для таблицы 2 */
            if (checkTable && numberTable==2) {
                table2.setColumns(line);
            }

            if (line.contains("<para></para>") && numberTable==2){
                checkParaPara = true;
            }
            if (!checkTable && line.contains("<para>") && numberTable==2 && checkParaPara){
                String str=line.substring(line.indexOf("<para>")+6,line.indexOf("</para>"));
                if (str.length()!=0) {
                    textAtrb.add(str);
                }
            }
        }

        /* Создание интерфейса для заполнения Excel */
        Workbook wb=new HSSFWorkbook();
        Sheet sheet0=wb.createSheet("Лист 1"); // Создания Листа 1 в Книге
        /* Создание файла Комплект заглушек для гидроиспытания.xls */
        FileOutputStream fos=new FileOutputStream("Комплект заглушек для гидроиспытания.xls");
        /* Установление ширины столбцов */
        for (int i=0;i<table1.getNumberColumns();i++){
            String str=table1.getColmsWidth()[i];
            int n=400;
            try {
                n=Integer.parseInt(str);
            } catch (Exception ex){
                n= 300;
            }
            sheet0.setColumnWidth(i, (n*286/15));
        }
        /* Создане стилей ячеек */
        CellStyle styleBody=wb.createCellStyle();
        CellStyle styleName=wb.createCellStyle();
        /* Шрифт */
        Font font=wb.createFont();
        font.setFontName("Calibri");
        short heightFont=220;
        font.setFontHeight(heightFont);
        /* Установление стилей для общих ячеек */
        styleBody.setAlignment(HorizontalAlignment.LEFT);
        styleBody.setVerticalAlignment(VerticalAlignment.CENTER);
        styleBody.setFont(font);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setBorderTop(BorderStyle.THIN);
        /* Установление стилей для ячеек заголовков*/
        styleName.setAlignment(HorizontalAlignment.CENTER);
        styleName.setVerticalAlignment(VerticalAlignment.CENTER);
        styleName.setBorderBottom(BorderStyle.THIN);
        styleName.setBorderLeft(BorderStyle.THIN);
        styleName.setBorderRight(BorderStyle.THIN);
        styleName.setBorderTop(BorderStyle.THIN);
        styleName.setFont(font);

        int countRow=0; // Счетчик строк
        int countCell=0; // Счетчик ячейки
        /* Присвоение имени таблице */
        sheet0.addMergedRegion(new CellRangeAddress(countRow,countRow,0,table1.getNumberColumns()-1));
        Row row=sheet0.createRow(countRow);
        Cell cell= row.createCell(countCell);
        cell.setCellValue("Таблица "+table1.getNumberTable()+" "+table1.getTitle());
        cell.setCellStyle(styleName);

        countRow++;
        row=sheet0.createRow(countRow);
        /* Шапка */
        for (int i=0;i<table1.getNumberColumns();i++){
            String[] words=table1.getTheadList();
            cell= row.createCell(i);
            cell.setCellValue(words[i]);
            cell.setCellStyle(styleBody);
        }
        /* Заполнение таблицы */
        countRow++;
        for (int i=0;i<table1.getRowsList().size();i++){
            row=sheet0.createRow(countRow+i);
            List<String> rowList=table1.getRowsList().get(i);
            for (int j=0;j<rowList.size();j++){
                cell= row.createCell(j);
                cell.setCellValue(rowList.get(j));
                cell.setCellStyle(styleBody);
            }
        }
        /* Присвоение имени таблице */
        countRow++;
        countCell=0;
        sheet0.addMergedRegion(new CellRangeAddress(countRow,countRow,0,table1.getNumberColumns()-1));
        row=sheet0.createRow(countRow);
        cell= row.createCell(countCell);
        cell.setCellValue("Таблица "+table2.getNumberTable()+" "+table2.getTitle());
        cell.setCellStyle(styleName);
        countRow++;
        row=sheet0.createRow(countRow);
        /* Шапка */
        for (int i=0;i<table2.getNumberColumns();i++){
            String[] words=table2.getTheadList();
            cell= row.createCell(i);
            cell.setCellValue(words[i]);
            cell.setCellStyle(styleBody);
        }
        /* Заполнение таблицы */
        countRow++;
        for (int i=0;i<table2.getRowsList().size();i++){
            row=sheet0.createRow(countRow+i);
            List<String> rowList=table2.getRowsList().get(i);
            for (int j=0;j<rowList.size();j++){
                cell= row.createCell(j);
                cell.setCellValue(rowList.get(j));
                cell.setCellStyle(styleBody);
            }
        }

        /* Вывод текста атрибутов */
        countRow++;
        countCell=0;
        for (int i=0;i< textAtrb.size();i++){
            row=sheet0.createRow(countRow+i);
            cell= row.createCell(countCell);
            sheet0.addMergedRegion(new CellRangeAddress(countRow+i,countRow+i,0,table1.getNumberColumns()-1));
            cell.setCellValue(textAtrb.get(i));
            cell.setCellStyle(styleName);
        }
        /* Запись файла .xls */
        wb.write(fos);

        fos.close(); // Закрытие файла .xls
        scanner.close(); // Закрытие файла .xml
    }

}
