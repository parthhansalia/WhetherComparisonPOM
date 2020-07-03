package com.wrc.qa.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.wrc.qa.base.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtil extends TestBase {


	public static final float minTempratureDiff = (float) 0.1;
	public static final float maxTempratureDiff = 5;
	public static final float minWindDiff = (float) 0.01;
	public static final float maxWindDiff = 5;
	public static final float minHumidityDiff = 1;
	public static final float maxHumidityDiff = 30;





	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));
	}

	public static boolean inRange(double value, Double min, Double max) {
		return value > min && value < max;
	}

	public static String subString(int startIndex, int endIndex, String string) {

		return string.substring(startIndex, endIndex);

	}

	public static WebElement waitTillElementIsClickable(WebDriver driver, WebElement webElement) {

		 WebDriverWait wait = new WebDriverWait(driver, 20);

		return wait.until(ExpectedConditions.elementToBeClickable(webElement));

	}

	public static WebElement waitTillElementIsDisplayed(WebDriver driver, WebElement webElement) {

		WebDriverWait wait = new WebDriverWait(driver, 20);

		return wait.until(ExpectedConditions.visibilityOf(webElement));

	}



}
