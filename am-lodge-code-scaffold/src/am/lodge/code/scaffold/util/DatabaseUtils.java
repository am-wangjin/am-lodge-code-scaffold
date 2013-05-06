package am.lodge.code.scaffold.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;

public final class DatabaseUtils{

  private static URLClassLoader loader;

  private static String loadJars = "";

  private DatabaseUtils(){
    
  }

  public static DatabaseMetaData getDatabaseMetaData(IPreferenceStore store){
    String url = store.getString("url");
    String userName = store.getString("userName");
    String password = store.getString("password");
    String driveClass = store.getString("driveClass");
    String jars = store.getString("jars");
    try{
      return DatabaseUtils.getDatabaseMetaData(url, userName, password, driveClass, jars);
    }catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  public static void close(DatabaseMetaData dmd){
    try{
      dmd.getConnection().close();
    }catch (SQLException e){
      throw new RuntimeException(e);
    }
  }

  public static DatabaseMetaData getDatabaseMetaData(String url, String userName, String password, String driveClass, String jars) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
    if(!StringUtils.equals(loadJars, jars)){
      String[] jarStrs = jars.split(";");
      URL[] urls = new URL[jarStrs.length];
      for(int i = 0; i < jarStrs.length; i++){
        urls[i] = new File(jarStrs[i]).toURI().toURL();
      }
      loader = new URLClassLoader(urls);
      loadJars = jars;
    }
    @SuppressWarnings("rawtypes")
    Class clazz = loader.loadClass(driveClass);
    Driver driver = (Driver) clazz.newInstance();
    Properties prop = new Properties();
    prop.put("user", userName);
    prop.put("password", password);
    Connection con = driver.connect(url, prop);
    return con.getMetaData();
  }
}