package hellocucumber;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddProjectManagerToProjectSteps {
    @And("no project manager is assigned to project {string}")
    public void noProjectManagerIsAssignedToProject(String arg0) {
        throw new PendingException();
    }

    @When("an available project manager {string} is assigned to project {string} by employee {string}")
    public void anAvailableProjectManagerIsAssignedToProjectByEmployee(String arg0, String arg1, String arg2) {
        throw new PendingException();
    }

    @Then("{string} can see project {string} in the list of projects where {string} is the project manager")
    public void canSeeProjectInTheListOfProjectsWhereIsTheProjectManager(String arg0, String arg1, String arg2) {
        throw new PendingException();
    }

    @And("{string} is now the project manager of project {string}")
    public void isNowTheProjectManagerOfProject(String arg0, String arg1) {
        throw new PendingException();
    }

    @Given("an employee {string}")
    public void anEmployee(String arg0) {
        throw new PendingException();
    }

    @And("no project has been created")
    public void noProjectHasBeenCreated() {
        throw new PendingException();
    }

    @When("{string} tries to assign a project manager {string} to the project that doesn't exist")
    public void triesToAssignAProjectManagerToTheProjectThatDoesnTExist(String arg0, String arg1) {
        throw new PendingException();
    }

    @Then("{string} will get an error message saying that {string}")
    public void willGetAnErrorMessageSayingThat(String arg0, String arg1) {
        throw new PendingException();
    }
}
