package com.lgt.qa.driver.interal;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * WrappedChromeDriver封装了WebDriver的ChromeDriver类，实现findElement和findElements的重写
 *
 */
public class WrappedChromeDriver extends ChromeDriver {
	private static final Logger logger = LoggerFactory.getLogger(WrappedChromeDriver.class);
	
	public WrappedChromeDriver() {
		super();
	}
	public WrappedChromeDriver(ChromeDriverService service) {
		super(service);
	}
	public WrappedChromeDriver(ChromeDriverService service, ChromeOptions options) {
		super(service,options);
	}
	@Override
	protected WebElement findElement(String by, String using) {
		WebElement element = null;
		try {
			element = super.findElement(by, using);
			logger.info("通过"+by+"找到元素"+using);
		}catch (Exception e) {
			logger.error("通过"+by+"未能找到元素"+using);
			logger.error(e.toString());
		}
		return element;
	}
	@Override
	protected List<WebElement> findElements(String by, String using) {
		List<WebElement> elements = null;
		try {
			elements = super.findElements(by, using);
			logger.info("通过"+by+"找到"+elements.size()+"个元素"+using);
		}catch (Exception e) {
			logger.error("通过"+by+"未能找到元素"+using);
			logger.error(e.toString());
		}
		return elements;
	}
	@Override
	public void get(String url) {
		logger.info("正在打开网页"+url);
		super.get(url);
	}
}
