package am.lodge.code.scaffold.wizards.single;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import am.lodge.code.scaffold.database.Column;
import am.lodge.code.scaffold.database.Table;
import am.lodge.code.scaffold.util.Constants;
import am.lodge.code.scaffold.util.TreeUtils;

public class ColumnWizardPage extends WizardPage {

  private Text packages;

  private Text pagePath;

  private Tree columnTree;

  private String table;

  private boolean removePrefix;

  private String schem;

  public ColumnWizardPage(String title){
    super(title);
    setTitle(title);
    setPageComplete(false);
  }

  @Override
  public void createControl(Composite parent){
    Composite body = new Composite(parent, SWT.NONE);
    body.setLayout(new FormLayout());
    Label label = new Label(body, SWT.RIGHT);
    label.setText("package:");
    FormData formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(0, 0);
    label.setLayoutData(formData);

    packages = new Text(body, SWT.SINGLE | SWT.BORDER);
    formData = new FormData();
    formData.left = new FormAttachment(0, 80);
    formData.top = new FormAttachment(0, 0);
    formData.right = new FormAttachment(100, -1);
    packages.setLayoutData(formData);

    label = new Label(body, SWT.RIGHT);
    label.setText("page path:");
    formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(packages, 0);
    label.setLayoutData(formData);

    pagePath = new Text(body, SWT.SINGLE | SWT.BORDER);
    formData = new FormData();
    formData.left = new FormAttachment(0, 80);
    formData.top = new FormAttachment(packages, 1);
    formData.right = new FormAttachment(100, -1);
    pagePath.setLayoutData(formData);

    columnTree = new Tree(body, SWT.CHECK | SWT.BORDER);
    formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(pagePath, 1);
    formData.right = new FormAttachment(100, -1);
    formData.bottom = new FormAttachment(100, -1);
    columnTree.setLayoutData(formData);
    columnTree.addSelectionListener(new SelectionAdapter(){
      public void widgetSelected(SelectionEvent e){
        if(e.detail == SWT.CHECK){
          TreeItem item = (TreeItem)e.item;
          boolean oldState = isPageComplete();
          boolean checked = item.getChecked();
          TreeUtils.checkItems(item, checked);
          TreeUtils.checkPath(item.getParentItem(), checked, false);
          boolean state = getSelectColumns().size() > 0;
          setPageComplete(state);
          if(oldState != state)
            getWizard().getContainer().updateButtons();
        }
      }
    });
    setControl(body);
  }

  public void setRemovePrefix(boolean removePrefix){
    this.removePrefix = removePrefix;
  }

  public void initColumnTree(){
    DatabaseMetaData dmd = ((CodeScaffoldNewWizard)getWizard()).getDatabaseMetaData();
    try{
      TableWizardPage wizard = (TableWizardPage)getPreviousPage();
      String _schem = wizard.getSelectSchem();
      String _table = wizard.getSelectTable();
      if(_schem.equals(schem) && _table.equals(table)){
        return;
      }
      schem = _schem;
      table = _table;
      columnTree.removeAll();
      TreeItem root = new TreeItem(columnTree, SWT.NONE);
      root.setText(table);
      Set<String> pks = new HashSet<String>();
      ResultSet rs = dmd.getPrimaryKeys(null, schem, table);
      while(rs.next()){
        pks.add(rs.getString("COLUMN_NAME"));
      }

      rs = dmd.getColumns(null, schem, table, "%");
      while(rs.next()){
        TreeItem item = new TreeItem(root, SWT.NONE);
        String name = rs.getString("COLUMN_NAME");
        item.setText(name);
        item.setData(Constants.SQL_TYPE, rs.getString("TYPE_NAME"));
        item.setData(Constants.COLUMN_SIZE, rs.getInt("COLUMN_SIZE"));
        item.setData(Constants.DECIMAL_DIGITS, rs.getInt("DECIMAL_DIGITS"));
        item.setData(Constants.NULLABLE, rs.getInt("NULLABLE") == 1);
        if(pks.contains(name))
        item.setData("pk", true);
      }
    }catch (SQLException e){
      throw new RuntimeException(e);
    }
  }

  private List<TreeItem> getSelectColumns(){
    List<TreeItem> itemList = new ArrayList<TreeItem>();
    TreeItem[] items = columnTree.getItems()[0].getItems();
    for(TreeItem item: items){
      if(item.getChecked()){
        itemList.add(item);
      }
    }
    return itemList;
  }

  public Map<String, Object> getData(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put(Constants.PACKAGE, packages.getText());
    result.put(Constants.PAGE_PATH, pagePath.getText());
    Set<String> imports = new HashSet<String>();
    result.put(Constants.IMPORTS, imports);
    Table tableData = new Table(removePrefix);
    result.put(Constants.TABLE, tableData);
    tableData.setName(table);

    TreeItem[] items = columnTree.getItems()[0].getItems();
    for(TreeItem item: items){
      Column column = new Column();
      column.setName(item.getText());
      column.setSqlType((String)item.getData(Constants.SQL_TYPE));
      column.setColumnSize((Integer)item.getData("COLUMN_SIZE"));
      column.setDecimalDigits((Integer)item.getData("DECIMAL_DIGITS"));
      column.setNullable((Boolean)item.getData("DECIMAL_DIGITS"));
      
      if(item.getData("pk") != null){
        tableData.getPrimaryKeys().add(column);
      }else{
        tableData.getColumns().add(column);
      }
      if(item.getChecked()){
        if(Constants.IMPORT_MAP.get(column.getSqlType()) != null){
          imports.add(Constants.IMPORT_MAP.get(column.getSqlType()));
        }
        tableData.getSelectColumns().add(column);
      }
    }
    return result;
  }
}