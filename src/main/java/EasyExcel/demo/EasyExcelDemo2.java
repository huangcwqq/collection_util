package EasyExcel.demo;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Walker
 * @since 1.0 2018-09-28
 */
public class EasyExcelDemo2 {

    private static List<List<String>> work = new ArrayList<>();
    private static List<String> content = new ArrayList<String>();
    private static Map<String,String[]> map = new HashMap<String,String[]>();

    public static void main(String[] args) {
    File file = new File("E:\\汇总表\\201908");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : files){
                read(f);
            }
        }
//        for (List<String> list : work){
//            System.out.println(list);
//        }
//        write();
    }

    private static void read(final File file) {
    try (InputStream inputStream = new FileInputStream(file)) {
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, new AnalysisEventListener<List<String>>() {

                @Override
                public void invoke(List<String> object, AnalysisContext context) {
                    if (context.getCurrentSheet().getSheetNo()== 2){
                        if(context.getCurrentRowNum() > 0){
                            List<String> stringList = new ArrayList<>();
                            if(object.get(0) != null){
//                                System.out.println(file.getName()+":");
                                for(String str : object){
                                    if(str == null)
                                        break;
                                    stringList.add(str);
                                }
                                System.out.println(stringList.toString());
                                work.add(stringList);
                            }
                        }
                    }
                        System.out.println(context.getCurrentSheet().getSheetNo()+":"+context.getCurrentRowNum()+":"+context.getCurrentRowAnalysisResult());


                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {

                }
            });
            excelReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void write() {
    }


}


