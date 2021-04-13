import models.*;
import org.json.simple.JSONObject;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class GithubAPITests {
    private long projectId = 12201082;

    @Test
    public void getUsers() {
        User[] users = given()
                        .header("Authorization", "token")
                        .get("https://api.github.com/users")
                        .then()
                        .extract()
                        .as(User[].class);

        assertThat(users.length, equalTo(30));
    }

    @Test
    public void updateUserProject() {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "closed-rest-assured-test-project");
        jsonBody.put("state", "closed");

        Project updatedProject = given()
                .header("Authorization", "token")
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .body(jsonBody.toJSONString())
                .patch(String.format("https://api.github.com/projects/%s", this.projectId))
                .then()
                .statusCode(200)
                .extract()
                .as(Project.class);

        assertThat(updatedProject.name, equalTo("closed-rest-assured-test-project"));
        assertThat(updatedProject.state, equalTo("closed"));
    }

    @Test
    public void createUserProject() {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", "rest-assured-test-project");

        Project project = given()
                .header("Authorization", "token")
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .body(jsonBody.toJSONString())
                .post("https://api.github.com/user/projects")
                .then()
                .statusCode(201)
                .extract()
                .as(Project.class);

        assertThat(project.name, equalTo("rest-assured-test-project"));
        this.projectId = project.id;
    }

    @Test
    public void deleteUserProject() {
        given()
                .header("Authorization", "token")
                .header("Accept", "application/vnd.github.inertia-preview+json")
                .delete(String.format("https://api.github.com/projects/%s", this.projectId))
                .then()
                .statusCode(204);
    }
}
