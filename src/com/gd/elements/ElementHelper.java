package com.gd.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementHelper {
	
	private WebElement oWebElement;
	private String text;
	private String tagName;
	private boolean usingId = true;
	private boolean usingName = true;
	private boolean usingClassName = true;
	private boolean usingDataContent = false;
	
	private String ElementName;

	public ElementHelper(WebElement oWebElement)
	{
		this.oWebElement = oWebElement;
		text = oWebElement.getText().trim();
		tagName = oWebElement.getTagName();
	}
	
	
	public String getElementName() {
		return ElementName;
	}

	public void setElementName(String elementName) {
		ElementName = elementName;
	}

	public String getText() {
		return text;
	}

	public boolean isUsingId() {
		return usingId;
	}


	public void setUsingId(boolean usingId) {
		this.usingId = usingId;
	}


	public boolean isUsingName() {
		return usingName;
	}


	public void setUsingName(boolean usingName) {
		this.usingName = usingName;
	}
	

	public boolean isUsingClassName() {
		return usingClassName;
	}



	public void setUsingClassName(boolean usingClassName) {
		this.usingClassName = usingClassName;
	}



	public String evaluateElementName()	
	{
		StringBuilder elementName = new StringBuilder("");
		//get attribute of the element to build up an element name
		String id = oWebElement.getAttribute("id") == null ? "" : oWebElement.getAttribute("id").trim();
		String name = oWebElement.getAttribute("name") == null ? "" : oWebElement.getAttribute("name").trim();
		String src = oWebElement.getAttribute("src") == null ? "" : oWebElement.getAttribute("src").trim();		
		String className = oWebElement.getAttribute("class") == null ? "" : oWebElement.getAttribute("class").trim();
		String datacontent = oWebElement.getAttribute("data-content") == null ? "" : oWebElement.getAttribute("data-content").trim();
		
		if(!datacontent.equals("") && text.equals(""))
		{			
			text = datacontent;
			usingDataContent = true;
		}
		
		if(tagName.matches("a|p|label|span|button|li|strong|h[1-9]|em|b") && text != null && !text.trim().equals(""))
		{
			Pattern pattern = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]+",Pattern.DOTALL);
			Matcher matcher = pattern.matcher(text);
			if(matcher.find())
			{
				setUsingId(false);	
				setUsingName(false);
			}
			
		}	
				
		//String tag = oWebElement.getTagName().toLowerCase();
		if(id !=null && !id.equals("") && isUsingId())
		{
			elementName.append(Utils.evalName(id));
		}			
		else if(name != null && !name.equals("") && isUsingName())
		{
			elementName.append(Utils.evalName(name));
		}						
		else if(src != null && !src.equals(""))	
		{
			elementName.append(Utils.evalName(Utils.getNameFromSrc(src)));
		}			
		else if(text != null && !text.trim().equals("") && !Utils.evalName(text).equals(""))
		{
			elementName.append(Utils.evalName(text));
		}
		else if(className != null && !className.equals("") && isUsingClassName())
		{
			elementName.append(Utils.evalName(className));
			
		}
		else
		{
			elementName.append("Element");
		}

		return elementName.toString() + elementNameExt();
	}
	

	public String elementNameExt()	
	{
		
		String tag = oWebElement.getTagName().toLowerCase();
		String text = oWebElement.getText();
		String className = oWebElement.getAttribute("class");
		StringBuilder _tag = new StringBuilder("");
		
        //to append Element Type
        if (tag.equals("a"))
        {
            _tag.append("_Link");
        }
        else if (tag.equals("button"))
        {
            _tag.append("_Button");
        }
        else if (tag.matches("b|p|span|div|li"))
        {
        	if(text != null && !text.equals(""))  		
        		
        		if(className.toLowerCase().contains("error"))
        			{_tag.append("_Error");}
        		else
        			{_tag.append("_Text");}
        	else
        		_tag.append("_").append(tag.toUpperCase());
        }

        else if (tag.equals("label"))
        {
            _tag.append("_Label");
        }
        else if (tag.equals("img"))
        {
            _tag.append("_Image");
        }
        else if (tag.equals("input"))
        {
            if (oWebElement.getAttribute("type").equals("radio"))
                _tag.append("_RadioButton");
            else if (oWebElement.getAttribute("type").equals("checkbox"))
                _tag.append("_CheckBox");
            else
                _tag.append("_Input");
        }
        else if (tag.equals("select"))
        {
            _tag.append("_DropDown");
        }

        else if (tag.equals("table"))
        {
            _tag.append("_Table");
        }
        else if (tag.matches("h1|h2|h3|h4|h5"))
        {
            _tag.append("_").append(tag).append("_Title");
        }
        else if (tag.equals("i"))
        {
            _tag.append("_Icon");
        }
        else
        {
            _tag.append("_Text");
        }
        
		return _tag.toString();
	}


	/**
	 * xpath/xpaths to list out all the available webelement that should be added to a page
	 * */
	public static List<String> xpaths()
	{
		ArrayList<String> xpathArr = new ArrayList<String>();
		
		xpathArr.add(".//*[not(ancestor::table)][normalize-space(translate(text(),' ',' '))][not(ancestor::select)][not(self::sup)][not(self::iframe)][not(self::frame)]|.//input[not(ancestor::table)][@type!='hidden']|(.//img|.//select|.//i|.//a|.//h1|.//h2|.//h3|.//h4)[not(ancestor::table)]");		
		return xpathArr;
	}
	

	
	public String addNewGDElement(String pageName, String selector)
	{
		String elementName = evaluateElementName();

		return updateGDElement(pageName, elementName, selector);
	}
	
	public String updateGDElement(String pageName, String elementName, String selector)
	{
		StringBuilder gdelement = new StringBuilder();
		gdelement.append(pageName);
		gdelement.append(".addElement(\"").append(elementName).append("\",").append("\n");
		gdelement.append("	GdElement.new(").append("\n");
		if(Utils.isXpath(selector))
			gdelement.append("		:desktopxpath => \"").append(selector).append("\"");
		else
			gdelement.append("		:desktopcss => \"").append(selector).append("\"");
		
		if(tagName.matches("a|h[1-9]|li|span|strong|div|p|label|em|b") && !text.trim().equals("") && !usingDataContent)
			gdelement.append(",\n").append("		:text => \"").append(Utils.EscapeQuate(text)).append("\"");
		gdelement.append("\n").append("		)").append("\n");
		gdelement.append("	)").append("\n\n");
		
		return gdelement.toString();
	}
	
	public boolean shouldTextbeAdded()
	{
		//if element has more than 2 child elements, we can skip to add it's text
		return this.oWebElement.findElements(By.xpath(".//*")).size() > 2;
	}
	
	public static boolean shouldTextbeAdded(WebElement oWebElement)
	{
		//if element has more than 2 child elements, we can skip to add it's text
		return oWebElement.findElements(By.xpath(".//*")).size() > 2;
	}



	
	
}
