package rest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.time.SUTime.Range;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;

public class ext_temp {
	Date dt;	
	String text_s;
	List<CoreMap> timexAnnsAll;

	ext_temp(Date d, String s) {
		dt = d;
		text_s = s;
	}

	public Obj_set ext(AnnotationPipeline pipeline, int k) {
		
		Set<String> beg_date = new HashSet<String>();
		Set<String> end_date = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String[] words = text_s.split("\\s+");
		Range r;
		for (int i = 1; i < words.length - (k - 1); i += k) {
			String builder = words[i];
			for (int j = 1; j < k; j++) {
				builder += (" " + words[i + j]);
			}
			
			Annotation annotation = new Annotation(builder.toString());
			annotation.set(CoreAnnotations.DocDateAnnotation.class, sdf.format(dt));
			pipeline.annotate(annotation);
			timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
			for (CoreMap cm : timexAnnsAll) {
				try {
					r = cm.get(TimeExpression.Annotation.class).getTemporal().getRange();
					if (r != null) {
						if (r.beginTime() != null) {
							beg_date.add(r.beginTime().toString());
							end_date.add(r.endTime().toString());
						}
					}
				} catch (Exception ne) {
					System.out.print("Duh");
					continue;
				}
			}
		}
		Obj_set o = new Obj_set(beg_date, end_date, timexAnnsAll);
		
		return o;
	}
}
