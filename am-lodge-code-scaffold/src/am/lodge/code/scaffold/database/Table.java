package am.lodge.code.scaffold.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import am.lodge.code.scaffold.util.CamelCaseUtils;

public class Table extends BaseModel{

  private List<Column> primaryKeys = new ArrayList<Column>();

  private List<Column> columns = new ArrayList<Column>();

  private List<Column> selectColumns = new ArrayList<Column>();

  private boolean removePrefix;

  public Table(boolean removePrefix){
    this.removePrefix = removePrefix;
  }

  public List<Column> getPrimaryKeys(){
    return primaryKeys;
  }

  public void setPrimaryKeys(List<Column> primaryKeys){
    this.primaryKeys = primaryKeys;
  }

  public List<Column> getColumns(){
    return columns;
  }

  public void setColumns(List<Column> columns){
    this.columns = columns;
  }

  public String getLowerCamelName(){
    String name = getName();
    if(removePrefix){
      name = StringUtils.substringAfter(name, "_");
    }
    return CamelCaseUtils.toLowerCamelCase(name);
  }

  public String getUpperCamelName(){
    String name = getName();
    if(removePrefix){
      name = StringUtils.substringAfter(name, "_");
    }
    return CamelCaseUtils.toUpperCamelCase(name);
  }

  public List<Column> getSelectColumns(){
    return selectColumns;
  }

  public void setSelectColumns(List<Column> selectColumns){
    this.selectColumns = selectColumns;
  }
}