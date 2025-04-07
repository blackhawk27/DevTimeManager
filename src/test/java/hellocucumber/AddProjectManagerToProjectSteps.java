package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddProjectManagerToProjectSteps {
    @And("no project manager is assigned to project {string}")
    public void noProjectManagerIsAssignedToProject(String arg0) {
    }

    @When("an available project manager {string} is assigned to project {string} by employee {string}")
    public void anAvailableProjectManagerIsAssignedToProjectByEmployee(String arg0, String arg1, String arg2) {
    }

    @Then("{string} can see project {string} in the list of projects where {string} is the project manager")
    public void canSeeProjectInTheListOfProjectsWhereIsTheProjectManager(String arg0, String arg1, String arg2) {
    }

    @And("{string} is now the project manager of project {string}")
    public void isNowTheProjectManagerOfProject(String arg0, String arg1) {
    }

    @Given("an employee {string}")
    public void anEmployee(String arg0) {
    }

    @And("no project has been created")
    public void noProjectHasBeenCreated() {
    }

    @When("{string} tries to assign a project manager {string} to the project that doesn't exist")
    public void triesToAssignAProjectManagerToTheProjectThatDoesnTExist(String arg0, String arg1) {
    }

    @Then("{string} will get an error message saying that {string}")
    public void willGetAnErrorMessageSayingThat(String arg0, String arg1) {
    }
}
