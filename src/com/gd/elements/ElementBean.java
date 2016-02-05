package com.gd.elements;

import java.util.HashMap;
import java.util.Map.Entry;

public class ElementBean {	
	
	private String elementName;
	private String selector;
	
	private HashMap<String, String> defaultValues;

	public ElementBean(String elementName, String selector)
	{
		this.elementName = elementName;
		this.selector = selector;
	}
	
	public ElementBean(String elementName)
	{
		this.elementName = elementName;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public HashMap<String, String> getDefaultValues() {
		return defaultValues;
	}

	public void setDefaultValues(HashMap<String, String> defaultValues) {
		this.defaultValues = defaultValues;
	}
	
	public void addDefaultValue(String key, String value)
	{		
		if(defaultValues==null)
		{
			this.defaultValues = new HashMap<String, String>();
		}			
		this.defaultValues.put(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		
	    if (!(obj instanceof ElementBean)) {
	        return false;
	      }
		ElementBean other = (ElementBean)obj;
		return elementName.equals(other.elementName);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("Name: ").append(this.elementName).append("\n");
		text.append("selector: ").append(this.selector).append("\n");
		if(defaultValues == null)
		{
			return text.toString();
		}
		defaultValues.entrySet();
		for(Entry<String,String> en : defaultValues.entrySet())
		{
			text.append(en.getKey()).append(": ").append(en.getValue()).append("\n");
		}
		return text.toString();
	}


}
