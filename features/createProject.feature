Feature: Create Project

  Scenario: Successful creation of project
    Given an employee with name "Hans" is logged in
    When the employee creates a project
    And the employee inputs name "Program"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    Then the system creates the project "Program"
    And the system returns a project ID "25001"