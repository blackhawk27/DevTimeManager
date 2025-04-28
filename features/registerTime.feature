Feature: Register time
  Description: An employee registers time spent on an activity
  Actors: Employee

  Scenario: Successfully register time on activity
    Given an employee with id "E123" is logged in
    And that the employee is assigned to an "ActivityX" under "ProjectY"
    When the employee registers in the system that he started to work on "ActivityX" under "ProjectY" at 09:00 and stopped at 17:00
    Then the employee has worked 8 hours on "ActivityX" under "ProjectY"

  Scenario: Updating wrongly registered time on activity
    Given an employee with id "E123" is logged in
    And that the employee is assigned to an "ActivityX" under "ProjectY"
    And the employee registers in the system that he started to work on "ActivityX" under "ProjectY" at 09:00 and stopped at 15:00
    When the employee later corrects time spent in the system to say he started to work on "ActivityX" under "ProjectY" at 09:00 and stopped at 17:00
    Then the employee has worked 8 hours on "ActivityX" under "ProjectY" instead of 6 hours

  Scenario: Registering free time
    Given an employee with id "E123" is logged in
    When the employee registers freetime from day "2025-03-31" up to and including "2025-04-02"
    Then the employee has 3 days of free time

  Scenario: Register sick leave despite have registered work time
    Given an employee with id "E123" is logged in
    And the employee has registered work time on "ActivityX" under "ProjectY" on "2025-03-31"
    And the employee want to update the excisting registration on "2025-03-31"
    When the employee updates the registration to sick leave and gives an end date on "2025-04-02"
    Then the time in period "2025-03-31" up to and including "2025-04-02" is updated as sick leave
