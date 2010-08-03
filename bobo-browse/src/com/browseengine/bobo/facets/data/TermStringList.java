package com.browseengine.bobo.facets.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TermStringList extends TermValueList<String> {
	@Override
	public boolean add(String o) {
		if (o==null) o="";
		return ((List<String>)_innerList).add(o);
	}

	@Override
	protected List<?> buildPrimitiveList(int capacity) {
	  _type = String.class;
		if (capacity<0)
		{
			return new ArrayList<String>();	
		}
		else
		{
			return new ArrayList<String>(capacity);
		}
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o)>=0;
	}

	@Override
	public String format(Object o) {
		return (String)o;
	}

	@Override
	public int indexOf(Object o) {
		return Collections.binarySearch(((ArrayList<String>)_innerList), (String)o);
	}

	@Override
	public void seal() {
		((ArrayList<String>)_innerList).trimToSize();
	}

  @Override
  public boolean containsWithType(String val)
  {
    return Collections.binarySearch(((ArrayList<String>)_innerList), val)>=0;
  }

  @Override
  public int indexOfWithType(String o)
  {
    return Collections.binarySearch(((ArrayList<String>)_innerList), o);
  }

}
