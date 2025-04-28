Feature: Create Project

  Scenario: Successful creation of project
    Given an employee with name "Hans" is logged in
    When the employee creates a project
    And the employee inputs name "Program"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    Then the system creates the project "Program"
    And the system returns a project ID "25001"

   Scenario: Unsuccessful creation of a project
     Given an employee with name "Hans" is logged in
     When the employee creates a project
     And the employee inputs and empty name
     Then the system outputs the error message "Project name missing. Project has not been created."
     And the project is not created