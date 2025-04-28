Feature: Add Employee
  Description: An employee is assigned to an activity
  Actors: Employee, Project manager

  Scenario: Successfully assigning an employee to an activity
    Given a project "New Website" exists
    And an activity "Front End Development" exists in "New Website"
    And an employee with id "E123" exists
    When "E123" is assigned to "Front End Development"
    Then "E123" should be listed in the employee list for "Front End Development"

  Scenario: Add Employee to activity unsuccessfully because too many
  activities
    Given a project "ProjectX" exists
    And an activity "ActivityY" exists "ProjectX"
    And an employee "E123" exists
    And "E123" is already assigned to 10 activities
    When "E123" is assigned to "ActivityY"
    Then the system should return an error message "E123" "cannot be assigned to more than 10 activities"
