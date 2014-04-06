/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Catalina
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "W")
class W implements Comparable<W> {

    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "LEMMA")
    private String LEMMA;
    @XmlAttribute(name = "POS")
    private String POS;
    @XmlAttribute(name = "Type")
    private String Type;
    @XmlAttribute(name = "Value")
    private String Value;

    private String idSentence;
    private String idWord;

    public W() {
    }

    public W(String id, String LEMMA, String POS, String Type) {
	this.id = id;
	this.LEMMA = LEMMA;
	this.POS = POS;
	this.Type = Type;

    }

    public void setid(String id) {
	this.id = id;
    }

    public void setLEMMA(String LEMMA) {
	this.LEMMA = LEMMA;
    }

    public void setPOS(String POS) {
	this.POS = POS;
    }

    public void setType(String Type) {
	this.Type = Type;
    }

    public void setidSentence(String idSentence) {
	this.idSentence = idSentence;
    }

    public void setidWord(String idWord) {
	this.idWord = idWord;
    }

    public void setidSentenceAndidWord() {
	String[] parts = this.id.split("\\.");
	this.idSentence = parts[0];
	this.idWord = parts[1];
    }

    public String getid() {
	return id;
    }

    public String getLEMMA() {
	return LEMMA;
    }

    public String getPOS() {
	return POS;
    }

    public String getType() {
	return Type;
    }

    public String getidSentence() {
	return idSentence;
    }

    public String getidWord() {
	return idWord;
    }

    /**
     * @return the value
     */
    public String getValue() {
	return Value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
	Value = value;
    }

    @Override
    public String toString() {

	return "W[" + id + ", " + ", " + LEMMA + ", " + POS + ", " + Type + ']';
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((LEMMA == null) ? 0 : LEMMA.hashCode());
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	setidSentenceAndidWord();
	if (this == obj) return true;
	if (obj == null) return false;
	if (getClass() != obj.getClass()) return false;
	W other = (W) obj;
	if (LEMMA == null) {
	    if (other.LEMMA != null) return false;
	} else if (!LEMMA.equals(other.LEMMA)) return false;
	if (id == null) {
	    if (other.id != null) return false;
	} else if (!id.equals(other.id)) return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(W o) {
	setidSentenceAndidWord();
	o.setidSentenceAndidWord();
	int thisSID = Integer.parseInt(this.getidSentence());
	int thisWinSID = Integer.parseInt(this.getidWord());
	int oSID = Integer.parseInt(o.getidSentence());
	int oWinSID = Integer.parseInt(o.getidWord());

	int possibleReturn = thisSID - oSID;
	if (possibleReturn != 0) {
	    return possibleReturn;
	} else {
	    return thisWinSID - oWinSID;
	}
    }

}
