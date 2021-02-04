package net.yeeyaa.perception.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yeeyaa.eight.IBiProcessor;
import net.yeeyaa.eight.IProcessor;
import net.yeeyaa.eight.ITriProcessor;
import net.yeeyaa.eight.PlatformException;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;


public class SqlUtils implements IProcessor<Object, Object>, ITriProcessor<String, String, String, List<Map<String, Object>>> {
    protected IProcessor<Map<String, Object>, Connection> search;
    protected IBiProcessor<String, String, Connection> query;
    protected String user;
    protected String password;
   
    public void setUser(String user) {
		this.user = user;
	}

	public void setQuery(IBiProcessor<String, String, Connection> query) {
		this.query = query;
	}

	public void setSearch(IProcessor<Map<String, Object>, Connection> search) {
		this.search = search;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Object process(Object parameter) {
		if (parameter != null) try {
			if (parameter instanceof Object[]) {
				Object[] paras = (Object[])parameter;
				if (paras.length > 0) switch (paras.length) {
					case 1: return operate(user, password, (String)paras[0]);
					case 2: return operate((String)paras[1], password, (String)paras[0]);
					default: return operate((String)paras[1], (String)paras[2], (String)paras[0]);
				}
			} else return operate(user, password, parameter.toString());
		} catch (Exception e) {
			throw new PlatformException(SearchError.QUERY_EXCEPTION, e);
		}
		return null;
	}

	@Override
    public List<Map<String, Object>> operate(String user, String password, String sql) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
        	if (query == null) {
	            Map<String, Object> map = new HashMap<String, Object>(8);
	            if (user != null) map.put("user", user);
	            if (password != null) map.put("password", password.toString());
	            conn = search.process(map);
        	} else conn = query.perform(user, password.toString());
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            return new RowMapperResultSetExtractor<Map<String, Object>>(new ColumnMapRowMapper()).extractData(rs);
        } catch (Exception e) {
			throw new PlatformException(SearchError.QUERY_EXCEPTION, e);
		} finally {
            try {
            	if (rs != null) rs.close();
            } catch (Exception e) {
    			throw new PlatformException(SearchError.QUERY_EXCEPTION, e);
    		} finally {
                try {
                	if (st != null) st.close();
                } catch (Exception e) {
        			throw new PlatformException(SearchError.QUERY_EXCEPTION, e);
        		} finally {
                    if (conn != null) try {
                    	conn.close();
                    } catch (Exception e) {
            			throw new PlatformException(SearchError.QUERY_EXCEPTION, e);
            		}
                }
            }
        }
    }
}
