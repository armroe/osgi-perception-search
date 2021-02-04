package net.yeeyaa.perception.search;

import net.yeeyaa.eight.PlatformException.Type;

public enum SearchError implements Type {
	ERROR_CONNECTION_FAIL,
	QUERY_EXCEPTION,
	METHOD_NOT_SUPPORT;
	
	@Override
	public String getMessage() {	
		return name();
	}

	@Override
	public void setMessage(String message) {}

	@Override
	public Integer getCode() {
		return ordinal();
	}

	@Override
	public void setCode(Integer code) {}

	@Override
	public String getCate() {
		return getDeclaringClass().getSimpleName();
	}
	
	@Override
	public void setCate(String cate) {}
}