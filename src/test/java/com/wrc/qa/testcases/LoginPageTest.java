package com.wrc.qa.testcases;

import com.wrc.qa.testdata.WhetherDataModel;
import com.wrc.qa.util.TestUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import com.wrc.qa.base.TestBase;
import com.wrc.qa.pages.LoginPage;

import java.util.List;


public class LoginPageTest extends TestBase{

	private LoginPage loginPage;

	private WhetherDataModel whetherDataModelAPI;
	private WhetherDataModel whetherDataModelUI;

	public LoginPageTest(){
		super();
	}
	
	@BeforeClass
	public void setUp(){
		initialization();
		loginPage = new LoginPage();
		whetherDataModelAPI = new WhetherDataModel();
		whetherDataModelUI = new WhetherDataModel();
	}
	
	@Test(priority=1)
	public void validateWhetherSectionTest() {

		Assert.assertTrue(loginPage.goToWhetherSection());

	}
	
	@Test(priority=2, dependsOnMethods = "validateWhetherSectionTest")
	public void validateSelectedCityOnMapTest() throws InterruptedException {
		 Assert.assertTrue(loginPage.validateSelectedCityOnMap());

	}

	@Test(priority=3)
	public void validateWhetherDetailsForSelectedCityTest() {

		Assert.assertTrue(loginPage.validateWhetherDetailsForSelectedCity());

	}

	
	@Test(priority=4)
	public void whetherInfoForSelectedCityTest() {

		List<String> whetherInfo = loginPage.wetherInfoForSelectedCity();

		String tempInfo = whetherInfo.get(0).substring(whetherInfo.get(0).length() - 2);
		String windInfo = TestUtil.subString(6,10,whetherInfo.get(1));
		String humidityInfo = TestUtil.subString(10,whetherInfo.get(2).length() - 1,whetherInfo.get(2));

		whetherDataModelUI.setTemprature(Double.parseDouble(tempInfo));
		whetherDataModelUI.setWind(Double.parseDouble(windInfo));
		whetherDataModelUI.setHumidity(Double.parseDouble(humidityInfo));

		System.out.println("wind for selected city is " + whetherDataModelUI.getWind());


	}

	@Test(priority=5)
	public void fetchFromOpenWhtherAPI() {

		Response response = loginPage.responseValidation(prop.getProperty("openWhetherURL"));

		String tempFromOpenWhetherKelvin = response.jsonPath().getString("main.temp");
		Float tempratureFromOpenWhetherAPICelsius = Float.parseFloat(tempFromOpenWhetherKelvin) - 273.15F;

		String windFromOpenWhether = response.jsonPath().getString("wind.speed");

		String humidityFromOpenWhether = response.jsonPath().getString("main.humidity");

		whetherDataModelAPI.setTemprature(tempratureFromOpenWhetherAPICelsius);
		whetherDataModelAPI.setWind(Float.parseFloat(windFromOpenWhether));
		whetherDataModelAPI.setHumidity(Double.parseDouble(humidityFromOpenWhether));

		System.out.println("yesssss - " + whetherDataModelAPI.getWind());

	}

	@Test(priority=6)
	public void tempratureDifference() {

			if (whetherDataModelUI.getTemprature() == (whetherDataModelAPI.getTemprature())) {
				Assert.assertTrue(true);
			}
			else {
				Assert.assertTrue(loginPage.validateRange(Math.abs(whetherDataModelUI.getTemprature() - whetherDataModelAPI.getTemprature())
				, TestUtil.minTempratureDiff, TestUtil.maxTempratureDiff
				));
			}

	}

	@Test(priority=7)
	public void windDifference() {

		if (whetherDataModelUI.getWind() == (whetherDataModelAPI.getWind())) {
			Assert.assertTrue(true);
		}
		else {
			Assert.assertTrue(loginPage.validateRange(Math.abs(whetherDataModelUI.getWind() - whetherDataModelAPI.getWind())
					, TestUtil.minWindDiff, TestUtil.maxWindDiff
			));
		}

	}

	@Test(priority=8)
	public void humidityDifference() {

		System.out.println("Humidity Uiii - " + whetherDataModelUI.getHumidity());
		System.out.println("Humidity APII - " + whetherDataModelAPI.getHumidity());
		System.out.println("Differenceee - " + (whetherDataModelUI.getHumidity() - whetherDataModelAPI.getHumidity()));

		if (whetherDataModelUI.getHumidity() == (whetherDataModelAPI.getHumidity())) {
			Assert.assertTrue(true);
		}
		else {
			Assert.assertTrue(loginPage.validateRange(Math.abs(whetherDataModelUI.getHumidity() - whetherDataModelAPI.getHumidity())
					, TestUtil.minHumidityDiff, TestUtil.maxHumidityDiff
			));
		}

	}



	
	
	
	@AfterClass
	public void tearDown(){
		driver.quit();
	}
	
	
	
	

}
