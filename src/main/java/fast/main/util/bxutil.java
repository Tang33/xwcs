package fast.main.util;

public class bxutil {

	
	public String getbxxml_s(String UNITCODE,String APPLYNO) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><CONFIG><!--TYPE为IN表示传入B2B数据，OUT为B2B返回数据-->"
				+ "<!--WORKTYPE为0表示新增，为1表示新增返回,为2表示查询，为3表示查询返回--><TYPE>IN</TYPE><WORKTYPE>2</WORKTYPE></CONFIG><DATA>"
				+ "<!--主要信息--><POLICY><!-- 必填 --><UNITCODE>"+UNITCODE+"</UNITCODE><!-- 必填 --><APPLYNO>"+APPLYNO+"</APPLYNO>"
				+ "<!-- 查询投保单核保结果可为空 --><APPLYENDORSENO></APPLYENDORSENO></POLICY></DATA></ROOT>";

	}
	public String getbxxml_r(String UNITCODE,String APPLYNO) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><CONFIG><!--TYPE为IN表示传入B2B数据，OUT为B2B返回数据-->"
				+ "<!--WORKTYPE为0表示新增，为1表示新增返回,为2表示查询，为3表示查询返回--><TYPE>IN</TYPE><WORKTYPE>2</WORKTYPE></CONFIG><DATA>"
				+ "<!--主要信息--><POLICY><!-- 必填 --><UNITCODE>"+UNITCODE+"</UNITCODE><!-- 必填 --><APPLYNO>"+APPLYNO+"</APPLYNO>"
				+ "<!-- 查询投保单核保结果可为空 --><APPLYENDORSENO></APPLYENDORSENO></POLICY></DATA></ROOT>";

	}
}
