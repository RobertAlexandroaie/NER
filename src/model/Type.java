/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Robert
 */
public class Type {
    private String name;
    private ArrayList<String> subtypes;

    public Type(String name) {
	this.name = name;
	subtypes = new ArrayList<String>();
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
	this.name = name;
    }

    /**
     * @return the subtypes
     */
    public ArrayList<String> getSubtypes() {
	return subtypes;
    }

    /**
     * @param subtypes
     *            the subtypes to set
     */
    public void setSubtypes(ArrayList<String> subtypes) {
	this.subtypes = subtypes;
    }

    public boolean containsSubtype(String subtype) {
	return subtypes.contains(subtype);
    }

    public boolean add(String subtype) {
	return this.subtypes.add(subtype);
    }

    public boolean addArray(String[] subtypes) {
	return this.subtypes.addAll(Arrays.asList(subtypes));
    }
}
