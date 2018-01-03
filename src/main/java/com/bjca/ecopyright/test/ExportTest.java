package com.bjca.ecopyright.test;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.MyDate;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author by pikzas.
 * @Date 2016-11-10
 */
public class ExportTest {
    public static void main(String[] args) {
        try {
            File file = new File("C:/123.xls");
            Workbook wb = null;
            /**存放导入数据的list**/
            try {
                // 构造Workbook（工作薄）对象
                wb = Workbook.getWorkbook(file);
            } catch (BiffException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
            Sheet[] sheet = wb.getSheets();
            if (sheet != null && sheet.length > 0) {
                // 对每个工作表进行循环
                System.out.println("sheet.length:"+sheet.length);
                for (int i = 0; i < sheet.length; i++) {
                    File export = new File("C:/home/"+sheet[i].getName()+".txt");
                    BufferedWriter fw = null;
                    fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(export, true), "UTF-8"));
                    // 得到当前工作表的行数
                    int rowNum = sheet[i].getRows();
                    System.out.println("当前Excel数据条数："+rowNum);
                    for (int j = 0; j < rowNum; j++) {
                        Cell[] cells = sheet[i].getRow(j);
                        if (cells != null && cells.length > 0) {
                            // 对每个单元格进行循环
                            for (int k = 0; k < cells.length; k++) {
                                // 读取当前单元格的值
                                String cellValue = cells[k].getContents();
                                if(k==0){
                                    fw.append("'"+cellValue+"',");
//                                }else if(k==1){
//                                    fw.append("联系人："+cellValue);
//                                    fw.newLine();
//                                }else if(k==2){
//                                    if(Function.isEmpty(cellValue)){
//                                        fw.append("固定电话：无");
//                                        fw.newLine();
//                                    }else{
//                                        fw.append("固定电话："+cellValue);
//                                        fw.newLine();
//                                    }
//                                }else if(k==3){
//                                    if(Function.isEmpty(cellValue)){
//                                        fw.append("手机：无");
//                                        fw.newLine();
//                                    }else{
//                                        fw.append("手机："+cellValue);
//                                        fw.newLine();
//                                    }
//                                }else if(k==4){
//                                    if(Function.isEmpty(cellValue)){
//                                        fw.append("地址：无");
//                                        fw.newLine();
//                                    }else{
//                                        fw.append("地址："+cellValue);
//                                        fw.newLine();
//                                    }
//                                }else if(k==5){
//                                    if(Function.isEmpty(cellValue)){
//                                        fw.append("代理品牌：无");
//                                        fw.newLine();
//                                    }else{
//                                        fw.append("代理品牌："+cellValue);
//                                        fw.newLine();
//                                    }
//                                    fw.newLine();
                                    fw.flush();
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
