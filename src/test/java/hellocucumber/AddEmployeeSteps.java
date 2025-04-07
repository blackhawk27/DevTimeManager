package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddEmployeeSteps {
    @Given("a project {string} exists")
    public void aProjectExists(String arg0) {
    }

    @And("an activity {string} exists in {string}")
    public void anActivityExistsIn(String arg0, String arg1) {
    }

    @And("an employee {string} exists")
    public void anEmployeeExists(String arg0) {
    }

    @When("{string} is assigned to {string}")
    public void isAssignedTo(String arg0, String arg1) {
    }

    @Then("{string} should be listed in {string}'s employee list")
    public void shouldBeListedInSEmployeeList(String arg0, String arg1) {
    }

    @And("an activity {string} exists {string}")
    public void anActivityExists(String arg0, String arg1) {
    }

    @And("{string} is already assigned to {int} activities")
    public void isAlreadyAssignedToActivities(String arg0, int arg1) {
    }

    @Then("the system should return an error message {string} {string}")
    public void theSystemShouldReturnAnErrorMessage(String arg0, String arg1) {
    }
}
