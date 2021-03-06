package org.zkoss.zklargelivelist.model;

import java.util.ArrayList;
import java.util.List;

import bz.voter.management.Division;
import bz.voter.management.utils.FilterType;

import org.zkoss.zul.AbstractListModel;

public abstract class AbstractDivisionVotersPagingListModel<T> extends AbstractListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6613208067174831719L;
	
	private int _startPageNumber;
	private int _pageSize;
	private int _itemStartNumber;
	private Division _division;
	
	//internal use only
	private List<T> _items = new ArrayList<T>();

	public AbstractDivisionVotersPagingListModel(){
	}
	

	public AbstractDivisionVotersPagingListModel(Division division, int startPageNumber, int pageSize) {
		super();
		
		initialize(division, startPageNumber, pageSize);
		
		_items = getPageData(_division,_itemStartNumber, _pageSize);
	}


	public AbstractDivisionVotersPagingListModel(String search,Division division, int startPageNumber, int pageSize) {
		super();
		
		initialize(division, startPageNumber, pageSize);
		
		_items = getPageData(search, _division,_itemStartNumber, _pageSize);
	}

	public AbstractDivisionVotersPagingListModel(FilterType filterType ,Object filterObject,Division division, int startPageNumber, int pageSize) {
		super();
		
		initialize(division, startPageNumber, pageSize);
		
		_items = getPageData(filterType, filterObject, _division,_itemStartNumber, _pageSize);
	}

	private void initialize(Division division, int startPageNumber, int pageSize){
		this._division = division;
		this._startPageNumber = startPageNumber;
		this._pageSize = pageSize;
		this._itemStartNumber = startPageNumber * pageSize;
	}
	
	public abstract int getTotalSize();
	protected abstract List<T> getPageData(Division division, int itemStartNumber, int pageSize);
	protected abstract List<T> getPageData(String search,Division division, int itemStartNumber, int pageSize);
	protected abstract List<T> getPageData(FilterType filterType,Object filterObject,Division division, int itemStartNumber, int pageSize);
	
	@Override
	public Object getElementAt(int index) {
		return _items.get(index);
	}

	@Override
	public int getSize() {
		return _items.size();
	}
	
	public int getStartPageNumber() {
		return this._startPageNumber;
	}
	
	public int getPageSize() {
		return this._pageSize;
	}
	
	public int getItemStartNumber() {
		return _itemStartNumber;
	}
}
