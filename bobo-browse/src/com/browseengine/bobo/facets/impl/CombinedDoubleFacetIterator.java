package com.browseengine.bobo.facets.impl;

import java.util.List;
import java.util.NoSuchElementException;

import com.browseengine.bobo.api.DoubleFacetIterator;

/**
 * @author "Xiaoyang Gu<xgu@linkedin.com>"
 * 
 */
public class CombinedDoubleFacetIterator extends DoubleFacetIterator
{

  public double facet;

  private static class DoubleIteratorNode
  {
    public DoubleFacetIterator _iterator;
    public double _curFacet;
    public int _curFacetCount;

    public DoubleIteratorNode(DoubleFacetIterator iterator)
    {
      _iterator = iterator;
      _curFacet = -1;
      _curFacetCount = 0;
    }

    public boolean fetch(int minHits)
    {
      if (minHits > 0)
        minHits = 1;
      if ((_curFacet = _iterator.nextDouble(minHits)) != -1)
      {
        _curFacetCount = _iterator.count;
        return true;
      }
      _curFacet = -1;
      _curFacetCount = 0;
      return false;
    }

    public String peek()// bad
    {
      throw new UnsupportedOperationException();
      // if(_iterator.hasNext())
      // {
      // return _iterator.getFacet();
      // }
      // return null;
    }
  }

  private final DoubleFacetPriorityQueue _queue;

  private List<DoubleFacetIterator> _iterators;

  private CombinedDoubleFacetIterator(final int length)
  {
    _queue = new DoubleFacetPriorityQueue();
    _queue.initialize(length);
  }

  public CombinedDoubleFacetIterator(final List<DoubleFacetIterator> iterators)
  {
    this(iterators.size());
    _iterators = iterators;
    for (DoubleFacetIterator iterator : iterators)
    {
      DoubleIteratorNode node = new DoubleIteratorNode(iterator);
      if (node.fetch(1))
        _queue.add(node);
    }
    facet = -1;
    count = 0;
  }

