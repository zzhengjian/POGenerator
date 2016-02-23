package com.gd.elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import com.gd.constant.PageObjectType;


public class Property {
	
	
	public static String DefaultPath = "";
	public static String url = "";
	public static String parentNodeLocator = "//body";
	public static String SaveToPath = "";
	public static String pageFileName = "";
	public static int PageObject_Type;

	private static Properties setting = new Properties();
	
	public static void SetUp()
	{
		
		//get current jar file path
		try {
			DefaultPath = Property.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			DefaultPath = new File(".").getAbsolutePath();
			if(!Property.class.getProtectionDomain().getCodeSource().getLocation().getFile().toString().contains(".jar"))
			{
				DefaultPath = "C:/QA/POGen";
			}
			
			File fxdriver = new File(DefaultPath, "webdriver.xpi");
			System.setProperty("webdriver.firefox.driver", fxdriver.getAbsolutePath());
			
			System.out.println(fxdriver.getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//load properties
		try {
			setting.load(new BufferedReader(new FileReader(new File(DefaultPath, "Setting.Properties"))));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		parentNodeLocator = setting.getProperty("parentNodeLocator", "");
		PageObject_Type = Integer.parseInt(setting.getProperty("PageObjectType", String.valueOf(PageObjectType.PAGEOBJECT_IN_CUCUMBER)));
		SaveToPath = DefaultPath;
		System.out.println(parentNodeLocator);
	}
	
}
