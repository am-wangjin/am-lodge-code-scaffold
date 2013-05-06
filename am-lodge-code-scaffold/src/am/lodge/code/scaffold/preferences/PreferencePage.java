package am.lodge.code.scaffold.preferences;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import am.lodge.code.scaffold.Activator;
import am.lodge.code.scaffold.util.DatabaseUtils;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

  private StringFieldEditor url;

  private StringFieldEditor userName;

  private PasswordFieldEditor password;

  private StringFieldEditor driveClass;

  private JarsListEditor jars;

  public PreferencePage(){
    super(GRID);
    setPreferenceStore(Activator.getDefault().getPreferenceStore());
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI
   * blocks needed to manipulate various types of preferences. Each field editor
   * knows how to save and restore itself.
   */
  public void createFieldEditors(){
    addField(new DirectoryFieldEditor(PreferenceConstants.P_TEMPLATE_PATH, "&Template root path:", getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_SOUND_CODE_PATH, "&Source code root path:", getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_PAGE_PATH, "&Page root path:", getFieldEditorParent()));
    url = new StringFieldEditor(PreferenceConstants.P_URL, "&Url:", getFieldEditorParent());
    addField(url);
    driveClass = new StringFieldEditor(PreferenceConstants.P_DRIVE_CLASS, "&DriveClass:", getFieldEditorParent());
    addField(driveClass);
    userName = new StringFieldEditor(PreferenceConstants.P_USER_NAME, "&UserName:", getFieldEditorParent());
    addField(userName);
    password = new PasswordFieldEditor(PreferenceConstants.P_PASSWORD, "&Password:", getFieldEditorParent());
    addField(password);
    jars = new JarsListEditor(PreferenceConstants.P_JARS, "&Database drive jars", getFieldEditorParent());
    addField(jars);
    Button test = new Button(getFieldEditorParent(), SWT.PUSH);
    test.setFont(getFieldEditorParent().getFont());
    test.setText("test connect");
    GridData gd = new GridData();
    gd.widthHint = 100;
    test.setLayoutData(gd);
    test.addSelectionListener(getTestContectListener());
  }

  private SelectionListener getTestContectListener(){
    return new SelectionAdapter(){
      public void widgetSelected(SelectionEvent evt) {
        DatabaseMetaData dmd = null;
        try{
          dmd = DatabaseUtils.getDatabaseMetaData(url.getStringValue(), userName.getStringValue(), password.getStringValue(), driveClass.getStringValue(), jars.getJars());
        }catch(Exception ex){
          MessageDialog.openError(getShell(), "connect test error", ex.getMessage());
        }finally{
          if(dmd != null){
            MessageDialog.openInformation(getShell(), "connect test", "connect succeed");
            try{
              dmd.getConnection().close();
            }catch (SQLException e){
              throw new RuntimeException(e);
            }
          }
        }
      }
    };
  }

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench){
  }
}