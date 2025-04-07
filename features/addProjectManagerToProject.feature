Feature: Add project manager to project
  Description: An employee wants to add a project manager to a project

  Actors: Employee and project manager

  Scenario: Adding a project manager to a project without a manager
    Given a project "x" exists
    And no project manager is assigned to project "x"
    When an available project manager "P123" is assigned to project "x" by employee "E123"
    Then "P123" can see project "x" in the list of projects where "P123" is the project manager
    And "P123" is now the project manager of project "x"


  Scenario: Adding a project manager to a project that doesn't exist
    Given an employee "E123"
    And no project has been created
    When "E123" tries to assign a project manager "P123" to the project that doesn't exist
    Then "E123" will get an error message saying that "The project doesn't exist"

