package com.gd.elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class ElementGenerator {
	
	private String url;
	
	private WebDriver oWebDriver;
	
	
	public ElementGenerator(WebDriver oWebDriver) {
		this.oWebDriver = oWebDriver;
	}


	public void GeneratePageObject() {
		// TODO Auto-generated method stub
		url = Property.url;
		String selector = "";
		WebDriverWait wait = new WebDriverWait(oWebDriver, 50000);
		wait.until(PageHelper.pageLoaded(oWebDriver));
		List<WebElement> allElements = new ArrayList<WebElement>();
		List<WebElement> elements = oWebDriver.findElements(Utils.getBy(Property.parentNodeLocator));
		for(String xpath : ElementHelper.xpaths())
		{
			allElements.addAll(oWebDriver.findElement(Utils.getBy(Property.parentNodeLocator)).findElements(By.xpath(xpath)));
		}

		String pageName = Property.pageFileName.equals("") ? PageHelper.generatePageNameWithUrl(url) : Property.pageFileName;
		PageBean page = new PageBean(url,pageName);
		
		for(WebElement e : allElements)
		{
			
			if(e.isDisplayed())
			{
				selector = getSelectorFromElement(e);				
				
				if("".equals(selector))
				{
					continue;
				}				
				
				ElementHelper elementHelper = new ElementHelper(e);
				String elementName = elementHelper.evaluateElementName();
				
				//store elements to a data model
				ElementBean ele = new ElementBean(elementName, selector);
				if(!"".equals(elementHelper.getText()) && !e.getTagName().toLowerCase().matches("select"))
				{
					ele.addDefaultValue("text", elementHelper.getText());
				}
				page.addElement(ele);
			}			
				
		}
	
			

		//store page to a file
		try {
			String SavePath = Property.SaveToPath.equals("") ? Property.DefaultPath : Property.SaveToPath;
			if(SavePath.equals(Property.DefaultPath))
			{
				File dir = new File(SavePath, "/pages/");
				if(dir.exists() && dir.isDirectory())
				{
					SavePath = dir.getAbsolutePath();
				}
				
				if(!dir.exists() || !dir.isDirectory())
				{
					dir.mkdir();
					SavePath = dir.getAbsolutePath();
				}					
			}
			
			Save save = new Save(page);
			File pagefile = save.toPageObjectFile(SavePath, Property.PageObject_Type);	
			//delete file if already exists
			//File pagefile = new File(SavePath, "" + pageName + ".rb");
			if(pagefile.exists())
			{
				pagefile.delete();				
			}
		
			FileUtils.writeStringToFile(pagefile, save.getPageStream().toString(), "utf-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private String getSelectorFromElement(WebElement e) {		
		
		
		String selector = "";
		String getselectorjs = readScriptImpl("/com/gd/pogen/resources/getSelector.js");
		selector = (String) ((RemoteWebDriver) oWebDriver).executeScript(getselectorjs + " return utils.getCssSelectorFromNode(arguments[0]);", e);
		
		return selector;
	}
	
	private static  String readScriptImpl(String script) {
	    URL url = ElementGenerator.class.getResource(script);

	    if (url == null) {
	      throw new RuntimeException("Cannot locate " + script);
	    }

	    try {
	      return Resources.toString(url, Charsets.UTF_8);
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }	
}
