package fast.main.mapper;

import java.util.List;
import java.util.Map;

import fast.main.util.Mode;

public interface BaseMapper {
	Integer insert(String sql);
	Integer delete(String sql);
	Integer update(String sql);
	List<Map<String, Object>> query(String sql);
	int queryCount(String sql);
	List<Map<String, Object>> call(String sql);
	
}
