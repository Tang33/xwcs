package fast.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.Super;

@Controller
@RequestMapping("show")
public class MainController extends Super {
	
	@Autowired
	BaseService bs;
	
	@RequestMapping(value="/nszhsnum.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String nszhsnum(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select count(*) num,to_char(rk_rq,'yyyy') year from sb_zsxx where to_char(rk_rq,'yyyy') = (select TO_CHAR(SYSDATE,'yyyy') from dual) or to_char(rk_rq,'yyyy') = (select TO_CHAR(SYSDATE,'yyyy')-1 from dual)  group by to_char(rk_rq,'yyyy') where jd_dm='"+ssdw_dm+"'";
			List<Map<String, Object>> list = bs.query(sql);
			return this.toJson("000", "查询失败！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 一般公共预算前20
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/ssTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String ssTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			//String str = "and xx.jddm like '"+ssdw_dm+"%' )) order by zf desc " ;
//			if (ssdw_dm.equals("00")) {
//				ssdw_dm="%";
//				//str = " )) order by zf desc";
//			}
			String sql = "select * from FAST_YBGGYS where bs = '0' and jd_dm = '"+ssdw_dm+"' order by dnljje desc";
//			String sql = " select * from( select nsrmc,dnljje,TQLJJE,bs,jd_dm,(dnljje-NVL(tqljje,0)) zf,to_char(round(dnljje/NVL(tqljje,1)-1,4),'FM99999999999990.0000') zzbl\r\n" + 
//					"          from( select nvl(xx.nsrmc,'无') nsrmc,xx.dnlj dnljje,xx.tqlj TQLJJE, '0' bs ,xx.jddm jd_dm\r\n" + 
//					"           from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnlj desc nulls last) AS RN FROM fast_ybggys_vw T) xx where RN<=20" +str;
//			
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 一般公共预算增幅前20
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/ssZFTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String ssZFTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			//String str = "and xx.jddm like '"+ssdw_dm+"%' )) order by zf desc " ;
//			if (ssdw_dm.equals("00")) {
//				ssdw_dm="%";
//				//str = " )) order by zf desc";
//			}
			String sql = "select * from FAST_YBGGYS where bs = '1' and jd_dm = '"+ssdw_dm+"' order by zf desc";
//			String sql = "select * from( select nsrmc,dnljje,TQLJJE,bs,jd_dm,(dnljje-NVL(tqljje,0)) zf,to_char(round(dnljje/NVL(tqljje,1)-1,4),'FM99999999999990.0000') zzbl\r\n" + 
//					"           from(select nvl(xx.nsrmc,'无') nsrmc ,xx.dnlj dnljje,xx.tqlj TQLJJE,'1' bs ,xx.jddm jd_dm\r\n" + 
//					"           from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) desc nulls last) AS RN FROM fast_ybggys_vw T) xx where RN<=20  " +str;
//			
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 一般公共预算减幅前20
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/ssJFTop.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String ssJFTop(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			//String str = "and xx.jddm like '"+ssdw_dm+"%' )) order by zf desc " ;
//			if (ssdw_dm.equals("00")) {
//				ssdw_dm="%";
//				//str = " )) order by zf desc";
//			}
			String sql = "select * from FAST_YBGGYS where bs = '2' and jd_dm = '"+ssdw_dm+"' order by zf";
//			String sql = " select * from(select nsrmc,dnljje,TQLJJE,bs,jd_dm,(dnljje-NVL(tqljje,0)) zf,to_char(round(dnljje/NVL(tqljje,1)-1,4),'FM99999999999990.0000') zzbl\r\n" + 
//					"           from( select nvl(xx.nsrmc,'无') nsrmc,xx.dnlj dnljje,xx.tqlj TQLJJE,'2' bs,xx.jddm jd_dm\r\n" + 
//					"        from (SELECT T.*,ROW_NUMBER() OVER (ORDER BY (T.dnlj-nvl(T.tqlj,0)) nulls last) AS RN FROM fast_ybggys_vw T) xx where RN<=20" +str;
//			
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 新增纳税人查询
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/xznsrQuery.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String xznsrQuery(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap) {
		try {
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");	
			String sql = "select * from gh_xznsr where jd_dm='"+ssdw_dm+"'";	
			System.out.println(sql);	
			List<Map<String, Object>> list = bs.query(sql);

			return this.toJson("000", "查询成功！", list);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}
	
	/**
	 * 税种占比
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/szzb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String szzb(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select zsxm_mc name,ROUND(dnljje/10000,2) value from FAST_NSZZB where zsxm_mc = '营改增增值税' and jd_dm='"+ssdw_dm+"' or  zsxm_mc = '增值税' and jd_dm='"+ssdw_dm+"' or  zsxm_mc = '企业所得税' and jd_dm='"+ssdw_dm+"' union select '其他' name,ROUND((select sum(dnljje) from FAST_NSZZB where zsxm_mc != '营改增增值税' and  zsxm_mc != '增值税' and  zsxm_mc != '企业所得税' and jd_dm='"+ssdw_dm+"')/10000, 2) value from dual";
			list = bs.query(sql);
			System.out.println(sql);
			System.out.println(list);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list1);
		}
	}
	
	/**
	 * 行业占比
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/hyzb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String hyzb(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "SELECT hymc name,ROUND(dnljje/10000,2) value FROM (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnljje DESC) AS RN FROM FAST_NHYZB T where T.jd_dm='"+ssdw_dm+"')WHERE RN <= 3 and jd_dm='"+ssdw_dm+"' union select '其他' name,ROUND(((select sum(dnljje) from FAST_NHYZB where jd_dm='"+ssdw_dm+"')-(select sum(dnljje) FROM (SELECT T.*,ROW_NUMBER() OVER (ORDER BY T.dnljje DESC) AS RN FROM FAST_NHYZB T where T.jd_dm='"+ssdw_dm+"')WHERE RN <= 3 and jd_dm='"+ssdw_dm+"'))/10000, 2) value from dual";
			System.out.println(sql);
			list = bs.query(sql);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list1);
		}
	}
	
	/**
	 * 月累计
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/monthTotal.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String monthTotal(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select TO_CHAR(SYSDATE,'yyyy')+0 year,yf,ROUND(dyljje/10000,2) se from FAST_YLJ where jd_dm='"+ssdw_dm+"' union select TO_CHAR(SYSDATE,'yyyy')-1 year,yf,ROUND(tqljje/10000,2) se from FAST_YLJ where jd_dm='"+ssdw_dm+"'";
			System.out.println("monthTotal:"+sql);
			List<Map<String, Object>> list = bs.query(sql);
			Map<String, String> map;
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				for (String key : list.get(i).keySet()) {
					map.put(key.toLowerCase(), list.get(i).get(key).toString());
				}
				list1.add(map);
			}
			return this.toJson("000", "查询成功！",list1);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败", list1);
		} 
	}
	
	/**
	 * 税种占比详情
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/szzbAll.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String szzbAll(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select * from FAST_NSZZB where jd_dm='"+ssdw_dm+"' order by dnljje desc";
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	/**
	 * 月累计详情
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/yljAll.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String yljAll(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select * from FAST_YLJ where jd_dm='"+ssdw_dm+"'";
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
	
	/**
	 * 行业占比详情
	 * @param request
	 * @param response
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/hyzbAll.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String hyzbAll(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> rmap){
		List<Map<String, Object>> list = new ArrayList<>();
		try{
			String ssdw_dm = (String) request.getSession().getAttribute("dwid");
			String sql = "select * from FAST_NHYZB where jd_dm='"+ssdw_dm+"' order by dnljje desc";
			System.out.println(sql);
			list = bs.query(sql);
			return this.toJson("000", "查询成功！",list);
		}catch(Exception e){
			e.printStackTrace();
			return this.toJson("001", "查询失败！",list);
		}
	}
	
}
