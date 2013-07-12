package am.lodge.code.scaffold.stirrer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import am.lodge.code.scaffold.database.Table;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeStirrer{

  private Map<String, Object> data;

  private File inRoot;

  private IContainer outRoot;

  private String outPath;

  private Shell shell;

  public CodeStirrer(Map<String, Object> data, File inRoot, IContainer outRoot, String outPath, Shell shell){
    this.data = data;
    this.inRoot = inRoot;
    this.outRoot = outRoot;
    this.outPath = outPath;
    this.shell = shell;
  }

  public void create(){
    try{
      if(!"".equals(outPath)){
        outRoot = createFolders(outPath.split("[./]"), outRoot);
      }
      createScaffold(inRoot, outRoot);
    }catch (CoreException e){
      throw new RuntimeException(e);
    }catch (IOException e){
      throw new RuntimeException(e);
    }catch (TemplateException e){
      throw new RuntimeException(e);
    }
  }

  private IContainer createFolders(String[] folders, IContainer parent) throws CoreException{
    if(folders.length == 0)
      return parent;
    else{
      IContainer folder = createFolder(folders[0], parent);
      folders = (String[]) ArrayUtils.remove(folders, 0);
      return createFolders(folders, folder);
    }
  }

  private IContainer createFolder(String path, IContainer parent) throws CoreException{
    IFolder folder = parent.getFolder(new Path(path));
    if(!folder.exists()){
      folder.create(true, true, null);
    }
    return folder;
  }

  private void createScaffold(File file, IContainer parent) throws CoreException, IOException, TemplateException{
    File[] subs = file.listFiles();
    Configuration config = new Configuration();
    config.setDirectoryForTemplateLoading(file);
    config.setObjectWrapper(new DefaultObjectWrapper());
    for (File f : subs){
      if(f.isFile()){
        createFile(f, parent, config);
      }else{
        IContainer folder = createFolder(f.getName(), parent);
        createScaffold(f, folder);
      }
    }
  }

  private void createFile(File file, IContainer outDir, Configuration config) throws CoreException, IOException, TemplateException{
    String suffix = StringUtils.substringAfterLast(file.getName(), ".");
    if(StringUtils.equals(suffix, "ftl")){
      Template temp = config.getTemplate(file.getName());
      String fileName = StringUtils.removeEnd(file.getName(), ".ftl");
      String begin = StringUtils.substringBefore(fileName, "[");
      String end = StringUtils.substringAfter(fileName, "]");
      String key = StringUtils.substringAfter(fileName, "[");
      key = StringUtils.substringBefore(key, "]");

      if(StringUtils.contains(fileName, "[")){
        String v = "";
        if(data.containsKey(key)){
          @SuppressWarnings("unchecked")
          Map<String, Object> map = (Map<String, Object>)data.get(key);
          v = ((Table)map.get("table")).getUpperCamelName();
        } else{
          v = ((Table)data.get("table")).getUpperCamelName();
        }
        fileName = begin + v + end;
      }
      IFile outFile = outDir.getFile(new Path(fileName));
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      temp.process(data, new OutputStreamWriter(out));
      if(outFile.exists()){
        boolean cover = MessageDialog.openConfirm(shell, "warning", "file " + outFile.getName() + " is exist cover?");
        if(cover){
          outFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, true, null);
        }
      }else{
        outFile.create(new ByteArrayInputStream(out.toByteArray()), true, null);
      }
    }else{
      IFile outFile = outDir.getFile(new Path(file.getName()));
      if(outFile.exists()){
        outFile.setContents(new FileInputStream(file), true, true, null);
      }else{
        outFile.create(new FileInputStream(file), true, null);
      }
    }
  }
}