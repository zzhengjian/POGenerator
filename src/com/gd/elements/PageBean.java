package com.gd.elements;

import java.util.ArrayList;

public class PageBean {
	
	public static String Platform;

	private String pageName;
	private String url;

	private ArrayList<ElementBean> elements = new ArrayList<>();
	
	public PageBean(String url, String pageName)
	{
		this.url = url;
		this.pageName = pageName;
	}	
	
	public PageBean(String pageName)
	{
		this.pageName = pageName;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public ArrayList<ElementBean> getElements() {
		return elements;
	}

	public void setElements(ArrayList<ElementBean> elements) {
		this.elements = elements;
	}
	
	
	public boolean addElement(ElementBean e)
	{
		int index = 0;
		while(elements.contains(e))
		{
			//make duplicated elementName to be ElementName_Tag_1
			String elementName = e.getElementName();
			int len = elementName.split("_").length;

			try {
				
				index = Integer.parseInt(elementName.split("_")[len-1]);				
				e.setElementName(elementName.replace("_"+index, "_" + (index + 1)));				
			} catch (NumberFormatException e1) {
				
				e.setElementName(elementName + "_" + (index + 1));
			}			

		}
		
		elements.add(e);
		return true;
	}
	
	public void removeElement(ElementBean e)
	{
		elements.remove(e);	
	}
	
	
	public int getElementCount()
	{		
		return elements.size();
	}

	
	
}
