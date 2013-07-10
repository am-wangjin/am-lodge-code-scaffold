package am.lodge.code.scaffold.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import am.lodge.code.scaffold.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer{

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
   * initializeDefaultPreferences()
   */
  public void initializeDefaultPreferences(){
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setDefault(PreferenceConstants.P_TEMPLATE_PATH, "");
    store.setDefault(PreferenceConstants.P_SOUND_CODE_PATH, "src");
    store.setDefault(PreferenceConstants.P_PAGE_PATH, "WebContect");
    store.setDefault(PreferenceConstants.P_URL, "");
    store.setDefault(PreferenceConstants.P_USER_NAME, "");
    store.setDefault(PreferenceConstants.P_PASSWORD, "");
    store.setDefault(PreferenceConstants.P_DRIVE_CLASS, "");
    store.setDefault(PreferenceConstants.P_JARS, "");
  }
}