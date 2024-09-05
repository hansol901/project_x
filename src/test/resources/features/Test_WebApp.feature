Feature: Verify web application functionalities

  @Run
  Scenario: Verify webapp Login
    Given user is on home page
    When user press on login button
    And enter 'a' as Identifikationsnummer
    And enter 'b' as Password
    When press Login button
    Then pop-up contains correct message