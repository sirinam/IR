package sam.crosslingual;
/*
002 * Licensed to the Apache Software Foundation (ASF) under one or more
003 * contributor license agreements.  See the NOTICE file distributed with
004 * this work for additional information regarding copyright ownership.
005 * The ASF licenses this file to You under the Apache License, Version 2.0
006 * (the "License"); you may not use this file except in compliance with
007 * the License.  You may obtain a copy of the License at
008 *
009 *     http://www.apache.org/licenses/LICENSE-2.0
010 *
011 * Unless required by applicable law or agreed to in writing, software
012 * distributed under the License is distributed on an "AS IS" BASIS,
013 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
014 * See the License for the specific language governing permissions and
015 * limitations under the License.
016 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

/** Simple command-line based search demo. */
public class SearchFiles 
{

  private static final String FIELD_CONTENTS = "contents";

  private SearchFiles() {}


  public static HashMap<String,HashSet<String>> getSynonymMap(String input_query) throws Exception
  {
	  HashMap<String,HashSet<String>> hm=new HashMap<>();
	  
  	  // making list of POS full forms
	  FileReader fr=new FileReader(new File("D:\\IR_Project\\Multi_Lang_Search\\Pos_Tag_List"));
      BufferedReader br = new BufferedReader(fr);
      String s="";
      HashMap<String,String> full_POS =new HashMap<String,String>();
      while((s=br.readLine()) != null)
      {
          if(!s.equals(""))
          {
              String st[]=s.split(" ", 2);
              full_POS.put(st[0].trim(), st[1].trim());
          }
      }
      //System.out.println("Full POS: "+full_POS);
	  
	  // adding wordnet dictionary to program
	  File f=new File("D:\\WordNet\\2.1\\dict");
      System.setProperty("wordnet.database.dir", f.toString());

      // getting wordnet database instance
      WordNetDatabase database = WordNetDatabase.getFileInstance();

      String usage =
      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
     
      
   // doing POS Tagging
      MaxentTagger tagger =  new MaxentTagger("D:\\Tagger\\english-left3words-distsim.tagger");
      String tagged = tagger.tagString(input_query);
      //System.out.println(tagger.tagSet());
     
      //System.out.println("tagged string is: "+tagged);
      
      StringTokenizer sz=new StringTokenizer(tagged," ");
      HashMap<String,String> hs_tag=new HashMap<String,String>();
      while(sz.hasMoreTokens())
      {
          String s1=sz.nextToken();
          StringTokenizer sw=new StringTokenizer(s1,"_");
          String word=sw.nextToken();
          String tag=sw.nextToken();
          hs_tag.put(word,tag);
      }
      
      //System.out.println("input query with tags :"+ hs_tag);
      
      
      String q_words[]=input_query.split(" ");
      Synset[] synsets;
      String string_query="";
      for(int it1=0;it1<q_words.length;it1++)
      {
    	  String POS_tag=full_POS.get(hs_tag.get(q_words[it1])).toUpperCase();
    	  if(POS_tag.contains("ADVERB"))
    	  {
    		   System.out.println(q_words[it1]+" is adverb");
    		   synsets = database.getSynsets(q_words[it1],SynsetType.ADVERB);
    	  }
    	  else if(POS_tag.contains("VERB"))
    	  {
    		  System.out.println(q_words[it1]+" is verb");
    		  synsets = database.getSynsets(q_words[it1],SynsetType.VERB);
    	  }
    	  else if(POS_tag.contains("NOUN"))
    	  {
    		  System.out.println(q_words[it1]+" is noun");
    		  synsets = database.getSynsets(q_words[it1],SynsetType.NOUN);
    	  }
    	  else if(POS_tag.contains("ADJECTIVE"))
    	  {
    		  System.out.println(q_words[it1]+" is adjective");
    		  synsets = database.getSynsets(q_words[it1],SynsetType.ADJECTIVE);
    	  }
    	  else
    	  {
    		  System.out.println("its not a verb, noun,adjnective");
    		  synsets = database.getSynsets(q_words[it1]);
    	  }
    	  
    	  // getting all synonym
    	  HashSet<String> hs = new HashSet<String>(); 
    	  if (synsets.length > 0)
    	  {
    		  for (int i = 0; i < synsets.length; i++)
    		  {
    			  String[] wordForms = synsets[i].getWordForms();
    			  for (int j = 0; j < wordForms.length; j++)
    			  {
    				  hs.add(wordForms[j].toLowerCase());
    			  }    			  
    		  }
    		  
    		  //showing all synsets
//    		  System.out.println("ALL Synonyms: ");
//    		  for(String s1:hs)
//    		  {
//    			  System.out.println(s1);
//    		  }
    	  }
    	  else
    	  {
    		  System.err.println("No synsets exist that contain the word form '" + q_words[it1] + "'");
    		  hs.add(q_words[it1]);  
    	  }
    	  
    	  hm.put(q_words[it1], hs);
      }
    	  return hm;	      
  }
  
  
  
