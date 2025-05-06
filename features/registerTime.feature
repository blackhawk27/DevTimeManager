Feature: Register time
  Description: An employee registers time spent on an activity
  Actors: Employee

  Scenario: Successfully register time on activity
    Given a project named "ProjectY" is prepared for time registration
    And an activity named "Coding", start date "01/06/2025" and end date "15/06/2025" is prepared for time registration in project "ProjectY"
    And an employee with id "E123" is logged in
    And employee "E123" is assigned to the activity "ActivityX" for time registration
    When the employee registers in the system that he started to work on "ActivityX" under "ProjectY" at "09:00" and stopped at "17:00"
    Then the employee has worked 8 hours on "ActivityX" under "ProjectY"

  Scenario: Updating wrongly registered time on activity
    Given a project named "ProjectY" is prepared for time registration
    And an activity named "Coding", start date "01/06/2025" and end date "15/06/2025" is prepared for time registration in project "ProjectY"
    And an employee with id "E123" is logged in
    And employee "E123" is assigned to the activity "ActivityX" for time registration
    And the employee registers in the system that he started to work on "ActivityX" under "ProjectY" at "09:00" and stopped at "15:00"
    When the employee later corrects time spent in the system to say he started to work on "ActivityX" under "ProjectY" at "09:00" and stopped at "17:00"
    Then the employee has worked 8 hours on "ActivityX" under "ProjectY" instead of 6 hours

  Scenario: Registering free time
    Given an employee with id "E123" is logged in
    When the employee registers free time from day "31/03/2025" up to and including "02/04/2025"
    Then the employee has 3 days of free time

  Scenario: Register sick leave despite have registered work time
    Given a project named "ProjectY" is prepared for time registration
    And an activity named "Coding", start date "01/06/2025" and end date "15/06/2025" is prepared for time registration in project "ProjectY"
    And an employee with id "E123" is logged in
    And employee "E123" is assigned to the activity "ActivityX" for time registration
    And the employee has registered work time on "ActivityX" under "ProjectY" on "31/03/2025"
    And the employee want to update the existing registration on "31/03/2025"
    When the employee updates the registration to sick leave and gives an end date on "02/04/2025"
    Then the time in period "31/03/2025" up to and including "02/04/2025" is updated as sick leave
