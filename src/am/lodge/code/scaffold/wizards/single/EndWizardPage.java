package am.lodge.code.scaffold.wizards.single;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import am.lodge.code.scaffold.util.Constants;

public class EndWizardPage extends WizardPage {

  private Text packages;

  private Text pagePath;

  public EndWizardPage(String title){
    super(title);
    setTitle(title);
    setPageComplete(true);
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
    setControl(body);
  }

  public Map<String, Object> getData(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put(Constants.PACKAGE, packages.getText());
    result.put(Constants.PAGE_PATH, pagePath.getText());
    return result;
  }
}