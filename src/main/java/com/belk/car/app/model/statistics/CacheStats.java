package com.belk.car.app.model.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheStats implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1656643536842752455L;

	public static final String TOTAL = "Total";
	private String name;
	private long totalCacheHits;
	private long totalCacheMiss;
	private long totalCount;	
	private long inMemoryCount;
	private List<CacheStats> domainStats = new ArrayList<CacheStats>();
	
	
	
	/**
	 * @param name
	 * @param totalCacheHits
	 * @param totalCacheMiss
	 */
	public CacheStats(String name, long totalCacheHits, long totalCacheMiss) {
		super();
		this.name = name;
		this.totalCacheHits = totalCacheHits;
		this.totalCacheMiss = totalCacheMiss;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the totalCacheHits
	 */
	public long getTotalCacheHits() {
		return totalCacheHits;
	}
	/**
	 * @param totalCacheHits the totalCacheHits to set
	 */
	public void setTotalCacheHits(long totalCacheHits) {
		this.totalCacheHits = totalCacheHits;
	}
	/**
	 * @return the totalCacheMiss
	 */
	public long getTotalCacheMiss() {
		return totalCacheMiss;
	}
	/**
	 * @param totalCacheMiss the totalCacheMiss to set
	 */
	public void setTotalCacheMiss(long totalCacheMiss) {
		this.totalCacheMiss = totalCacheMiss;
	}
	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the domainStats
	 */
	public List<CacheStats> getDomainStats() {
		return domainStats;
	}
	/**
	 * @param domainStats the domainStats to set
	 */
	public void setDomainStats(List<CacheStats> domainStats) {
		this.domainStats = domainStats;
	}
	
	
	public long getInMemoryCount() {
		return inMemoryCount ;
	}
	
	public void setInMemoryCount(long inMemoryCount) {
		this.inMemoryCount = inMemoryCount;
	}
	
	
	
}
