package com.mqtt.FileUtils;

import java.io.File;
import java.io.IOException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @Description: excel转为json
 * @Author: DR
 * @Date: 2024/3/22 10:54
 */

public class ExcleToJson {

    public static JSONArray excleToJson() {
        Workbook workbook;
        Sheet sheet;
        Cell cell1,cell2,cell3,cell4,cell5,cell6,cell7,cell8,cell9,cell10,cell11;
        JSONArray jsons = new JSONArray();

        try {
            //获取一个Excel文件  只支持.xls格式
            workbook = Workbook.getWorkbook(new File( "E:\\ideaProjects\\testmqtt\\xlsx\\mingdan.xls"));
            //获取文件里的某个表  从0开始
            sheet = workbook.getSheet(0);
            for(int i = 1; i < sheet.getRows(); i++) {
                //循环读出每一数据格的数据
                //sheet.getCell(列，行);
                cell1 = sheet.getCell(0 , i);
                cell2 = sheet.getCell(1 , i);
                cell3 = sheet.getCell(2 , i);
                cell4 = sheet.getCell(3 , i);
                cell5 = sheet.getCell(4 , i);
                cell6 = sheet.getCell(5 , i);
                cell7 = sheet.getCell(6 , i);
                cell8 = sheet.getCell(7 , i);
                cell9 = sheet.getCell(8 , i);
                cell10 = sheet.getCell(9 , i);
                cell11 = sheet.getCell(10 , i);
                //每一行创建一个JSONObject对象
                JSONObject object = new JSONObject();
                object.put("mdType", cell1.getContents());
                object.put("userName", cell2.getContents());
                object.put("userSex", cell3.getContents());
                object.put("userDepartment", cell4.getContents());
                object.put("idCard", cell5.getContents());
                object.put("userPhone", cell6.getContents());
                object.put("userAddress", cell7.getContents());
                object.put("userBirthday", cell8.getContents());
                object.put("startTime", cell9.getContents());
                object.put("endTime", cell10.getContents());
                object.put("rzTime", cell11.getContents());
                //加入json队列
                jsons.add(object);
            }
            //对队列进行输出或者其他操作
//            System.out.println(jsons.toJSONString());
            workbook.close();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsons;
    }

}
