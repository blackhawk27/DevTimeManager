Feature: Add project manager to project
  Description: Assigned employees can assign any assigned employee project manager privileges if none exists.
  After a manager is assigned, only he can transfer role.

  Scenario: Adding a project manager to a project without a manager
    Given a project "New Website" exists with no manager
    And an employee with id "E123" is assigned to "New Website"
    And an employee with id "E456" is assigned to "New Website"
    And no Project Manager is assigned to "New Website"
    When "E123" assigns "E456" as project manager to "New Website"
    Then "E456" should be able to see "New Website" in their list of managed projects
    And "E456" should be the project manager of "New Website"
    And "E456" should now be of the ProjectManager class

  Scenario: Unsuccessfully adding a project manager to a project that already has a manager
    Given a project "New Website" exists with "E123" as the project manager
    When "E456" tries to assign "E456" as the project manager of "New Website"
    Then "E456" will get an error message "This project already has a project manager. You cannot assign a new one."

  Scenario: Unsuccessfully adding a project manager because acting employee is not part of the project
    Given a project "New Website" exists with no manager
    And an employee with id "E123" is assigned to "New Website"
    And an employee with id "E456" is not assigned to "New Website"
    When "E456" tries to assign "E456" as the project manager of "New Website"
    Then "E456" will get an error message "E456 is not assigned to the project and cannot assign a manager."

