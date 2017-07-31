package com.gd.elements;

import java.io.File;

import com.gd.constant.PageObjectType;
import com.gd.elements.Utils;
import com.google.gson.Gson;

public class Save {
	
	private PageBean page;
	
	private StringBuilder pageStream = new StringBuilder();
	
	public StringBuilder getPageStream() {
		return pageStream;
	}

	public Save(PageBean page)
	{
		this.page = page;
	}
	
	public File toPageObjectFile(String path, int type)
	{
		File file = null;
		switch(type)
		{
			case PageObjectType.PAGEOBJECT_IN_CUCUMBER:
				toCucumber();
				file = new File(path, page.getPageName() + ".rb");
				break;
			case PageObjectType.PAGEOBJECT_IN_JAVA:
				toPOM();
				file = new File(path, page.getPageName() + ".java");
				break;
			case PageObjectType.PAGEOBJECT_IN_JSON:
				toJsonFile();
				file = new File(path, page.getPageName() + ".json");
				break;
			default:
				toCucumber();
				file = new File(path, page.getPageName() + ".rb");
				break;
		}
		return file;
		
		
	}
	
	public void toCucumber()
	{
		pageStream.append("# encoding=utf-8").append("\n\n");
		pageStream.append(page.getPageName());
		pageStream.append(" = GdPage.new(\"");
		pageStream.append(page.getUrl()).append("\")").append("\n\n");
		
		for(ElementBean ele : page.getElements())
		{
			String selector = ele.getSelector();
			pageStream.append(page.getPageName());
			pageStream.append(".addElement(\"").append(ele.getElementName()).append("\",").append("\n");
			pageStream.append("	GdElement.new(").append("\n");
			if(Utils.isXpath(selector)){
				pageStream.append("		:desktopxpath => \"").append(selector).append("\"").append(",\n");
				pageStream.append("		:rwdxpath => \"").append(selector).append("\"");
			}

			else{
				pageStream.append("		:desktopcss => \"").append(selector).append("\"").append(",\n");
				pageStream.append("		:rwdcss => \"").append(selector).append("\"");
			}
				
			
			if(ele.getDefaultValues() !=null && ele.getDefaultValues().containsKey("text") && ele.getDefaultValues().get("text") !=null )
			{
				pageStream.append(",\n").append("		:text => \"").append(Utils.EscapeQuate(ele.getDefaultValues().get("text"))).append("\"");
			}
			pageStream.append("\n").append("		)").append("\n");
			pageStream.append("	)").append("\n\n");
		}
		
		pageStream.append("Pages.addPage(\"").append(page.getPageName()).append("\", ").append(page.getPageName()).append(")");
	}
	
	public void toPOM()
	{
		toPOM(null);
	}
	
	public void toPOM(String packageName)
	{
		if(packageName !=null && !"".equals(packageName))
		{
			pageStream.append("package ").append(packageName).append(";\n\n");
		}
		
		pageStream
		.append("import org.openqa.selenium.WebElement;").append("\n")
		.append("import org.openqa.selenium.support.FindBy;").append("\n\n")
		.append("public class ").append(page.getPageName()).append(" {").append("\n\n")
		.append("	public final static String url = \"").append(page.getUrl()).append("\";\n\n");
		for(ElementBean ele :page.getElements())
		{
			
			String selector = ele.getSelector();
			String how = Utils.isXpath(selector) ? "xpath" : "css";
			pageStream.append("	@FindBy(").append(how).append(" = \"").append(selector).append("\")").append("\n")
			.append("	WebElement ").append("o").append(ele.getElementName()).append(";\n\n");
		}
		pageStream.append("}");		
		
	}
	
	
	public void toJsonFile()
	{
		String json = new Gson().toJson(page);
		pageStream.append(json);
	}
}
