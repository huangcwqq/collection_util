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
public class EasyExcelDemo3 {

    private static List<List<String>> work = new ArrayList<>();
    private static List<String> content = new ArrayList<String>();
    private static Map<String,String[]> map = new HashMap<String,String[]>();

    public static void main(String[] args) {
        File file = new File("E:\\DailyTable");
    }

    private static void read(final File file) {
    try (InputStream inputStream = new FileInputStream("E:\\DailyTable\\广州集市重点巡查作业日汇报表-20190702.xlsx")) {
            ExcelReader excelReader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null, new AnalysisEventListener<List<String>>() {

                @Override
                public void invoke(List<String> object, AnalysisContext context) {
//

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


