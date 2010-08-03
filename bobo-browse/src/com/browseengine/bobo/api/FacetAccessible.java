package com.browseengine.bobo.api;

import java.util.List;

public interface FacetAccessible 
{
	/**
	 * Gets gathered top facets
	 * @return list of facets
	 */
	List<BrowseFacet> getFacets();
	
	/**
	 * Gets the facet given a value. This is a way for random accessing into the facet data structure.
	 * @param value Facet value
	 * @return a facet with count filled in
	 */
	BrowseFacet getFacet(String value);
  
	public void close();
	
	/**
	 * Returns an iterator to visit all the facets
	 * @return	Returns a FacetIterator to iterate over all the facets
	 */
	FacetIterator iterator();
}
