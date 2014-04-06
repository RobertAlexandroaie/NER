package gazetteer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import model.GazetteerLocalResult;

import org.apache.commons.lang.StringUtils;

public class GazetteerLocal {

    protected String Nume;

    public File fileLocation;
    public File fileInstitution;
    public File filePerson;

    public BufferedReader buffLocation;
    public BufferedReader buffInstitution;
    public BufferedReader buffPerson;
    public BufferedReader buffTemp;
    public FileInputStream myFisL; // f stream location
    public FileInputStream myFisI; // f stream institution
    public FileInputStream myFisP; // f stream person

    Writer writer;
    Writer writerPad;
    public String str;
    public int nrBytes;
    String[] tokens;
    String delims;
    String newline;
    String projectPath;

    private ArrayList<GazetteerLocalResult> results;

    public GazetteerLocal(String sNume, String path) throws IOException {
	results = new ArrayList<GazetteerLocalResult>();
	Nume = sNume.replaceAll("~", " ");
	Nume.replaceAll("~", " ");
	// separator
	delims = "\\|";
	nrBytes = 200;
	newline = System.getProperty("line.separator");
	projectPath = path;
	try {
	    addPadding();
	    closeFiles();
	    openPadFiles();
	    writer = null;
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("outGazetier.txt"), "utf-8"));
	    writer.write("");

	    checkEquality("locatie");
	    checkEquality("institutie");
	    checkEquality("persoana");

	    System.out.println("done");

	    writer.close();
	    closeFiles();
	} catch (UnsupportedEncodingException e) {
	    System.out.println(e.getMessage());
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}

    }

    public void openRawFiles() throws FileNotFoundException, UnsupportedEncodingException {
	fileLocation = new File(projectPath + "/" + "locatie.gzt");
	fileInstitution = new File(projectPath + "/" + "institutie.gzt");
	filePerson = new File(projectPath + "/" + "persoana.gzt");

	myFisL = new FileInputStream(fileLocation);
	myFisI = new FileInputStream(fileInstitution);
	myFisP = new FileInputStream(filePerson);

	buffLocation = new BufferedReader(new InputStreamReader(myFisL, "UTF-8"));
	buffInstitution = new BufferedReader(new InputStreamReader(myFisI, "UTF-8"));
	buffPerson = new BufferedReader(new InputStreamReader(myFisP, "UTF-8"));

    }

    public void openPadFiles() throws FileNotFoundException, UnsupportedEncodingException {
	fileLocation = new File(projectPath + "/" + "locatiePad.gzt");
	fileInstitution = new File(projectPath + "/" + "institutiePad.gzt");
	filePerson = new File(projectPath + "/" + "persoanaPad.gzt");

	myFisL = new FileInputStream(fileLocation);
	myFisI = new FileInputStream(fileInstitution);
	myFisP = new FileInputStream(filePerson);

	buffLocation = new BufferedReader(new InputStreamReader(myFisL, "UTF-8"));
	buffInstitution = new BufferedReader(new InputStreamReader(myFisI, "UTF-8"));
	buffPerson = new BufferedReader(new InputStreamReader(myFisP, "UTF-8"));

    }

    public void closeFiles() throws IOException {

	buffLocation.close();
	buffInstitution.close();
	buffPerson.close();

    }

    public void reOpenRawFiles() throws IOException {
	openRawFiles();
	closeFiles();
	openRawFiles();
	// System.out.println("Files reopened");
    }

    // Adaugarea de padding la fiecare linie pentru a avea un numar fix de bytes
    public void addPadding() throws IOException {
	reOpenRawFiles();
	String myTempString;
	String myPad = new String(" ");
	writerPad = null;

	// writer pentru locatie

	writerPad = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projectPath + "/" + "locatiePad.gzt"), "utf-8"));
	buffTemp = buffLocation;
	while ((str = buffTemp.readLine()) != null) {
	    myTempString = StringUtils.rightPad(str, nrBytes, myPad);
	    writerPad.append(myTempString + newline);
	}
	writerPad.close();
	// writer pentru institutie
	writerPad = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projectPath + "/" + "institutiePad.gzt"), "utf-8"));
	buffTemp = buffInstitution;
	while ((str = buffTemp.readLine()) != null) {
	    myTempString = StringUtils.rightPad(str, nrBytes, myPad);
	    writerPad.append(myTempString + newline);
	}

	// writer pentru persoana
	writerPad = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projectPath + "/" + "persoanaPad.gzt"), "utf-8"));
	buffTemp = buffPerson;
	while ((str = buffTemp.readLine()) != null) {
	    myTempString = StringUtils.rightPad(str, nrBytes, myPad);
	    writerPad.append(myTempString + newline);
	}
	writerPad.close();
	closeFiles();
    }

    // Verific din "n" in "n" linii daca "Numele" se afla in acel range
    public void checkEquality(String tipul) {
	StringBuilder sb = new StringBuilder("");
	int compArg;
	int compArg2;
	int compEq;
	try {
	    if (tipul == "locatie") buffTemp = buffLocation;
	    else if (tipul == "institutie") buffTemp = buffInstitution;
	    else if (tipul == "persoana") buffTemp = buffPerson;

	    while ((str = buffTemp.readLine()) != null) {
		tokens = str.split(delims);
		String sNume = Nume;

		if (tokens[0].contains("#")) {
		    sb = new StringBuilder(tokens[0]);
		    sb.deleteCharAt(0);

		    compArg = sNume.compareTo(sb.toString());
		    compArg2 = sNume.compareTo(tokens[1]);
		    // System.out.println("C1:"+ compArg + "-"+ "C2:"+compArg2 +
		    // sb.toString()+"="+tokens[1]);
		    if ((compArg >= 0) && (compArg2 <= 0)) {
			continue;
		    } else {

			buffTemp.skip((200 * 200) - 4);
			continue;
		    }

		}
		compEq = sNume.compareToIgnoreCase(tokens[0]);

		// System.out.println(str);
		if (compEq == 0) {
		    // System.out.println("Eq:" + tokens[0] + " tipul " +
		    // tokens[1]);
		    // try {
		    // writer.append(tokens[0] + "*" + tipul + "*" + tokens[1] +
		    // newline);
		    // } catch (IOException ex) {
		    //
		    // }
		    String type = tipul.trim();
		    if (tipul.trim().equals("locatie")) {
			type = "locație";
		    }
		    if (tipul.trim().equals("persoana")) {
			type = "persoană";
		    }
		    if (tipul.trim().equals("institutie")) {
			type = "instituție";
		    }
		    GazetteerLocalResult newResult = new GazetteerLocalResult(tokens[0].trim(), type, tokens[1].trim());
		    if (!results.contains(newResult)) {
			results.add(newResult);
		    }
		}

	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * @return the results
     */
    public ArrayList<GazetteerLocalResult> getResults() {
	return results;
    }

}
