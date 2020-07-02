package com.wrc.qa.pages;

import com.wrc.qa.util.TestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import com.wrc.qa.base.TestBase;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;


public class LoginPage extends TestBase{

	private String getCity = prop.getProperty("city");

	private String cityXpath = "//*[@id= \""+getCity+"\"]";

	private boolean cityFoundOnMap = false;

	private List<String> whetherData = new ArrayList<>();

	//Page Factory - OR:
	@FindBy(id="h_sub_menu")
	WebElement subMenu;
	
	@FindBy(xpath="//*[@id=\"subnav\"]/div/div/div/div/div/a[8]")
	WebElement whetherOption;
	
	@FindBy(id="searchBox")
	WebElement searchBox;

	@FindBy(xpath="//*[@class=\"leaflet-popup-content\"]/div[1]/span[4]")
	WebElement tempInfo;

	@FindBy(xpath="//*[@class=\"leaflet-popup-content\"]/div[1]/span[2]")
	WebElement windInfo;

	@FindBy(xpath="//*[@class=\"leaflet-popup-content\"]/div[1]/span[3]")
	WebElement humityInfo;

	@FindBy(xpath="//*[@class=\"leaflet-popup-content\"]/div[1]/div/span[2]")
	WebElement cityOnWhetherDetails;

	@FindBy(className="leaflet-popup-content")
	WebElement whetherDetails;

	@FindBy(how = How.CLASS_NAME, using = "cityText")
	List<WebElement> cityOnMap;
	
	//Initializing the Page Objects:
	public LoginPage(){
		PageFactory.initElements(driver, this);
	}
	
	public boolean goToWhetherSection() {

		subMenu = TestUtil.waitTillElementIsClickable(driver, subMenu);
		subMenu.click();

		whetherOption = TestUtil.waitTillElementIsClickable(driver, whetherOption);
		whetherOption.click();

		searchBox = TestUtil.waitTillElementIsDisplayed(driver, searchBox);
		return searchBox.isDisplayed();
	}


	public boolean validateSelectedCityOnMap() throws InterruptedException {

		searchBox.sendKeys(prop.getProperty("city"));
		Thread.sleep(2000);
		searchBox.sendKeys(Keys.ENTER);

		WebElement city = driver.findElement(By.xpath(cityXpath));
		city.click();


		cityOnMap
				.forEach(i -> {
					if (i.getText().contains(getCity)) {
						cityFoundOnMap = true;
						i.click();
					}
				});

		return cityFoundOnMap;

	}

	public boolean validateWhetherDetailsForSelectedCity() {

		cityOnWhetherDetails = TestUtil.waitTillElementIsDisplayed(driver, cityOnWhetherDetails);
		return whetherDetails.isDisplayed() && cityOnWhetherDetails.getText().contains(getCity);

	}
	
	public List<String> wetherInfoForSelectedCity() {

		tempInfo = TestUtil.waitTillElementIsDisplayed(driver, tempInfo);
		whetherData.add(tempInfo.getText());
		whetherData.add(windInfo.getText());
		whetherData.add(humityInfo.getText());

		return whetherData;
	}

	public Response responseValidation(String endpoint) {

		RestAssured.defaultParser = Parser.JSON;

		return   given().
					headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
					queryParam("appid", prop.getProperty("appid")).
					queryParam("q", getCity).
				 when().
					get(endpoint).
				 then().
					contentType(ContentType.JSON).
					statusCode(200).
					extract().
					response();
	}

	public boolean validateRange(double value, double min, double max) {

		return TestUtil.inRange(value, min, max);


	}

	
}
