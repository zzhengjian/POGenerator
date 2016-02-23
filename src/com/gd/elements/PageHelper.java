package com.gd.elements;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PageHelper {
	
	public static boolean isFlex = true;
	public String WorkspacePath = "C:\\azheng-QA-Workspace\\QA\\Cucumber\\Projects";
	private String PageName;
	private String Url;
	
	public String getPageName() {
		return PageName;
	}

	public void setPageName(String pageName) {
		PageName = pageName;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
		
	}
	
	public static String getPagePrefix(Projects project)
	{
		String toReturn = null;
		switch(project)
		{
			case GD:
				toReturn =  isFlex ? "GDFlex_" : "GD_";
				break;
			case Nascar:
				toReturn =  isFlex ? "NascarFlex_" : "Nascar_";
				break;
			case Rush:
				toReturn =  isFlex ? "RushFlex_" : "Rush_";
				break;
			case Walmart:
				toReturn =  "";
				break;
			case GoBank:
				toReturn = "";
			case FSC:
				toReturn = "";
			default:
				toReturn = "";
				break;
				
		}
		
		return toReturn;
	}
	
	public static Projects getProjectFromUrl(String url)
	{
		if(url.contains("walmartmoneycard"))
			return Projects.Walmart;
		else if(url.contains("racing"))
			return Projects.Nascar;
		else if(url.contains("rush"))
			return Projects.Rush;
		else if(url.contains("greendot") && !url.contains("racing"))
			return Projects.GD;
		else if(url.contains("pos"))
			return Projects.FSC;
		else 
			throw new WebDriverException("Not implemented project");
		
	}

	public static String generatePageNameWithUrl(String url)
	{
		return  getPagePrefix(getProjectFromUrl(url)) + Utils.getPageNameFromUrl(url);
	}
	
	public static String generatePageNameWithProject(Projects project, String pageName)
	{
		return  getPagePrefix(project) + pageName;
	}
	
	public static StringBuilder buildTopPageLine(StringBuilder page, String url)
	{
		
		//page.append(PageHelper.getPagePrefix(PageHelper.getProjectFromUrl(url)));
		//page.append(Utils.getPageNameFromUrl(url));
		page.append("# encoding=utf-8").append("\n\n");
		page.append(Property.pageFileName);
		page.append(" = GdPage.new(\"");
		page.append(url).append("\")").append("\n\n");
		return page;		
	}
	
	
	public enum Projects {		
		GD,
		Walmart,
		GoBank,
		FSC,
		Nascar,
		Rush,
	}
	
	public void writeToFile(String pageText)
	{
		try {
			FileUtils.writeStringToFile(new File("C:/test.txt"), pageText, "utf-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
	}
	
	public static ExpectedCondition<Boolean> pageLoaded(final WebDriver driver) {
	    return new ExpectedCondition<Boolean>() {
	      @Override
	      public Boolean apply(WebDriver driver){ 

	        	return (Boolean)((JavascriptExecutor)driver).executeScript("return document.readyState == 'complete';");

      }

	      @Override
	      public String toString() {
	        return String.format("Page (%s) to become loaded", driver);
	      }
	    };
	 }
}
