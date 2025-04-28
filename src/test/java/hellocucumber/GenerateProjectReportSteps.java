package hellocucumber;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GenerateProjectReportSteps {
    @And("{string} has a budgeted time of {int} hours")
    public void hasABudgetedTimeOfHours(String arg0, int arg1) {
        throw new PendingException();
    }

    @And("the total registered work time on {string} is {int} hours")
    public void theTotalRegisteredWorkTimeOnIsHours(String arg0, int arg1) {
        throw new PendingException();
    }

    @And("the project manager is logged in")
    public void theProjectManagerIsLoggedIn() {
        throw new PendingException();
    }

    @When("the project manager generates a report for {string}")
    public void theProjectManagerGeneratesAReportFor(String arg0) {
        throw new PendingException();
    }

    @Then("the system displays the total registered hours as {int}")
    public void theSystemDisplaysTheTotalRegisteredHoursAs(int arg0) {
        throw new PendingException();
    }

    @And("the system displays the budgeted time as {int}")
    public void theSystemDisplaysTheBudgetedTimeAs(int arg0) {
        throw new PendingException();
    }

    @And("the system displays the unallocated hours as {int}")
    public void theSystemDisplaysTheUnallocatedHoursAs(int arg0) {
        throw new PendingException();
    }

    @And("the system confirms that the project is within budget")
    public void theSystemConfirmsThatTheProjectIsWithinBudget() {
        throw new PendingException();
    }

    @And("the system displays the estimated remaining work time")
    public void theSystemDisplaysTheEstimatedRemainingWorkTime() {
        throw new PendingException();
    }

    @And("no hours have been registered on {string}")
    public void noHoursHaveBeenRegisteredOn(String arg0) {
        throw new PendingException();
    }

    @And("the system warns {string}")
    public void theSystemWarns(String arg0) {
        throw new PendingException();
    }

    @Given("no project named {string} exists")
    public void noProjectNamedExists(String arg0) {

    }

    @When("the project manager attempts to generate a report for {string}")
    public void theProjectManagerAttemptsToGenerateAReportFor(String arg0) {
    }

    @Then("the system displays an error message {string}")
    public void theSystemDisplaysAnErrorMessage(String arg0) {
    }

    @And("{string} has no budgeted time set")
    public void hasNoBudgetedTimeSet(String arg0) {
    }

    @When("the project manager attempts to generate a report without selecting a project")
    public void theProjectManagerAttemptsToGenerateAReportWithoutSelectingAProject() {
    }
}
