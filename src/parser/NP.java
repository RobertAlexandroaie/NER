/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
// import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Catalina
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NP")
class NP {

    @XmlElement(name = "NP")
    private List<NP> nounPhrases;
    @XmlElement(name = "W")
    private List<W> words;
    @XmlElement(name = "HEAD")
    private List<HEAD> heads;

    public NP() {
    }

    public NP(List<NP> nps, List<W> ws, List<HEAD> hs) {
	this.nounPhrases = nps;
	this.words = ws;
	this.heads = hs;
    }

    public void setNounPhrases(List<NP> nps) {
	this.nounPhrases = nps;
    }

    public void setWords(List<W> ws) {
	this.words = ws;
    }

    public List<W> getWords() {
	return words;
    }

    /**
     * @return the hs
     */
    public List<HEAD> getHeads() {
	return heads;
    }

    /**
     * @param hs
     *            the hs to set
     */
    public void setHeads(List<HEAD> hs) {
	this.heads = hs;
    }

    /**
     * @return the nps
     */
    public List<NP> getNounPhrases() {
	return nounPhrases;
    }

    @Override
    public String toString() {
	// return "\n" + "     NP{" + "\n" + "nps=" + nps + "\n" + "hs=" + hs +
	// "\n" + ", ws=" + ws + '}';
	return "\n" + "     NP" + "\n" + "nps=" + nounPhrases + "\n" + "head=" + heads + "\n" + ", ws=" + words;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((heads == null) ? 0 : heads.hashCode());
	result = prime * result + ((nounPhrases == null) ? 0 : nounPhrases.hashCode());
	result = prime * result + ((words == null) ? 0 : words.hashCode());
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
	NP other = (NP) obj;
	if (heads == null) {
	    if (other.heads != null) return false;
	} else if (!heads.equals(other.heads)) return false;
	if (nounPhrases == null) {
	    if (other.nounPhrases != null) return false;
	} else if (!nounPhrases.equals(other.nounPhrases)) return false;
	if (words == null) {
	    if (other.words != null) return false;
	} else if (!words.equals(other.words)) return false;
	return true;
    }

}
