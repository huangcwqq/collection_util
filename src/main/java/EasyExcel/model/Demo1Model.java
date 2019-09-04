package EasyExcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class Demo1Model extends BaseRowModel {
    @ExcelProperty(value = "文件名" ,index = 0)
    private String name;

    @ExcelProperty(value = "文件路径" ,index = 1)
    private String path;

    @ExcelProperty(value = "下载次数" ,index = 2)
    private Integer downloadTime;

    @ExcelProperty(value = "是否分区" ,index = 3)
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Integer downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
