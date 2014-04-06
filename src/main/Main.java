/**
 * 
 */
package main;

import gazetteer.GazetteerLocal;
import gazetteer.GazetteerWeb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import model.GazetteerLocalResult;
import model.GazetteerWebResult;
import parser.Document;
import parser.Entity;
import parser.M1Parser;
import parser.JAXBXMLHandler;
import util.SubtypeSelector;

/**
 * @author Robert
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
	if (args.length == 3 && args[0] != null && args[1] != null && args[2] != null) {

	    String input = args[0];
	    String output = args[1];
	    String path = args[2];
	    
	    ArrayList<GazetteerLocalResult> localResults = null;
	    ArrayList<GazetteerWebResult> webResults = null;
	    M1Parser parser = null;
	    BufferedReader reader = null;
	    BufferedWriter writer = null;

	    try {
		Document document = JAXBXMLHandler.unmarshal(new File(input));
		parser = new M1Parser();
		parser.extractEntities(document);

		ArrayList<Entity> entityList = parser.getEntities();
		ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();

		refineEntityList(entityList, entitiesToRemove);

		for (Entity entity : entityList) {
		    String entityName = entity.getName();

		    GazetteerLocal localSearch = new GazetteerLocal(entityName, path);
		    localResults = localSearch.getResults();

		    GazetteerWeb webSearch = new GazetteerWeb();
		    webSearch.searchFor(entityName);
		    webSearch.dummyScore();
		    webResults = webSearch.getResults();

		    selectTypeAndSubtype(entity, localResults, webResults);
		    System.out.println(entity.getName() + " " + entity.getType() + " " + entity.getSubtype());
		}

		reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));

		writeEntityList(entityList, reader, writer);
	    } catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		try {
		    if (writer != null) {
			writer.close();
		    }
		    if (reader != null) {
			reader.close();
		    }
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }

    private static void compactEntities(ArrayList<Entity> list, int start, int stop) {
	Entity ent = list.get(start);
	for (Entity entity : list.subList(start + 1, stop + 1)) {
	    ent.setName(ent.getName().trim() + "~" + entity.getName().trim());
	}
    }

    private static void refineEntityList(ArrayList<Entity> entityList, ArrayList<Entity> entitiesToRemove) {
	int i = 0, j = 1;
	int start = 0;

	while (j < entityList.size()) {
	    Entity ent1 = entityList.get(i);
	    Entity ent2 = entityList.get(j);

	    String[] ids1 = ent1.getId().trim().split(Pattern.quote("."));
	    String[] ids2 = ent2.getId().trim().split(Pattern.quote("."));
	    int idW1 = Integer.parseInt(ids1[1]);
	    int idW2 = Integer.parseInt(ids2[1]);
	    int idS1 = Integer.parseInt(ids1[0]);
	    int idS2 = Integer.parseInt(ids2[0]);
	    if (idS1 == idS2 && Math.abs(idW1 - idW2) == 1) {
		j++;
		i++;
	    } else {
		compactEntities(entityList, start, i);
		entitiesToRemove.addAll(entityList.subList(start + 1, i + 1));
		i = j;
		j++;
		start = i;
	    }
	}
	entityList.removeAll(entitiesToRemove);
    }

    private static void selectTypeAndSubtype(Entity entity, ArrayList<GazetteerLocalResult> localResults, ArrayList<GazetteerWebResult> webResults) {
	SubtypeSelector.scoreSubtypes(entity, webResults, localResults);
	Collections.sort(localResults);
	Collections.sort(webResults);

	GazetteerLocalResult bestLocalResult = null;
	GazetteerWebResult bestWebResult = null;

	int bestLocalScore = 0;
	if (localResults.size() > 0) {
	    bestLocalResult = localResults.get(localResults.size() - 1);
	    bestLocalScore = bestLocalResult.getScore();
	}

	int bestWebScore = 0;
	if (webResults.size() > 0) {
	    bestWebResult = webResults.get(webResults.size() - 1);
	    bestWebScore = bestWebResult.getScore();
	}

	if (bestLocalScore > 0 && bestLocalScore >= bestWebScore && bestLocalResult != null) {
	    entity.setType(bestLocalResult.getType());
	    entity.setSubtype(bestLocalResult.getSubtype());
	} else {
	    if (bestWebResult != null) {
		entity.setType(bestWebResult.getType());
		entity.setSubtype(bestWebResult.getSubtype());
	    }
	}
    }

    private static void writeEntityList(ArrayList<Entity> entityList, BufferedReader reader, BufferedWriter writer) throws IOException {
	while (true) {
	    String line = reader.readLine();
	    if (line != null && !line.isEmpty() && line.trim().equals("</POS_Output>")) {
		writer.write("\t<ENTITIES>");
		writer.newLine();

		for (Entity entity : entityList) {
		    String entityValue = "";
		    entityValue += "\t\t<ENTITY ID=\"";
		    entityValue += entityList.indexOf(entity) + 1;
		    entityValue += "\" REF=\"";
		    entityValue += entity.getId();
		    entityValue += "\" TYPE=\"";
		    entityValue += entity.getType();
		    entityValue += "\" SUBTYPE=\"";
		    entityValue += entity.getSubtype();
		    entityValue += "\">";
		    entityValue += entity.getName();
		    entityValue += "</ENTITY>";
		    writer.write(entityValue);
		    writer.newLine();
		}
		writer.write("\t</ENTITIES>");
		writer.newLine();
		writer.write("</POS_Output>");
		writer.newLine();
		break;
	    } else {
		writer.write(line);
		writer.newLine();
	    }
	}
	writer.flush();
    }
}
