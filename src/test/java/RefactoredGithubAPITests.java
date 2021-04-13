import models.Project;
import models.User;
import org.json.simple.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RefactoredGithubAPITests {
    @Test
    public void getUsers() {
        User[] users = new GithubAPIRequestService()
                .getUsersRequest()
                .as(User[].class);

        assertThat(users.length, equalTo(30));
    }

    @Test
    public void updateUserProject() {
        long projectId = 12201074;
        String updatedName = "closed-rest-assured-test-project";
        String updatedState = "closed";

        Project updatedProject = new GithubAPIRequestService()
                .updateUserProjectRequest(projectId, updatedName, updatedState)
                .then()
                .statusCode(200)
                .extract()
                .as(Project.class);

        assertThat(updatedProject.name, equalTo("closed-rest-assured-test-project"));
        assertThat(updatedProject.state, equalTo("closed"));
    }

    @Test
    public void createUserProject() {
        String projectName = "rest-assured-test-project";
        Project project = new GithubAPIRequestService()
                .createUserProjectRequest(projectName)
                .then()
                .statusCode(201)
                .extract()
                .as(Project.class);

        assertThat(project.name, equalTo("rest-assured-test-project"));
    }

    @Test
    public void deleteUserProject() {
        long projectId = 12201074;
        new GithubAPIRequestService().deleteUserProjectRequest(projectId).then().statusCode(204);
    }
}
