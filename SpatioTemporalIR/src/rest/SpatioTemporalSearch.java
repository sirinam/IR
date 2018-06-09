package rest;


import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.TimeAnnotator;

@Path("/stsearch")
public class SpatioTemporalSearch {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String stsearch(@QueryParam("query") String query,@QueryParam("location") String location)//, @QueryParam("DateTime") String DateTime)
	{
		String obj="";
		//code to detect food or movie or other
		//zomato rest call
		//bookmyshow rest call
		//lucene index
		//String filen = args[0];
		//String ind = args[1];
		//if (args.length > 0) 
		//{
			//File dir = new File(filen);
			//quesString = scan.next();
			//File index_dir = new File(quesString);
			
			System.out.println(query+" "+location);
			try{
//			BufferedReader br = new BufferedReader(new FileReader("updatedcities.csv"));
//			HashMap<String,String> coordinates = new HashMap<String, String>();
//			String line;
//			HashSet<String> hs = new HashSet<String>();
//			int nFiles = 100;
//			while ((line = br.readLine()) != null) {
//				String s[] = line.split(",");
//				if (s.length == 3)
//					coordinates.put(s[2], s[0] + " " + s[1]);
//			}
//		
//		 // Make Information Retrieval Pipeline
//			Properties props = new Properties();
//		    AnnotationPipeline pipeline = new AnnotationPipeline();
//		    pipeline.addAnnotator(new TokenizerAnnotator(false));
//		    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
//		    pipeline.addAnnotator(new POSTaggerAnnotator(false));
//		    pipeline.addAnnotator(new TimeAnnotator("sutime", props));
//		    
//		 // Extract Data from xml files and make Inverted Index
//			File dir = new File("output");
//			File indexD = new File("index");
//			create_ind crtInd = new create_ind(dir,indexD,coordinates);		
//			//crtInd.extract(pipeline);
//			 
//		 // Search for a query string in the Index
//			crtInd.searchstr(query, pipeline);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			
			
//			
//			try {
//				new create_ind().searchstr(query);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		
		
		
		return query+" "+location;
	}

}
