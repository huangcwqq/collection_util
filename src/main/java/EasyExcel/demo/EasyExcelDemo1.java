package EasyExcel.demo;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import EasyExcel.model.Demo1Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Walker
 * @since 1.0 2018-09-28
 */
public class EasyExcelDemo1 {

    private static List<List<String>> work = new ArrayList<>();
    private static List<String> content = new ArrayList<String>();
    private static Map<String,String[]> map = new HashMap<String,String[]>();

    static {
        content.add("_番禺");
        content.add("_越秀");
        content.add("_白云");
        content.add("_从化");
        content.add("_天河");
        content.add("_花都");
        content.add("_西区");
        content.add("_增城");
        content.add("_黄埔");
    }

    public static void main(String[] args) {
        read();
        write();
    }

    private static void read() {
        try (InputStream inputStream = new FileInputStream("E:\\ExcelDemo\\input\\temp.xlsx")) {
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, new AnalysisEventListener<List<String>>() {

                @Override
                public void invoke(List<String> object, AnalysisContext context) {
                    List<String> stringList = new ArrayList<>();
                    if(context.getCurrentRowNum() > 0) {
                        for (String o : object) {
                            if (o == null)
                                break;
                            String str = o;
                            for (int i = 0; i < content.size(); i++) {
                                if (str.contains(content.get(i))) {
                                    String temp1 = str.substring(0, str.indexOf(content.get(i)));
                                    String temp2 = str.substring(str.indexOf(content.get(i))+content.get(i).length()+1);
                                    filter(temp1,temp2);
                                    str = temp1 +"_AA_"+temp2;
                                    break;
                                }
                            }
                            if (str.contains("_GZ")) {
                                String temp1 = str.substring(0, str.indexOf("_GZ")).trim();
                                String temp2 = str.substring(str.indexOf("_GZ")+"_GZ".length()+1);
                                str = filter(temp1,temp2);
                            }
                            if (str.contains("_广州_")) {
                                String temp1 = str.substring(0, str.indexOf("_广州_")).trim();
                                String temp2 = str.substring(str.indexOf("_广州_")+"_广州_".length());
                                str = filter(temp1,temp2);
                            }
                            if (str.contains("_广州.")) {
                                String temp1 = str.substring(0, str.indexOf("_广州.")).trim();
                                String temp2 = str.substring(str.indexOf("_广州.")+"_广州.".length());
                                str = filter(temp1,temp2);
                            }
                            if (str.contains("_广州")) {
                                String temp1 = str.substring(0, str.indexOf("_广州")).trim();
                                String temp2 = str.substring(str.indexOf("_广州")+"_广州".length());
                                str = filter(temp1,temp2);
                            }
                            if (str.contains("_201")) {
                                String temp1 = str.substring(0, str.indexOf("_201")).trim();
                                String temp2 = str.substring(str.indexOf("_201")+1);
                                str = filter(temp1,temp2);
                            }
                            if (str.contains(".201")) {
                                String temp1 = str.substring(0, str.indexOf(".201")).trim();
                                String temp2 = str.substring(str.indexOf(".201")+1);
                                str = filter(temp1,temp2);
                            }
                            if (str.contains(".")) {
                                String temp1 = str.substring(0, str.indexOf(".")).trim();
                                String temp2 = str.substring(str.indexOf(".")+".".length());
                                str = filter(temp1,temp2);
                            }
                            stringList.add(str);
                        }
                        work.add(stringList);
                    }
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
        ExcelWriter dailyWriter = null;
        ExcelWriter monthWriter = null;
        try {
            OutputStream dailyStream = new FileOutputStream("E:\\ExcelDemo\\output\\日表.xlsx");
            OutputStream monthStream = new FileOutputStream("E:\\ExcelDemo\\output\\月表.xlsx");
            dailyWriter = new ExcelWriter(dailyStream, ExcelTypeEnum.XLSX);
            monthWriter = new ExcelWriter(monthStream, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0, Demo1Model.class);
            for(List<String> cellList : work){
                String[] str = null;
                if(map.containsKey(cellList.get(0))) {
                    str = map.get(cellList.get(0));
                    int cou = Integer.valueOf(str[1])+Integer.valueOf(cellList.get(2));
                    str[1] = String.valueOf(cou);
                    map.put(cellList.get(0), str);
                }else {
                    str = new String[2];
                    str[0] = cellList.get(1);
                    str[1] = cellList.get(2);
                    map.put(cellList.get(0), str);
                }
            }
            writeData(dailyWriter,monthWriter,sheet);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            dailyWriter.finish();
            monthWriter.finish();
        }
    }

    private static void writeData(ExcelWriter dailyWriter,ExcelWriter monthWriter,Sheet sheet) {
        List<Demo1Model> dailyList = new ArrayList<>();
        List<Demo1Model> monthList = new ArrayList<>();
        for (Map.Entry<String,String[]> entry : map.entrySet()) {
            Demo1Model demo1Model = new Demo1Model();
//            String[] strings = entry.getKey().split(",");
            demo1Model.setName(entry.getKey());
            demo1Model.setPath(entry.getValue()[0]);
            demo1Model.setDownloadTime(Integer.valueOf(entry.getValue()[1]));
            demo1Model.setRemarks("");
            if(entry.getKey().contains("YYYYMMDD")){
                dailyList.add(demo1Model);
            }else {
                monthList.add(demo1Model);
            }
        }
        dailyWriter.write(dailyList,sheet);
        monthWriter.write(monthList,sheet);
    }

    private static String filter(String temp1,String temp2){
        String s = "";
//        if(("大广州基站下有语音通话的全量移动号码").equals(temp1) || "V网成员明细数据_按省口径".equals(temp1) || "V网成员汇总数据_按省口径".equals(temp1) || "TEST_KD_BI_INF".equals(temp1)){
//            s = (temp1 + "_YYYYMMDD").trim();
//            return s;
//        }
//        if(("专线合作分成模式明细数据").equals(temp1) || "V网成员明细数据_按省口径".equals(temp1) || "V网成员汇总数据_按省口径".equals(temp1)){
//            s = (temp1 + "_YYYYMM").trim();
//            return s;
//        }
//        if(temp2.length() >= 15 || temp2.length() == 14){
//            s = (temp1 + "_YYYYMMDD").trim() ;
//        }else if(temp2.length() == 13){
//            s = (temp1 + "_YYYYMM").trim();
//        }else if(temp2.length() == 11){
//            s = (temp1 + "_YYYYMMDD").trim();
//        }else if(temp2.length() == 9 || temp2.length() == 10){
//            s = (temp1 + "_YYYYMM").trim();
//        }
        if(temp1.contains(".")){
            temp1 = temp1.substring(0,temp1.indexOf(".")).trim();
        }
        if(temp2.contains(".")){
            temp2= temp2.substring(0,temp2.indexOf(".")).trim();
        }
        if(temp2.length() == 8){
            s = (temp1 + "_YYYYMMDD").trim() ;
        }
        if(temp2.length() == 6){
            s = (temp1 + "_YYYYMM").trim();
        }
        if("".equals(s)){
            System.out.println(temp1);
        }
        return s;
    }

}


