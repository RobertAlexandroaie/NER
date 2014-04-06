/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adina
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEAD")
public class HEAD {

    @XmlElement(name = "W")
    private List<W> words;

    public HEAD() {
    }

    public HEAD(List<W> words) {
	this.words = words;
    }

    /**
     * @return the words
     */
    public List<W> getWords() {
	return words;
    }

    /**
     * @param ws
     *            the words to set
     */
    public void setWords(List<W> words) {
	this.words = words;
    }

    @Override
    public String toString() {
	return "\n" + "  HEAD" + "     W=" + words;
    }
}
