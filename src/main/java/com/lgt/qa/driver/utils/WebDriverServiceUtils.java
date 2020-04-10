package com.lgt.qa.driver.utils;

import com.lgt.qa.driver.interal.WrappedChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * WebDriverServiceUtils封装了Driver的实例化方法，通过设置webdriver.browser系统属性来选取需要的浏览器，默认为Google Chrome。
 * 该类使用单例模式，首次被加载就会启动一个对应浏览器的driverService，需要确保浏览器对应的可执行driver文件位于classpath路径中。
 *
 */
public class WebDriverServiceUtils {
	private static final Logger logger = LoggerFactory.getLogger(WebDriverServiceUtils.class);
	private static DriverService service;
	private static String browser;
	static {
		browser = System.getProperty("webdriver.browser", "chrome");
		if("chrome".equalsIgnoreCase(browser)) {
			String file = WebDriverServiceUtils.class
					.getClassLoader()
					.getResource("chromedriver.exe")
					.getFile();
			if(file != null) {
				logger.info("在classpath中找到chromedriver驱动，使用指定driver启动service");
				service = new ChromeDriverService.Builder()
						.usingAnyFreePort()
						.usingDriverExecutable(new File(file))
						.build();
			}else {
				logger.info("没有在classpath中找到chromedriver驱动，使用默认driver启动service");
				service = new ChromeDriverService.Builder()
						.usingAnyFreePort()
						.build();
			}
		}else {
			throw new RuntimeException("不支持的浏览器，请在webdriver.browser属性中设置正确的浏览器名称");
		}
	}
	/**
	 * 静态方法，基于当前的service，启动一个浏览器，并获取浏览器的driver实例
	 * @return WebDriver实例，如果为null，表示浏览器实例启动失败
	 */
	public static WebDriver getWebDriver() {
		if(service instanceof ChromeDriverService) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-web-security");
//			return new ChromeDriver((ChromeDriverService) service,options);
			return new WrappedChromeDriver((ChromeDriverService) service,options);
		}else {
			return null;
		}
	}
	
	/**
	 * 在所有测试结束后，用于关闭运行着的service实例
	 */
	public static void stopService() {
		service.stop();
	}
}
