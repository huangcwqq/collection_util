package POI.model;

import POI.Header;
import lombok.Data;

@Data
public class Demo1Model {
    @Header("问题作业(报错原因)")
    private String number;

    @Header("问题原因")
    private String reason;

}
