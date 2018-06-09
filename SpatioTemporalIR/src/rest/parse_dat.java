package rest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
public class parse_dat {
	/*String f;
	parse_dat(String fin)
	{
		f = fin;
	}*/
	NYTCorpusDocument timesDocument;
	String text;
	public String parse(TarEntry t, TarInputStream tis)
	{
		try
		{
		//File destPath = new File("output/"+t.getName());
		System.out.println(t.getName());
		String fileName = t.getName();
		/*if(t.isDirectory())
		{
			System.out.println("Directory");
			return null;
		}*/
		/*FileOutputStream fout = new FileOutputStream(t.getName());
		tis.copyEntryContents(fout);
		fout.close();
		File fin = new File(destPath.toString());*/
		fileName = new File(fileName).getName();
		
        File newFile = new File("G:\\NYT_Index\\" +fileName);
        System.out.println(newFile.getAbsoluteFile());
        //new File(newFile.getParent()).mkdirs();
		NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
		//File file = new File(f);
		FileOutputStream fout = new FileOutputStream(newFile);
		tis.copyEntryContents(fout);
		fout.close();
		
		//File fin = new File("G:\\IRP_Index\\" + File.separator + fileName);
		
		//File fin = new File(destPath.toString());
		//timesDocument = parser.parseNYTCorpusDocumentFromFile(fin, false);
		//text = timesDocument.getHeadline();
		//new File(newFile.getAbsolutePath()).delete();
		//newFile.delete();
		//fin.delete();
		}
		catch(Exception ex) 
		{
           	System.err.println("An IOException was caught!");
           	ex.printStackTrace();
        }
		return text;
	}
}
