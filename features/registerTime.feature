Feature: Register time
  Description: An employee registers time spent on an activity
  Actors: Employee

  Scenario: Successfully register time on activity
    Given the employee is logged in
    And that an employee is assigned to an "ActivityX" under "ProjectY"
    When the employee registers in the system that he arrived at 09:00 and left at 17:00
    Then the employee has worked 8 hours on "ActivityX"

  Scenario: Updating wrongly registered time on activity
    Given the employee is logged in
    And that an employee is assigned to an "ActivityX" under "ProjectY"
    And he has registered his time from 09:00 to 15:00
    When the employee later corrects time spent in the system to say he arrived at 09:00 and left at 17:00
    Then the employee has worked 8 hours on "ActivityX" instead of 6