Feature: Generate project report
  Description: A project manager creates a report with a given budget time
  Actors: Project manager

  Scenario: Successfully generating a report
    Given a project with the title "ProjectX" exists
    And employee "E123" has logged in to manage activities
    And a new activity named "BudgetActivity" with start date "01/05/2025" and end date "31/05/2025" is added to the project "ProjectX"
    And "BudgetActivity" in "ProjectX" has a budgeted time of 100 hours
    And 75 hours of work are registered on activity "BudgetActivity" in "ProjectX"
    When the project manager generates a report for "ProjectX"
    Then the system displays the total registered hours as 75
    And the system displays the budgeted time as 100
    And the system displays the unallocated hours as 25
    And the system confirms that the project is within budget
    And the system displays the estimated remaining work time

  Scenario: Generating a report for a project with no registered work time
    Given a project with the title "ProjectY" exists
    And employee "E123" has logged in to manage activities
    And a new activity named "BudgetActivity" with start date "01/05/2025" and end date "31/05/2025" is added to the project "ProjectY"
    And "BudgetActivity" in "ProjectY" has a budgeted time of 50 hours
    And no hours have been registered on "ProjectY"
    When the project manager generates a report for "ProjectY"
    Then the system displays the total registered hours as 0
    And the system displays the budgeted time as 50
    And the system warns "No work has been registered on this project yet"

  Scenario: Generating a report for a non-existent project
    Given no project named "NonExistingProject" exists
    And employee "E123" has logged in to manage activities
    When the project manager attempts to generate a report for "NonExistingProject"
    Then the system displays an error message "Project not found"

  Scenario: Generating a report when budgeted time is not set
    Given a project with the title "ProjectZ" exists
    And employee "E123" has logged in to manage activities
    And a new activity named "BudgetActivity" with start date "01/05/2025" and end date "31/05/2025" is added to the project "ProjectZ"
    And 10 hours of work are registered on activity "BudgetActivity" in "ProjectZ"
    When the project manager generates a report for "ProjectZ"
    Then the system displays an error message "Budgeted time is missing for this project"

  Scenario: Generating a report when no project is selected
    Given employee "E123" has logged in to manage activities
    When the project manager attempts to generate a report without selecting a project
    Then the system displays an error message "No project selected"
