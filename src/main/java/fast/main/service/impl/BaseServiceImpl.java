package fast.main.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fast.main.mapper.BaseMapper;
import fast.main.service.BaseService;

@Service
@Transactional
public class BaseServiceImpl implements BaseService{
	
	@Autowired
	BaseMapper mapper;

	@Override
	public List<Map<String, Object>> query(String sql) {
		if(mapper==null){
			System.out.println("mapper为空。。。。。。。。。。。。");
		}
		return mapper.query(sql);
	}
	@Override
	public Map<String, Object> queryOne(String sql) {
		if(mapper==null){
			System.out.println("mapper为空。。。。。。。。。。。。");
		}
		sql="SELECT * FROM  (SELECT a.*,rownum RN FROM ("+sql+") a WHERE ROWNUM <= 1) WHERE RN >= 0";
		System.out.println(sql);
		List<Map<String, Object>> list=mapper.query(sql);
		if(list!=null&&list.size()==1) {
			return list.get(0);
		}else {
			return null;
		}
	}
	@Override
	public List<Map<String, Object>> query(String sql,String pageNo,String pageSize) {
		if(mapper==null){
			System.out.println("mapper为空。。。。。。。。。。。。");
		}
		int start=(Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize);
		int end=(Integer.parseInt(pageNo))*Integer.parseInt(pageSize);
		sql="SELECT * FROM  (SELECT a.*,rownum RN FROM ("+sql+") a WHERE ROWNUM <= "+end+") WHERE RN >"+start;
		System.out.println(sql);
		List<Map<String, Object>> list=mapper.query(sql);
		return list;
	}
	
	@Override
	public int queryCount(String sql) {
		return mapper.queryCount(sql);
	}  
	
	@Override
	public Integer insert(String sql) {
		return mapper.insert(sql);
	}

	@Override
	public Integer delete(String sql) {
		return mapper.delete(sql);
	}

	@Override
	public Integer update(String sql) {
		return mapper.update(sql);
	}

	@Override
	public List<Map<String, Object>> call(String sql) {
		return mapper.call(sql);
	}


}
