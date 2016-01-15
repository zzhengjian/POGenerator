package com.gd.elements;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

public class POGen {

	private JFrame frame;
	private JTextField urlTextField;
	private JTextField filePathTextField;
	private JLabel lblUrl;
	private JTextField fileNameTextField;
	private JLabel lblFilename;
	private JButton btnGoTo;

	
	
	public WebDriver oWebDriver;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					POGen window = new POGen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public POGen() {
		initialize();
		Property.SetUp();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 277);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		urlTextField = new JTextField();
		urlTextField.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				Property.url = urlTextField.getText();
				System.out.println(Property.url);
			}
		});
		urlTextField.setBounds(72, 8, 278, 20);
		frame.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Property.SaveToPath = filePathTextField.getText();
				Property.url = urlTextField.getText();
				Property.pageFileName = fileNameTextField.getText();
				new ElementGenerator().GeneratePageObject(oWebDriver);
			}
		});
		btnRun.setBounds(10, 198, 91, 23);
		frame.getContentPane().add(btnRun);
		
		final JButton btnStartFirefox = new JButton("Start Firefox");
		btnStartFirefox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {					
				
				if(btnStartFirefox.getText().toLowerCase().contains("start"))
				{
					FirefoxProfile profile = new FirefoxProfile();					
					oWebDriver = new FirefoxDriver();
					oWebDriver.manage().timeouts().pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
					btnStartFirefox.setText("Stop Firefox");				
				}
				else
				{		
					if(oWebDriver!=null)
					{
						oWebDriver.quit();
						btnStartFirefox.setText("Start Firefox");	
					}					
				}
			}
		});
		btnStartFirefox.setBounds(10, 148, 107, 23);
		frame.getContentPane().add(btnStartFirefox);
		
		filePathTextField = new JTextField();
		filePathTextField.setBounds(72, 39, 278, 23);
		frame.getContentPane().add(filePathTextField);
		filePathTextField.setColumns(10);
		
		JLabel lblSaveto = new JLabel("SaveTo");
		lblSaveto.setBounds(10, 46, 46, 14);
		frame.getContentPane().add(lblSaveto);
		
		lblUrl = new JLabel("Url");
		lblUrl.setBounds(10, 14, 46, 14);
		frame.getContentPane().add(lblUrl);
		
		fileNameTextField = new JTextField();
		fileNameTextField.setBounds(72, 73, 86, 20);
		frame.getContentPane().add(fileNameTextField);
		fileNameTextField.setColumns(10);
		
		lblFilename = new JLabel("FileName");
		lblFilename.setBounds(10, 79, 56, 14);
		frame.getContentPane().add(lblFilename);
		
		btnGoTo = new JButton("GoTo");
		btnGoTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(oWebDriver != null)
				{
					Property.url = urlTextField.getText();
					oWebDriver.get(Property.url);
					WebDriverWait wait = new WebDriverWait(oWebDriver, 20000);
					wait.until(PageHelper.pageLoaded(oWebDriver));
					String className = 	oWebDriver.findElement(By.xpath("//html")).getAttribute("class");
					if(className!=null && className.toLowerCase().contains("flex"))
					{
						PageHelper.isFlex = true;						
					}
					else
					{
						PageHelper.isFlex = false;
					}
					fileNameTextField.setText(PageHelper.generatePageNameWithUrl(Property.url));
				}
			}
		});
		btnGoTo.setBounds(370, 7, 70, 23);
		frame.getContentPane().add(btnGoTo);
	}
}
