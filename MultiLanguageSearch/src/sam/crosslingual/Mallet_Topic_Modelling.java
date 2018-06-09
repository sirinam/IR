package sam.crosslingual;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Mallet_Topic_Modelling 
{
	
	
	public static HashMap<String,ArrayList<Double>> makeDoc_topic_metric(String doc_topic_path) throws Exception
	{
		HashMap<String,ArrayList<Double>> hm=new HashMap<>();
		BufferedReader br=new BufferedReader(new FileReader(doc_topic_path));
		String line=null;
		
		while((line=br.readLine())!=null)
		{
			String values[]=line.split(" ");
			ArrayList<Double> topics=new ArrayList<>();
			
			for(int i=2;i<values.length;i++)
			{
				topics.add(Double.parseDouble(values[i]));
			}
			//System.out.println("File path: "+values[0]);
			hm.put(values[1].substring(6), topics);
		}
		br.close();
		return hm;	
	}
	
	public static HashMap<Integer,ArrayList<String>> makeTopic_key_metric(String topic_key_path) throws Exception 
	{
		HashMap<Integer,ArrayList<String>> hm=new HashMap<Integer,ArrayList<String>>();
		BufferedReader br=new BufferedReader(new FileReader(topic_key_path));
		String line=null;
		
		while((line=br.readLine())!=null)
		{
			String values[]=line.split(" ");
			ArrayList<String> keys=new ArrayList<String>();
			
			for(int i=2;i<values.length;i++)
			{
				keys.add(values[i]);
			}
			//System.out.println(values[0]);
			hm.put(Integer.parseInt(values[0]),keys);
			//System.out.println(values[0]);
			//System.out.println(keys);
			
		}
		br.close();
		return hm;
	}
	
	public static HashMap<String,ArrayList<Integer>> makeKey_topic_metric(HashMap<Integer,ArrayList<String>> hm)
	{
		HashMap<String,ArrayList<Integer>> key_topic=new HashMap<>();
		ArrayList<String> keys=new ArrayList<>();
		Set<Integer> topics=hm.keySet();
		for(Integer i:topics)
		{
			keys.addAll(hm.get(i));
		}
		System.out.println("total size "+keys.size());
		for(String s:keys)
		{
			ArrayList<Integer> all_topics=new ArrayList<>();
			for(Integer i:topics)
			{
				if(hm.get(i).contains(s))
				{
					all_topics.add(i);
				}
			}
			key_topic.put(s, all_topics);
		}
		return key_topic;
	}
	
	
}
