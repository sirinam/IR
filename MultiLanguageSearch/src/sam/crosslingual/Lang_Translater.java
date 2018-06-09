package sam.crosslingual;
import java.net.*;
import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.*;


public class Lang_Translater 
{
  public static String translate(String args) throws Exception
  {
	  // API Key
	  // trnsl.1.1.20160420T023202Z.cddccb17bc2a7b3f.5f22390edf85a1324e01b794b3416d3afcc3b49b
	    //args="Deepak";
	    Translate.setClientId("Vikas_translation");
		Translate.setClientSecret("us2d362oaVG3oXuRGay/zKNINbTr43bmq4Jz3S9uqeg=");
		                          //"us2d362oaVG3oXuRGay/zKNINbTr43bmq4Jz3S9uqeg="
		String translatedText=Translate.execute(args, Language.ENGLISH, Language.HINDI);
		System.out.println(translatedText);
		
		return translatedText;
  }
}