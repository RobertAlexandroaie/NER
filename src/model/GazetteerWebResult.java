/**
 * 
 */
package model;

/**
 * @author Robert
 */
public class GazetteerWebResult implements Comparable<GazetteerWebResult> {
    private String name;
    private String type;
    private String subtype;
    private int score = 0;

    public GazetteerWebResult(String name, String subtype) {
	super();
	this.name = name;
	this.subtype = subtype;
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

    /**
     * @return the score
     */
    public int getScore() {
	return score;
    }

    /**
     * @param score
     *            the score to set
     */
    public void setScore(int score) {
	this.score = score;
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

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(GazetteerWebResult o) {
	return score - o.getScore();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "[" + name + ", " + type  + ", " + subtype + ", " + score + "]";
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + score;
	result = prime * result + ((subtype == null) ? 0 : subtype.hashCode());
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
	GazetteerWebResult other = (GazetteerWebResult) obj;

	if (subtype == null) {
	    if (other.subtype != null) return false;
	} else if (!subtype.equals(other.subtype)) return false;
	return true;
    }

}
