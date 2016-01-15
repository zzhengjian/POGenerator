package com.gd.elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gd.elements.PageHelper.Projects;
import com.google.common.collect.ImmutableMap;

public class ElementGenerator {
	
	private String url;

	
	public void GeneratePageObject(WebDriver oWebDriver) {
		// TODO Auto-generated method stub
		url = Property.url;
		WebDriverWait wait = new WebDriverWait(oWebDriver, 20000);
		wait.until(PageHelper.pageLoaded(oWebDriver));
		List<WebElement> allElements = new ArrayList<WebElement>();
		List<WebElement> elements = oWebDriver.findElements(Utils.getBy(Property.parentNodeLocator));
		for(String xpath : ElementHelper.xpaths())
		{
			allElements.addAll(oWebDriver.findElement(Utils.getBy(Property.parentNodeLocator)).findElements(By.xpath(xpath)));
		}
		
		StringBuilder spage = new StringBuilder();
		String pageName = Property.pageFileName.equals("") ? PageHelper.generatePageNameWithUrl(url) : Property.pageFileName;
		PageHelper.buildTopPageLine(spage, url);
		for(WebElement e : allElements)
		{
			if(e.isDisplayed())
			{

				Response response = null;
				String selector = "";
				Command command = new Command(((FirefoxDriver)oWebDriver).getSessionId(),DriverCommand.ELEMENT_EQUALS,ImmutableMap.of("id", ((RemoteWebElement)e).getId(),"other", ((RemoteWebElement)e).getId()));
				try {
					response = ((FirefoxDriver)oWebDriver).getCommandExecutor().execute(command);
					selector = (String)response.getValue();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				if("".equals(selector))
				{
					continue;
				}
				ElementHelper elementHelper = new ElementHelper(e);				
				spage.append(elementHelper.addNewGDElement(pageName, selector));
			}
				
		}
		spage.append("Pages.addPage(\"").append(pageName).append("\", ").append(pageName).append(")");
		System.out.println(spage.toString());
		
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
			
			//delete file if already exists
			File pagefile = new File(SavePath, "" + pageName + ".rb");
			if(pagefile.exists())
			{
				pagefile.delete();				
			}
		
			FileUtils.writeStringToFile(pagefile, spage.toString(), "utf-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
