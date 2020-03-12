package fast.main.util;

public class Mode {
	private String otype;
	private String type;
	private String value;
	public Mode() {
		super();
	}
	public Mode(String type, String value ) {
		super();
		this.otype = "IN";
		this.value = value;
		this.type = type;
	}
	
	public Mode(String otype, String type, String value) {
		super();
		this.otype = otype;
		this.value = value;
		this.type = type;
	}


	public String getOtype() {
		return otype;
	}
	public void setOtype(String otype) {
		this.otype = otype;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
