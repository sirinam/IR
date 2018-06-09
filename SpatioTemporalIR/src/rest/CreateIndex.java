package rest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;

import com.sun.xml.internal.ws.api.pipe.PipelineAssembler;

import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.util.CoreMap;

public class CreateIndex {
	File dir;
	File index_d;
	// static Directory INDEX_DIRECTORY = new RAMDirectory();
	// static String[] fields = {"contents", "Begin_date", "End_date"};
	static File DIRECTORY_URL = new File("index");
	static Directory INDEX_DIRECTORY;
	Obj_set o;
	Obj_spat os;
	HashMap<String, Obj_spat> spatial;
	HashMap<String, String> coordinates;
	// static String FILES_TO_INDEX_DIRECTORY =
	// "H:\\SS_IIIT\\Information_Retrieval\\01\\30";
	// static String FIELD_PATH = "URL";
	// static String FIELD_CONTENTS = "body";
	// static String FIELD_HEADLINE = "headline";
	public CreateIndex() {
		dir = null;
		index_d = null;
		coordinates = null;
	}

	CreateIndex(File filen, File ind, HashMap<String, String> coordinates) {
		dir = filen;
		index_d = ind;
		this.coordinates = coordinates;
	}

	public static void createIndex(String body, String headline, Obj_set obj, Obj_spat obj1, String path,
			BufferedWriter bw) throws CorruptIndexException, LockObtainFailedException, IOException {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		Path p = Paths.get(DIRECTORY_URL.toURI());
		INDEX_DIRECTORY = new SimpleFSDirectory(p);
		IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, conf);
		// File dir = new File("G:\\IRP_Index\\temp.txt");
		// BufferedWriter bw =new BufferedWriter(new
		// FileWriter("SpatialFile.txt"));
		// filename of xml
		char arr[] = path.toCharArray();
		String filename = "";
		for (int i = 0; i < arr.length; i++) {
			if ((int) arr[i] >= (int) '0' && (int) arr[i] <= (int) '9') {
				filename = path.substring(i, path.length());
				break;
			}
		}

		Iterator<String> it = obj.beg_date.iterator();
		Iterator<String> it2 = obj.end_date.iterator();
		Document document = new Document();
		String full_text = headline.concat(" " + body);
		String b_date = " ";
		String e_date = " ";
		String latstr = "";
		String lonstr = "";
		Iterator<String> it3 = obj1.lat.iterator();
		Iterator<String> it4 = obj1.lon.iterator();
		// String path = dir.getCanonicalPath();
		document.add(new StringField("url", path, Field.Store.YES));
		while (it.hasNext()) {
			b_date = b_date.concat(it.next() + " ");
		}
		// System.out.println(b_date);
		while (it2.hasNext()) {
			e_date = e_date.concat(it2.next() + " ");
		}
		while (it3.hasNext() && it4.hasNext()) {
			String loclat = it3.next();
			String loclon = it4.next();
			String temp = loclat + " " + loclon;
			latstr = latstr.concat(loclat + " ");
			lonstr = latstr.concat(loclon + " ");
			filename = filename + " " + temp;
			bw.write(filename);
			bw.newLine();
		}
		document.add(new TextField("Begin_date", b_date, Field.Store.YES));
		document.add(new TextField("End_date", e_date, Field.Store.YES));
		document.add(new TextField("contents", full_text, Field.Store.YES));
		document.add(new TextField("headline", headline, Field.Store.YES));
		document.add(new TextField("latitude", latstr, Field.Store.YES));
		document.add(new TextField("longitude", lonstr, Field.Store.YES));
		// System.out.println(document.getField("Begin_date"));
		indexWriter.addDocument(document);
		// System.out.println(document.getField("contents"));
		System.out.println(indexWriter.numDocs());

