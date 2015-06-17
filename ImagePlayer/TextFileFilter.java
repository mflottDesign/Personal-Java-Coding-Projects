import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TextFileFilter extends FileFilter
{
     public boolean accept(File f)
    {
        if(f.isDirectory())
        {
            return true;
        }
        return f.getName().endsWith(".txt");
    }
 
    public String getDescription()
    {
        return "Text files (*.txt)";
    }
}