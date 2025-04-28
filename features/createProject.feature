Feature: Create Project

  Scenario: Successful creation of project
    Given an employee with id "E123" is logged in
    When the employee inputs name "New Website"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    And the employee creates the project
    Then the system creates the project "New Website"
    And the system returns a project ID "25001"

  Scenario: Unsuccessful creation of a project
    Given an employee with id "E123" is logged in
    And the employee inputs and empty name
    When the employee creates the project
    Then the system outputs the error message "Project name missing. Project has not been created."
    And the project is not created
