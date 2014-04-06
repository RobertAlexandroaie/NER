/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.GazetteerLocalResult;
import model.GazetteerWebResult;
import model.Type;
import parser.Entity;

/**
 * @author Robert
 */
public class SubtypeSelector {
    private static HashMap<String, ArrayList<Pattern>> firstLevelPatterns;
    private static HashMap<String, ArrayList<Pattern>> secondLevelPatterns;
    private static Type locatie = new Type("locație");
    private static Type persoana = new Type("persoană");
    private static Type institutie = new Type("instituție");
    private static String tokenPattern = "[ăîâșțĂÎÂȘȚa-zA-Z 0-9,\"'„”`~-]*";
    private static String tokenEntityNamePattern = "#SPID#";

    private static void initTypes() {
	String[] locationSubtypes = { "stație", "campus", "oraș", "continent", "coordonate", "țară", "ţară", "baraj", "cartier", "deal",
		"intersecție", "munte", "parc", "câmpie", "județ", "municipiu", "localitate", "sat", "comună", "râu", "rîu", "fluviu", "pârâu",
		"stradă", "colină", "bulevard", "regiune", "casă memorială", "oraș", "locație", "vârf", "vf", "rezervație", "lac" };
	String[] persoanaSubtypes = { "artist", "atlet", "inginer", "președinte", "preot", "calugăr", "sfînt", "sfânt", "conducător", "domnitor",
		"prinț", "rege", "scriitor", "sportiv", "familie", "romancier", "poet", "lider religios", "politician", "autor", "actor", "militar",
		"persoană", "mitropolit" };
	String[] institutieSubtypes = { "bancă", "biserică", "primărie", "universitate", "colegiu", "guvern", "spital", "mall", "muzeu", "operă",
		"orfelinat", "filarmonică", "teatru", "facultate", "structură", "clădire", "instituție", "asociație", "cetate", "mitropolie" };

	locatie.addArray(locationSubtypes);
	persoana.addArray(persoanaSubtypes);
	institutie.addArray(institutieSubtypes);
    }

    private static void addLocalResults(ArrayList<GazetteerLocalResult> localResults, String entityName) {
	for (String subtype : locatie.getSubtypes()) {
	    GazetteerLocalResult newResult = new GazetteerLocalResult(entityName, locatie.getName(), subtype);
	    if (!localResults.contains(newResult)) {
		localResults.add(newResult);
	    }
	}
	for (String subtype : persoana.getSubtypes()) {
	    GazetteerLocalResult newResult = new GazetteerLocalResult(entityName, persoana.getName(), subtype);
	    if (!localResults.contains(newResult)) {
		localResults.add(newResult);
	    }
	}
	for (String subtype : institutie.getSubtypes()) {
	    GazetteerLocalResult newResult = new GazetteerLocalResult(entityName, institutie.getName(), subtype);
	    if (!localResults.contains(newResult)) {
		localResults.add(newResult);
	    }
	}
    }

    private static void setWebResultsType(ArrayList<GazetteerWebResult> webResults) {
	for (GazetteerWebResult webResult : webResults) {
	    String webResultSubtype = webResult.getSubtype().trim();
	    if (locatie.containsSubtype(webResultSubtype)) {
		webResult.setType(locatie.getName());
	    } else if (institutie.containsSubtype(webResultSubtype)) {
		webResult.setType(institutie.getName());
	    } else if (persoana.containsSubtype(webResultSubtype)) {
		webResult.setType(persoana.getName());
	    } else {
		webResult.setType("altele");
	    }
	}

    }

