package com.octave.foot.utils;

import android.os.Environment;

import com.octave.foot.beans.CenterOfPressure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 使用jxl来导入Excel
 * Created by Paosin Von Scarlet on 2017/1/9.
 */

public class FileTools {
    public String sdPath(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdFile = Environment.getExternalStorageDirectory();
            String filePath = sdFile.getAbsolutePath();
            return filePath + "/" + path;
        }
        return null;
    }

    public List<CenterOfPressure> readExcel(String path) {
        List<CenterOfPressure> list = new ArrayList<CenterOfPressure>();
        try {
            Workbook workbook = Workbook.getWorkbook(new File(sdPath(path)));
            //获取第一个工作表的对象
            Sheet sheet = workbook.getSheet(0);
            //获取第一列第一行的的单元格
            for (int i = 0; i < sheet.getRows(); i++) {
                CenterOfPressure centerOfPressure = new CenterOfPressure();
                centerOfPressure.setFrame(Integer.parseInt(sheet.getCell(0, i).getContents()));
                centerOfPressure.setMs(Float.parseFloat(sheet.getCell(1, i).getContents()));
                centerOfPressure.setX(Float.parseFloat(sheet.getCell(2, i).getContents()));
                centerOfPressure.setY(Float.parseFloat(sheet.getCell(3, i).getContents()));
                centerOfPressure.setForce(Integer.parseInt(sheet.getCell(4, i).getContents()));
                list.add(centerOfPressure);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return list;
    }
}
