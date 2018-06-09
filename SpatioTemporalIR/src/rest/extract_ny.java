package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;


public class extract_ny 
{
	String some;
	public String work(String Filename)
	{
		try
		{
			FileInputStream f = new FileInputStream(Filename);
			
			GZIPInputStream gis = new GZIPInputStream(f); 
			TarInputStream tis = new TarInputStream(gis);
			TarEntry t = tis.getNextEntry();
			while(t!=null)
			{
				if(t.isDirectory())
				{
					t=tis.getNextEntry();
					continue;
				}
				some = new parse_dat().parse(t,tis);
				System.out.println(some);
				
				t = tis.getNextEntry();
			}
			gis.close();
			tis.close();
		}
		catch(IOException ex) 
		{
           	System.err.println("An IOException was caught!");
           	ex.printStackTrace();
        }
	return some;
	}
}
