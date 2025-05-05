Feature: Create Activity
  Description: A project manager creates an activity and assigns employees to it.
  Actors: Project Manager

  Scenario: Successfully create a new activity in an existing project
    Given a project with the name "ProjectX" is already created
    And employee "E123" has logged in to manage activities
    When a new activity named "DesignPhase" is added to the project "ProjectX"
    Then the activity "DesignPhase" should now be part of the project "ProjectX"

  Scenario: Assign employee to an activity
    Given a project with the name "ProjectX" is already created
    And employee "E123" has logged in to manage activities
    And the activity "DesignPhase" is part of the project "ProjectX"
    When employee "E123" is assigned to the activity "DesignPhase"
    Then employee "E123" should appear on the list of assigned employees for "DesignPhase"

  Scenario: Fail to assign employee to more than 10 activities
    Given employee "E999" has already logged in and is assigned to 10 activities
    When the project manager attempts to assign employee "E999" to a new activity called "OverflowActivity"
    Then the system should display the error "E999 cannot be assigned to more than 10 activities"

  Scenario: Cannot assign the same employee twice
    Given a project with the name "ProjectX" is already created
    And employee "E123" has logged in to manage activities
    And the activity "DesignPhase" is part of the project "ProjectX"
    And employee "E123" is already assigned to the activity "DesignPhase"
    When the project manager tries to assign employee "E123" to "DesignPhase" again
    Then the system should display the error "E123 is already assigned to DesignPhase"

  Scenario: Prevent duplicate activity names in the same project
    Given a project with the name "ProjectA" is already created
    And employee "E111" has logged in to manage activities
    When a new activity named "Development" is added to the project "ProjectA"
    And the project manager tries to add another activity named "Development" to "ProjectA"
    Then the system should display the error "Activity with name 'Development' already exists in project 'ProjectA'"

  Scenario: Allow same activity name in different projects
    Given a project with the name "ProjectA" is already created
    And a second project with the name "ProjectB" is created
    And employee "E222" has logged in to manage activities
    When a new activity named "Testing" is added to the project "ProjectA"
    And another activity named "Testing" is added to the project "ProjectB"
    Then both projects should contain an activity named "Testing"
