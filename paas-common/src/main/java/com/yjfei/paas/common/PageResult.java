package com.yjfei.paas.common;
import lombok.Data;

@Data
public class PageResult<T> extends Result{
	
	private Page page = new Page(0,0,0);
	
	
	@Data
	public static class Page{
		private long total;
		private int current;
		private int pageSize;
		
		public Page(long total,int current,int pageSize){
			this.total = total;
			this.current = current;
			this.pageSize = pageSize;
		}
	}
	
}