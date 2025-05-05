Feature: Create Activity
  Description: A project manager creates an activity and assigns employees to it.
  Actors: Project Manager

  Scenario: Successfully create a new activity with ID and valid dates
    Given a project with the name "ProjectA" is already created
    And project manager is logged in as employee "E123"
    When a new activity with name "DesignPhase", start date "01/05/2025" and end date "15/05/2025" is added to the project "ProjectA"
    Then the activity "DesignPhase" should now be part of the project "ProjectA"

  Scenario: Fail to create activity with end date before start date
    Given a project with the name "ProjectA" is already created
    And project manager is logged in as employee "E123"
    When a new activity with name "InvalidDates", start date "20/05/2025" and end date "10/05/2025" is added to the project "ProjectA"
    Then the system should display the error "End date cannot be before start date"

  Scenario: Fail to create two activities with same name in same project
    Given a project with the name "ProjectA" is already created
    And project manager is logged in as employee "E123"
    And a new activity with name "DuplicateName", start date "01/06/2025" and end date "10/06/2025" is added to the project "ProjectA"
    When the project manager tries to add another activity with name "DuplicateName", start date "11/06/2025" and end date "20/06/2025" to project "ProjectA"
    Then the system should display the error "Activity with name 'DuplicateName' already exists in project 'ProjectA'"

  Scenario: Allow same activity name in different projects
    Given a project with the name "ProjectA" is already created
    And a second project with the name "ProjectB" is created
    And project manager is logged in as employee "E222"
    When a new activity with name "Testing", start date "01/07/2025" and end date "10/07/2025" is added to the project "ProjectA"
    And another activity with name "Testing", start date "05/07/2025" and end date "15/07/2025" is added to the project "ProjectB"
    Then both projects should contain an activity named "Testing"

  Scenario: Assign employee to an activity
    Given a project with the name "ProjectX" is already created
    And project manager is logged in as employee "E123"
    And a new activity with name "DesignPhase", start date "01/06/2025" and end date "10/06/2025" is added to the project "ProjectX"
    When employee "E123" is assigned to the activity "DesignPhase"
    Then employee "E123" should appear on the list of assigned employees for "DesignPhase"

  Scenario: Fail to assign employee to more than 10 activities
    Given a project with the name "ProjectX" is already created
    And project manager is logged in as employee "E123"
    And employee "E999" is logged in and already assigned to 10 activities
    When a new activity with name "OverflowActivity", start date "01/07/2025" and end date "05/07/2025" is added to the project "ProjectX"
    And employee "E999" is assigned to the activity "OverflowActivity"
    Then the system should display the error "E999 cannot be assigned to more than 10 activities"

  Scenario: Cannot assign the same employee twice
    Given a project with the name "ProjectX" is already created
    And project manager is logged in as employee "E123"
    And a new activity with name "DesignPhase", start date "01/08/2025" and end date "10/08/2025" is added to the project "ProjectX"
    And employee "E123" is already assigned to the activity "DesignPhase"
    When the project manager tries to assign employee "E123" to "DesignPhase" again
    Then the system should display the error "Activity with name 'DesignPhase' already exists in project 'ProjectX'"


  Scenario: Prevent duplicate activity names in the same project
    Given a project with the name "ProjectC" is already created
    And project manager is logged in as employee "E111"
    When a new activity with name "Development", start date "01/09/2025" and end date "15/09/2025" is added to the project "ProjectC"
    When the project manager tries to add another activity with name "Development", start date "16/09/2025" and end date "30/09/2025" to project "ProjectC"
    Then the system should display the error "Activity with name 'Development' already exists in project 'ProjectC'"
