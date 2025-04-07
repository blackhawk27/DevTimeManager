Feature: Add Employee
  Description: An employee is assigned to an activity
  Actors: Employee, Project manager

  Scenario: Add Employee to activity successfully
    Given a project "ProjectX" exists
    And an activity "ActivityY" exists in "ProjectX"
    And an employee "E123" exists
    When "E123" is assigned to "ActivityY"
    Then "E123" should be listed in "ActivityY"'s employee list

  Scenario: Add Employee to activity unsuccessfully because too many
  activities
    Given a project "ProjectX" exists
    And an activity "ActivityY" exists "ProjectX"
    And an employee "E123" exists
    And "E123" is already assigned to 10 activities
    When "E123" is assigned to "ActivityY"
    Then the system should return an error message "E123" "cannot be assigned to more than 10 activities"
