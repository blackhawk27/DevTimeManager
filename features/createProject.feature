Feature: Create Project

  Scenario: Successful creation of project
    Given an employee with id "E123" logs in to create project
    When the employee inputs name "New Website"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    And the employee inputs budgeted time 120.0
    And the employee creates the project
    Then the system creates the project "New Website"
    And the system returns a project ID "25001"


  Scenario: Unsuccessful creation of a project with empty name
    Given an employee with id "E123" logs in to create project
    And the employee inputs and empty name
    When the employee creates the project
    Then the system outputs the error message "Project name missing. Project has not been created."
    And the project is not created

  Scenario: Unsuccessful creation of a project with invalid start date
    Given an employee with id "E123" logs in to create project
    When the employee inputs name "New Website"
    And the employee inputs start date "32/01/2025"
    And the employee inputs end date "01/02/2025"
    And the employee creates the project
    Then the system outputs the error message "Invalid date format. Project has not been created."
    And the project is not created

  Scenario: Unsuccessful creation of a project with invalid end date
    Given an employee with id "E123" logs in to create project
    When the employee inputs name "New Website"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "31/02/2025"
    And the employee creates the project
    Then the system outputs the error message "Invalid date format. Project has not been created."
    And the project is not created

  Scenario: Unsuccessful creation of a project with end date before start date
    Given an employee with id "E123" logs in to create project
    When the employee inputs name "New Website"
    And the employee inputs start date "15/03/2025"
    And the employee inputs end date "01/02/2025"
    And the employee creates the project
    Then the system outputs the error message "Start date must be before or equal to end date. Project has not been created."
    And the project is not created

  Scenario: Unsuccessful creation of a project when employee is not logged in
    Given an employee with id "E123" is not logged in
    When the employee inputs name "New Website"
    And the employee inputs start date "01/01/2025"
    And the employee inputs end date "01/02/2025"
    And the employee creates the project
    Then the system outputs the error message "Employee must be logged in to create a project."
    And the project is not created

  Scenario: Unsuccessful creation of a project with the same name as an already existing project
    Given an employee with id "E123" logs in to create project
    And the employee inputs name "New Website"
    And the employee inputs start date "01/06/2025"
    And the employee inputs end date "30/06/2025"
    And the employee creates the project
    And the employee inputs name "New Website"
    And the employee inputs start date "01/07/2025"
    And the employee inputs end date "31/07/2025"
    And the employee creates the project
    Then the system outputs the error message "Project already exists. New project has not been created."
    And the project is not created
