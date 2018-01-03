package com.bjca.ecopyright.util;

import com.bjca.ecopyright.soft.model.Software;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
  /**
   * Excel导入/导出数据用（针对中心导入的Excel模板设计）
   * @author bxt-chenjian
   * @date 2016.5.19
   */
public class ReadWriteExcelUtil {

    /** 
     * 从excel文件中讀取所有的內容 
     * @param inputStream  excel文件流
     * @return 需要的list对象
     */  
    public static List<Software> readExcel(InputStream inputStream) {
        Workbook wb = null;  
        /**存放导入数据的list**/
        List<Software> softlist = new ArrayList<Software>();
        try {  
            // 构造Workbook（工作薄）对象  
            wb = Workbook.getWorkbook(inputStream);
        } catch (BiffException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        if (wb == null)  
            return softlist;  
        // 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了  
        Sheet[] sheet = wb.getSheets();  
        
        if (sheet != null && sheet.length > 0) {  
            // 对每个工作表进行循环  
        	System.out.println("sheet.length:"+sheet.length);
            for (int i = 0; i < 1; i++) {
                // 得到当前工作表的行数  
                int rowNum = sheet[i].getRows(); 
                System.out.println("当前Excel数据条数："+rowNum);
                for (int j = 0; j < rowNum; j++) {
                	/**存放每条数据的对象**/
                	Software software = new Software();
                    // 得到当前行的所有单元格  
                    Cell[] cells = sheet[i].getRow(j);
                    if (cells != null && cells.length > 0) {
                        // 对每个单元格进行循环
                        for (int k = 0; k < cells.length; k++) {
                            // 读取当前单元格的值
                            //对序列号进行判断 如果没有序列号 则为空行
                            String cellValue = cells[k].getContents();
                                /**如果读入的是NUMBER 说明是日期，需要转换为日期格式**/
                                Date date = new Date();
                                if(cells[k].getType()==CellType.NUMBER ||cells[k].getType()==CellType.NUMBER_FORMULA){
                                    String changeNumToDate = changeNumToDate(cellValue);
                                    date = MyDate.get(changeNumToDate);
                                }
                                if(cells[k].getType()==CellType.DATE ){
                                    DateCell dc = (DateCell)cells[k];
                                    date = dc.getDate();
                                }
                                /**组装导入数据对象**/
                                if(k == 0){
                                    software.setAcceptdate(date);
                                }else if(k==1){
                                    software.setSerialnum(cellValue);
                                }else if(k == 4){
                                    software.setApplyperson(cellValue);
                                }else if(k == 5){
                                    software.setSoftwarename(cellValue);
                                }else if(k == 6){
                                    software.setSalesman(cellValue);
                                }else if(k == 7){
                                    software.setCertificatedate(date);
                                }
                        }
                    }  
                    softlist.add(software);
                }  
                System.out.println("softlist长度："+softlist.size());
            }  
        }  
        // 最后关闭资源，释放内存  
        wb.close();  
        return softlist;  
    }  
    
    /**
     * 制证出库导入execl
     * @date 2016-6-21下午2:52:23
     * @author chenping
     * @param inputStream //2016-8-12 改用流 可避免使用零食文件
     * @return
     */
    public static List<Software> readZZCKExcel(InputStream inputStream) {
    	Workbook wb = null;  
    	/**存放导入数据的list**/
    	List<Software> softlist = new ArrayList<Software>();
    	try {  
    		// 构造Workbook（工作薄）对象  
    		wb = Workbook.getWorkbook(inputStream);
    	} catch (BiffException e) {  
    		e.printStackTrace();  
    	} catch (IOException e) {  
    		e.printStackTrace();  
    	}  
    	if (wb == null)  
    		return softlist;  
    	// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了  
    	Sheet[] sheet = wb.getSheets();  
    	
    	if (sheet != null && sheet.length > 0) {  
    		// 对每个工作表进行循环  
    		System.out.println("sheet.length:"+sheet.length);
    		for (int i = 0; i < 1; i++) {
    			// 得到当前工作表的行数  
    			int rowNum = sheet[i].getRows(); 
    			System.out.println("当前Excel数据条数："+rowNum);
    			for (int j = 0; j < rowNum; j++) {
    				/**存放每条数据的对象**/
    				Software software = new Software();
    				// 得到当前行的所有单元格  
    				Cell[] cells = sheet[i].getRow(j);
    				if (cells != null && cells.length > 0) {
                        // 对每个单元格进行循环
                        for (int k = 0; k < cells.length; k++) {
                            // 读取当前单元格的值
                            String cellValue = cells[k].getContents();
                            /**如果读入的是NUMBER 说明是日期，需要转换为日期格式**/
                            Date date = new Date();
                            if(cells[k].getType()==CellType.NUMBER ||cells[k].getType()==CellType.NUMBER_FORMULA){
                                String changeNumToDate = changeNumToDate(cellValue);
                                date = MyDate.get(changeNumToDate);
                            }
                            if(cells[k].getType()==CellType.DATE ){
                                DateCell dc = (DateCell)cells[k];
                                date = dc.getDate();
                            }
                            /**组装导入数据对象**/
                            if(k == 1){
                                software.setAcceptdate(date);
                            }else if(k==2){
                                software.setSoftwarename(cellValue);
                            }else if(k == 4){
                                software.setSerialnum(cellValue);
                            }
                        }
                    }
    				softlist.add(software);
    			}  
    			System.out.println("softlist长度："+softlist.size());
    		}  
    	}  
    	// 最后关闭资源，释放内存  
    	wb.close();  
    	return softlist;  
    }  
    /**
     * jxl导入Excel数据读取时如果为日期读入的是一串数字，该功能可格式化该数字为日期格式
     * @param s 传入需要格式化数字日期
     * @return
     * @author bxt-chenjian
     * @date 2016.5.19
     */
    public static String changeNumToDate(String s){
        String rtn= "1900-01-01";
        try{
	         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	         java.util.Date date1 = new java.util.Date();
	         date1 = format.parse("1900-01-01");
	         long i1 = date1.getTime();
			//这里要减去2，(Long.parseLong(s)-2) 不然日期会提前2天，具体原因不清楚，
			//估计和java计时是从1970-01-01开始有关
	         //而excel里面的计算是从1900-01-01开始
	         i1= i1/1000+( (Long.parseLong(s)-2)*24*3600);
	         date1.setTime(i1*1000);
	         rtn=format.format(date1);
        }catch(Exception e){
        	rtn= "1900-01-01";
        }
        return rtn;
       }

    /**
     * 把內容寫入excel文件中 
     *  
     * @param fileName 
     *            要寫入的文件的名稱 
     *@author bxt-chenjian
     *@date 2016.5.19
     */  
    public static boolean writeExcel(String fileName,List<Software> softList) {  
        WritableWorkbook wwb = null;  
        try {  
            // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象  
            wwb = Workbook.createWorkbook(new File(fileName));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        if (wwb != null) {  
            // 创建一个可写入的工作表  
            // Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置  
            WritableSheet ws = wwb.createSheet("sheet1", 0);  
            // 下面开始添加单元格  添加表头
            Label labelC1 = new Label(0, 0, "接收日期");  
            Label labelC2 = new Label(1, 0, "流水号");  
            Label labelC3 = new Label(2, 0, "申请人");  
            Label labelC4 = new Label(3, 0, "软件全称");  
            Label labelC5 = new Label(4, 0, "业务员");  
            Label labelC6 = new Label(5, 0, "出证日期"); 
            try {
				ws.addCell(labelC1);
				ws.addCell(labelC2);
				ws.addCell(labelC3);
				ws.addCell(labelC4);
				ws.addCell(labelC5);
				ws.addCell(labelC6);
			} catch (RowsExceededException e1) {
				e1.printStackTrace();
			} catch (WriteException e1) {
				e1.printStackTrace();
			}
            /**将需要导出数据依次存入每个表格**/
            for (int i = 1; i < softList.size()+1; i++) {  
                for (int j = 0; j < 6; j++) {  
                    // 这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行  
                	Label labelC = new Label(0, 0, null);
                	if(j == 0){
                		labelC = new Label(j, i,MyDate.toString(softList.get(i-1).getAcceptdate(),"yyyy年MM月dd日"));  
                	}else if(j == 1){
                		labelC = new Label(j, i, softList.get(i-1).getSerialnum());  
                	}else if(j == 2){
                		labelC = new Label(j, i, softList.get(i-1).getApplyperson());  
                	}else if(j == 3){
                		labelC = new Label(j, i, softList.get(i-1).getSoftwarename());  
                	}else if(j == 4){
                		labelC = new Label(j, i, softList.get(i-1).getSalesman());  
                	}else if(j == 5){
                		labelC = new Label(j, i, MyDate.toString(softList.get(i-1).getCertificatedate(),"yyyy年MM月dd日"));  
                	}
                    try {  
                        // 将生成的单元格添加到工作表中  
                        ws.addCell(labelC);  
                    } catch (RowsExceededException e) {  
                        e.printStackTrace();  
                    } catch (WriteException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
            try {  
                // 从内存中写入文件中  
                wwb.write();  
                // 关闭资源，释放内存  
                wwb.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } catch (WriteException e) {  
                e.printStackTrace();  
            }  
        }
		return true;  
    }


    /**
     * 批量核费导入Execl
     * @date 2016-9-18下午2:58:42
     * @mail humin@bjca.org.cn
     * @author humin
     * @param inputStream
     * @return
     */
    public static List<Software> readAccountExcel(InputStream inputStream) {
    	Workbook wb = null;  
    	/**存放导入数据的list**/
    	List<Software> softlist = new ArrayList<Software>();
    	try {  
    		// 构造Workbook（工作薄）对象  
    		wb = Workbook.getWorkbook(inputStream);
    	} catch (BiffException e) {  
    		e.printStackTrace();  
    	} catch (IOException e) {  
    		e.printStackTrace();  
    	}  
    	if (wb == null)  
    		return softlist;  
    	// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了  
    	Sheet[] sheet = wb.getSheets();  
    	
    	if (sheet != null && sheet.length > 0) {  
    		// 对每个工作表进行循环  
    		for (int i = 0; i < 1; i++) {
    			// 得到当前工作表的行数  
    			int rowNum = sheet[i].getRows(); 
    			for (int j = 0; j < rowNum; j++) {
    				/**存放每条数据的对象**/
    				Software software = new Software();
    				// 得到当前行的所有单元格  
    				Cell[] cells = sheet[i].getRow(j);
    				if (cells != null && cells.length > 0) {
                        // 对每个单元格进行循环
                        for (int k = 0; k < cells.length; k++) {
                            // 读取当前单元格的值
                            String cellValue = cells[k].getContents();
                            /**如果读入的是NUMBER 说明是日期，需要转换为日期格式**/
                            Date date = new Date();
                            if(cells[k].getType()==CellType.NUMBER ||cells[k].getType()==CellType.NUMBER_FORMULA){
                                String changeNumToDate = changeNumToDate(cellValue);
                                date = MyDate.get(changeNumToDate);
                            }
                            /**组装导入数据对象**/
                            if(k == 1){
                            	software.setSerialnum(cellValue);
                            }
                        }
                    }
    				softlist.add(software);
    			}  
    			System.out.println("softlist长度："+softlist.size());
    		}  
    	}  
    	// 最后关闭资源，释放内存  
    	wb.close();  
    	return softlist;  
    }

  
}  