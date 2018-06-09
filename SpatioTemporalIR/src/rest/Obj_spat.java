package rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.util.CoreMap;

public class Obj_spat {
	List<String> lat; 
	List<String> lon;
	
	Obj_spat(List<String> lat, List<String> lon  )
	{
		this.lat=lat;
		this.lon=lon;
	}

	public Obj_spat() 
	{
		// TODO Auto-generated constructor stub
		
		 
	}
}
