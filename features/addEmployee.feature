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
    Given a project "New Website" exists
    And an activity "Front End Development" exists in "New Website"
    And an employee with id "E123" exists
    And "E123" is already assigned to 10 activities
    When "E123" is assigned to "Front End Development"
    Then the system should return an error message "E123 cannot be assigned to more than 10 activities"

  Scenario: Unsuccessful assignment of employee to activity because employee is already assigned to activity
    Given a project "New Website" exists
    And an activity "Front End Development" exists in "New Website"
    And an employee with id "E123" exists
    And "E123" is assigned to "Front End Development"
    When "E123" is assigned to "Front End Development" again
    Then the system should return an error message "E123 is already assigned to Front End Development"
