package am.lodge.code.scaffold.util;

import java.util.HashMap;
import java.util.Map;

public final class Constants{

  public final static String SOURCE = "source";

  public final static String PAGE = "page";

  public final static String TABLE = "table";

  public final static String PACKAGE = "package";

  public final static String PAGE_PATH = "pagePath";

  public final static String SQL_TYPE = "sqlType";

  public final static String COLUMN_SIZE = "columnSize";

  public final static String DECIMAL_DIGITS = "decimalDigits";

  public final static String NULLABLE = "nullable";

  public final static String TYPE = "type";

  public final static String COLUMNS = "columns";

  public final static String NAME = "name";

  public final static String ATTRNAME = "attrName";

  public final static String IMPORTS = "imports";

  public static Map<String, String> TYPE_MAP = new HashMap<String, String>();

  public static Map<String, String> IMPORT_MAP = new HashMap<String, String>();

  static{
    TYPE_MAP.put("VARCHAR", "String");
    TYPE_MAP.put("CHAR", "String");
    TYPE_MAP.put("DECIMAL", "Integer");
    TYPE_MAP.put("TIMESTAMP", "Date");

    IMPORT_MAP.put("TIMESTAMP", "import java.util.Date;");
  }

  private Constants(){
  }
}