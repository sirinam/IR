package sam.crosslingual;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;


public class Cross_Linguil_IR 
{
	
	final static private String english_doc_topic_path="D:/IR_Project/Multi_Lang_Search/topic_compostion.txt";
	final static private String english_topic_key_path="D:/IR_Project/Multi_Lang_Search/topics_keywords.txt";
	
	final static private String hindi_doc_topic_path="D:/IR_Project/Multi_Lang_Search/hindi_topic_compostion.txt";
	final static private String hindi_topic_key_path="D:/IR_Project/Multi_Lang_Search/hindi_topics_keywords.txt";
	public static void main(String[] args) throws Exception
	{ 
	 //Indexing for English Word
	   //System.out.println("............Indexing for English Language..........");
	   Analyzer eng_analyzer = new StandardAnalyzer();
	   String eng_indexPath = "D:\\English_Index";
	   String docsPath = "D:\\IR_DataSet\\wiki_engcorpus_in_text";
	   //String docsPath="D:\\IR_DataSet\\Megha_Eng_wiki-small\\en\\articles";
       //IndexFiles.indexing(eng_analyzer,eng_indexPath,docsPath);
       
     //Indexing for Hindi Word  
       //System.out.println("............Indexing for Hindi Language..........");
       Analyzer hindi_analyzer = new HindiAnalyzer();
	   String hindi_indexPath = "D:\\Hindi_Index";
	   String hindi_docsPath = "D:\\IR_DataSet\\Hindi_DataSet";
	   //String hindi_docsPath = "D:\\IR_DataSet\\Yogesh_hindi_wikipedia-hi-html";
       //IndexFiles.indexing(hindi_analyzer,hindi_indexPath,hindi_docsPath);
       
       HashMap<String,ArrayList<Double>> doc_topic=  Mallet_Topic_Modelling.makeDoc_topic_metric(english_doc_topic_path);
       HashMap<Integer,ArrayList<String>> topic_key= Mallet_Topic_Modelling.makeTopic_key_metric(english_topic_key_path);
       HashMap<String,ArrayList<Integer>> key_topic= Mallet_Topic_Modelling.makeKey_topic_metric(topic_key);
       
       //System.out.println("doc_topic:");
//       System.out.println(doc_topic);
//       
//       int m=0;
//       for(String s:doc_topic.keySet())
//       {
//    	   System.out.println(s+"  "+doc_topic.get(s));
//    	   m++;
//    	   if(m==5)
//    		   break;
//    	   
//       }
       
       
        //Search for phrase...
	    //System.out.println(".......Searching for phrase.........");
	    System.out.println("Enter the Query :");
	    BufferedReader in = null;
	    String index = "D:\\English_Index";
	    String field = "contents";
	    int hitsPerPage = 10;
	    String queries = null;
	    String queryString = null;
	    boolean raw = false;
	    if (queries != null) 
	    {
	      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
	    } 
	    else 
	    {
	      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
	    }
	    
	    String line =  in.readLine();
	    
	    if (line == null || line.length() == -1) 
		{
			System.err.println("Provide an valid string");
		}
		if (line.length() == 0)
		{
			System.err.println("Empty String .Plz provive an non empty string");
		} 
	    //Scanner sc=new Scanner(System.in);
	   // String input_query=sc.next();
	    //Set of related synonyms
	    HashMap<String,HashSet<String>> hm=SearchFiles.getSynonymMap(line);
	    
	    String query_format=SearchFiles.makeQuery(line, hm, true);
	  
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
	    IndexSearcher searcher = new IndexSearcher(reader);
	    //Analyzer analyzer = new StandardAnalyzer();
	
//	    /*//IndexSearcher searcher = new IndexSearcher(reader);


    searcher.setSimilarity(new ClassicSimilarity () {
	   	   	public double idf(long docFreq,Long docCount){
	   	   		return Math.log((docCount+1)/(docFreq+1)) + 1.;
	   	   	}
	   	   }
   	      );


	   
	      
	    
	  QueryParser queryParser = new QueryParser(field, new StandardAnalyzer());
	  Query query = queryParser.parse(query_format);
	  //System.out.println(" query is "+query);
	  TopDocs results=SearchFiles.doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
	  
	  
	  
	  // printing refreshed list of different topic at top
	  ArrayList<Retrieve_Docs> firstPages=new ArrayList<>();
	  ArrayList<Retrieve_Docs> lastPages=new ArrayList<>();
	  printRefreshList(results,searcher,doc_topic,firstPages,lastPages);
//	  System.out.println("Refreshed List is:");
//	  for (Retrieve_Docs s:firstPages)
//		  System.out.println(s.path +" "+s.score);
//	  for(Retrieve_Docs s:lastPages)
//		  System.out.println(s.path +" "+s.score);
	 
	  
	  
	  
	  
	  
	  
	  
//	  QueryParser queryParser = new QueryParser(field, new StandardAnalyzer());
//	  Query query = queryParser.parse("\"daily newspaper\"");
//	  System.out.println(" query is "+query);
//	  SearchFiles.doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
	   
	      // search with query parser
	 
	  //Search the phrase in Hindi 
	  
	  
	  HashMap<String,ArrayList<Double>> hindi_doc_topic=  Mallet_Topic_Modelling.makeDoc_topic_metric(hindi_doc_topic_path);
      HashMap<Integer,ArrayList<String>> hindi_topic_key= Mallet_Topic_Modelling.makeTopic_key_metric(hindi_topic_key_path);
      HashMap<String,ArrayList<Integer>> hindi_key_topic= Mallet_Topic_Modelling.makeKey_topic_metric(hindi_topic_key);
	  
	  
	  
	 
	 String hindi_Search=SearchFiles.makeQuery(line, hm,false);
	 IndexReader reader_hindi = DirectoryReader.open(FSDirectory.open(Paths.get(hindi_indexPath)));
	 IndexSearcher searcher_hindi = new IndexSearcher(reader_hindi);
	 //IndexSearcher searcher = new IndexSearcher(reader);
	 //IndexSearcher searcher = new IndexSearcher(reader);


	 searcher_hindi.setSimilarity(new ClassicSimilarity () {
		   	public float computeNorm(FieldInvertState state,String field){
		   		return 1.0f;
		   	}
		   }
		      );

	 QueryParser queryParser_hindi = new QueryParser(field, new HindiAnalyzer());
	  Query query_hindi = queryParser_hindi.parse(hindi_Search);
	  //System.out.println(" query is "+query_hindi);
	   TopDocs td=SearchFiles.doPagingSearch(in, searcher_hindi, query_hindi, hitsPerPage, raw, queries == null && queryString == null);
	      
	   ArrayList<Retrieve_Docs> hindi_firstPages=new ArrayList<>();
		  ArrayList<Retrieve_Docs> hindi_lastPages=new ArrayList<>();
	      printRefreshList(td,searcher_hindi,hindi_doc_topic,hindi_firstPages,hindi_lastPages);
	      
//	      System.out.println("Refreshed List is:");
//		  for (Retrieve_Docs s:hindi_firstPages)
//			  System.out.println(s.path +" "+s.score);
//		  for(Retrieve_Docs s:hindi_lastPages)
//			  System.out.println(s.path +" "+s.score);
		  
		  
		  ArrayList<Retrieve_Docs> all_firstPages=new ArrayList<>();
		  all_firstPages.addAll(firstPages);
		  all_firstPages.addAll(hindi_firstPages);
		  
		  //System.out.println(" all first pages size: "+all_firstPages.size());
		  Collections.sort(all_firstPages,new Comparator<Retrieve_Docs>() 
		  {
		        @Override public int compare(Retrieve_Docs p1, Retrieve_Docs p2) 
		        {
		        	if(p1.score < p2.score)
		        		return 1;
		        	else if(p1.score > p2.score)
		        		return -1;
		        	else
		        		return 0;
		        }
		        });
		  
		  //System.out.println("final pages:");
		  for (Retrieve_Docs s:all_firstPages)
			  System.out.println(s.path +" "+s.score);
		  
		  
		  ArrayList<Retrieve_Docs> all_lastPages=new ArrayList<>();
		  all_lastPages.addAll(lastPages);
		  all_lastPages.addAll(hindi_lastPages);
		  
		  //System.out.println(" all first pages size: "+all_lastPages.size());
		  Collections.sort(all_lastPages,new Comparator<Retrieve_Docs>() {
		        @Override public int compare(Retrieve_Docs p1, Retrieve_Docs p2) 
		        {
		        	if(p1.score < p2.score)
		        		return 1;
		        	else if(p1.score > p2.score)
		        		return -1;
		        	else
		        		return 0;
		        }
		        });
		  for (Retrieve_Docs s:all_lastPages)
			  System.out.println(s.path +" "+s.score);
		  
		  
	      //Relevance_FeedBack.toTfIdf(reader, td.scoreDocs[0].doc);
	      
	      
	 System.out.println("Ho gya search.......");
	
	 reader.close();
	    
        
  }
  private static void printRefreshList(TopDocs results,IndexSearcher searcher,HashMap<String,ArrayList<Double>> doc_topic,ArrayList<Retrieve_Docs> firstPages,ArrayList<Retrieve_Docs> lastPages) throws Exception 
  {
	// TODO Auto-generated method stub
	  
	  ArrayList<Retrieve_Docs> top10docs=new ArrayList<Retrieve_Docs>();
	  //ArrayList<String> top10docs=new ArrayList<>();
	  
	  ScoreDoc[] hits = results.scoreDocs;
	  for(int i=0;i<10 && i< hits.length;i++)
	  {
		  Document doc = searcher.doc(hits[i].doc);
		  String path = doc.get("path");
		  if(path!=null)
		  {
			  Retrieve_Docs d=new Retrieve_Docs();
			  d.path=path;
			  d.score=hits[i].score;
			  top10docs.add(d);
		  }
	  }
	  
//	  ArrayList<String> firstPages=new ArrayList<>();
//	  ArrayList<String> lastPages=new ArrayList<>();
	  HashSet<Integer> firstTopics=new HashSet<>();
	  HashSet<Integer> lastTopics=new HashSet<>();
	  for(int i=0;i<10 && i< hits.length;i++)
	  {
		  //System.out.println(" page at tops  "+top10docs.get(i));
		 // System.out.println(doc_topic.containsKey(top10docs.get(i).path));
		  ArrayList<Double> probs=doc_topic.get(top10docs.get(i).path);
		  
		  int temp_topic=maxIndex(probs);
		  
		  if(firstTopics.contains(temp_topic))
		  {
			  lastPages.add(top10docs.get(i));
			  lastTopics.add(temp_topic);
		  }
		  else
		  {
			  firstPages.add(top10docs.get(i));
			  firstTopics.add(temp_topic);
		  }
	  }
	  
	  //System.out.println("topic for top docs :" +firstTopics);
	  //System.out.println("topic for last docs :" +lastTopics);
	  
	  
	
  }
public static int maxIndex(List<Double> list) {
	  int i=0;
	  Double max=null;
	  int maxIndex=-1;
	  for (Double x : list) 
	  {
	    if ((x!=null) && ((max==null) || (x>max))) 
	    {
	      max = x;
	      maxIndex = i;
	    }
	    i++;
	  }
	  return maxIndex;
	}
}
