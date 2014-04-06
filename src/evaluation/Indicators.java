package evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Indicators {

	private float recall;
	private float precision;
	private float fMeasure;
	private String goldFile;
	private String testFile;
	List<EvaluationEntity> listEntityGF = new ArrayList<EvaluationEntity>();
	List<EvaluationEntity> listEntityTF = new ArrayList<EvaluationEntity>();
	
	public Indicators(String goldFile, String testFile){
		this.goldFile = goldFile;
		this.testFile= testFile;
		this.setEntityLists();
		this.computeRecall();
		this.computePrecision();
		this.computefMeasure();
	}
	
	public Document getDOMParserDoc(String fileName){
		
		// parser for goldfile and testfile
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			return doc;
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//liste entitati din goldfile si testfile
	public void setEntityLists(){
		
		listEntityGF.clear();;
		listEntityTF.clear();;
		Document docGF = this.getDOMParserDoc(goldFile);
		Document docTF = this.getDOMParserDoc(testFile);
		docGF.getDocumentElement().normalize();
		docTF.getDocumentElement().normalize();
		
		NodeList nListGF = docGF.getElementsByTagName("ENTITY");
		NodeList nListTF = docTF.getElementsByTagName("ENTITY");
		
		for (int i = 0; i < nListGF.getLength(); i++) {
			Node nNode = nListGF.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				listEntityGF.add(new EvaluationEntity(eElement.getAttribute("ID"), 
						eElement.getAttribute("REF"), eElement.getAttribute("TYPE"), eElement.getAttribute("SUBTYPE")));
			}
		}
		
		for (int i = 0; i < nListTF.getLength(); i++) {
			Node nNode = nListTF.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				listEntityTF.add(new EvaluationEntity(eElement.getAttribute("ID"), 
						eElement.getAttribute("REF"), eElement.getAttribute("TYPE"), eElement.getAttribute("SUBTYPE")));
			}
		}
		
	}
	
	//numarul de entitati detectate corect in testfile
	public float getNumOfCorrectEntitiesTF(){
		float num=0;
		for(int i=0;i<this.listEntityTF.size();i++){
			for(int j=0;j<this.listEntityGF.size();j++){
				if(listEntityGF.get(j).getRef().compareTo(listEntityTF.get(i).getRef())==0 &&
				   listEntityGF.get(j).getSubtype().compareTo(listEntityTF.get(i).getSubtype())==0 &&
				   listEntityGF.get(j).getType().compareTo(listEntityTF.get(i).getType())==0 ){
					num++;
					break;
				}
			}
		}
		return num;
	}
	
	//calculeaza indicatorul RECALL
	public void computeRecall(){
		float numOfCorrectEntities = this.getNumOfCorrectEntitiesTF();
		float numOfEntitiesGF = this.listEntityGF.size();
		this.recall = numOfCorrectEntities/numOfEntitiesGF;
		System.out.print("Recall: "+this.recall+" \n");
	}
	
	//calculeaza indicatorul PRECISION
	public void computePrecision(){
		float numOfCorrectEntities = this.getNumOfCorrectEntitiesTF();
		float numOfEntitiesTF = this.listEntityTF.size();
		this.precision = numOfCorrectEntities/numOfEntitiesTF;
		System.out.print("Precision: "+this.precision+"\n");
	}
	
	//calculeaza indicatorul fMeasure
	public void computefMeasure(){
		this.fMeasure = (2* this.recall * this.precision)/(this.recall+this.precision);
		System.out.print("fMeasure: "+this.fMeasure+"\n");
	}

	public float getRecall() {
		return recall;
	}

	public float getPrecision() {
		return precision;
	}

	public float getfMeasure() {
		return fMeasure;
	}
	
	
}