    private static String buildLocalSentence(Entity entity) {

	String[] entityId = entity.getId().split(Pattern.quote("."));
	int idOfEntityInS = Integer.parseInt(entityId[1]);
	idOfEntityInS--;
	int wordsInEntity = entity.getName().split(Pattern.quote(" ")).length;

	StringBuilder sentenceBuild = new StringBuilder();

	List<String> localSentenceWords = entity.getSentence();
	int i = 0;
	while (i < localSentenceWords.size()) {
	    if (i < idOfEntityInS || i >= idOfEntityInS + wordsInEntity) {
		sentenceBuild.append(localSentenceWords.get(i++).trim() + " ");
	    } else {
		sentenceBuild.append("#SPID# ");
		i += wordsInEntity;
	    }
	}

	String[] splitSentences = sentenceBuild.toString().split(Pattern.quote("."));
	int wordsSoFar = 0;
	for (i = 0; i < splitSentences.length; i++) {
	    String sentence = splitSentences[i];
	    wordsSoFar += sentence.split(" ").length;
	    if (wordsSoFar > idOfEntityInS) {
		return sentence;
	    }
	}
	return splitSentences[i - 1];
    }

    public static void scoreSubtypes(Entity entity, ArrayList<GazetteerWebResult> webResults, ArrayList<GazetteerLocalResult> localResults) {
	String entityName = entity.getName().trim();

	initTypes();
	addLocalResults(localResults, entityName);
	setWebResultsType(webResults);

	setPatterns(entityName);

	String localSentence = buildLocalSentence(entity);
	scoreFirstLevel(webResults, localResults, localSentence);
	scoreSecondLevel(webResults, localResults, localSentence, entityName);
    }

    private static void scoreFirstLevel(ArrayList<GazetteerWebResult> webResults, ArrayList<GazetteerLocalResult> localResults, String sentence) {
	for (GazetteerWebResult webResult : webResults) {
	    if (firstLevelPatterns.containsKey(webResult.getSubtype())) {
		ArrayList<Pattern> patternList = firstLevelPatterns.get(webResult.getSubtype());
		for (Pattern pattern : patternList) {
		    Matcher matcher = pattern.matcher(sentence);
		    if (matcher.matches()) {
			webResult.setScore(webResult.getScore() + 30);
		    }
		}
	    }
	}

	for (GazetteerLocalResult localResult : localResults) {
	    if (firstLevelPatterns.containsKey(localResult.getSubtype())) {
		ArrayList<Pattern> patternList = firstLevelPatterns.get(localResult.getSubtype());
		for (Pattern pattern : patternList) {
		    Matcher matcher = pattern.matcher(sentence);
		    if (matcher.matches()) {
			localResult.setScore(localResult.getScore() + 30);
		    }
		}
	    }
	}
    }

    private static void scoreSecondLevel(ArrayList<GazetteerWebResult> webResults, ArrayList<GazetteerLocalResult> localResults, String sentence,
	    String entityName) {
	String clearName = clear(entityName);

	for (GazetteerWebResult webResult : webResults) {
	    if (secondLevelPatterns.containsKey(webResult.getSubtype())) {
		String subtype = webResult.getSubtype();
		if (clearName.startsWith(subtype)) {
		    webResult.setScore(webResult.getScore() + 49);
		} else {
		    ArrayList<Pattern> patternList = secondLevelPatterns.get(subtype);
		    for (Pattern pattern : patternList) {
			Matcher matcher = pattern.matcher(sentence);
			if (matcher.matches()) {
			    webResult.setScore(webResult.getScore() + 50);
			}
		    }
		}
	    }
	}

	for (GazetteerLocalResult localResult : localResults) {
	    if (secondLevelPatterns.containsKey(localResult.getSubtype())) {
		String subtype = localResult.getSubtype();
		if (clearName.startsWith(subtype)) {
		    localResult.setScore(localResult.getScore() + 49);
		} else {
		    ArrayList<Pattern> patternList = secondLevelPatterns.get(localResult.getSubtype());
		    for (Pattern pattern : patternList) {
			Matcher matcher = pattern.matcher(sentence);
			if (matcher.matches()) {
			    localResult.setScore(localResult.getScore() + 50);
			}
		    }
		}
	    }
	}
    }

    /**
     * @param entityName
     * @return
     */
    private static String clear(String entityName) {
	String cleared = entityName.trim().toLowerCase().replaceAll("~", " ");
	return cleared;
    }

