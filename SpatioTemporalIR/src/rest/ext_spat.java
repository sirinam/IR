package rest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ext_spat {
	String text_s;

	ext_spat(String s) {
		text_s = s;
	}

	public Obj_spat ext(HashMap<String, String> hm, int k) {
		long startTime = System.currentTimeMillis();
		List<String> lat = new ArrayList<String>();
		List<String> lon = new ArrayList<String>();

		String[] words = text_s.split("\\s+");
		for (int i = 1; i < words.length - (k - 1); i += k) {
			String builder = words[i].toLowerCase();
			if (hm.containsKey(builder)) {
				String s[] = hm.get(builder).split(" ");
				lat.add(s[0]);
				lon.add(s[1]);
			}

		}
		
		// Add Lat Lon of NYT Headquarters incase no spatial cue is found
		if(lat.isEmpty())
			lat.add("40.7128");
		if(lon.isEmpty())
			lon.add("74.0059");
		
		System.out.println("lat :" + lat);
		System.out.println("lon :" + lon);
		
		Obj_spat o = new Obj_spat(lat, lon);
		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) + "ms");
		return o;

	}
}
