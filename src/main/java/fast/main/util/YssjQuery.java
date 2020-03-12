package fast.main.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import fast.main.service.BaseService;

/**
 * 税务登记
 * 
 * @author Administrator
 *
 */
public class YssjQuery implements Callable<List<Map<String, Object>>> {
	
	private BaseService bs = null;
	private String sql = null;
	private Integer pageNo = null;
	private Integer pageSize = null;
	private int index = 0;



	

	public YssjQuery(BaseService bs, String sql, Integer pageNo, Integer pageSize, int index) {
		super();
		this.bs = bs;
		this.sql = sql;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.index = index;
	}





	@Override
	public List<Map<String, Object>> call() throws Exception {
		System.out.println("线程ID：" + Thread.currentThread().getId() + "=====开始！第" + index + "个任务");
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		
		try {
			lists = bs.query(sql, String.valueOf(pageNo), String.valueOf(pageSize));
			
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
			return lists;
		}

	}


}
