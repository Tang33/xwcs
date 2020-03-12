package fast.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fast.main.util.JdbcConnectedPro;
import fast.main.util.Mode;
import fast.main.util.Super;

public class yskmcx extends Super{

    public String init(Map<String, Object> rmap){
        try{
            //初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
            initMap(rmap);
            return "sssjgl/yskmcx";
        }catch(Exception e){
            e.printStackTrace();
            return "sssjgl/yskmcx";
        }
    }


    // 导出excel
    public Object export(Map<String, Object> rmap) {
        try {
            // 初始化 bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）
            initMap(rmap);
            String yearNmonth = getValue(this.getForm().get("yearNmonth")).toString();//年月
            String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+yearNmonth+"'";
            String sqlall="";
            if(this.getBs().queryOne(sql_check) == null){
                sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by f.yskmdm";
            }else{
                sqlall="select f.YSKMDM dm,f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+yearNmonth+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by dm";
            }
            List<Map<String, Object>> map=this.getBs().query(sqlall);

            Map<String, Object> map1 = new HashMap<String, Object>();
            String[] cols = { "预算科目代码", "预算科目名称", "地方口径" };
            String[] keys = { "YSKMDM", "YSKMMC", "SUM2" };
            map1.put("fileName", yearNmonth+"按预算科目代码汇总.xls");
            map1.put("cols", cols);
            map1.put("keys", keys);
            map1.put("list", map);
            return map1;
        } catch (Exception e) {
            e.printStackTrace();
            return this.toJson("009", "查询异常！");
        }
    }






    public String yskm(Map<String, Object> rmap) {
        try {
            initMap(rmap);
            String date = getValue(this.getForm().get("date"));
			Object state = this.getRequest().getSession().getAttribute("state");
            int count = 0;
            String page = getValue(this.getForm().get("pageNo"));
            String pagesize = getValue(this.getForm().get("pageSize"));
            List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
           String sql_check = "select * from xwcs_gsdr_temp where rk_rq='"+date+"'";
            if(this.getBs().queryOne(sql_check) == null){
                String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(dfzse) sum2 from SB_NSRXX where yskmdm is not null and to_char(rk_rq,'yyyymm')='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by f.yskmdm";
                System.out.println(sqlall);
                List<Map<String, Object>> listd=this.getBs().query(sqlall);
                count = listd.size();
                this.getRequest().getSession().setAttribute("listd",listd);
                this.getRequest().getSession().setAttribute("count",count);
                for (int i = 0; i < listd.size(); i++) {
                    if(i >= (Integer.parseInt(page)-1)*Integer.parseInt(pagesize) && i<=Integer.parseInt(page)*Integer.parseInt(pagesize)){
                        Map<String, Object> map=listd.get(i);
                        map.put("yskmdm", map.get("YSKMDM"));
                        map.put("yskmmc", map.get("YSKMMC"));
                        map.put("qkj", map.get("SUM1"));
                        map.put("dfkj", map.get("SUM2"));
                        map.put("sj", date);
                        list.add(map);
                    }
                }
                return this.toJson("000", "查询成功！",list, count);
            }else{
                String sqlall="select f.* from (select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{3}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{3}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{5}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{5}')) b on a.yskmdm = b.yskmdm union all select a.yskmmc,b.* from dm_yskm a right join (select regexp_substr(yskmdm,'^\\d{7}') yskmdm,sum(zse) sum1,sum(se) sum2 from xwcs_gsdr_temp where yskmdm is not null and rk_rq='"+date+"' group by regexp_substr(yskmdm,'^\\d{7}')) b on a.yskmdm = b.yskmdm) f where sum2 !='0' order by f.yskmdm";
                System.out.println(sqlall);
                List<Map<String, Object>> listd=this.getBs().query(sqlall);
                count = listd.size();
                this.getRequest().getSession().setAttribute("listd",listd);
                this.getRequest().getSession().setAttribute("count",count);
                for (int i = 0; i < listd.size(); i++) {
                    if(i >= (Integer.parseInt(page)-1)*Integer.parseInt(pagesize) && i<=Integer.parseInt(page)*Integer.parseInt(pagesize)){
                        Map<String, Object> map=listd.get(i);
                        map.put("yskmdm", map.get("YSKMDM"));
                        map.put("yskmmc", map.get("YSKMMC"));
                        map.put("qkj", map.get("SUM1"));
                        map.put("dfkj", map.get("SUM2"));
                        map.put("sj", date);
                        list.add(map);
                    }
                }
                return this.toJson("000", "查询成功！",list, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.toJson("009", "查询失败！");
        }
    }

}