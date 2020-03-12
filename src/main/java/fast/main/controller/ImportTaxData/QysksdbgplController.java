package fast.main.controller.ImportTaxData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fast.main.service.BaseService;
import fast.main.util.ExcelRead;
import fast.main.util.JdbcConnectedPro;
import fast.main.util.Super;
import net.sf.json.JSONArray;


/**
 * 企业税款属地变更(自定义)
 * @author Administrator
 *
 */
@Controller
@RequestMapping("qysksdbgpl")
public class QysksdbgplController extends Super{

	@Autowired
	BaseService bs;
	
	@RequestMapping(value="/init.do",produces = "text/plain;charset=utf-8")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）	
			return "ImportTaxData/qysksdbgpl";
		} catch (Exception e) {
			e.printStackTrace();
			return "ImportTaxData/qysksdbgpl";
		}
	}
	
	/**
	 * 将数据从excel取出并存到临时表
	 * @param rmap
	 * @return
	 */
	@RequestMapping(value="/doInput.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String doInput(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		String filename = getValue(form.get("filename"));
		InputStream is = null;
		try {
			
			Map<String,Object> instemp =  insttemp(is, filename,form);
			return this.toJson("000", "查询成功！",instemp);
			
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询失败！");
		}
	}
	
	/**
	 * 按单笔拆分多个街道
	 * 
	 * @param rmap
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	@RequestMapping(value="/PLXG.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object PLXG(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {	
			Long sjq = System.currentTimeMillis();		
			String sql_rkrq_max = this.getValue(form.get("rkrq"));			//入库日期		
			String nsrmc = this.getValue(form.get("nsrmcs"));			//纳税人名称数组		
			String nsrmcs [] = nsrmc.split(",");			//拆分纳税人名称数组		
			int qjxId = 1 ;		
			//获取税种并拆分成数组
			String sz = getValue(form.get("sz"));		//税种数组
			String [] szs = sz.split(",");			//拆分税种数组
			//获取街道代码
			String jddm = getValue(form.get("jddm"));		//街道代码
			//查询纳税人表最大日期		
			Double  zse = 0.00;		
			for(int j = 0 ; j<nsrmcs.length;j++){
				//删除sb_nsrxx_bf表里的当月数据
				String delnsr = " delete from SB_NSRXX_BF s where NSRMC =  ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') ";
				bs.delete(delnsr);
				//根据纳税人名称和入库日期查询数据序号
				String nsrXh = "select xh from  SB_NSRXX s where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and hfid is null";
				String xhStr = ""; 
				List<Map<String,Object>> nsrXhs = bs.query(nsrXh);
				for(int x = 0 ; x < nsrXhs.size() ; x++){				
					xhStr+=nsrXhs.get(x).get("XH")+",";
				}		
				if(xhStr.length() > 0) {
					xhStr = xhStr.substring(0, xhStr.length() - 1);
				}			
				for(int i = 0 ; i < szs.length ; i++){
					if(szs[i]!=""){
						//识别各个税收
						if (szs[i].equals("增值税")) {
							szs[i]= "zzs";
						} else if (szs[i].equals("营改增增值税")) {
							szs[i] = "ygzzzs";
						} else if (szs[i].equals("营业税")) {
							szs[i]= "yys";
						} else if (szs[i].equals("企业所得税")) {
							szs[i]= "qysds";
						} else if (szs[i].equals("个人所得税")) {
							szs[i]= "grsds";
						} else if (szs[i].equals("车船税")) {
							szs[i]= "ccs";
						} else if (szs[i].equals("房产税")) {
							szs[i]= "fcs";
						} else if (szs[i].equals("印花税")) {
							szs[i]= "yhs";
						} else if (szs[i].equals("城市维护建设税")) {
							szs[i]= "cswhjss";
						} else if (szs[i].equals("地方教育附加")) {
							szs[i]= "dfjyfj";
						} else if (szs[i].equals("教育附加")) {
							szs[i]= "jyfj";
						} else if (szs[i].equals("城镇土地使用税")) {
							szs[i]= "cztdsys";
						} else if (szs[i].equals("环保税")) {
							szs[i]= "hbs";
						}				
						//根据每个税种去往备份表中添加要操作的数据				
//						String updateNsrxxBf = "update SB_NSRXX_BF_TEMP set "+szs[i]+"="+szs[i]+"-("+szs[i]+"*"+2+") ";
//						bs.update(updateNsrxxBf);
						//拆分税种金额，添加一正一负两条数据
						//上办部分负数 数据 下班部分正数数据
						if (szs[i].equals("zzs")) {
							
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBfOld = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,'-'||ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ZZS,'-'||(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"'  ,NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfOld);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,zzs,(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("ygzzzs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ygzzzs,'-'||(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,'-'||YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ygzzzs,(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("yys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,'-'||YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yys,'-'||(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yys,(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);					
						} else if (szs[i].equals("qysds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+ 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,'-'||QYSDS_GS,QYSDS_DS,'-'||QYSDS,'-'||qysds,'-'||(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,qysds,(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);		
						} else if (szs[i].equals("grsds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,'-'||GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||grsds,'-'||(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							Integer insert = bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,grsds,(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							Integer insert1 =bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("ccs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ccs,'-'||(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ccs,(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("fcs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,'-'||FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||fcs,'-'||(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,fcs,(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("yhs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,'-'||YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yhs,'-'||(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yhs,(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("cswhjss")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cswhjss,'-'||(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,'-'||CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cswhjss,(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("dfjyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||dfjyfj,'-'||(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,dfjyfj,(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("jyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||jyfj,'-'||(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,jyfj,(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("cztdsys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cztdsys,'-'||(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cztdsys,(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("hbs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||hbs,'-'||(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,hbs,(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						}		 
					}
					
					String selecSum = "select sum(zse) a from SB_NSRXX_BF  where nsrmc = '"+nsrmcs[j]+"' and zse > 0 group by nsrmc  ";
					List<Map<String, Object>> sum = bs.query(selecSum);
					if(sum.size()>0){
						zse+=Double.parseDouble(this.getValue(sum.get(0).get("A")));
					}
					
				}
				
			}
			
			
			//导回真实表
			String bftoNsrxxNewSql = "insert into SB_NSRXX xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
					"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
					"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh) " + 
					"SELECT " + 
					"xh,NSRMC,RK_RQ,jd_dm,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,CSWHJSS,DFJYFJ	,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,qxj,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,hfid,NSRSBH " + 
					" FROM SB_NSRXX_BF s WHERE  s.sjc = '"+sjq+"' ";
			bs.insert(bftoNsrxxNewSql);
//			//再次清空备份表
			String deltemp = "delete from SB_NSRXX_BF where sjc = '"+sjq+"'";
			bs.delete(deltemp);
			Map<String, Object> resultMap = new HashMap<>();
			//再次导入老街道信息，修改老街道负值信息
			resultMap.put("companyCount", nsrmcs.length);
			resultMap.put("totalAmount", zse);
			resultMap.put("params", "");
			resultMap.put("sz", sz);
			resultMap.put("jd", jddm);
			return this.toJson("000", "查询成功！", resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		  }
		
		
	}


	
	
	/**
	 * 让规则执行
	 */
	public  String PLXG2(String nsrmc,String sz,String sql_rkrq_max,String jddm,BaseService bs ) {
		try {		
			Long sjq = System.currentTimeMillis();
			String nsrmcs [] = nsrmc.split(",");	
			int qjxId = 1 ;		
			//获取税种并拆分成数组
//			String sz = getValue(form.get("sz"));
			String [] szs = sz.split(",");
			//查询纳税人表最大日期
			Double  zse = 0.00;
			for(int j = 0 ; j<nsrmcs.length;j++){
				
				String delnsr = " delete from SB_NSRXX_BF s where NSRMC =  ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') ";
				bs.delete(delnsr);
				String nsrXh = "select xh from  SB_NSRXX s where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and hfid is null";
				String xhStr = "";
				List<Map<String,Object>> nsrXhs = bs.query(nsrXh);
				for(int x = 0 ; x < nsrXhs.size() ; x++){				
					xhStr+=nsrXhs.get(x).get("XH")+",";
				}	
				if(xhStr.length() > 0) {
					xhStr = xhStr.substring(0, xhStr.length() - 1);
				}	
				for(int i = 0 ; i < szs.length ; i++){
					if(szs[i]!=""){
						//识别各个税收
						if (szs[i].equals("增值税")) {
							szs[i]= "zzs";
						} else if (szs[i].equals("营改增增值税")) {
							szs[i] = "ygzzzs";
						} else if (szs[i].equals("营业税")) {
							szs[i]= "yys";
						} else if (szs[i].equals("企业所得税")) {
							szs[i]= "qysds";
						} else if (szs[i].equals("个人所得税")) {
							szs[i]= "grsds";
						} else if (szs[i].equals("车船税")) {
							szs[i]= "ccs";
						} else if (szs[i].equals("房产税")) {
							szs[i]= "fcs";
						} else if (szs[i].equals("印花税")) {
							szs[i]= "yhs";
						} else if (szs[i].equals("城市维护建设税")) {
							szs[i]= "cswhjss";
						} else if (szs[i].equals("地方教育附加")) {
							szs[i]= "dfjyfj";
						} else if (szs[i].equals("教育附加")) {
							szs[i]= "jyfj";
						} else if (szs[i].equals("城镇土地使用税")) {
							szs[i]= "cztdsys";
						} else if (szs[i].equals("环保税")) {
							szs[i]= "hbs";
						}
						//根据每个税种去往备份表中添加要操作的数据			
//						String updateNsrxxBf = "update SB_NSRXX_BF_TEMP set "+szs[i]+"="+szs[i]+"-("+szs[i]+"*"+2+") ";
//						bs.update(updateNsrxxBf);
						//上办部分负数 数据 下班部分正数数据
						if (szs[i].equals("zzs")) {
							
							String sql = "select * from SB_NSRXX  where nsrmc = '' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBfOld = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,'-'||ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ZZS,'-'||(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"'  ,NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfOld);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,zzs,(ZZS*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("ygzzzs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ygzzzs,'-'||(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,'-'||YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ygzzzs,(ygzzzs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("yys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,'-'||YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yys,'-'||(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yys,(yys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("qysds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc"+ 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,'-'||QYSDS_GS,QYSDS_DS,'-'||QYSDS,'-'||qysds,'-'||(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,qysds,(qysds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);			
						} else if (szs[i].equals("grsds")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,'-'||GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||grsds,'-'||(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,grsds,(grsds*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("ccs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,'-'||CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||ccs,'-'||(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,ccs,(ccs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
						} else if (szs[i].equals("fcs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,'-'||FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||fcs,'-'||(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,fcs,(fcs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("yhs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,'-'||YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||yhs,'-'||(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,yhs,(yhs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);	
						} else if (szs[i].equals("cswhjss")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cswhjss,'-'||(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,'-'||CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cswhjss,(cswhjss*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("dfjyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||dfjyfj,'-'||(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,dfjyfj,(dfjyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("jyfj")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||jyfj,'-'||(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,jyfj,(jyfj*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("cztdsys")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||cztdsys,'-'||(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,cztdsys,(cztdsys*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						} else if (szs[i].equals("hbs")) {
							String sql = "select * from SB_NSRXX  where nsrmc = '"+nsrmcs[j]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" < 0 ";
							int count = bs.queryCount(sql);
							if(count > 0){
								continue;
							}
							String updateNsrxxZs = "update sb_nsrxx s set qxj = '"+qjxId+"'  where nsrmc = ('"+nsrmcs[j]+"') and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj is null and  "+szs[i]+" > 0 ";
							bs.update(updateNsrxxZs);
							String nsrxxBf = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC, RK_RQ,JD_DM,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,'-'||hbs,'-'||(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM, LR_SJ,YGZZZS,CSWHJSS,DFJYFJ,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,'-'||HBS,"+qjxId+",ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBf);
							
							String nsrxxBfNew = "insert into SB_NSRXX_BF xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
									"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
									"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh, xh.sjc" + 
									")"
									+ "SELECT "
									+"seq_sb_nsrxx.nextval,NSRMC,RK_RQ,"+jddm+",HY_DM,'-'||ZZS,'-'||YYS,'-'||GRSDS,'-'||FCS,'-'||YHS,'-'||CCS,'-'||QYSDS_GS,'-'||QYSDS_DS,'-'||QYSDS,hbs,(hbs*BL/100),QYXZ,HHNSRMC,LRRY_DM,LR_SJ,'-'||YGZZZS,'-'||CSWHJSS,'-'||DFJYFJ	,'-'||JYFJ,XSSR,GDS,ZSXM,BL,'-'||CZTDSYS,HBS,	"+qjxId+"	,ZSPMDM,	HYDL	,HYZL,	YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,'"+xhStr+"',NSRSBH,'"+sjq+"'"
									+ " FROM SB_NSRXX s WHERE s.NSRMC =  '"+nsrmcs[j]+"'  and s.Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and  "+szs[i]+" != 0 and qxj =1 ";
							bs.insert(nsrxxBfNew);
							
						}					
					}
					
					String selecSum = "select sum(zse) a from SB_NSRXX_BF  where nsrmc = '"+nsrmcs[j]+"' and zse > 0 group by nsrmc  ";
					List<Map<String, Object>> sum = bs.query(selecSum);
					if(sum.size()>0){
						zse+=Double.parseDouble(this.getValue(sum.get(0).get("A")));
					}
					
				}
				
			}
			
			//导回真实表
			String bftoNsrxxNewSql = "insert into SB_NSRXX xh (xh, xh.nsrmc, xh.rk_rq, xh.jd_dm, xh.hy_dm, xh.zzs, xh.yys, xh.grsds, xh.fcs, xh.yhs, xh.ccs, xh.qysds_gs, xh.qysds_ds," + 
					"xh.qysds, xh.zse, xh.dfzse, xh.qyxz, xh.hhnsrmc, xh.lrry_dm, xh.lr_sj, xh.ygzzzs, xh.cswhjss, xh.dfjyfj, xh.jyfj, xh.xssr, xh.gds, xh.zsxm, xh.bl, xh.cztdsys," + 
					"xh.hbs, xh.qxj, xh.zspmdm, xh.hydl, xh.hyzl, xh.yskmdm, xh.dzsphm, xh.djxhs, xh.zspm, xh.yzpzxh, xh.sksx, xh.hfid, xh.nsrsbh) " + 
					"SELECT " + 
					"xh,NSRMC,RK_RQ,jd_dm,HY_DM,ZZS,YYS,GRSDS,FCS,YHS,CCS,QYSDS_GS,QYSDS_DS,QYSDS,zse,dfzse,QYXZ,HHNSRMC,LRRY_DM,LR_SJ,YGZZZS,CSWHJSS,DFJYFJ	,JYFJ,XSSR,GDS,ZSXM,BL,CZTDSYS,HBS,qxj,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX,hfid,NSRSBH " + 
					" FROM SB_NSRXX_BF s WHERE  s.sjc = '"+sjq+"' ";
			bs.insert(bftoNsrxxNewSql);
//			//再次清空备份表
			String deltemp = "delete from SB_NSRXX_BF where sjc = '"+sjq+"'";
			bs.delete(deltemp);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
			
		  }
		
		
	}

	/**
	 * 按单笔拆分多个街道
	 * 
	 * @param form
	 * @return 返回状态 000 拆分成功，刷新页面（009 拆分失败）
	 */
	@RequestMapping(value="/CF",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object CF(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String str = getValue(form.get("str"));
			String sz = getValue(form.get("sz"));//sz-税种
			JSONArray ja = JSONArray.fromObject(str);
			String szzd = "";//szzd-税种字段
			if (sz.equals("增值税")) {
				szzd = "zzs";
			} else if (sz.equals("营改增增值税")) {
				szzd = "ygzzzs";
			} else if (sz.equals("营业税")) {
				szzd = "yys";
			} else if (sz.equals("企业所得税")) {
				szzd = "qysds";
			} else if (sz.equals("个人所得税")) {
				szzd = "grsds";
			} else if (sz.equals("车船税")) {
				szzd = "ccs";
			} else if (sz.equals("房产税")) {
				szzd = "fcs";
			} else if (sz.equals("印花税")) {
				szzd = "yhs";
			} else if (sz.equals("城市维护建设税")) {
				szzd = "cswhjss";
			} else if (sz.equals("地方教育附加")) {
				szzd = "dfjyfj";
			} else if (sz.equals("教育附加")) {
				szzd = "jyfj";
			} else if (sz.equals("城镇土地使用税")) {
				szzd = "cztdsys";
			} else if (sz.equals("环保税")) {
				szzd = "hbs";
			}

			List<Map<String, Object>> list = (List<Map<String, Object>>) ja;

			for (int i = 0; i < list.size(); i++) {
				String querysql = "select * from sb_nsrxx s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				
				Map<String, Object> map = bs.queryOne(querysql);
				String deletesql = "delete from sb_nsrxx_bf s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				
				bs.delete(deletesql);
				String sql = "insert into sb_nsrxx_bf select * from sb_nsrxx s  " + "where s.xh='"+ getValue(list.get(i).get("xh")) + "'";
				
				bs.insert(sql);
				String updatesql = "update sb_nsrxx s set " + szzd + "='0',qxj='"+getValue(list.get(i).get("xh"))+"' where s.xh='"
						+ getValue(list.get(i).get("xh")) + "'";//qxj-区县级
				
				if (sz.equals("企业所得税")) {
					updatesql = "update sb_nsrxx s set " + szzd + "='0',qysds_gs='0',qysds_ds='0',qxj='"+getValue(list.get(i).get("xh"))+"' where s.xh='"
							+ getValue(list.get(i).get("xh")) + "'";
				}
				
				bs.update(updatesql);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
				String rkrq = getValue(map.get("RK_RQ")).substring(0, getValue(map.get("RK_RQ")).lastIndexOf("."));
				Date date = new Date();
				
				rkrq = rkrq.substring(0, rkrq.lastIndexOf("-")).replaceAll("-", "");

				if (szzd.lastIndexOf("qysds") > -1) {
					szzd = "qysds,qysds_gs";
					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX) "
							+ "valuesseq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + rkrq
							+ "','yyyyMM'),'" + getValue(list.get(i).get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(list.get(i).get("zse")) + "'" + ",'" + getValue(list.get(i).get("zse"))
							+ "','" + getValue(map.get("QYXZ")) + "','" + getValue(map.get("HHNSRMC")) + "','"
							+ getValue(map.get("LRRY_DM")) + "',to_date('" + formatter.format(date) + "','yyyyMM'),'"
							+ getValue(map.get("XSSR")) + "','" + getValue(map.get("GDS")) + "','"
							+ getValue(map.get("ZSXM")) + "','" + getValue(map.get("BL")) + "','"+getValue(list.get(i).get("xh"))+"','"
							+ getValue(map.get("ZSPMDM")) + "','" + getValue(map.get("HYDL")) + "','"
							+ getValue(map.get("HYZL")) + "','" + getValue(map.get("YSKMDM")) + "','"
							+ getValue(map.get("DZSPHM")) + "','" + getValue(map.get("DJXHS")) + "','"
							+ getValue(map.get("ZSPM")) + "','" + getValue(map.get("YZPZXH")) + "','"
							+ getValue(map.get("SKSX")) + "')";
					
					bs.insert(insetsql);
				} else {
					String insetsql = "insert into sb_nsrxx(XH,NSRMC,rk_rq,JD_DM,HY_DM," + szzd
							+ ",QYXZ,HHNSRMC,LRRY_DM,LR_SJ,XSSR,GDS,"
							+ "ZSXM,BL,QXJ,ZSPMDM,HYDL,HYZL,YSKMDM,DZSPHM,DJXHS,ZSPM,YZPZXH,SKSX) "
							+ "values(seq_sb_nsrxx.nextval,'" + getValue(map.get("NSRMC")) + "',to_date('" + rkrq
							+ "','yyyyMM'),'" + getValue(list.get(i).get("jddm")) + "','" + getValue(map.get("HY_DM"))
							+ "','" + getValue(list.get(i).get("zse")) + "','" + getValue(map.get("QYXZ")) + "','"
							+ getValue(map.get("HHNSRMC")) + "','" + getValue(map.get("LRRY_DM")) + "',to_date('"
							+ formatter.format(date) + "','yyyyMM'),'" + getValue(map.get("XSSR")) + "','"
							+ getValue(map.get("GDS")) + "','" + getValue(map.get("ZSXM")) + "','"
							+ getValue(map.get("BL")) + "','"+getValue(list.get(i).get("xh"))+"','" + getValue(map.get("ZSPMDM")) + "','"
							+ getValue(map.get("HYDL")) + "','" + getValue(map.get("HYZL")) + "','"
							+ getValue(map.get("YSKMDM")) + "','" + getValue(map.get("DZSPHM")) + "','"
							+ getValue(map.get("DJXHS")) + "','" + getValue(map.get("ZSPM")) + "','"
							+ getValue(map.get("YZPZXH")) + "','" + getValue(map.get("SKSX")) + "')";
					
					bs.insert(insetsql);
				}
			}
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	/**
	 * 将拆分的删除，恢复原数据
	 * 
	 * @param form
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	@RequestMapping(value="/HF.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object HF(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			
			String sql_rkrq_max = this.getValue(form.get("rkrq"));			//入库日期
			
			String nsrmcs = this.getValue(form.get("nsrmcs"));			//纳税人名称数组
			
			String [] nsrmc = nsrmcs.split(",");
			
			for(int i = 0 ; i < nsrmc.length ; i++){
				
				String sql = "select to_char(hfid) hfid from SB_NSRXX  where nsrmc = '"+nsrmc[i]+"'  and hfid is not null and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and qxj !=2 ";
				
				List<Map<String,Object>> hfidList = bs.query(sql);
				
				String hfid = "";
				if(hfidList.size()>0){
					
					 hfid =  (String) hfidList.get(0).get("HFID");
					
				}
				
				String sql2 = "select to_char(hfid) hfid from SB_NSRXX  where nsrmc = '"+nsrmc[i]+"'  and hfid is not null and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and qxj =2 ";
				
				List<Map<String,Object>> hfidList2 = bs.query(sql2);
				
				if(hfidList2.size()>0){
					
				String del2 = "delete from SB_NSRXX  where nsrmc = '"+nsrmc[i]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm') and qxj = 2 and hfid is not null ";
				bs.delete(del2);
				
				}
				
				if(hfid!=""){
					String del = "delete from SB_NSRXX  where nsrmc = '"+nsrmc[i]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  and xh not in ("+hfid+") and qxj != 2";
					bs.delete(del);
					
				}
				
				String update = "update SB_NSRXX  set hfid='' , qxj=''  where nsrmc = '"+nsrmc[i]+"' and Rk_Rq=to_date('"+sql_rkrq_max+"','yyyy-mm')  ";
				bs.update(update);
				
			}
			
			return this.toJson("000", "查询成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}


	@RequestMapping(value="/queryInit.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String queryInit(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			// 街道
			String sql = "select * from dm_jd ";
			List<Map<String, Object>> jdlist = bs.query(sql);
			// 行业
			sql = "select * from dm_hyml";
			List<Map<String, Object>> hylist = bs.query(sql);
			// 重点税源企业
			/*
			 * sql=this.getSql("去查重点税源企业"); List<Map<String, Object>>
			 * zdsylist=this.bs.query(sql);
			 */
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
			map.put("hylist", hylist);
			map.put("jdlist", jdlist);
			// map.put("zdsylist",zdsylist);
			return this.toJson("000", "查询成功！", map);
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "查询异常！");
		}
	}

	
	/**
	 * 保存模板
	 * 
	 * @param form
	 * @return 返回状态(000恢复成功,009恢复失败)
	 */
	@RequestMapping(value="/bcmb.do",produces = "text/plain;charset=utf-8")
	@ResponseBody
	public Object bcmb(HttpServletRequest request , HttpServletResponse response,
			@RequestParam Map<String, Object> form) {
		try {
			String mbmc = getValue(form.get("mbmc"));				//模板名称
			String nsrmcs = getValue(form.get("nsrmcs"));			//纳税人名称
			String sz = getValue(form.get("sz"));			//税种
			String jd = getValue(form.get("jd"));			//街道
			String mbms = getValue(form.get("mbms"));		//模板描述
//			List<Map<String, Object>> nsrs = (List<Map<String, Object>>) JSONArray.fromObject(params);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			String sql = "insert into fast_mb(SQL,MBMC, NSRMC, SZ, JD, STATUS, BS, HFID, CREATETIME,MS) values('/qysksdbgpl//PLXGMB.do','" 
					+ mbmc + "', '" + nsrmcs + "' ,'" + sz + "' ,'" + jd + "', '1', '3', '', to_date('" + formatter.format(new Date()) + "', 'yyyy-MM-dd'),'"+mbms+"')";
			this.bs.insert(sql);


			return this.toJson("000", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return this.toJson("009", "保存异常！");
		}
	}



	//导入
	private Map<String, Object> insttemp(InputStream is, String filename,@RequestParam Map<String, Object> form) {
		List<Map<String, Object>> results = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		String nsr = "";
		try {
			is = new FileInputStream(new File(filename));
			Map<String, Integer> titles = new HashMap<String, Integer>();
			titles.put("社会信用代码（纳税人识别号）", -1);
			titles.put("纳税人名称", -1);
			titles.put("税种", -1);

			List<Map<String, String>> list = ExcelRead.pomExcel(filename, is, titles);
			for(Map<String, String> map : list) {
				String nsrmc = map.get("纳税人名称");
				String sz = map.get("税种");
				List<Map<String, Object>> tempList = getNsr(nsrmc,sz,form);
				
				nsr=nsr+nsrmc+",";
				results.addAll(tempList);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		map1.put("nsrmc", nsr);
		map1.put("arr", results);
		
		System.out.println("刷新后的数量为："+results.size());
		return map1;
	}

	public Map<String, String> getSz(String sz){
		String result = "";
		if(sz.equals("增值税")){
			result = "ZZS";
		}else if(sz.equals("营业税")){
			result = "YYS";
		}else if(sz.equals("个人所得税")){
			result = "GRSDS";
		}else if(sz.equals("房产税")){
			result = "FCS";
		}else if(sz.equals("印花税")){
			result = "YHS";
		}else if(sz.equals("车船税")){
			result = "CCS";
		}else if(sz.equals("企业所得税")){
			result = "QYSDS";
		}else if(sz.equals("营改增增值税")){
			result = "YGZZZS";
		}else if(sz.equals("城市维护建设税")){
			result = "CSWHJSS";
		}else if(sz.equals("地方教育附加")){
			result = "DFJYFJ";
		}else if(sz.equals("教育附加")){
			result = "JYFJ";
		}else if(sz.equals("城镇土地使用税")){
			result = "CZTDSYS";
		}else if(sz.equals("环保税")){
			result = "HBS";
		}
		Map<String, String> map = new HashMap<String,String>();
		map.put("税种中文", sz);
		map.put("税种简称", result);
		return map;
	}

	public List<Map<String, Object>> getNsr(String nsrmc,String sz,@RequestParam Map<String, Object> form) {
		List<Map<String, Object>> results = new ArrayList<>();

		
		
		String sql_rkrq_max = this.getValue(form.get("rkrq"));
		
		String dateCondition = "' and rk_rq = to_date('" + sql_rkrq_max + "','yyyyMM') ";
		if(sz!=null && !sz.trim().equals("")){
			Map<String,String> map = getSz(sz);
			String sql_sz = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'"+map.get("税种中文")+"' zsxm,sum("+map.get("税种简称")+"*bl/100) zsedf,sum("+map.get("税种简称")+") zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq, qxj "
					+ " from SB_NSRXX  s " + "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
			List<Map<String, Object>> qysds = bs.query(sql_sz);
			if (qysds != null) {
				results.addAll(qysds);
			}
		}else{
			String sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'企业所得税' zsxm,sum(qysds*bl/100) zsedf,sum(qysds) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq, qxj "
					+ " from SB_NSRXX  s " + "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'企业所得税',rk_rq,qxj,xh";
			List<Map<String, Object>> qysds = bs.query(sql);
			if (qysds != null) {
				results.addAll(qysds);

			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'增值税' zsxm,sum(zzs*bl/100) zsedf,sum(zzs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'增值税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> zzs = bs.query(sql);
			if (zzs != null) {
				results.addAll(zzs);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营改增增值税' zsxm,sum(ygzzzs*bl/100) zsedf,sum(ygzzzs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'营改增增值税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> ygzzzs = bs.query(sql);
			if (ygzzzs != null) {
				results.addAll(ygzzzs);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'营业税' zsxm,sum(yys*bl/100) zsedf,sum(yys) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'营业税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> yys = bs.query(sql);
			if (yys != null) {
				results.addAll(yys);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'个人所得税' zsxm,sum(grsds*bl/100) zsedf,sum(grsds) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'个人所得税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> grsds = bs.query(sql);
			if (grsds != null) {
				results.addAll(grsds);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'车船税' zsxm,sum(ccs*bl/100) zsedf,sum(ccs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'车船税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> ccs = bs.query(sql);
			if (ccs != null) {
				results.addAll(ccs);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'房产税' zsxm,sum(fcs*bl/100) zsedf,sum(fcs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'房产税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> fcs = bs.query(sql);
			if (fcs != null) {
				results.addAll(fcs);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'印花税' zsxm,sum(yhs*bl/100) zsedf,sum(yhs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'印花税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> yhs = bs.query(sql);
			if (yhs != null) {
				results.addAll(yhs);
			}		
			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城市维护建设税' zsxm,sum(cswhjss*bl/100) zsedf,sum(cswhjss) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'城市维护建设税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> cswhjss = bs.query(sql);
			if (cswhjss != null) {
				results.addAll(cswhjss);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'地方教育附加' zsxm,sum(dfjyfj*bl/100) zsedf,sum(dfjyfj) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'地方教育附加' ,rk_rq,qxj,xh";
			List<Map<String, Object>> dfjyfj = bs.query(sql);
			if (dfjyfj != null) {
				results.addAll(dfjyfj);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'教育附加' zsxm,sum(jyfj*bl/100) zsedf,sum(jyfj) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'教育附加' ,rk_rq,qxj,xh";
			List<Map<String, Object>> jyfj = bs.query(sql);
			if (jyfj != null) {
				results.addAll(jyfj);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'城镇土地使用税' zsxm,sum(cztdsys*bl/100) zsedf,sum(cztdsys) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'城镇土地使用税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> cztdsys = bs.query(sql);
			if (cztdsys != null) {
				results.addAll(cztdsys);
			}

			sql = "select xh,nsrmc,(select jd_mc from dm_jd d where d.jd_dm=s.jd_dm)jd_mc,'环保税' zsxm,sum(hbs*bl/100) zsedf,sum(hbs) zse,TO_CHAR(RK_RQ,'YYYY-MM') rk_rq,qxj from SB_NSRXX  s  "
					+ "where nsrmc = '" + nsrmc + dateCondition + " and zse != 0  and zse is not null group by nsrmc,jd_dm,'环保税' ,rk_rq,qxj,xh";
			List<Map<String, Object>> hbs = bs.query(sql);
			if (hbs != null) {
				results.addAll(hbs);
			}
		}
//		System.out.println(results);

		return results;
	}

	public static BigDecimal getBigDecimal( Object value ) {  
		BigDecimal ret = null;  
		if( value != null ) {  
			if( value instanceof BigDecimal ) {  
				ret = (BigDecimal) value;  
			} else if( value instanceof String ) {  
				ret = new BigDecimal( (String) value );  
			} else if( value instanceof BigInteger ) {  
				ret = new BigDecimal( (BigInteger) value );  
			} else if( value instanceof Number ) {  
				ret = new BigDecimal( ((Number)value).doubleValue() );  
			} else {  
				throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
			}  
		}  
		return ret;  
	}  


}
