package evaluation;

public class EvaluationEntity {

	private String id;
	private String ref;
	private String type;
	private String subtype;
	
	public EvaluationEntity(String id,String ref,String type , String subtype){
		this.id = id;
		this.ref = ref;
		this.type = type;
		this.subtype = subtype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	
	
}

