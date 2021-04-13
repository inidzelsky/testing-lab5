import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class GithubAPIRequestService {
    Response getUsersRequest() {
        return given()
                .get(Config.githubAPIgetUsersPath)
                .then()
                .extract().response();
    }

    Response updateUserProjectRequest(long projectId, String updateName, String updateState) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", updateName);
        jsonBody.put("state", updateState);

        return given()
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .body(jsonBody.toJSONString())
                .pathParam("projectId", projectId)
                .patch(Config.githubAPIupdateUserProjectPath)
                .then()
                .extract().response();
    }

    Response createUserProjectRequest(String projectName) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", projectName);

        return given()
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .body(jsonBody.toJSONString())
                .post(Config.githubAPIcreateUserProjectPath)
                .then()
                .extract().response();
    }

    Response deleteUserProjectRequest(long projectId) {
        return given()
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .pathParam("projectId", projectId)
                .delete(Config.githubAPIdeleteUserProjectPath)
                .then()
                .extract().response();
    }

    private RequestSpecification given() {
        return RestAssured.given()
                .baseUri(Config.githubAPIBaseURI)
                .contentType(ContentType.JSON)
                .header("Authorization", Config.githubAPIToken);
    }
}
