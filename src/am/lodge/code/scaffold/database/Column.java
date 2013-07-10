package am.lodge.code.scaffold.database;

import org.apache.commons.collections.MapUtils;

import am.lodge.code.scaffold.util.CamelCaseUtils;
import am.lodge.code.scaffold.util.Constants;

public class Column extends BaseModel{

  private String sqlType;

  private Integer columnSize;

  private Integer decimalDigits;

  private Boolean nullable;

  public String getSqlType(){
    return sqlType;
  }

  public void setSqlType(String sqlType){
    this.sqlType = sqlType;
  }

  public String getJavaType(){
    return MapUtils.getString(Constants.TYPE_MAP, sqlType, "String");
  }

  public String getLowerCamelName(){
    return CamelCaseUtils.toLowerCamelCase(getName());
  }

  public String getUpperCamelName(){
    return CamelCaseUtils.toUpperCamelCase(getName());
  }

  public Integer getDecimalDigits(){
    return decimalDigits;
  }

  public void setDecimalDigits(Integer decimalDigits){
    this.decimalDigits = decimalDigits;
  }

  public Boolean getNullable(){
    return nullable;
  }

  public void setNullable(Boolean nullable){
    this.nullable = nullable;
  }

  public Integer getColumnSize(){
    return columnSize;
  }

  public void setColumnSize(Integer columnSize){
    this.columnSize = columnSize;
  }
}