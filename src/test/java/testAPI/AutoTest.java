package testAPI;


import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class AutoTest {

    static {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    public void getUser() {
        given()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .get("api/users?page=1")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("data.id", Matchers.hasSize(6));

    }

    @Test
    public void createNewUser() {
        String name = "JunnHoo";
        String job = "Engineer";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("job", job);

        given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-TYpe", "application/json")
                .body(jsonObject.toString())
                .post("api/users")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(name))
                .assertThat().body("job", Matchers.equalTo(job))
                .assertThat().body("$", Matchers.hasKey("id"))
                .assertThat().body("$", Matchers.hasKey("createdAt"));
    }

    @Test
    public void editUser() {
        int userId = 2;
        String newName = "PutUser";
        String email = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.email");
        String l_name = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.last_name");
        String ava = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.avatar");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userId);
        jsonObject.put("email", email);
        jsonObject.put("first_name", newName);
        jsonObject.put("last_name", l_name);
        jsonObject.put("avatar", ava);

        given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("api/users/" + userId)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));
    }

    @Test
    public void patchUser() {
        int userId = 3;
        String newName = "PatchName";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", newName);

        given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/" + userId)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));
    }

    @Test
    public void deleteUser() {
        int userDelete = 4;
        given()
                .header("x-api-key", "reqres-free-v1")
                .when()
                .delete("api/users/" + userDelete)
                .then()
                .assertThat().statusCode(204);
    }
}