		// }
		indexWriter.close();
	}

	public static String searchIndex(String searchString, String bd, String ed, String lat, String lon) throws IOException {
		// System.out.println("Searching for '" + searchString + "'");
		Similarity simfn = new LMJelinekMercerSimilarity((float) 0.75);
		Path p = Paths.get(DIRECTORY_URL.toURI());
		INDEX_DIRECTORY = new SimpleFSDirectory(p);
		IndexReader indexReader = DirectoryReader.open(INDEX_DIRECTORY);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(simfn);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser queryParser1 = new QueryParser("contents", analyzer);
		// QueryParser queryParser2 = new QueryParser("Begin_date", analyzer);
		// QueryParser queryParser3 = new QueryParser("End_date", analyzer);
		// MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields,
		// analyzer);
		Query query = null;
		try {
			query = queryParser1.parse(searchString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Query query_bt = null; try { query_bt = queryParser2.parse(bd); }
		 * catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } Query query_et = null; try { query_et =
		 * queryParser3.parse(ed); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		// Term term = new Term("Begin_date", bd);
		// Query query_bt = new TermQuery(term);

		// TermQuery tq = new TermQuery(new Term("start_date", bd));
		// Filter cf = new QueryWrapperFilter(tq);

		TopDocs hits = indexSearcher.search(query, 10000);
		// TopDocs hits_bt = indexSearcher.search(query_bt,10000);
		// TopDocs hits_et = indexSearcher.search(query_et,10000);
		System.out.println("Number of hits: " + hits.totalHits);
		// System.out.println("Number of hits: " + hits_bt.totalHits);
		// System.out.println("Number of hits: " + hits_et.totalHits);
		// ScoreDoc[] top_bt = hits_bt.scoreDocs;
		ScoreDoc[] top = hits.scoreDocs;
		// ScoreDoc[] top_et = hits_bt.scoreDocs;
		// ScoreDoc[] intersect;
		// retrieve each matching document from the ScoreDoc array
		/*
		 * ListOrderedSet<Integer> beg_d = new ListOrderedSet<Integer>();
		 * ListOrderedSet<Integer> end_d = new ListOrderedSet<Integer>();
		 * ListOrderedSet<Integer> cont = new ListOrderedSet<Integer>(); for
		 * (int i = 0; i < top_bt.length&& i<top.length&&i<top_et.length; i++) {
		 * int id1 = top_bt[i].doc; beg_d.add(id1); int id2 = top_et[i].doc;
		 * end_d.add(id2); int id3 = top[i].doc; cont.add(id3); }
		 * Iterator<Integer> it = cont.iterator(); while(it.hasNext()) { int id
		 * = it.next(); if(!beg_d.contains(id)&& !end_d.contains(id)) {
		 * it.remove(); } } System.out.println(cont.size()); Iterator<Integer>
		 * it1 = cont.iterator();
		 */
		String res = "";

		int n = 1;
		Process proc;
		while (n < 11 && n <= hits.totalHits) {
			// int id = it1.next();
			// System.out.println(n+" "+hits.totalHits);
			int id = top[n - 1].doc;
			// System.out.println(id);
			Document txt = indexReader.document(id);
			res.concat(txt.get("headline"));
			// System.out.println("----------------------------------------------------------------");
			String f = txt.get("url");
			// proc = Runtime.getRuntime().exec("cmd.exe /c start iexplore "+
			// f);
			char arr[] = f.toCharArray();
			String filename = "";
			for (int i = 0; i < arr.length; i++) {
				if ((int) arr[i] >= (int) '0' && (int) arr[i] <= (int) '9') {
					filename = f.substring(i, f.length());
					break;
				}
			}
			System.out.println(n + " " + filename);
			/*
			 * String f1 = txt.get("Begin_date"); System.out.println(
			 * "Begin date " + f1); String f2 = txt.get("End_date");
			 * System.out.println("End Date " +f2); String f3 =
			 * txt.get("contents"); System.out.println("The body " +f3);
			 */
			n++;
		}
		return res;
		/*
		 * for (int i = 0; i < top_bt.length; i++) { int id = top_bt[i].doc;
		 * Document txt = indexReader.document(id); String hotelName =
		 * txt.get("Begin_date"); System.out.println(hotelName); }
		 * 
		 * /*Document txt = indexReader.document(id); String hotelName =
		 * txt.get("Begin_date"); System.out.println(hotelName); }
		 */
	}

	public void extract(AnnotationPipeline pipeline) throws IOException, ParseException {

		NYTCorpusDocument timesDocument;
		String ft;
		String text_s;
		// Document d;
		Date dt;
		NYTCorpusDocumentParser parser = new NYTCorpusDocumentParser();
		File[] directoryListing = dir.listFiles();
		BufferedWriter bw = new BufferedWriter(new FileWriter("SpatialFile.txt"));
		spatial = new HashMap<String, Obj_spat>();
		int nFiles = 100;

		if (directoryListing != null) {
			for (File child : directoryListing) {
				Obj_set obj = new Obj_set();
				Obj_spat obj1 = new Obj_spat();
				timesDocument = parser.parseNYTCorpusDocumentFromFile(child, false);
				String text_h;
				text_s = timesDocument.getBody();
				ft = text_s;
				String path = child.getCanonicalPath();

				char arr[] = path.toCharArray();
				String filename = "";
				for (int i = 0; i < arr.length; i++) {
					if ((int) arr[i] >= (int) '0' && (int) arr[i] <= (int) '9') {
						filename = path.substring(i, path.length());
						break;
					}
				}

				try {
					text_h = timesDocument.getHeadline();
					ft = text_s.concat(text_h);
				} catch (Exception e) {
					System.out.println("Exception in headline");
					text_h = text_s.substring(0, 50);
				}
				dt = timesDocument.getPublicationDate();
				// System.out.println(text_s);
				long startTime = System.currentTimeMillis();
				obj = new ext_temp(dt, ft).ext(pipeline, 3);
				obj1 = new ext_spat(ft).ext(coordinates, 1);
				long endTime = System.currentTimeMillis();

				spatial.put(filename, obj1);
				// System.out.println(obj.beg_date);
				// System.out.println(obj.end_date);
				createIndex(text_s, text_h, obj, obj1, path, bw);
				System.out.println("Total execution time: " + (endTime - startTime) + "ms");
				nFiles--;
				if (nFiles <= 0)
					break;

			}
		}
	}

	public String searchstr(String q, AnnotationPipeline pipeline) throws IOException, ParseException {
		// String q = "humana inc company reports Nov 30 1986";
		String list;
		Date d = new Date();
		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String dt = sdf.format(d);
		int k = 3;
		while (k >= 1) {
			o = new ext_temp(d, q).ext(pipeline, k);
			k--;
			if (!o.beg_date.isEmpty())
				break;
		}
		
		os = new ext_spat(q).ext(coordinates, 1);
		
		/*
		 * for(CoreMap cm : o.c) { q = q.replace(cm.toString(),""); }
		 */
		String b_date = "";
		String e_date = "";
		System.out.println(q);
		System.out.println(o.beg_date);
		System.out.println(o.end_date);
		Iterator<String> i1 = o.beg_date.iterator();
		Iterator<String> i2 = o.end_date.iterator();
		while (i1.hasNext()) {
			b_date = b_date.concat(" " + i1.next());
		}
		while (i2.hasNext()) {
			e_date = e_date.concat(" " + i2.next());
		}
		
		String lat = "";
		String lon = "";
		System.out.println(os.lat);
		System.out.println(os.lon);
		Iterator<String> itr1 = os.lat.iterator();
		Iterator<String> itr2 = os.lon.iterator();
		while (itr1.hasNext()) {
			lat = lat.concat(" " + itr1.next());
		}
		while (itr2.hasNext()) {
			lon = lon.concat(" " + itr2.next());
		}
		
		
		
		
		// System.out.print(i1.next());
		// System.out.print(i1.toString());
		// System.out.print(i2.next());
		if (o.beg_date.isEmpty()) {
			list = searchIndex(q, dt, dt, lat, lon);
		} else {
			list = searchIndex(q, b_date, e_date, lat, lon);
		}
		return list;
		// searchIndex(q,i1.next().toString(),i2.next());

	}
}
