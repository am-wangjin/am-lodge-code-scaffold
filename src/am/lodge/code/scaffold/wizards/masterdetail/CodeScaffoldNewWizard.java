package am.lodge.code.scaffold.wizards.masterdetail;

import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;

import am.lodge.code.scaffold.wizards.BaseNewWizard;
import am.lodge.code.scaffold.wizards.single.ColumnWizardPage;
import am.lodge.code.scaffold.wizards.single.EndWizardPage;
import am.lodge.code.scaffold.wizards.single.TableWizardPage;

public class CodeScaffoldNewWizard extends BaseNewWizard{

  private TableWizardPage one;

  private ColumnWizardPage two;

  private TableWizardPage three;

  private ColumnWizardPage four;

  private EndWizardPage end;

  protected void initWizardPages(){
    one = new TableWizardPage("select Master Table");
    addPage(one);
    two = new ColumnWizardPage("select Master Table Column");
    addPage(two);
    three = new TableWizardPage("select Detail Table");
    addPage(three);
    four = new ColumnWizardPage("select Detail Table Column");
    addPage(four);
    end = new EndWizardPage("Set End");
    addPage(end);
  }

  @Override
  public boolean performFinish(){
    boolean result = one.isPageComplete() && two.isPageComplete() && three.isPageComplete() && four.isPageComplete() && end.isPageComplete();
    if(!result)
      return false;
    final Map<String, Object> data = end.getData();
    data.put("master", two.getData());
    data.put("detail", four.getData());
    try{
      doFinish(data);
    }catch (Exception e){
      MessageDialog.openError(getShell(), "Error", e.getMessage());
      throw new RuntimeException(e);
    }
    return true;
  }
}