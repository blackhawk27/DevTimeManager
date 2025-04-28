Feature: Generate project report
  Description: A project manager creates a report with a given budget time
  Actors: Project manager

  Scenario: Successfully generating a report
    Given a project "ProjectX" exists
    And "ProjectX" has a budgeted time of 100 hours
    And the total registered work time on "ProjectX" is 75 hours
    And the project manager is logged in
    When the project manager generates a report for "ProjectX"
    Then the system displays the total registered hours as 75
    And the system displays the budgeted time as 100
    And the system displays the unallocated hours as 25
    And the system confirms that the project is within budget
    And the system displays the estimated remaining work time

  Scenario: Generating a report for a project with no registered work time
    Given a project "ProjectY" exists
    And "ProjectY" has a budgeted time of 50 hours
    And no hours have been registered on "ProjectY"
    And the project manager is logged in
    When the project manager generates a report for "ProjectY"
    Then the system displays the total registered hours as 0
    And the system displays the budgeted time as 50
    And the system warns "No work has been registered on this project yet"

  Scenario: Generating a report for a non-existent project
    Given no project named "NonExistingProject" exists
    And the project manager is logged in
    When the project manager attempts to generate a report for "NonExistingProject"
    Then the system displays an error message "Project not found"

  Scenario: Generating a report when budgeted time is not set
    Given a project "ProjectZ" exists
    And "ProjectZ" has no budgeted time set
    And the total registered work time on "ProjectZ" is 10 hours
    And the project manager is logged in
    When the project manager generates a report for "ProjectZ"
    Then the system displays an error message "Budgeted time is missing for this project"

  Scenario: Generating a report when no project is selected
    Given the project manager is logged in
    When the project manager attempts to generate a report without selecting a project
    Then the system displays an error message "No project selected"