  public static String makeQuery(String input_query,HashMap<String,HashSet<String>> hm,boolean English) throws Exception
  {   //changed by deepak
	  String q_words[] = null;
	  HashMap<String,HashSet<String>> hs_hindi = new HashMap<String,HashSet<String>>();
	  String query_hindi[]=new String[hm.keySet().size()];
	  if(English==false)
	  {
		  //q_words=input_query.split(" ");
		  int it=0;
		 for(String str: hm.keySet())
		 {
			 String hindi_key=Lang_Translater.translate(str);
			 query_hindi[it]=hindi_key;
			 Set<String>  synom=hm.get(str);
			 HashSet<String>  hindi_syno=new HashSet<String>();
			 for(String s: synom)
			 {
				 
				 String hindi_word=Lang_Translater.translate(s);
				 	hindi_syno.add(hindi_word);		 
			 }
			 //if(!hs_hindi.containsKey(hindi_key))
				hs_hindi.put(hindi_key,hindi_syno); 
			it++;
		 }
		 //System.out.println(hs_hindi);
		 q_words=query_hindi.clone();
		
		  //System.exit(0);
	  }	
	  //System.out.println("hindi query: "+query_hindi);
	
	  if(English==true)
	  {
		  q_words=input_query.split(" ");
	  }
	  String string_query="";
	  for(int it1=0;it1<q_words.length;it1++)
	  {
		  HashSet<String> hs=null;
		  //System.out.println("key word: "+q_words[it1]);
		  if(English==true)
		  {
			  hs=hm.get(q_words[it1]);
		  }
		  else
		  {
			  hs=hs_hindi.get(q_words[it1]);
		  }
		  if(hs!=null)
		  {
		  String temp[]=hs.toArray(new String[hs.size()]);
		  for(int i=0; i< temp.length;i++)
		  {
			  if(i==0)
			  {
				  string_query=string_query+"(";
				  String new_temp[]=temp[i].split(" ");    			  
				  // doing phrase check and putting AND between all words of phrase
				  if(new_temp.length>1)
				  {
					  string_query=MakePhraseQuery(new_temp, string_query);
				  }
				  else
				  {
					  string_query=string_query+temp[i];
				  }
				  if(i==temp.length-1)
				  {
					  string_query=string_query+")";
				  }
				  else
				  {
					  string_query=string_query+" OR ";
				  }
			  }
			  else if(i==temp.length-1)
			  {
				  String new_temp[]=temp[i].split(" ");
				  
				  // doing phrase check and putting AND between all words of phrase
				  if(new_temp.length>1)
				  {
					  string_query=MakePhraseQuery(new_temp, string_query);    				  
				  }
				  else
				  {
					  string_query=string_query+temp[i];
				  }	  
				  string_query=string_query+")";
			  }
			  else
			  { 
				  String new_temp[]=temp[i].split(" ");  
				  // doing phrase check and putting AND between all words of phrase
				  if(new_temp.length>1)
				  {
					  string_query=MakePhraseQuery(new_temp, string_query);
				  }
				  else
				  {
					  string_query=string_query+temp[i];
				  }
				  string_query=string_query+" OR ";	  
			  }    		  
		  }
		  if(it1 !=q_words.length-1)
		  {
			  string_query=string_query+" AND ";
		  }
		  }
	 }
	  
	 System.out.println("query in string format is: "+string_query);
	 return string_query;
	 
  }

