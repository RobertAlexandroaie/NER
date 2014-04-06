/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "POS_Output")
public class Document {

    @XmlElement(name = "S")
    private List<S> sentencess;

    //Must have no-argument constructor
    public Document() {
    }

    public Document(List<S> ss) {
        this.sentencess = ss;

    }

    public void setSentencess(List<S> ss) {
        this.sentencess = ss;
    }

    public List<S> getSentences() {
        return sentencess;
    }

    @Override
    public String toString() {
        //return "\n" + "POS_Output{" + "\n" + "ss=" + ss + '}';
        return "\n" + "POS_Output \n ---lista de S---" + "\n" + sentencess;
    }
}
