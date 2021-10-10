package utils;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class EpamMobileCloudApi {

    private static final String DOMAIN = "https://mobilecloud.epam.com/automation/api";
    private static KeyStore keyStore;
    private static String applicationId;
    private static boolean isApplicationInstalled;

    public static void init() {
        String password = TestProperties.get("cacertsPassword");
        KeyStore trustedKeyStore;
        // Relative path to cacerts
        String filename = System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
        System.out.printf("[INFO] EPAM certificates searched in %s\n", filename);
        try {
            trustedKeyStore = KeyStore.getInstance("jks");
            trustedKeyStore.load(
                new FileInputStream(filename),
                password.toCharArray());
            keyStore = trustedKeyStore;
        } catch (Exception ex) {
            System.out.println("Error while loading keystore:");
            ex.printStackTrace();
        }
    }


    public static void getDeviceInfo(String udid) {
        init();
        Response response = RestAssured
            .given()
            .trustStore(keyStore)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + TestProperties.get("epamMobileCloudToken"))
            .log().ifValidationFails()
            .when()
            .get(DOMAIN + "/device/" + udid);
        response
            .then()
            .log().status()
            .log().body();
    }


    public static void uploadApplication() {
        init();
        File application= new File(TestProperties.get("pathToApp"));
        System.out.println("[INFO] Start uploading application from path: " + application.getPath());
        Response response = RestAssured
            .given()
            .trustStore(keyStore)
            .header("Authorization", "Bearer " + TestProperties.get("epamMobileCloudToken"))
            .header("X-File-Name", application.getName())
            .header("X-Content-Type", "application/zip")
            .multiPart("file", application, "multipart/form-data")
            .log().ifValidationFails()
            .when()
            .post(DOMAIN + "/v1/spaces/artifacts/0");
        if(response.getStatusCode() == 200){
            applicationId = response.jsonPath().get("id");
            System.out.printf("[INFO] Application uploaded with id=%s \n", applicationId);
        } else {
            response.then().log().all();
        }
    }


    public static void installApplication(String udid) {
        init();
        Response response = RestAssured
            .given()
            .trustStore(keyStore)
            .header("Authorization", "Bearer " + TestProperties.get("epamMobileCloudToken"))
            .pathParam("serial", udid)
            .pathParam("fileId", applicationId)
            .log().ifValidationFails()
            .when()
            .get(DOMAIN + "/storage/install/{serial}/{fileId}");
        if(response.getStatusCode() == 201) {
            isApplicationInstalled = true;
            System.out.printf("[INFO] Application with id=%s installed\n", applicationId);
        } else {
            response.then().log().all();
        }
    }

    public static void deleteApplication() {
        init();
        Response response = RestAssured
            .given()
            .trustStore(keyStore)
            .header("Authorization", "Bearer " + TestProperties.get("epamMobileCloudToken"))
            .pathParam("fileId", applicationId)
            .log().ifValidationFails()
            .when()
            .delete(DOMAIN + "/v1/spaces/artifacts/0/{fileId}");
        if(response.getStatusCode() == 200) {
            System.out.printf("[INFO] Application with id=%s was deleted\n", applicationId);
        } else {
            response.then().log().all();
        }


    }

    public static boolean isApplicationInstalled() {
        return isApplicationInstalled;
    }

}
