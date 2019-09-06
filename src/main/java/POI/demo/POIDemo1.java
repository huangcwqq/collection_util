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
import org.springframework.core.io.ClassPathResource;

public class POIDemo1 {
    private static List<Demo1Model> modelList = new ArrayList<>();

    public static void main(String[] args){
        File file = new File("E:\\DailyTable");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : files){
                read(f);
            }
        }
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
    ClassPathResource resource =
        new ClassPathResource("template/汇总.xlsx");
        try {
            File outPutFile = resource.getFile();
           outputStream  = new FileOutputStream(outPutFile);
            ExcelHelper excelHelper = new ExcelHelper(outPutFile, 0);
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
}