  public CombinedDoubleFacetIterator(final List<DoubleFacetIterator> iterators,
      int minHits)
  {
    this(iterators.size());
    _iterators = iterators;
    for (DoubleFacetIterator iterator : iterators)
    {
      DoubleIteratorNode node = new DoubleIteratorNode(iterator);
      if (node.fetch(minHits))
        _queue.add(node);
    }
    facet = -1;
    count = 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#getFacet()
   */
  public String getFacet()
  {
    return format(facet);
  }

  public String format(double val)
  {
    return _iterators.get(0).format(val);
  }

  public String format(Object val)
  {
    return _iterators.get(0).format(val);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#getFacetCount()
   */
  public int getFacetCount()
  {
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.browseengine.bobo.api.FacetIterator#next()
   */
  public String next()
  {
    if (!hasNext())
      throw new NoSuchElementException("No more facets in this iteration");

    DoubleIteratorNode node = (DoubleIteratorNode) _queue.top();

    facet = node._curFacet;
    double next = -1;
    count = 0;
    while (hasNext())
    {
      node = (DoubleIteratorNode) _queue.top();
      next = node._curFacet;
      if ((next != -1) && (next != facet))
        break;
      count += node._curFacetCount;
      if (node.fetch(1))
        _queue.updateTop();
      else
        _queue.pop();
    }
    return format(facet);
  }

  /**
   * This version of the next() method applies the minHits from the facet spec
   * before returning the facet and its hitcount
   * 
   * @param minHits
   *          the minHits from the facet spec for CombinedFacetAccessible
   * @return The next facet that obeys the minHits
   */
  public String next(int minHits)
  {
    int qsize = _queue.size();
    if (qsize == 0)
    {
      facet = -1;
      count = 0;
      return null;
    }

    DoubleIteratorNode node = (DoubleIteratorNode) _queue.top();
    facet = node._curFacet;
    count = node._curFacetCount;
    while (true)
    {
      if (node.fetch(minHits))
      {
        node = (DoubleIteratorNode) _queue.updateTop();
      } else
      {
        _queue.pop();
        if (--qsize > 0)
        {
          node = (DoubleIteratorNode) _queue.top();
        } else
        {
          // we reached the end. check if this facet obeys the minHits
          if (count < minHits)
          {
            facet = -1;
            count = 0;
          }
          break;
        }
      }
      double next = node._curFacet;
      if (next != facet)
      {
        // check if this facet obeys the minHits
        if (count >= minHits)
          break;
        // else, continue iterating to the next facet
        facet = next;
        count = node._curFacetCount;
      } else
      {
        count += node._curFacetCount;
      }
    }
    return format(facet);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#hasNext()
   */
  public boolean hasNext()
  {
    return (_queue.size() > 0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Iterator#remove()
   */
  public void remove()
  {
    throw new UnsupportedOperationException(
        "remove() method not supported for Facet Iterators");
  }

  /**
   * Lucene PriorityQueue
   * 
   */
  public static class DoubleFacetPriorityQueue
  {
    private int size;
    private int maxSize;
    protected DoubleIteratorNode[] heap;

    /** Subclass constructors must call this. */
    protected final void initialize(int maxSize)
    {
      size = 0;
      int heapSize;
      if (0 == maxSize)
        // We allocate 1 extra to avoid if statement in top()
        heapSize = 2;
      else
        heapSize = maxSize + 1;
      heap = new DoubleIteratorNode[heapSize];
      this.maxSize = maxSize;
    }

    public final void put(DoubleIteratorNode element)
    {
      size++;
      heap[size] = element;
      upHeap();
    }

    public final DoubleIteratorNode add(DoubleIteratorNode element)
    {
      size++;
      heap[size] = element;
      upHeap();
      return heap[1];
    }

    public boolean insert(DoubleIteratorNode element)
    {
      return insertWithOverflow(element) != element;
    }

    public DoubleIteratorNode insertWithOverflow(DoubleIteratorNode element)
    {
      if (size < maxSize)
      {
        put(element);
        return null;
      } else if (size > 0 && !(element._curFacet < heap[1]._curFacet))
      {
        DoubleIteratorNode ret = heap[1];
        heap[1] = element;
        adjustTop();
        return ret;
      } else
      {
        return element;
      }
    }

    /** Returns the least element of the PriorityQueue in constant time. */
    public final DoubleIteratorNode top()
    {
      // We don't need to check size here: if maxSize is 0,
      // then heap is length 2 array with both entries null.
      // If size is 0 then heap[1] is already null.
      return heap[1];
    }

    /**
     * Removes and returns the least element of the PriorityQueue in log(size)
     * time.
     */
    public final DoubleIteratorNode pop()
    {
      if (size > 0)
      {
        DoubleIteratorNode result = heap[1]; // save first value
        heap[1] = heap[size]; // move last to first
        heap[size] = null; // permit GC of objects
        size--;
        downHeap(); // adjust heap
        return result;
      } else
        return null;
    }

    public final void adjustTop()
    {
      downHeap();
    }

    public final DoubleIteratorNode updateTop()
    {
      downHeap();
      return heap[1];
    }

    /** Returns the number of elements currently stored in the PriorityQueue. */
    public final int size()
    {
      return size;
    }

    /** Removes all entries from the PriorityQueue. */
    public final void clear()
    {
      for (int i = 0; i <= size; i++)
      {
        heap[i] = null;
      }
      size = 0;
    }

    private final void upHeap()
    {
      int i = size;
      DoubleIteratorNode node = heap[i]; // save bottom node
      int j = i >>> 1;
      while (j > 0 && (node._curFacet < heap[j]._curFacet))
      {
        heap[i] = heap[j]; // shift parents down
        i = j;
        j = j >>> 1;
      }
      heap[i] = node; // install saved node
    }

    private final void downHeap()
    {
      int i = 1;
      DoubleIteratorNode node = heap[i]; // save top node
      int j = i << 1; // find smaller child
      int k = j + 1;
      if (k <= size && (heap[k]._curFacet < heap[j]._curFacet))
      {
        j = k;
      }
      while (j <= size && (heap[j]._curFacet < node._curFacet))
      {
        heap[i] = heap[j]; // shift up child
        i = j;
        j = i << 1;
        k = j + 1;
        if (k <= size && (heap[k]._curFacet < heap[j]._curFacet))
        {
          j = k;
        }
      }
      heap[i] = node; // install saved node
    }
  }

  @Override
  public double nextDouble()
  {
    if (!hasNext())
      throw new NoSuchElementException("No more facets in this iteration");

    DoubleIteratorNode node = (DoubleIteratorNode) _queue.top();

    facet = node._curFacet;
    double next = -1;
    count = 0;
    while (hasNext())
    {
      node = (DoubleIteratorNode) _queue.top();
      next = node._curFacet;
      if ((next != -1) && (next != facet))
        break;
      count += node._curFacetCount;
      if (node.fetch(1))
        _queue.updateTop();
      else
        _queue.pop();
    }
    return facet;
  }

  @Override
  public double nextDouble(int minHits)
  {
    int qsize = _queue.size();
    if (qsize == 0)
    {
      facet = -1;
      count = 0;
      return -1;
    }

    DoubleIteratorNode node = (DoubleIteratorNode) _queue.top();
    facet = node._curFacet;
    count = node._curFacetCount;
    while (true)
    {
      if (node.fetch(minHits))
      {
        node = (DoubleIteratorNode) _queue.updateTop();
      } else
      {
        _queue.pop();
        if (--qsize > 0)
        {
          node = (DoubleIteratorNode) _queue.top();
        } else
        {
          // we reached the end. check if this facet obeys the minHits
          if (count < minHits)
          {
            facet = -1;
            count = 0;
          }
          break;
        }
      }
      double next = node._curFacet;
      if (next != facet)
      {
        // check if this facet obeys the minHits
        if (count >= minHits)
          break;
        // else, continue iterating to the next facet
        facet = next;
        count = node._curFacetCount;
      } else
      {
        count += node._curFacetCount;
      }
    }
    return facet;
  }
}