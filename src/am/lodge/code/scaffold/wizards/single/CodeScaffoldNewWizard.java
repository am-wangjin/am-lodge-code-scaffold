package am.lodge.code.scaffold.wizards.single;

import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;

import am.lodge.code.scaffold.wizards.BaseNewWizard;

public class CodeScaffoldNewWizard extends BaseNewWizard{

  private TableWizardPage one;

  private ColumnWizardPage two;

  private ColumnDescWizardPage three;

  private EndWizardPage end;

  protected void initWizardPages(){
    one = new TableWizardPage("Select Table");
    addPage(one);
    two = new ColumnWizardPage("Select Column");
    addPage(two);
    three = new ColumnDescWizardPage("Set Column Desc");
    addPage(three);
    end = new EndWizardPage("Set End");
    addPage(end);
  }

  @Override
  public boolean performFinish(){
    boolean result = one.isPageComplete() && two.isPageComplete() && three.isPageComplete() && end.isPageComplete();
    if(!result)
      return false;
    final Map<String, Object> data = three.getData();
    data.putAll(end.getData());
    try{
      doFinish(data);
    }catch (Exception e){
      MessageDialog.openError(getShell(), "Error", e.getMessage());
      throw new RuntimeException(e);
    }
    return true;
  }
}