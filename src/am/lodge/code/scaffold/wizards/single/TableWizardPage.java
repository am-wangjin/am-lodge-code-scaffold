package am.lodge.code.scaffold.wizards.single;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import am.lodge.code.scaffold.wizards.BaseNewWizard;

public class TableWizardPage extends WizardPage{

  private Button removePrefix;

  private Tree tableTree;

  private boolean enableNext;

  private String selectSchem;

  private String selectTable;

  public TableWizardPage(String title){
    super(title);
    setTitle(title);
    setPageComplete(false);
    enableNext = false;
  }

  @Override
  public void createControl(Composite parent){
    Composite body = new Composite(parent, SWT.NONE);
    body.setLayout(new FormLayout());
    FormData formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(0, 0);
    Label label = new Label(body, SWT.RIGHT);
    label.setText("remove prefix:");
    label.setLayoutData(formData);

    removePrefix = new Button(body, SWT.CHECK);
    removePrefix.setSelection(true);
    formData = new FormData();
    formData.left = new FormAttachment(label, 5);
    formData.top = new FormAttachment(0, 0);
    removePrefix.setLayoutData(formData);

    tableTree = new Tree(body, SWT.SINGLE | SWT.BORDER);
    formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(label, 5);
    formData.right = new FormAttachment(100, -1);
    formData.bottom = new FormAttachment(100, -1);
    tableTree.setLayoutData(formData);
    tableTree.addSelectionListener(new SelectionAdapter(){
      public void widgetSelected(SelectionEvent e){
        TreeItem item = (TreeItem)e.item;
        Object v = item.getData("isTable");
        boolean chahe = enableNext;
        enableNext = v != null;
        if(enableNext){
          selectTable = item.getText();
          selectSchem = (String) item.getData("schem");
        }
        if(chahe != enableNext){
          canFlipToNextPage();
          getWizard().getContainer().updateButtons();
        }
      }
    });
    initTableTree();
    setControl(body);
  }

  private void initTableTree(){
    DatabaseMetaData dmd = ((BaseNewWizard)getWizard()).getDatabaseMetaData();
    try{
      String databaseName = dmd.getDatabaseProductName();
      TreeItem root = new TreeItem(tableTree, SWT.NONE);
      root.setText(databaseName);
      ResultSet rs = dmd.getSchemas();
      while (rs.next()){
        TreeItem item = new TreeItem(root, SWT.NONE);
        String schem = rs.getString("TABLE_SCHEM");
        item.setText(schem);
        ResultSet tables = dmd.getTables(null, schem, "%", new String[] { "TABLE" });
        while (tables.next()){
          String tableName = tables.getString("TABLE_NAME");
          TreeItem sub = new TreeItem(item, SWT.NONE);
          sub.setText(tableName);
          sub.setData("schem", schem);
          sub.setData("isTable", true);
        }
      }
    }catch (SQLException e){
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean canFlipToNextPage(){
    return enableNext;
  }

  @Override
  public boolean isPageComplete(){
    IWizardPage[] pages = getWizard().getPages();
    if(enableNext && pages[1].isPageComplete()){
      return true;
    }
    return false;
  }

  public IWizardPage getNextPage(){
    ColumnWizardPage wizard = (ColumnWizardPage)super.getNextPage();
    wizard.initColumnTree();
    wizard.setRemovePrefix(removePrefix.getSelection());
    return wizard;
  }

  public String getSelectTable(){
    return selectTable;
  }

  public String getSelectSchem(){
    return selectSchem;
  }
}