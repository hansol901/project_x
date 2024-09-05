Feature: Verify windows application functionalities

  @Run
  Scenario Outline: Verify windows calculator
    Given the user opens the system calculator
    When the user enters number <FirstNumber>
    And the user enters number <SecondNumber>
    When the user presses on the <Operation> sign
    Then the result is as expected
    Examples:
      | FirstNumber | SecondNumber | Operation |
      | 1           | 2            | +         |
      | 2.0         | 3.1          | -         |
      | -1          | 2            | +         |
      | 2           | 2            | *         |
      | 10          | 5            | /         |
      | 10          | 3            | /         |