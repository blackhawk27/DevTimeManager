Feature: Create Project

  Scenario: Successful creation of project
    Given an employee is logged in
    And there exists no projects in the system
    When the employee chooses "Create Project"
    And the employee inputs name "Program"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    Then the system creates the project "Program"
    And the system returns a project ID "25001"