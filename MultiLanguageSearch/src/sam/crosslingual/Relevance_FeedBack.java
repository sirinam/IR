package sam.crosslingual;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;

public class Relevance_FeedBack 
{

	// Returns an array of TermWeights representing 
	  // the document whose identifier in reader is docId in tf-idf format, 
	  // with base 10 logs. 
	  // The vector is not normalized (may have length != 1)
	  public static void toTfIdf(IndexReader reader, int docId) throws Exception 
	  {
	     // get Lucene representation of a Term-Frequency vector
	     //TermFreqVector tfv = reader.getTermFreqVector(docId,"contents");
		  Bits liveDocs = MultiFields.getLiveDocs(reader);
		  TermsEnum termEnum = MultiFields.getTerms(reader, "contents").iterator();
		  BytesRef term = null;
		  //TFIDFSimilarity tfidfSim = new DefaultSimilarity();
		  int docCount = reader.numDocs();

		  
		  Document dc=reader.document(docId);
		  List<IndexableField> index_f=dc.getFields();
		  
		  for(IndexableField idf:index_f)
		  {
			  System.out.println(idf.stringValue());
		  }
		  String all_words[]=dc.getValues("content");
		  
		  
		  System.out.println("words in docs");
		  for(String temp:all_words)
		  {
			  System.out.println(temp);
		  }
		  System.out.println();
		  
//		  while ((term = termEnum.next()) != null) 
//		  {
//			    Term termInstance = new Term("contents", term);
//			    long indexDf = reader.docFreq(termInstance);      
//
//			    DocsEnum docs = termEnum.docs(reader.getLiveDocs())
//			    while(docs.next() != DocsEnum.NO_MORE_DOCS) 
//			    {
//			        double tfidf = tfidfSim.tf(docs.freq()) * tfidfSim.idf(docCount, indexDf);
//			    }
//		  }
		  
		  
		  
		  
		  
		  
		  
	     // split it into two Arrays: one for terms, one for frequencies;
	     // Lucene guarantees that terms are sorted
//	     Terms terms = reader.getTermVector(docId, "contents");
//	     Iterator<TermsEnum> it=(Iterator<TermsEnum>) terms.iterator();
//	     while(it.hasNext())
//	     {
//	    	 TermsEnum te=it.next();
//	    	 te.docFreq();
//	    	 //System.out.println(it.next());
//	     }
//	     int[] freqs = tfv.getTermFrequencies();
//
//	     TermWeight[] tw = new TermWeight[terms.length];
//
//	     // compute the maximum frequence of a term in the document
//	     int fmax = freqs[0];
//	     for (int i = 1; i < freqs.length; i++) {
//	         if (freqs[i] > fmax) fmax = freqs[i];
//	     }
//
//	     // number of docs in the index
//	     int nDocs = reader.numDocs();
//
//	     for (int i = 0; i < tw.length; i++) 
//	     {
//	         ... code to compute stuff ...
//	         tw[i] = new TermWeight(terms[i],...stuff...);
//	     }
//
//	     return tw;
	  }
	
}
