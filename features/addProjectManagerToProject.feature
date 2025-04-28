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

