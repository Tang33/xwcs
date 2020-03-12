package fast.main.service;

import java.util.List;
import java.util.Map;

public interface BaseService {
	Integer insert(String sql);
	Integer delete(String sql);
	Integer update(String sql);
	List<Map<String, Object>> query(String sql);
	Map<String, Object> queryOne(String sql);
	int queryCount(String sql);
	List<Map<String, Object>> call(String sql);
	List<Map<String, Object>> query(String sql, String pageNo, String pageSize);
}
