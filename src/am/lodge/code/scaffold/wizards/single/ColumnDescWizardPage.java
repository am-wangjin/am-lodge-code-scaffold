package am.lodge.code.scaffold.wizards.single;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;

import am.lodge.code.scaffold.database.Column;
import am.lodge.code.scaffold.database.Table;
import am.lodge.code.scaffold.util.Constants;

public class ColumnDescWizardPage extends WizardPage {

  private TableViewer tableView;

  private Map<String, Object> data;

  public ColumnDescWizardPage(String title) {
    super(title);
    setTitle(title);
    setPageComplete(true);
  }

  @Override
  public void createControl(Composite parent) {
    Composite body = new Composite(parent, SWT.NONE);
    final Clipboard clipboard = new Clipboard(parent.getShell().getDisplay());
    body.setLayout(new FormLayout());
    FormData formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(0, 0);
    Button copy = new Button(body, SWT.PUSH);
    copy.setText("copy for caribean");
    copy.setLayoutData(formData);
    
    tableView = new TableViewer(body, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
    formData = new FormData();
    formData.left = new FormAttachment(0, 0);
    formData.top = new FormAttachment(copy, 5);
    formData.right = new FormAttachment(100, -1);
    formData.bottom = new FormAttachment(100, -1);
    tableView.getTable().setLayoutData(formData);
    tableView.getTable().setHeaderVisible(true);
    tableView.getTable().setLinesVisible(true);
    TableLayout layout = new TableLayout();
    tableView.getTable().setLayout(layout);

    layout.addColumnData(new ColumnWeightData(10));
    new TableColumn(tableView.getTable(), SWT.NONE).setText("字段名");
    layout.addColumnData(new ColumnWeightData(10));

    TableViewerColumn column = new TableViewerColumn(tableView, SWT.LEFT, 1);
    column.getColumn().setText("描述");
    column.setEditingSupport(new ColumnEditingSupport(tableView));

    tableView.setLabelProvider(new TableLableProvider());
    tableView.setContentProvider(new ArrayContentProvider());
    setControl(body);

    copy.addListener(SWT.Selection, new Listener () {
      public void handleEvent (Event e) {
        if(data != null){
          TextTransfer transfer = TextTransfer.getInstance();
          String str = (String)clipboard.getContents(transfer);
          String[] values = StringUtils.split(str, System.getProperty("line.separator"));
          Table tableData = (Table) data.get(Constants.TABLE);
          List<Column> columns = tableData.getAllColumns();
          for(int i = 0; i < values.length; i++){
            Column column = columns.get(i);
            column.setDesc(StringUtils.trim(values[i]));
            tableView.refresh();
          }
        }
      }
    });
  }

  public void updateColumnData(Map<String, Object> data) {
    this.data = data;
    Table tableData = (Table) this.data.get(Constants.TABLE);
    tableView.setInput(tableData.getAllColumns());
  }

  static class TableLableProvider extends LabelProvider implements
      ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
      return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
      Column column = (Column) element;
      switch (columnIndex) {
      case 0:
        return column.getName();
      case 1:
        return column.getDesc();
      }
      return null;
    }
  }

  static class ColumnEditingSupport extends EditingSupport {

    private CellEditor editor;

    private TableViewer viewer;

    public ColumnEditingSupport(TableViewer viewer) {
      super(viewer);
      this.viewer = viewer;
      editor = new TextCellEditor(viewer.getTable());
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
      return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
      return true;
    }

    @Override
    protected Object getValue(Object element) {
      return ((Column) element).getDesc();
    }

    @Override
    protected void setValue(Object element, Object value) {
      ((Column) element).setDesc(value.toString());
      viewer.refresh();
    }
  }

  public Map<String, Object> getData() {
    return data;
  }
}