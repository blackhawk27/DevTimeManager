package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegisterTimeSteps {
    @Given("the employee is logged in")
    public void theEmployeeIsLoggedIn() {
    }

    @And("that an employee is assigned to an {string} under {string}")
    public void thatAnEmployeeIsAssignedToAnUnder(String arg0, String arg1) {
    }

    @When("the employee registers in the system that he arrived at {int}:{int} and left at {int}:{int}")
    public void theEmployeeRegistersInTheSystemThatHeArrivedAtAndLeftAt(int arg0, int arg1, int arg2, int arg3) {
    }

    @Then("the employee has worked {int} hours on {string}")
    public void theEmployeeHasWorkedHoursOn(int arg0, String arg1) {
    }

    @And("he has registered his time from {int}:{int} to {int}:{int}")
    public void heHasRegisteredHisTimeFromTo(int arg0, int arg1, int arg2, int arg3) {
    }

    @When("the employee later corrects time spent in the system to say he arrived at {int}:{int} and left at {int}:{int}")
    public void theEmployeeLaterCorrectsTimeSpentInTheSystemToSayHeArrivedAtAndLeftAt(int arg0, int arg1, int arg2, int arg3) {
    }

    @Then("the employee has worked {int} hours on {string} instead of {int}")
    public void theEmployeeHasWorkedHoursOnInsteadOf(int arg0, String arg1, int arg2) {
    }
}
