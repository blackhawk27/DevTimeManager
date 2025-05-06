Feature: Add Employee
  Description: An employee is assigned to an activity
  Actors: Employee, Project manager

  Scenario: Successfully assigning an employee to an activity
    Given a project "WebsiteRedesign" exists
    And an activity named "UI Design", start date "01/06/2025" and end date "15/06/2025" exists in "WebsiteRedesign"
    And an employee with id "EMP001" exists
    When "EMP001" is assigned to "UI Design"
    Then "EMP001" should be listed in the employee list for "UI Design"

  Scenario: Fail to assign employee to activity because too many activities
    Given a project "InternalTool" exists
    And an activity named "Backend Refactor", start date "01/06/2025" and end date "30/06/2025" exists in "InternalTool"
    And an employee with id "EMP002" exists
    And "EMP002" is already assigned to 10 other activities
    When "EMP002" is assigned to "Backend Refactor"
    Then the system should return an error message "EMP002 cannot be assigned to more than 10 activities"

  Scenario: Fail to assign employee who is already assigned to activity
    Given a project "MobileApp" exists
    And an activity named "API Integration", start date "01/06/2025" and end date "30/06/2025" exists in "MobileApp"
    And an employee with id "EMP003" exists
    And "EMP003" is already assigned to "API Integration"
    When "EMP003" is assigned to "API Integration" again
    Then the system should return an error message "EMP003 is already assigned to API Integration"
