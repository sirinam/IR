package rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.util.CoreMap;

public class Obj_set {
	Set<String> beg_date; 
	Set<String> end_date;
	List<CoreMap> c;
	
	Obj_set(Set<String> beg_date2, Set<String> end_date2, List<CoreMap> cm  )
	{
		this.beg_date=beg_date2;
		this.end_date=end_date2;
		this.c = cm;
	}

	public Obj_set() 
	{
		// TODO Auto-generated constructor stub
		
		 
	}
}
