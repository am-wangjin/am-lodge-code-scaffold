package am.lodge.code.scaffold.preferences;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class JarsListEditor extends ListEditor{

  public JarsListEditor(String name, String labelText, Composite parent){
    super(name, labelText, parent);
  }

  public String getJars(){
    return createList(getList().getItems());
  }

  @Override
  protected String createList(String[] items){
    String jarStr = "";
    for(String v: items){
      jarStr = jarStr + v + ";";
    }
    if(jarStr.length() > 0)
      jarStr = jarStr.substring(0, jarStr.length() - 1);
    return jarStr;
  }

  @Override
  protected String getNewInputObject(){
    FileDialog fileDialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
    fileDialog.open();
    String path = fileDialog.getFilterPath();
    if(StringUtils.isNotBlank(fileDialog.getFileName())){
      return path + System.getProperties().getProperty("file.separator") + fileDialog.getFileName();
    }
    return null;
  }

  @Override
  protected String[] parseString(String stringList){
    return stringList.split(";");
  }
}