  private static String MakePhraseQuery(String[] new_temp, String string_query) 
  {
	  String temp="";
	  temp=temp+"\"";
	  
	  for(int j=0;j<new_temp.length;j++)
	  {
			temp=temp+new_temp[j];
			if(j!=new_temp.length-1)
			{
				temp=temp+" ";
			}  
	  }
	  temp=temp+"\"";
//	  for(int j=0;j<new_temp.length;j++)
//	  {
//		  if(j==0)
//		  {
//			  string_query=string_query+"("+new_temp[j]+" AND ";
//		  }
//		  else if(j==new_temp.length-1)
//		  {
//			  string_query=string_query+new_temp[j]+")";
//		  }
//		  else
//		  {
//			  string_query=string_query+new_temp[j]+" AND ";
//		  }
//	  }
	  //System.out.println("phrase query: "+ temp);
	  string_query=string_query+temp;
	  
	return string_query;
  }
  
  
  

/**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static TopDocs doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
    ScoreDoc[] hits = results.scoreDocs;
    
    int numTotalHits = results.totalHits;
    //System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
//    while (true) 
//    {
//      if (end > hits.length) {
//        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
//        System.out.println("Collect more (y/n) ?");
//        String line = in.readLine();
//       if (line.length() == 0 || line.charAt(0) == 'n') {
//          break;
//        }
//
//        hits = searcher.search(query, numTotalHits).scoreDocs;
//      }
//      
//      end = Math.min(hits.length, start + hitsPerPage);
//      
//      for (int i = start; i < end; i++) 
//      {
//        if (raw) {                              // output raw format
//          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
//          continue;
//        }
//
//       Document doc = searcher.doc(hits[i].doc);
//        String path = doc.get("path");
//        if (path != null) 
//        {
//          System.out.println((i+1) + ". " + path);
//          String title = doc.get("title");
//          if (title != null) {
//            System.out.println("   Title: " + doc.get("title"));
//          }
//        } else {
//          System.out.println((i+1) + ". " + "No path for this document");
//        }
//                  
//      }
//
//      if (!interactive || end == 0) 
//      {
//        break;
//      }
//
//      if (numTotalHits >= end) {
//        boolean quit = false;
//        while (true) {
//          System.out.print("Press ");
//         if (start - hitsPerPage >= 0) {
//            System.out.print("(p)revious page, ");  
//          }
//          if (start + hitsPerPage < numTotalHits) {
//           System.out.print("(n)ext page, ");
//          }
//          System.out.println("(q)uit or enter number to jump to a page.");
//          
//          String line = in.readLine();
//          if (line.length() == 0 || line.charAt(0)=='q') {
//        	  
//            quit = true;
//            break;
//          }
//          if (line.charAt(0) == 'p') {
//            start = Math.max(0, start - hitsPerPage);
//            break;
//          } else if (line.charAt(0) == 'n') {
//            if (start + hitsPerPage < numTotalHits) {
//              start+=hitsPerPage;
//            }
//            break;
//          } else {
//            int page = Integer.parseInt(line);
//            if ((page - 1) * hitsPerPage < numTotalHits) {
//              start = (page - 1) * hitsPerPage;
//              break;
//            } else {
//              System.out.println("No such page");
//            }
//          }
//        }
//        if (quit) break;
//        end = Math.min(numTotalHits, start + hitsPerPage);
//      }
//    }
    return results;
  }
}