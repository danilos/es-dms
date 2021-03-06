package com.github.richardwilly98.esdms;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.richardwilly98.esdms.api.SearchResult;
import com.google.common.base.Objects;

public class SearchResultImpl<T> implements SearchResult<T> {

	private static Logger log = Logger.getLogger(SearchResultImpl.class);
	
	private long milliseconds;	
	private Set<T> resultItems;
	private int index;
	private int pageSize;
	private String statement;
	Set<String> view;
	
	public SearchResultImpl(){
		this(0,25,null,null);
	}
	
	public SearchResultImpl(int index, int pageSize, String statement, Set<String> view){
		this.index = index;
		this.pageSize = pageSize;
		this.statement = statement;
		this.view = view;
		resultItems = new LinkedHashSet<T>();
	}
	
	public void setPageIndex(int page){
		this.index = page * pageSize;
	}
	
	public int	getPageIndex(){
		return this.index / pageSize;
	}
	
	public int	getPageSize(){
		return this.pageSize;
	}
	
	public void setPageSize(int size){
		this.pageSize = size;
	}
	public String getStatement(){
		return this.statement;
	}
	public void setStatement(String statement){
		this.statement = statement;
	}
	
	public Set<String> getColumns(){
		throw new UnsupportedOperationException();
//		Set<String> columns = new HashSet<String>();
//		//extract columns from query statement;
//		return columns;
	}
	
	public void setView(Set<String> view){
		throw new UnsupportedOperationException();
		//this.view = view;
	}
	public Set<String> getView(){
		throw new UnsupportedOperationException();
		//return this.view;
	}
	
	public int getTotalHits(){
		return this.resultItems.size();
	}
	public boolean	hasData(){
		log.debug("entering hasData(): resultItems.size: " + resultItems.size() + " index position: " + this.index);
		return this.resultItems.size() > this.index;
	}
	public Set<T> getItems(){
		return this.resultItems;
	}

	public Set<T>	getNextPage(){
		log.debug("entering getNextPage()");
		if(index > this.resultItems.size()) return null;
		
		Set<T> page = new LinkedHashSet<T>();
		Iterator<T> iterat = this.resultItems.iterator();
		
		for(int pos = 0; pos < index; pos++) iterat.next();
		
		for (int i=0; i< pageSize; i++){
			if(!iterat.hasNext()) break;
			index++;
			page.add(iterat.next());
		}
		
		log.debug("returning page with: " + page.size() + " result items");
		log.debug("exiting getNextPage()");
		return page;
	}
	
	public Set<T>	getPage(int page){
		log.debug("entering getPage()");
		int index = page * this.pageSize;
		if(index > this.resultItems.size()) return null;
		
		Set<T> data = new LinkedHashSet<T>();
		Iterator<T> iterat = this.resultItems.iterator();
		
		for(int pos = 0; pos < index; pos++) iterat.next();
		
		for (int i=0; i<pageSize; i++){
			if(!iterat.hasNext()) break;
			index++;
			data.add(iterat.next());
		}
		
		log.debug("returning page with: " + data.size() + " result items");
		log.debug("exiting getPage()");
		return data;
	}
	
	public void sort(Comparator<T> c){
		List<T> list = new ArrayList<T>(this.resultItems);
		Collections.sort(list, c);
		this.resultItems.clear();
		this.resultItems.addAll(list);
	}
	
	public void setElapsedTime(long milliseconds)
	{
		this.milliseconds = milliseconds;
	}
	
	public long getElapsedTime(){
		return this.milliseconds;
	}
	
	public void first(){
		index = 0;
	}
	
	public int size(){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return resultItems.size();
	}
	
	public boolean isEmpty(){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.isEmpty();
	}
	
	public boolean add(T element){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.add(element);
	}
	
	public boolean remove(T element){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.remove(element);
	}
	
	public Iterator<T> iterator(){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.iterator();
	}
	
	public boolean contains(T element){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.contains(element);
	}
	
	public boolean containsAll(Collection<T> c){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.containsAll(c);
	}
	
	public boolean addAll(Collection<T> c){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.addAll(c);
	}
		
	public boolean removeAll(Collection<T> c){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.removeAll(c);
	}
	
	public boolean retainAll(Collection<T> c){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.retainAll(c);
	}
	
	public void clear(){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		this.resultItems.clear();
	}
	
	public Object[] toArray(){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.toArray();
	}
	
	public T[] toArray(T[] a){
		if (this.resultItems == null) this.resultItems = new LinkedHashSet<T>();
		return this.resultItems.toArray(a);
	}
	
	public String toString(){
		return Objects.toStringHelper(this.resultItems).add("milliseconds", this.milliseconds).toString();
	}
	
	
}