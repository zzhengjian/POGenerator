package com.gd.elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;

public class Utils {
	
	
	
	public static String capitalizeWord(String text)
	{	
		if(text==null)
		{
			throw new WebDriverException("error input");
		}
		
		if(isCamelCased(text))
		{
			return text;
		}
	
		if(text.length() > 1)
		{					
			return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
		}
		return text.toUpperCase();
		
	}

	public static String getPageNameFromUrl(String url)
	{
		String[] _url = url.split("/");		
		String[] _pageName = _url[_url.length - 1].split("#")[0].split("\\?")[0].split("-");
		String pageName = "";
		for(int index=0;index<_pageName.length;index++)
		{
			pageName += capitalizeWord(_pageName[index]);
		}
		
		return pageName;
	}

	/**
	 * evaluate text to generate a Element Name
	 * */
	public static String evalName(String text)
	{
		Pattern pattern = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]+",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		
		StringBuilder _text = new StringBuilder("");
		int i = 0;
		while(matcher.find())
		{			
			_text.append(capitalizeWord(matcher.group()));
			i++;
			if(i==3) break;				
		}
				
		return _text.toString();
	}
	
	public static String replaceAllSpecialChar(String text)
	{

		return text.replaceAll("[-+.^:,?#$&*@!\"{}<>~;']","");
		
	}
	
	
	public static boolean isCamelCased(String text)
	{
		
		return text.matches("^[A-Z].*")&&!text.equals(text.toUpperCase());
	}

	public static String getNameFromSrc(String src)
	{
		if(src==null)
		{
			return null;
		}		
		String[] _src = src.split("/");
		//return a source name
		return _src[_src.length - 1].split("\\.")[0];		
	}
	
	/**
	 * get By from String 
	 * this method only supports css and xpath
	 * 
	 * */
	public static By getBy(String selector)
	{
		
		if(isXpath(selector))
		{
			return By.xpath(selector);
		}
		else
		{
			return By.cssSelector(selector);
		}
	}
	
	public static boolean isXpath(String selector)
	{
		if(selector.startsWith("./") || selector.startsWith("/") || selector.startsWith("(/") || selector.startsWith("(./") || selector.startsWith("html"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public static String EscapeQuate(String text)
	{
		if(text != null && text.contains("\""))
		{
			return text.replace("\"", "\\\"");
		}
		return text;
	}
}
