package gazetteer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.GazetteerWebResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

public class GazetteerWeb {
    /** API Key for the registered developer project for your application. */
    private static final String API_KEY = "AIzaSyAw1L4Y3V5PdySRuFKiqTsfERt_N13-skw";

    private ArrayList<GazetteerWebResult> results;
    private ArrayList<String> restricted = new ArrayList<>();

    public GazetteerWeb() {
	super();
	results = new ArrayList<GazetteerWebResult>();
	initRestricted();
    }

    private void initRestricted() {
	restricted.add("monarh");
	restricted.add("deceda");
	restricted.add("fotbal");
	restricted.add("disc");
	restricted.add("audio");
	restricted.add("televiziune");
	restricted.add("specie");
    }

    public String[] search(String name) {
	String[] resultArray = new String[10];
	int i = 0;
	try {
	    HttpTransport httpTransport = new NetHttpTransport();
	    HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
	    JSONParser parser = new JSONParser();
	    GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/search");
	    url.put("query", name);
	    url.put("lang", "ro");
	    url.put("limit", "10");
	    url.put("indent", "true");
	    url.put("key", API_KEY);
	    HttpRequest request = requestFactory.buildGetRequest(url);
	    HttpResponse httpResponse = request.execute();
	    JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
	    JSONArray results = (JSONArray) response.get("result");
	    for (Object result : results) {
		try {
		    // verify if the result contains a notable subtype
		    JsonPath.read(result, "$notable.name");
		    // store the subtype in the string array
		    resultArray[i] = JsonPath.read(result, "$notable.name");
		    i++;
		} catch (com.jayway.jsonpath.InvalidPathException e) {
		    // no notable subtype
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return clean(resultArray);
    }

    public static String[] clean(final String[] v) {
	List<String> list = new ArrayList<String>(Arrays.asList(v));
	list.removeAll(Collections.singleton(null));
	return list.toArray(new String[list.size()]);
    }

    public void searchFor(String name) {
	String value = name.replaceAll("~", " ");
	String[] subtypes = search(value);
	if (subtypes.length >= 1) {
	    for (String subtype : subtypes) {
		addResult(name, subtype);
	    }
	} else {
	    System.out.println("no entity found with the specified name");
	}
    }

    /**
     * @return the results
     */
    public ArrayList<GazetteerWebResult> getResults() {
	return results;
    }

    /**
     * 
     */
    public void addResult(String name, String subtype) {
	String[] subtypes = subtype.split("/");
	for (int i = 0; i < subtypes.length; i++) {
	    if (isNotRestricted(subtypes[i])) {
		String filtered = filter(subtypes[i]);
		GazetteerWebResult newResult = new GazetteerWebResult(name, filtered.trim().toLowerCase());
		if (!results.contains(newResult)) {
		    results.add(newResult);
		}
	    }
	}
    }

    public void dummyScore() {
	if (results != null && !results.isEmpty()) {
	    int maxScore = results.size();
	    for (GazetteerWebResult result : results) {
		result.setScore(maxScore - results.indexOf(result));
	    }
	}
    }

    private boolean isNotRestricted(String subtype) {
	for (String restriction : restricted) {
	    if (subtype.toLowerCase().contains(restriction)) {
		return false;
	    }
	}
	return true;
    }

    private String filter(String string) {
	char[] chars = string.toCharArray();
	for (int i = 0; i < chars.length; i++) {
	    switch (chars[i]) {
	    case 'ş':
		chars[i] = 'ș';
		break;
	    case 'ţ':
		chars[i] = 'ț';
		break;
	    case 'ă':
		chars[i] = 'ă';
		break;
	    }
	}
	String filtered = new String(chars);
	return filtered;
    }

}
