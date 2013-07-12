package am.lodge.code.scaffold.wizards.single;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import am.lodge.code.scaffold.wizards.BaseNewWizard;

public class TableWizardPage extends WizardPage{

  private Text aliasName;

  private FilteredTree tableTree;

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
    label.setText("alias name:");
    label.setLayoutData(formData);

    aliasName = new Text(body, SWT.SINGLE | SWT.BORDER);
    formData = new FormData();
    formData.left = new FormAttachment(label, 5);
    formData.right = new FormAttachment(100, -1);
    formData.top = new FormAttachment(0, 0);
    aliasName.setLayoutData(formData);

    tableTree = new FilteredTree(body, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE, new PatternFilter(), true);
    formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(aliasName, 5);
    formData.right = new FormAttachment(100, -1);
    formData.bottom = new FormAttachment(100, -1);
    tableTree.setLayoutData(formData);
    TreeViewer treeViewer = tableTree.getViewer();
    treeViewer.setContentProvider(new TreeNodeContentProvider());
    treeViewer.setLabelProvider(new TreeViewerLabelProvider());
    treeViewer.setInput(new TreeNode[]{getTreeNode()});
    treeViewer.getTree().addSelectionListener(new SelectionAdapter(){
      public void widgetSelected(SelectionEvent e){
        TreeItem item = (TreeItem)e.item;
        TreeNode treeNode = (TreeNode)item.getData();
        boolean chahe = enableNext;
        enableNext = treeNode.getValue() instanceof Map;
        if(enableNext){
          @SuppressWarnings("unchecked")
          Map<String, String> date = (Map<String, String>) treeNode.getValue();
          selectTable = date.get("tableName");
          selectSchem = date.get("schem");
          aliasName.setText(selectTable);
        }
        if(chahe != enableNext){
          canFlipToNextPage();
          getWizard().getContainer().updateButtons();
        }
      }
    });
    setControl(body);
  }


  class TreeViewerLabelProvider extends LabelProvider{
    @SuppressWarnings("unchecked")
    public String getText(Object obj) {
      TreeNode node = (TreeNode)obj;
      Object value = node.getValue();
      if (value instanceof String) {
        return (String)value;  
      }else{
        return ((Map<String, String>)value).get("tableName");
      }
    } 
  }

  private TreeNode getTreeNode(){
    DatabaseMetaData dmd = ((BaseNewWizard)getWizard()).getDatabaseMetaData();
    TreeNode root = new TreeNode(null);
    try{
      String databaseName = dmd.getDatabaseProductName();
      root = new TreeNode(databaseName);
      ResultSet rs = dmd.getSchemas();
      List<TreeNode> schemList = new ArrayList<TreeNode>();
      while (rs.next()){
        String schem = rs.getString("TABLE_SCHEM");
        TreeNode schemNode = new TreeNode(schem);
        ResultSet tables = dmd.getTables(null, schem, "%", new String[] { "TABLE" });
        schemNode.setParent(root);
        schemList.add(schemNode);
        List<TreeNode> tableList = new ArrayList<TreeNode>();
        while (tables.next()){
          String tableName = tables.getString("TABLE_NAME");
          Map<String, String> data = new HashMap<String, String>();
          data.put("tableName", tableName);
          data.put("schem", schem);
          TreeNode tableNode = new TreeNode(data);
          tableNode.setParent(schemNode);
          tableList.add(tableNode);
        }
        schemNode.setChildren((TreeNode[])tableList.toArray(new TreeNode[0]));
      }
      root.setChildren((TreeNode[])schemList.toArray(new TreeNode[0]));
    }catch (SQLException e){
      throw new RuntimeException(e);
    }
    return root;  
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
    wizard.setAliasName(aliasName.getText());
    return wizard;
  }

  public String getSelectTable(){
    return selectTable;
  }

  public String getSelectSchem(){
    return selectSchem;
  }
}