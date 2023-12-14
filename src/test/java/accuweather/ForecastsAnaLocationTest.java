package accuweather;


import io.restassured.http.Method;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import weather.Weather;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class ForecastsAnaLocationTest extends AccuweatherAbstractTest {


    @Test
    void testGetResponseForecasts1Days() {
//        Weather weather = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
//                .when().get(getBaseUrl() + "/forecasts/v1/daily/1day/{locationKey}")
//                .then().statusCode(200).time(lessThan(2000L))
//                .extract().response().body().as(Weather.class);
//        Assertions.assertEquals(1, weather.getDailyForecasts().size());

        Response response = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/1day/{locationKey}");
        String headerAm = response.getHeader("am");
        int sizeWeatherForecast = response.body().as(Weather.class).getDailyForecasts().size();
        int statusCode = response.getStatusCode();
        Assertions.assertAll(() -> Assertions.assertEquals(1, sizeWeatherForecast),
                () -> Assertions.assertEquals("true", headerAm),
                () -> Assertions.assertEquals(200, statusCode));
    }
    @Test
    void testGetResponseForecasts10Days() {
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");

        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");

        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));
    }
    @Test
    void testGetResponseForecasts15Days() {

//        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
//                .when()
//                .request(Method.GET,getBaseUrl()+"/forecasts/v1/daily/15day/{locationKey}")
//                .then().extract()
//                .jsonPath()
//                .getString("Code");
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");

        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");

        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));

    }





    @Test
    void testResponseDateAutocompleteSearch() {
        //вариант через assertThat() (но после первого бага проверка прекращается)
        given().queryParam("apikey", getApiKey()).queryParam("q", "Moscow")
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .then().assertThat().statusCode(200).time(lessThan(2000L))
                .statusLine("HTTP/1.1 200 OK")
                .header("Content-Encoding", "gzip")
                .body("[0].LocalizedName", equalTo("Moscow"))
                .body("[0].Key", equalTo("294021"));
        //вариант через assertAll() (выводит все баги)
        JsonPath response = given().queryParam("apikey", getApiKey()).queryParam("q", "Moscow")
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .body().jsonPath();
        Assertions.assertAll(() -> Assertions.assertEquals("Moscow", response.get("[0].LocalizedName")),
                () -> Assertions.assertEquals("294021", response.get("[0].Key")));


    }
}
