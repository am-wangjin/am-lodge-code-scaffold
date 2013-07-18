package am.lodge.code.scaffold.database;

import java.util.ArrayList;
import java.util.List;

import am.lodge.code.scaffold.util.CamelCaseUtils;

public class Table extends BaseModel{

  private List<Column> primaryKeys = new ArrayList<Column>();

  private List<Column> columns = new ArrayList<Column>();

  private List<Column> selectColumns = new ArrayList<Column>();

  private String aliasName;

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

  public List<Column> getAllColumns(){
    List<Column> result = new ArrayList<Column>();
    result.addAll(getPrimaryKeys());
    result.addAll(getColumns());
    return result;
  }

  public String getLowerCamelName(){
    return CamelCaseUtils.toLowerCamelCase(getAliasName());
  }

  public String getUpperCamelName(){
    return CamelCaseUtils.toUpperCamelCase(getAliasName());
  }

  public List<Column> getSelectColumns(){
    return selectColumns;
  }

  public void setSelectColumns(List<Column> selectColumns){
    this.selectColumns = selectColumns;
  }

  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
  }
}