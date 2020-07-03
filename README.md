This project compares weather details from NDTV website and Openweathermap API.

Technologies: Selenium, Java, Rest Assured
Framework: TestNG with POM

Flow of this project:
-> Implemented with Page Object Model pattern using Selenium with TestNG framework.
-> Fetching data from NDTV site and OpenWeatherMap site
-> Configurable inputs has been stored in config.property file
-> All the comparable inputs storing in Model class
-> All the test cases added in HomePageTest
-> Implementation of the test cases added in HomePage
-> TestUtil class is being used for utilities methods
-> Used implicit and explicit timeout to maintain dynamic graceful execution
-> Used dynamic xpath instead of chained manner
-> To run project - Run the testng.xml file

Here I have implemented some feature like,
-> WebEventListener is for listen the even and log accordingly.
        -> In case of any exception, It take the screenshot and store in the screenshots folder
-> ExtentReporterNG is for generating amazing report
        -> It's generating the report WeatherComparisonReport in test-output folder. it's presenting the data into table and chart format test case wise.

