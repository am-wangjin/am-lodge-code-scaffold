package am.lodge.code.scaffold.wizards.masterdetail;

import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;

import am.lodge.code.scaffold.wizards.BaseNewWizard;
import am.lodge.code.scaffold.wizards.single.ColumnDescWizardPage;
import am.lodge.code.scaffold.wizards.single.ColumnWizardPage;
import am.lodge.code.scaffold.wizards.single.EndWizardPage;
import am.lodge.code.scaffold.wizards.single.TableWizardPage;

public class CodeScaffoldNewWizard extends BaseNewWizard{

  private TableWizardPage masterOne;

  private ColumnWizardPage masterTwo;

  private ColumnDescWizardPage masterThree;

  private TableWizardPage detailOne;

  private ColumnWizardPage detailTwe;

  private ColumnDescWizardPage detailThree;

  private EndWizardPage end;

  protected void initWizardPages(){
    masterOne = new TableWizardPage("select Master Table");
    addPage(masterOne);
    masterTwo = new ColumnWizardPage("select Master Table Column");
    addPage(masterTwo);
    masterThree = new ColumnDescWizardPage("Set Master Table Column Desc");
    addPage(masterThree);

    detailOne = new TableWizardPage("select Detail Table");
    addPage(detailOne);
    detailTwe = new ColumnWizardPage("select Detail Table Column");
    addPage(detailTwe);
    detailThree = new ColumnDescWizardPage("Set Detail Table Column Desc");
    addPage(detailThree);

    end = new EndWizardPage("Set End");
    addPage(end);
  }

  @Override
  public boolean performFinish(){
    boolean result = masterOne.isPageComplete() && masterTwo.isPageComplete() && detailOne.isPageComplete() && detailTwe.isPageComplete() && end.isPageComplete();
    if(!result)
      return false;
    final Map<String, Object> data = end.getData();
    data.put("master", masterThree.getData());
    data.put("detail", detailThree.getData());
    try{
      doFinish(data);
    }catch (Exception e){
      MessageDialog.openError(getShell(), "Error", e.getMessage());
      throw new RuntimeException(e);
    }
    return true;
  }
}