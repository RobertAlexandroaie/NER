/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Adina
 */
public class Entity implements Comparable<Entity> {

    private String name;
    private ArrayList<String> sentence;
    private String id;
    private String type;
    private String subtype;

    public Entity(String name, String id, ArrayList<String> sentence) {
	this.id = id;
	setName(name);
	type = "altele";
	subtype = "altele";
	this.sentence = sentence;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	char[] nameChars = name.toCharArray();
	nameChars[0] = Character.toUpperCase(nameChars[0]);
	this.name = new String(nameChars);
    }

    /**
     * @return the sentence
     */
    public List<String> getSentence() {
	return sentence;
    }

    /**
     * @param sentence
     *            the sentence to set
     */
    public void setSentence(ArrayList<String> sentence) {
	this.sentence = sentence;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    @Override
    public String toString() {
	return "E[" + id + ", " + name + "]";
    }

    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the subtype
     */
    public String getSubtype() {
	return subtype;
    }

    /**
     * @param subtype
     *            the subtype to set
     */
    public void setSubtype(String subtype) {
	this.subtype = subtype;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Entity o) {
	String oID = o.getId();

	String[] thisIDs = id.split(Pattern.quote("."));
	String[] oIDs = oID.split(Pattern.quote("."));

	int possibleReturn = Integer.parseInt(thisIDs[0]) - Integer.parseInt(oIDs[0]);
	if (possibleReturn != 0) {
	    return possibleReturn;
	} else {
	    return Integer.parseInt(thisIDs[1]) - Integer.parseInt(oIDs[1]);
	}
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) return true;
	if (obj == null) return false;
	if (getClass() != obj.getClass()) return false;
	Entity other = (Entity) obj;
	if (id == null) {
	    if (other.id != null) return false;
	} else if (!id.equals(other.id)) return false;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name)) return false;
	return true;
    }

}
