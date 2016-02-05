package com.gd.elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

public class ElementGenerator {
	
	private String url;

	
	public void GeneratePageObject(WebDriver oWebDriver) {
		// TODO Auto-generated method stub
		url = Property.url;
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
				System.out.println(e.getTagName() + " : " +  e.getText());
				Response response = null;
				String selector = "";
				Command command = new Command(((FirefoxDriver)oWebDriver).getSessionId(),DriverCommand.ELEMENT_EQUALS,ImmutableMap.of("id", ((RemoteWebElement)e).getId(),"other", ((RemoteWebElement)e).getId()));
				try {
					response = ((FirefoxDriver)oWebDriver).getCommandExecutor().execute(command);
					selector = (String)response.getValue();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassCastException e2)
				{
					e2.printStackTrace();
				}
				
				
				if("".equals(selector))
				{
					continue;
				}
				
				
				
				ElementHelper elementHelper = new ElementHelper(e);
				String elementName = elementHelper.evaluateElementName();
				
				//store elements to a data model
				ElementBean ele = new ElementBean(elementName, selector);
				if(!"".equals(elementHelper.getText()))
				{
					ele.addDefaultValue("text", elementHelper.getText());
				}
				page.addElement(ele);
			}
				
		}
	
		Save save = new Save(page);
		save.toCucumber();
		
		

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
		
			FileUtils.writeStringToFile(pagefile, save.getPageStream().toString(), "utf-8");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
