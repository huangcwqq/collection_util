package POI.demo;

import POI.ExcelHelper;
import POI.model.Demo1Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

public class POIDemo1 {
    private static List<Demo1Model> modelList = new ArrayList<>();

    public static void main(String[] args){
    File file = new File("E:\\Summary\\201908");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : files){
                read(f);
            }
        }
//        writeTxt(modelList);
        write(modelList);
    }
    private static void read(File file){
        InputStream inputStream = null ;
        ExcelHelper excelHelper = null;
        List<Demo1Model> models = null;
        try {
            inputStream = new FileInputStream(file);
            excelHelper = new ExcelHelper(inputStream, 2);
            models = excelHelper.readObjects(0,Demo1Model.class);
            if (models != null && models.size() > 0) {
                for (Demo1Model model : models) {
                    modelList.add(model);
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    private static void write(List<?> objects){
        OutputStream outputStream = null;
        InputStream input = null ;
//    ClassPathResource resource = new ClassPathResource("201908.xlsx");
        try {
//            File outPutFile = resource.getFile();
      File outPutFile = new File("E:\\Summary\\output\\201908.xlsx");
            input = new FileInputStream(outPutFile);
            ExcelHelper excelHelper = new ExcelHelper(input, 2);
            outputStream  = new FileOutputStream(outPutFile);
            int headerRow = 0;
            if (objects.size() >= 1) {
                for (int i = 0; i < objects.size(); i++) {
                    excelHelper.writeObjects(headerRow, objects);
                }
                excelHelper.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.println("导出失败");
                e.printStackTrace();
            }
        }
    }

    private static void writeTxt(List<Demo1Model> modelList){
        OutputStream outputStream = null;
        try {
      File outPutFile = new File("E:\\汇总表\\Output\\201908.txt");
            outputStream  = new FileOutputStream(outPutFile);
            StringBuilder out = new StringBuilder();
            for(Demo1Model model : modelList){
                out.append(model.getNumber()).append(" ").append(model.getReason()).append("\n");
            }
            byte[] bytes = out.toString().getBytes();
           outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                System.out.println("导出失败");
                e.printStackTrace();
            }
        }
    }
}
