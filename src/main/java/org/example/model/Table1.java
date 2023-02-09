package org.example.model;

import java.util.List;

public class Table1 extends Table{

    /* Талица 1*/
    public Table1() {
        super();
    }



    public Table1(short numberTable, String title, short numberColumns) {
        super(numberTable, title, numberColumns);
    }

    @Override
    public short getNumberTable() {
        return super.getNumberTable();
    }

    @Override
    public void setNumberTable(short numberTable) {
        super.setNumberTable(numberTable);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public short getNumberColumns() {
        return super.getNumberColumns();
    }

    @Override
    public void setNumberColumns(short numberColumns) {
        super.setNumberColumns(numberColumns);
    }

    @Override
    public String[] getColmsWidth() {
        return super.getColmsWidth();
    }

    @Override
    public void setColmsWidth(String[] colmsWidth) {
        super.setColmsWidth(colmsWidth);
    }

    @Override
    public void setColumns(String line) {
        super.setColumns(line);
    }

    @Override
    public String[] getTheadList() {
        return super.getTheadList();
    }

    @Override
    public void setTheadList(String[] theadList) {
        super.setTheadList(theadList);
    }

    @Override
    public List<List<String>> getRowsList() {
        return super.getRowsList();
    }

    @Override
    public void setRowsList(List<List<String>> rowsList) {
        super.setRowsList(rowsList);
    }
}
