package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    /* Номер таблицы */
    short numberTable;
    /*Название таблицы*/
    private String title;
    /* Количество колонок */
    private short numberColumns;
    /* Свойство ячеек */
    private  String[] colmsWidth=new String[4] ;
    /* Шабка таблицы */
    private String[] theadList=new  String[4];
    /* Строки с содержимым */
    private List<List<String>> rowsList=new ArrayList<>();


    public Table() {

    }
    public Table(short numberTable, String title, short numberColumns) {
        this.numberTable = numberTable;
        this.title = title;
        this.numberColumns=numberColumns;
    }

    public String[] getTheadList() {
        return theadList;
    }

    public void setTheadList(String[] theadList) {
        this.theadList = theadList;
    }
    public short getNumberTable() {
        return numberTable;
    }

    public void setNumberTable(short numberTable) {
        this.numberTable = numberTable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getNumberColumns() {
        return numberColumns;
    }

    public void setNumberColumns(short numberColumns) {
        this.numberColumns = numberColumns;
    }

    public String[] getColmsWidth() {
        return colmsWidth;
    }

    public void setColmsWidth(String[] colmsWidth) {
        this.colmsWidth = colmsWidth;
    }

    public List<List<String>> getRowsList() {
        return rowsList;
    }

    public void setRowsList(List<List<String>> rowsList) {
        this.rowsList = rowsList;
    }

    private boolean checkThead=false; // Проверка, что это поля шапки таблицы
    private List<String> rowList=new ArrayList<>(); // Массив 1 строки
    private boolean checkTbody=false; // Проверка, что это поля тела таблицы
    private boolean checkRow=false; // Проверка, что это поля строки


    public void setColumns(String line){
        /* Получение количество столбцов */
        if (line.contains("<tgroup cols")) {
            short colms = 0;
            String str = line.substring(line.indexOf("<tgroup cols=") + 14, line.indexOf("<tgroup cols=") + 15);
            colms = Short.parseShort(str);
            setNumberColumns(colms);
        }
        /* Уточнение количества столбцов в свойствах столбцов */
        if (colmsWidth.length<numberColumns){
            colmsWidth=new String[numberColumns];
        }
        /* Присвоение значения ширины каждой колонки */
        if (line.contains("colnum=\"")) {
            String str = line.substring(line.indexOf("colnum="));
            String[] words = str.split("\"");
            short nCol=Short.parseShort(words[1]);
            if (words[5].contains("pt")){
                colmsWidth[nCol-1]=words[5].substring(0,words[5].indexOf("pt"));
            } else {
                colmsWidth[nCol - 1] = words[5];
            }
        }
        /* Проверка, что line из шапки */
        if(line.contains("<thead>")){
            checkThead=true;
        } else if (line.contains("</thead>")){
            checkThead=false;
        }
        /* Уточнение количества столбцов в шапке */
        if (theadList.length<numberColumns){
            theadList=new String[numberColumns];
        }
        /* Присвоение названия каждого столбца */
        if (line.contains("colname=") &&checkThead){
            String str=line.substring(line.indexOf("colname=\"")+9,line.indexOf("colname=\"")+10);
            short nCol=Short.parseShort(str);
            String str1=line.substring(line.indexOf("<para>")+6,line.indexOf("</para>"));
            theadList[nCol-1]=str1;
        }
        /* Проверка вхождения line в тело таблицы */
        if (line.contains("<tbody>")){
            checkTbody=true;
        }
        if (line.contains("</tbody>")){
            checkTbody=false;
        }
        /* Проверка вхождения line в строку таблицы */
        if (line.contains("<row")){
            checkRow=true;
        }
        if (line.contains("</row>")){
            checkRow=false;
        }
        /* Пополнение строки таблицы данными */
        if (checkTbody && checkRow && line.contains("colname=")){
            String[] words = line.split("<para>");
            String[] words1=words[0].split("\"");
            int nCol=(Integer.parseInt(words1[1]))-1;
            String str="";
            if (words.length==3) {
                str = words[1].substring(0,words[1].indexOf("</")) +"\n "+ words[2].substring(0,words[2].indexOf("</"));
            } else {
                str = words[1].substring(0,words[1].indexOf("</"));
            }
            rowList.add(nCol,str);
        }

        /* Наполнения массива данными из строки */
        if (checkTbody && line.contains("</row>") && !checkThead){
            rowsList.add(rowList);
            rowList=new ArrayList<>();
        }
    }



}
