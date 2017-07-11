package com.gd.elements;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;


public class Property {
	
	
	public static String DefaultPath = "";
	public static String url = "";
	public static String parentNodeLocator = "//body";
	public static String SaveToPath = "";
	public static String pageFileName = "";
	public static int PageObject_Type = 3;

	public static void SetUp()
	{
		
		//get current jar file path
		try {
			DefaultPath = Property.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			DefaultPath = new File(".").getAbsolutePath();
			if(!Property.class.getProtectionDomain().getCodeSource().getLocation().getFile().toString().contains(".jar"))
			{
				File temp = Paths.get(System.getProperty("user.home"), "PoGen").toFile();
				// create directory if needed
			    if (!Files.exists(temp.toPath())) {
			        try {
						FileUtils.forceMkdir(temp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			    
			    DefaultPath = temp.getAbsolutePath();
			    SaveToPath = DefaultPath;
			}			

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
