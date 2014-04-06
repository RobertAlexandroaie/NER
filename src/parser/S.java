/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Catalina
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "S")
class S implements Comparable<S> {

    @XmlElement(name = "W")
    private List<W> words;
    @XmlElement(name = "NP")
    private List<NP> nounPhrases;
    @XmlAttribute(name = "id")
    private String id;

    public S() {
    }

    public S(List<W> ws, List<NP> nps, String id) {
	this.words = ws;
	this.nounPhrases = nps;
	this.id = id;
    }

    public void setWords(List<W> ws) {
	this.words = ws;
    }

    public void setNounPhrases(List<NP> nps) {
	this.nounPhrases = nps;
    }

    public void setid(String id) {
	this.id = id;
    }

    public List<W> getWords() {
	return words;
    }

    public List<NP> getNounPhrases() {
	return nounPhrases;
    }

    public String getid() {
	return id;
    }

    @Override
    public String toString() {
	// return "\n" + "  S{" + "\n" + ", id=" + id + ",ws=" + ws + "\n" +
	// ", nps=" + nps + '}';
	return "\n" + "  S cu id=" + id + "\n" + "lista de W ale S cu id=" + id + "\n" + words + "\n\n" + "lista de NP ale S cu id=" + id + "\n"
		+ nounPhrases;
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
	S other = (S) obj;
	if (id == null) {
	    if (other.id != null) return false;
	} else if (!id.equals(other.id)) return false;
	if (nounPhrases == null) {
	    if (other.nounPhrases != null) return false;
	} else if (!nounPhrases.equals(other.nounPhrases)) return false;
	if (words == null) {
	    if (other.words != null) return false;
	} else if (!words.equals(other.words)) return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(S o) {
	int thisID = Integer.parseInt(id);
	int oID = Integer.parseInt(o.getid());
	return thisID - oID;
    }

}