    private static void setPatterns(String entityName) {
	firstLevelPatterns = new HashMap<String, ArrayList<Pattern>>();
	secondLevelPatterns = new HashMap<String, ArrayList<Pattern>>();

	ArrayList<Pattern> patternList = new ArrayList<Pattern>();
	addSpecificVerbPatterns(patternList, "picta");
	addSpecificVerbPatterns(patternList, "compune");
	addSpecificWorkDonePatterns(patternList, "pictat");
	addSpecificWorkDonePatterns(patternList, "compus");
	firstLevelPatterns.put("artist", patternList);

	patternList = new ArrayList<Pattern>();
	firstLevelPatterns.put("atlet", patternList);

	patternList = new ArrayList<Pattern>();
	addSpecificVerbPatterns(patternList, "castiga");
	firstLevelPatterns.put("sportiv", patternList);

	patternList = new ArrayList<Pattern>();
	addSpecificVerbPatterns(patternList, "construi");
	addSpecificVerbPatterns(patternList, "ridica");
	addSpecificWorkDonePatterns(patternList, "construit");
	addSpecificWorkDonePatterns(patternList, "ridicat");
	firstLevelPatterns.put("inginer", patternList);

	addSecondLevelPatterns();
    }

    private static void addSpecificVerbPatterns(ArrayList<Pattern> patternList, String verb) {
	patternList.add(Pattern.compile(tokenPattern + tokenEntityNamePattern + "[ ]" + verb + tokenPattern));
    }

    private static void addSpecificWorkDonePatterns(ArrayList<Pattern> patternList, String attribute) {
	patternList.add(Pattern.compile(tokenPattern + attribute + "[ ]de[ ]" + tokenEntityNamePattern + tokenPattern));
    }

    private static void addSecondLevelPatterns() {
	ArrayList<Pattern> patternList;
	for (String subtype : locatie.getSubtypes()) {
	    patternList = secondLevelPatterns.get(subtype);
	    if (patternList == null || patternList.isEmpty()) {
		patternList = new ArrayList<>();
		addToBePatterns(patternList, subtype);

		if (subtype.equals("vârf")) {
		    addToBePatterns(patternList, "vf");
		}

		secondLevelPatterns.put(subtype, patternList);
	    }
	}

	for (String subtype : institutie.getSubtypes()) {
	    patternList = secondLevelPatterns.get(subtype);
	    if (patternList == null || patternList.isEmpty()) {
		patternList = new ArrayList<>();
		addToBePatterns(patternList, subtype);
		secondLevelPatterns.put(subtype, patternList);
	    }
	}

	for (String subtype : persoana.getSubtypes()) {
	    patternList = secondLevelPatterns.get(subtype);
	    if (patternList == null || patternList.isEmpty()) {
		patternList = new ArrayList<>();
		addToBePatterns(patternList, subtype);

		if (subtype.equals("sfânt")) {
		    addToBePatterns(patternList, "sf");
		}
		secondLevelPatterns.put(subtype, patternList);
	    }
	}
    }

    private static void addToBePatterns(ArrayList<Pattern> patternList, String subtype) {
	patternList.add(Pattern.compile(tokenPattern + subtype + "[ ]" + tokenEntityNamePattern + tokenPattern));
	patternList.add(Pattern.compile(tokenPattern + subtype + ":*[ ]" + "([ăîâșțĂÎÂȘȚa-zA-Z 0-9`~\\(\\)]*,*[ ]*și*[ ]*)* "
		+ tokenEntityNamePattern + tokenPattern));
	patternList.add(Pattern.compile(tokenPattern + tokenEntityNamePattern + tokenPattern + "fi[ ]" + subtype + tokenPattern));
    }

    /**
     * @return the locatie
     */
    public static Type getLocatie() {
	return locatie;
    }

    /**
     * @return the persoana
     */
    public static Type getPersoana() {
	return persoana;
    }

    /**
     * @return the institutie
     */
    public static Type getInstitutie() {
	return institutie;
    }
}
