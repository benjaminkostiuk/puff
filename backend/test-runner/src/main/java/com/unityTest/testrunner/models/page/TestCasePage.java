package com.unityTest.testrunner.models.page;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.models.response.Author;
import com.unityTest.testrunner.models.response.TestCase;
import io.swagger.annotations.ApiModel;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;

@ApiModel(value = "TestCasePage", description = "Page request for test cases")
public class TestCasePage extends BasePage<TestCase> {
    public TestCasePage(Page<Case> page) {
        super(page.map(Case::toTestCase));
    }

    /**
     * TestCasePage constructor
     * @param page Page of cases
     * @param keycloak Keycloak api connection
     */
    public TestCasePage(Page<Case> page, Keycloak keycloak) {
        super(page.map(caze -> {
            // Get user representation with id from keycloak connection
            UserRepresentation user = keycloak.realm("puff").users().get(caze.getAuthorId()).toRepresentation();
            // Create author object with the user representation
            Author author = new Author(user.getFirstName(), user.getLastName());
            return caze.toTestCase(author);
        }));
    }

    /**
     * TestCasePage constructor
     * @param page Page of cases
     * @param author Author of all cases
     */
    public TestCasePage(Page<Case> page, Author author) {
        super(page.map(caze -> caze.toTestCase(author)));
    }
}
