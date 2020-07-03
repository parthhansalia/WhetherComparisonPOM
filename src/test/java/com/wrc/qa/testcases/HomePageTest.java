package com.wrc.qa.testcases;

import com.wrc.qa.Models.WeatherDataModel;
import com.wrc.qa.util.TestUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import com.wrc.qa.base.TestBase;
import com.wrc.qa.pages.HomePage;
import java.util.List;


public class HomePageTest extends TestBase{

	private HomePage homePage;

	private WeatherDataModel weatherDataModelAPI;
	private WeatherDataModel weatherDataModelUI;

	public HomePageTest(){
		super();
	}

	@BeforeClass
	public void setUp(){
		initialization();
		homePage = new HomePage();
		weatherDataModelAPI = new WeatherDataModel();
		weatherDataModelUI = new WeatherDataModel();
	}

	@Test(priority=1, description = "Go to the whether section and check Searchbox for Pin city")
	public void validateWhetherSectionTest() throws InterruptedException {

		Assert.assertTrue(homePage.goToWhetherSection());

	}
	@Test(priority=2, dependsOnMethods = "validateWhetherSectionTest", description = "Verifying the selected city is showing on the map")
	public void validateSelectedCityOnMapTest() throws InterruptedException {
		 Assert.assertTrue(homePage.validateSelectedCityOnMap());

	}
	@Test(priority=3, description = "Verifying whether information on popup")
	public void validateWhetherDetailsForSelectedCityTest() {

		Assert.assertTrue(homePage.validateWhetherDetailsForSelectedCity());

	}


	@Test(priority=4, description = "Gathering whether info for selected city")
	public void whetherInfoForSelectedCityTest() {

		List<String> whetherInfo = homePage.wetherInfoForSelectedCity();

		String tempInfo = whetherInfo.get(0).substring(whetherInfo.get(0).length() - 2);
		String windInfo = TestUtil.subString(6,10,whetherInfo.get(1));
		String humidityInfo = TestUtil.subString(10,whetherInfo.get(2).length() - 1,whetherInfo.get(2));

		weatherDataModelUI.setTemprature(Float.parseFloat(tempInfo));
		weatherDataModelUI.setWind(Float.parseFloat(windInfo));
		weatherDataModelUI.setHumidity(Float.parseFloat(humidityInfo));

		System.out.println("wind for selected city is " + weatherDataModelUI.getWind());


	}

	@Test(priority=5, description = "Gathering same city info from Open whether API")
	public void fetchFromOpenWhtherAPI() {

		Response response = homePage.responseValidation(prop.getProperty("openWhetherURL"));

		String tempFromOpenWhetherKelvin = response.jsonPath().getString("main.temp");
		Float tempratureFromOpenWhetherAPICelsius = Float.parseFloat(tempFromOpenWhetherKelvin) - 273.15F;

		String windFromOpenWhether = response.jsonPath().getString("wind.speed");

		String humidityFromOpenWhether = response.jsonPath().getString("main.humidity");

		weatherDataModelAPI.setTemprature(tempratureFromOpenWhetherAPICelsius);
		weatherDataModelAPI.setWind(Float.parseFloat(windFromOpenWhether));
		weatherDataModelAPI.setHumidity(Float.parseFloat(humidityFromOpenWhether));


	}

	@Test(priority=6, description = "Verifying temperature difference")
	public void temperatureDifference() {

			if (weatherDataModelUI.getTemprature() == (weatherDataModelAPI.getTemprature())) {
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(homePage.validateRange(Math.abs(weatherDataModelUI.getTemprature() - weatherDataModelAPI.getTemprature())
				, TestUtil.minTempratureDiff, TestUtil.maxTempratureDiff
				));
			}
	}

	@Test(priority=7, description = "Verifying wind difference")
	public void windDifference() {

		System.out.println("From UII - " +weatherDataModelUI.getWind());
		System.out.println("From APII - " +weatherDataModelAPI.getWind());
		System.out.println("Diff - " + Math.abs(weatherDataModelUI.getWind() - weatherDataModelAPI.getWind()));

		if (weatherDataModelUI.getWind() == (weatherDataModelAPI.getWind())) {
			Assert.assertTrue(true);
		}
		else {
			Assert.assertTrue(homePage.validateRange(Math.abs(weatherDataModelUI.getWind() - weatherDataModelAPI.getWind())
					, TestUtil.minWindDiff, TestUtil.maxWindDiff
			));
		}

	}

	@Test(priority=8, description = "Verifying humidity difference")
	public void humidityDifference() {

		if (weatherDataModelUI.getHumidity() == (weatherDataModelAPI.getHumidity())) {
			Assert.assertTrue(true);
		}
		else {
			Assert.assertTrue(homePage.validateRange(Math.abs(weatherDataModelUI.getHumidity() - weatherDataModelAPI.getHumidity())
					, TestUtil.minHumidityDiff, TestUtil.maxHumidityDiff
			));
		}

	}



	
	
	
	@AfterClass
	public void tearDown(){
		driver.quit();
	}
	
	
	
	

}
