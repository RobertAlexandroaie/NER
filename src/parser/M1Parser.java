/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Robert
 */
public class M1Parser {

    ArrayList<Entity> entities = new ArrayList<Entity>();
    List<S> sentences = new ArrayList<S>();

    public void extractEntities(Document document) {
	HashMap<S, LinkedList<W>> wordsOfSentence = new HashMap<>();

	LinkedList<W> words = new LinkedList<W>();
	LinkedList<NP> nounPhrases = new LinkedList<NP>();
	LinkedList<HEAD> heads = new LinkedList<HEAD>();

	if (document.getSentences() != null && !document.getSentences().isEmpty()) {
	    sentences = document.getSentences();
	}

	Collections.sort(sentences);

	for (S sentence : sentences) {
	    words = new LinkedList<>();

	    nounPhrases.addAll(getNounPhrasesFromS(sentence));
	    heads.addAll(getHeadsFromNounPhrases(nounPhrases));

	    words.addAll(sentence.getWords());
	    words.addAll(getWordsFromNounPhrases(nounPhrases));
	    words.addAll(getWordsFromHeads(heads));
	    Collections.sort(words);
	    wordsOfSentence.put(sentence, words);
	}

	buildEntities(wordsOfSentence);
	Collections.sort(entities);
    }

    private void buildEntities(HashMap<S, LinkedList<W>> wordsOfSentence) {
	boolean isProperNoun = false;
	boolean hasCapitalLetter = false;
	boolean isPunctuation = false;
	Entity entity;
	for (S sentence : sentences) {
	    LinkedList<W> words = wordsOfSentence.get(sentence);
	    ArrayList<String> sentenceOfEntity = getWordsList(words);

	    W word1 = words.get(0);
	    W word2;

	    String lemma = word1.getLEMMA();
	    String pos = word1.getPOS();
	    String type = word1.getType();
	    String value = word1.getValue();
	    String precLemma;

	    isProperNoun = isProperNoun(lemma, pos, type);
	    hasCapitalLetter = hasCapitalLetter(value);

	    if (isProperNoun) {
		entity = new Entity(lemma, word1.getid(), sentenceOfEntity);
		if (!entities.contains(entity)) {
		    entities.add(entity);
		}
	    }

	    for (int i = 0; i < words.size() - 1; i++) {
		word1 = words.get(i);
		word2 = words.get(i + 1);

		precLemma = word1.getLEMMA();
		lemma = word2.getLEMMA();
		type = word2.getType();
		pos = word2.getPOS();
		value = word2.getValue();

		isProperNoun = isProperNoun(lemma, pos, type);
		hasCapitalLetter = hasCapitalLetter(value);
		isPunctuation = isPunctuation(precLemma);

		if (isProperNoun || (isPunctuation && hasCapitalLetter)) {
		    entity = new Entity(lemma, word2.getid(), sentenceOfEntity);
		    if (!entities.contains(entity)) {
			entities.add(entity);
		    }
		}
	    }
	}
    }

    private boolean isPunctuation(String lemma) {
	if (lemma.contains(".!,?\"'„”") && lemma.length() == 1) {
	    return true;
	} else {
	    return false;
	}
    }

    private boolean isProperNoun(String lemma, String pos, String type) {
	return type != null && pos != null && pos.trim().toLowerCase().equals("noun") && type.trim().toLowerCase().equals("proper");
    }

    private boolean hasCapitalLetter(String value) {
	if (value != null) {
	    char[] letters = value.toCharArray();
	    for (int i = 0; i < letters.length; i++) {
		char c = letters[i];
		if (Character.isAlphabetic(c)) {
		    if (Character.isUpperCase(c) && !value.matches("[XILVMC-]*")) {
			return true;
		    } else {
			return false;
		    }
		}
	    }
	}
	return false;
    }

    private ArrayList<String> getWordsList(LinkedList<W> words) {
	ArrayList<String> sentence = new ArrayList<>();
	for (W word : words) {
	    sentence.add(word.getLEMMA() + " ");
	}
	return sentence;
    }

    private LinkedList<NP> getNounPhrasesFromS(S sentence) {
	List<NP> nounPhrases = sentence.getNounPhrases();
	if (nounPhrases != null && !nounPhrases.isEmpty()) {
	    LinkedList<NP> NPQueue = new LinkedList<NP>();
	    NPQueue.addAll(nounPhrases);
	    LinkedList<NP> NPFullQueue = new LinkedList<NP>();
	    NPFullQueue.addAll(nounPhrases);

	    while (!NPQueue.isEmpty()) {
		NP poll = NPQueue.poll();
		addNounPhrasesToQueue(poll.getNounPhrases(), NPQueue);
		addNounPhrasesToQueue(poll.getNounPhrases(), NPFullQueue);
	    }

	    return NPFullQueue;
	}
	return null;
    }

    private LinkedList<W> getWordsFromNounPhrases(LinkedList<NP> nounPhrases) {
	LinkedList<W> WQueue = new LinkedList<W>();
	if (nounPhrases != null && !nounPhrases.isEmpty()) {
	    for (NP nounPhrase : nounPhrases) {
		addWordsToQueue(nounPhrase.getWords(), WQueue);
	    }
	}
	return WQueue;
    }

    private LinkedList<W> getWordsFromHeads(LinkedList<HEAD> heads) {
	LinkedList<W> WQueue = new LinkedList<W>();
	if (heads != null && !heads.isEmpty()) {
	    for (HEAD head : heads) {
		addWordsToQueue(head.getWords(), WQueue);
	    }
	}
	return WQueue;

    }

    private LinkedList<HEAD> getHeadsFromNounPhrases(LinkedList<NP> nounPhrases) {
	LinkedList<HEAD> HEADQueue = new LinkedList<HEAD>();
	if (nounPhrases != null && !nounPhrases.isEmpty()) {
	    for (NP nounPhrase : nounPhrases) {
		addHeadsToQueue(nounPhrase.getHeads(), HEADQueue);
	    }
	}
	return HEADQueue;

    }

    private void addHeadsToQueue(List<HEAD> source, LinkedList<HEAD> dest) {
	if (source != null && !source.isEmpty()) {
	    for (HEAD head : source) {
		if (!dest.contains(head)) {
		    dest.add(head);
		}
	    }
	}
    }

    private void addNounPhrasesToQueue(List<NP> source, LinkedList<NP> dest) {
	if (source != null && !source.isEmpty()) {
	    for (NP nounPhrase : source) {
		if (!dest.contains(nounPhrase)) {
		    dest.add(nounPhrase);
		}
	    }
	}
    }

    private void addWordsToQueue(List<W> source, LinkedList<W> dest) {
	if (source != null && !source.isEmpty()) {
	    for (W word : source) {
		if (!dest.contains(word)) {
		    dest.add(word);
		}
	    }
	}
    }

    /**
     * @return the entities
     */
    public ArrayList<Entity> getEntities() {
	return entities;
    }

    /**
     * @param entities
     *            the entities to set
     */
    public void setEntities(ArrayList<Entity> entities) {
	this.entities = entities;
    }
}
