package hellocucumber;

public class RegisterTimeSteps {
    public RegisterTimeSteps() {
        Given("^an employee with id \"([^\"]*)\" is logged in$", (String arg0) -> {
        });
        And("^that the employee is assigned to an \"([^\"]*)\" under \"([^\"]*)\"$", (String arg0, String arg1) -> {
        });
        When("^the employee registers in the system that he started to work on \"([^\"]*)\" under \"([^\"]*)\" at (\\d+):(\\d+) and stopped at (\\d+):(\\d+)$", (String arg0, String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5) -> {
        });
        Then("^the employee has worked (\\d+) hours on \"([^\"]*)\" under \"([^\"]*)\"$", (Integer arg0, String arg1, String arg2) -> {
        });
        And("^the employee registers in the system that he started to work on \"([^\"]*)\" under \"([^\"]*)\" at (\\d+):(\\d+) and stopped at (\\d+):(\\d+)$", (String arg0, String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5) -> {
        });
        When("^the employee later corrects time spent in the system to say he started to work on \"([^\"]*)\" under \"([^\"]*)\" at (\\d+):(\\d+) and stopped at (\\d+):(\\d+)$", (String arg0, String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5) -> {
        });
        Then("^the employee has worked (\\d+) hours on \"([^\"]*)\" under \"([^\"]*)\" instead of (\\d+) hours$", (Integer arg0, String arg1, String arg2, Integer arg3) -> {
        });
        When("^the employee registers freetime from day \"([^\"]*)\" up to and including \"([^\"]*)\"$", (String arg0, String arg1) -> {
        });
        Then("^the employee has (\\d+) days of free time$", (Integer arg0) -> {
        });
        And("^the employee has registered work time on \"([^\"]*)\" under \"([^\"]*)\" on \"([^\"]*)\"$", (String arg0, String arg1, String arg2) -> {
        });
        And("^the employee want to update the excisting registration on \"([^\"]*)\"$", (String arg0) -> {
        });
        When("^the employee updates the registration to sick leave and gives an end date on \"([^\"]*)\"$", (String arg0) -> {
        });
        Then("^the time in period \"([^\"]*)\" up to and including \"([^\"]*)\" is updated as sick leave$", (String arg0, String arg1) -> {
        });
    }
}
