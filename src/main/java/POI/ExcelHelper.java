package POI;

import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExcelHelper implements Closeable {
  private Workbook workbook;
  private Sheet sheet;

  public ExcelHelper(InputStream in, int sheetIdx) throws IOException {
    workbook = getWorkbook(in);
    sheet = workbook.getSheetAt(sheetIdx);
    in.close();
  }

  public ExcelHelper(File file, int sheetIdx) throws IOException {
    InputStream in = new FileInputStream(file);
    workbook = getWorkbook(in);
    sheet = workbook.getSheetAt(sheetIdx);
    in.close();
  }

  public ExcelHelper(File file) throws IOException {
    InputStream in = new FileInputStream(file);
    workbook = getWorkbook(in);
    in.close();
  }

  public void setCellValue(String address, Object value) {
    if (value != null) {
      Cell cell = getCell(address);
      setCellValue(cell, value);
    }
  }

  public void setCellValue(int rowIdx, int colIdx, Object value) {
    if (value != null) {
      Cell cell = getCell(rowIdx, colIdx);
      setCellValue(cell, value);
    }
  }

  private void setCellValue(Cell cell, Object value) {
    if (value instanceof String) {
      cell.setCellType(CellType.STRING);
      cell.setCellValue((String) value);
    } else if (value instanceof Double || value instanceof Integer) {
      cell.setCellType(CellType.NUMERIC);
      cell.setCellValue((Double) value);
    } else if (value instanceof Date) {
      cell.setCellType(CellType.NUMERIC);
      cell.setCellValue((Date) value);
    } else if (value instanceof Boolean) {
      cell.setCellType(CellType.BOOLEAN);
      cell.setCellValue((Boolean) value);
    } else {
      cell.setCellType(CellType.BLANK);
    }
  }

  public void setCellFormula(String address, String formula) {
    Cell cell = getCell(address);
    cell.setCellType(CellType.FORMULA);
    cell.setCellFormula(formula);
  }

  public void setCellFormula(int rowIdx, int colIdx, String formula) {
    Cell cell = getCell(rowIdx, colIdx);
    cell.setCellType(CellType.FORMULA);
    cell.setCellFormula(formula);
  }

  private Cell getCell(String address) {
    CellAddress cellAddress = new CellAddress(address);
    int rowIdx = cellAddress.getRow();
    int colIdx = cellAddress.getColumn();
    return getCell(rowIdx, colIdx);
  }

  private Cell getCell(int rowIdx, int colIdx) {
    Row row = CellUtil.getRow(rowIdx, sheet);
    Cell cell = CellUtil.getCell(row, colIdx);
    return cell;
  }

  public void setPicture(String address, File file) throws IOException {
    CellAddress cellAddress = new CellAddress(address);
    int rowIdx = cellAddress.getRow();
    int colIdx = cellAddress.getColumn();
    if (sheet instanceof XSSFSheet) {
      setXSSFPicture(rowIdx, colIdx, file);
    } else {
      setHSSFPicture(rowIdx, colIdx, file);
    }
  }

  public void setPicture(int rowIdx, int colIdx, File file) throws IOException {
    if (sheet instanceof XSSFSheet) {
      setXSSFPicture(rowIdx, colIdx, file);
    } else {
      setHSSFPicture(rowIdx, colIdx, file);
    }
  }

  private void setXSSFPicture(int rowIdx, int colIdx, File file) throws IOException {
    XSSFSheet xssfSheet = (XSSFSheet) sheet;
    XSSFClientAnchor anchor =
        new XSSFClientAnchor(
            (int) (127 * 9525 * 0.1),
            (int) (76 * 9525 * 0.1),
            0,
            0,
            colIdx,
            rowIdx,
            colIdx,
            rowIdx);
    int format =
        file.getName().toLowerCase().endsWith(".png")
            ? XSSFWorkbook.PICTURE_TYPE_PNG
            : XSSFWorkbook.PICTURE_TYPE_JPEG;
    int index = xssfSheet.getWorkbook().addPicture(getPictureBytes(file), format);
    XSSFDrawing patriarch = xssfSheet.createDrawingPatriarch();
    XSSFPicture picture = patriarch.createPicture(anchor, index);
    picture.resize(0.9);
  }

  private void setHSSFPicture(int rowIdx, int colIdx, File file) throws IOException {
    HSSFSheet hssfSheet = (HSSFSheet) sheet;
    HSSFClientAnchor anchor =
        new HSSFClientAnchor(
            (int) (1023 * 0.1),
            (int) (255 * 0.1),
            0,
            0,
            (short) colIdx,
            rowIdx,
            (short) colIdx,
            rowIdx);
    int format =
        file.getName().toLowerCase().endsWith(".png")
            ? HSSFWorkbook.PICTURE_TYPE_PNG
            : HSSFWorkbook.PICTURE_TYPE_JPEG;
    int index = hssfSheet.getWorkbook().addPicture(getPictureBytes(file), format);
    HSSFPatriarch patriarch = hssfSheet.createDrawingPatriarch();
    HSSFPicture picture = patriarch.createPicture(anchor, index);
    picture.resize(0.9);
  }

  private byte[] getPictureBytes(File file) throws IOException {
    InputStream in = new FileInputStream(file);
    byte[] bytes = new byte[in.available()];
    in.read(bytes);
    in.close();
    return bytes;
  }

  public void setDataFormat(int rowIdx, int colIdx, String format) {
    Cell cell = getCell(rowIdx, colIdx);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.cloneStyleFrom(cell.getCellStyle());
    cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
    cell.setCellStyle(cellStyle);
  }

  public void copyRow(int startRow, int n) {
    sheet.shiftRows(startRow, sheet.getLastRowNum(), n, true, false);
    int oldRowIdx = startRow + n;
    List<CellRangeAddress> mergedRegions = new ArrayList<>();
    for (CellRangeAddress cellRangeAddress : sheet.getMergedRegions()) {
      if (cellRangeAddress.containsRow(oldRowIdx)) {
        mergedRegions.add(cellRangeAddress);
      }
    }
    Row oldRow = sheet.getRow(oldRowIdx);
    for (int i = 0; i < n; i++) {
      int newRowIdx = startRow + i;
      Row newRow = sheet.createRow(newRowIdx);
      newRow.setHeight(oldRow.getHeight());
      for (int j = 0; j <= oldRow.getLastCellNum(); j++) {
        Cell oldCell = oldRow.getCell(j);
        if (oldCell != null) {
          Cell newCell = newRow.createCell(j);
          copyCell(oldCell, newCell);
        }
      }
      for (CellRangeAddress cellRangeAddress : mergedRegions) {
        cellRangeAddress.setFirstRow(newRowIdx);
        cellRangeAddress.setLastRow(newRowIdx);
        sheet.addMergedRegion(cellRangeAddress);
      }
    }
  }

  private void copyCell(Cell source, Cell target) {
    CellType cellType = source.getCellTypeEnum();
    if (cellType == CellType.STRING) {
      target.setCellValue(source.getStringCellValue());
    } else if (cellType == CellType.NUMERIC) {
      target.setCellValue(source.getNumericCellValue());
    } else if (cellType == CellType.FORMULA) {
      target.setCellFormula(source.getCellFormula());
    } else if (cellType == CellType.BOOLEAN) {
      target.setCellValue(source.getBooleanCellValue());
    } else if (cellType == CellType.ERROR) {
      target.setCellValue(source.getErrorCellValue());
    }
    target.setCellType(cellType);
    target.setCellComment(source.getCellComment());
    target.setCellStyle(source.getCellStyle());
    target.setHyperlink(source.getHyperlink());
  }

  public Object getCellValue(String address) {
    Cell cell = getCell(address);
    if (cell == null) {
      return null;
    }
    return getCellValue(cell);
  }

  /*public Object getCellValue(int rowIdx, int colIdx) {
  	Cell cell = SheetUtil.getCell(sheet, rowIdx, colIdx);
  	if (cell == null) {
  		return null;
  	}
  	return getCellValue(cell);
  }*/

  private Object getCellValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    CellType cellType = cell.getCellTypeEnum();
    if (cellType == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (cellType == CellType.NUMERIC) {
      return getNumericCellValue(cell);
    } else if (cellType == CellType.FORMULA) {
      return getFormulaCellValue(cell);
    } else if (cellType == CellType.BOOLEAN) {
      return cell.getBooleanCellValue();
    } else if (cellType == CellType.ERROR) {
      return cell.getErrorCellValue();
    }
    return null;
  }

  private Object getNumericCellValue(Cell cell) {
    Double value = cell.getNumericCellValue();
    if (DateUtil.isCellDateFormatted(cell)) {
      return DateUtil.getJavaDate(value);
    } else if (value % 1 == 0) {
      return new DecimalFormat("0").format(value);
    }
    return value;
  }

  private Object getFormulaCellValue(Cell cell) {
    CellType cellType = cell.getCachedFormulaResultTypeEnum();
    if (cellType == CellType.NUMERIC) {
      return cell.getNumericCellValue();
    } else if (cellType == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (cellType == CellType.BOOLEAN) {
      return cell.getBooleanCellValue();
    } else if (cellType == CellType.ERROR) {
      return cell.getErrorCellValue();
    } else {
      return null;
    }
  }

  public String getCellValueString(String address) {
    CellAddress cellAddress = new CellAddress(address);
    int rowIdx = cellAddress.getRow();
    int colIdx = cellAddress.getColumn();
    return getCellValueString(rowIdx, colIdx);
  }

  public String getCellValueString(int rowIdx, int colIdx) {
    Cell cell = getCell(rowIdx, colIdx);
    return getCellValueString(cell);
  }

  private String getCellValueString(Cell cell) {
    Object object = getCellValue(cell);
    if (object == null) {
      return null;
    } else if (object instanceof String) {
      return object.toString().trim();
    } else if (object instanceof Double) {
      return String.valueOf(object);
    } else if (object instanceof Date) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return df.format(object);
    }
    return null;
  }

  public void mergeCells(int firstRow, int lastRow, int firstCol, int lastCol) {
    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
  }

  public void writeObjects(int headerRow, List<?> objects) {
    Class<?> objectType = objects.get(0).getClass();
    //		writeHeader(headerRow, objectType);
    try {
      List<Method> getters = getGetters(objectType);
      int startRow = headerRow + 1;
      for (int i = 0; i < objects.size(); i++) {
        Object object = objects.get(i);
        int rowIdx = startRow + i;
        for (int j = 0; j < getters.size(); j++) {
          Method getter = getters.get(j);
          Object value = getter.invoke(object);
          setCellValue(rowIdx, j, value);
        }
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Reflective operation error");
    }
  }

  private void writeHeader(int headerRow, Class<?> objectType) {
    Field[] fields = objectType.getDeclaredFields();
    int colIdx = 0;
    for (Field field : fields) {
      if (field.isAnnotationPresent(Header.class)) {
        Header header = field.getAnnotation(Header.class);
        String headerValue = header.value();
        setCellValue(headerRow, colIdx++, headerValue);
      }
    }
  }

  private List<Method> getGetters(Class<?> objectType)
      throws NoSuchFieldException, SecurityException, NoSuchMethodException {
    List<Method> getters = new ArrayList<>();
    Field[] fields = objectType.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Header.class)) {
        String fieldName = field.getName();
        String methodName =
            "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method getter = objectType.getDeclaredMethod(methodName);
        getters.add(getter);
      }
    }
    return getters;
  }

  public <T> List<T> readObjects(int headerRow, Class<T> objectType) {
    List<T> objects = new ArrayList<>();
    try {
      List<Method> setters = getSetters(headerRow, objectType);
      int unique = getUnique(headerRow, objectType);
      int startRow = headerRow + 1;
      //      Set<Object> set = new HashSet<>();
      for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (isEmptyRow(row)) {
          continue;
        }
        //        if (unique >= 0) {
        //          Cell cell = row.getCell(unique);
        //          if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
        //            continue;
        //          } else {
        //            Object cellValue = getCellValue(cell);
        //            if (set.contains(cellValue)) {
        //              continue;
        //            } else {
        //              set.add(cellValue);
        //            }
        //          }
        //        }
        T object = (T) objectType.newInstance();
        setObjectValues(setters, row, object);
        objects.add(object);
      }
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Reflective operation error");
    }
    return objects;
  }

  private <T> void setObjectValues(List<Method> setters, Row row, T object)
      throws ReflectiveOperationException {
    for (int j = 0; j < setters.size(); j++) {
      Method setter = setters.get(j);
      if (setter == null) {
        continue;
      }
      Class<?> paramType = setter.getParameterTypes()[0];
      Cell cell = row.getCell(j);
      Object cellValue = getCellValue(cell);
      if (cellValue != null && cellValue.getClass() != paramType) {
        try {
          cellValue = convertType(paramType, cellValue);
        } catch (Exception e) {
          String cellAddress = new CellAddress(cell).formatAsString();
          String msg =
              "Type error at cell(" + cellAddress + "), [" + paramType.getName() + "] was Required";
          throw new IllegalArgumentException(msg);
        }
      }
      setter.invoke(object, cellValue);
    }
  }

  private List<Method> getSetters(int headerRow, Class<?> objectType)
      throws NoSuchFieldException, SecurityException, NoSuchMethodException {
    List<Method> setters = new ArrayList<>();
    List<String> fieldNames = getFieldNames(headerRow, objectType);
    for (String fieldName : fieldNames) {
      if (fieldName == null) {
        setters.add(null);
      } else {
        String methodName =
            "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class<?> paramType = objectType.getDeclaredField(fieldName).getType();
        Method setter = objectType.getDeclaredMethod(methodName, paramType);
        setters.add(setter);
      }
    }
    return setters;
  }

  private int getUnique(int headerRow, Class<?> objectType)
      throws NoSuchFieldException, SecurityException, NoSuchMethodException {
    Field[] fields = objectType.getDeclaredFields();
    List<String> uniqueHeaderName = new ArrayList<>();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Header.class)) {
        Header header = field.getAnnotation(Header.class);
        boolean unique = header.unique();
        if (unique) {
          uniqueHeaderName.add(header.value());
          break;
        }
      }
    }

    Row row = sheet.getRow(headerRow);
    for (int i = 0; i <= row.getLastCellNum(); i++) {
      String cellValue = getCellValueString(row.getCell(i));
      if (uniqueHeaderName == null) {
        return -1;
      }
      return i;
    }
    return -1;
  }

  private <T> List<String> getFieldNames(int headerRow, Class<T> objectType) {
    Map<String, String> map = new HashMap<>();
    Field[] fields = objectType.getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      if (field.isAnnotationPresent(Header.class)) {
        Header header = field.getAnnotation(Header.class);
        String headerValue = header.value();
        map.put(headerValue, fieldName);
      }
    }
    Row row = sheet.getRow(headerRow);
    List<String> fieldNames = new ArrayList<>();
    for (Cell cell : row) {
      String cellValue = getCellValueString(cell);
      fieldNames.add(map.get(cellValue));
    }
    return fieldNames;
  }

  private boolean isEmptyRow(Row row) {
    if (row == null) {
      return true;
    }
    for (Cell cell : row) {
      if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
        return false;
      }
    }
    return true;
  }

  private Object convertType(Class<?> paramType, Object cellValue) throws ParseException {
    String valueString = String.valueOf(cellValue);
    if (paramType == String.class) {
      return valueString;
    } else if (paramType == Double.class) {
      return Double.valueOf(valueString);
    } else if (paramType == Integer.class) {
      return Integer.valueOf(valueString);
    } else if (paramType == Date.class) {
      return DateUtil.getJavaDate(Double.valueOf(valueString));
    }
    return cellValue;
  }

  public int append(InputStream in, int sheetIdx, int startRow) throws IOException {
    int newRowIdx = sheet.getLastRowNum() + 1;
    Workbook newWorkbook = getWorkbook(in);
    Sheet newSheet = newWorkbook.getSheetAt(sheetIdx);
    int count = 0;
    for (int i = startRow; i <= newSheet.getLastRowNum(); i++) {
      Row newRow = newSheet.getRow(i);
      if (isEmptyRow(newRow)) {
        continue;
      }
      for (int j = 0; j <= newRow.getLastCellNum(); j++) {
        Cell newCell = newRow.getCell(j);
        Cell oldCell = getCell(newRowIdx + count, j);
        if (newCell != null) {
          setCellValue(oldCell, getCellValue(newCell));
        }
      }
      count++;
    }
    newWorkbook.close();
    return count;
  }

  private Workbook getWorkbook(InputStream in) throws IOException {
    BufferedInputStream bis = new BufferedInputStream(in);
    byte[] bytes = new byte[4];
    bis.mark(0);
    bis.read(bytes);
    String magicNumber = Hex.encodeHexString(bytes);
    bis.reset();
    Workbook workbook = null;
    if (magicNumber.equals("504b0304")) {
      workbook = new XSSFWorkbook(bis);
    } else if (magicNumber.equals("d0cf11e0")) {
      workbook = new HSSFWorkbook(bis);
    }
    return workbook;
  }

  public void write(OutputStream out) throws IOException {
    workbook.write(out);
    out.close();
  }

  @Override
  public void close() throws IOException {
    workbook.close();
  }
}
