Feature: Update a Student Registration

  Scenario: Update Student Registration attributes

    Given follow student registration exists in database with below attributes:
      | StudentName  |  registrationnumber   | dateofbirth           |  address |
      | Ram          |  2007093A             | 2016-01-23T12:34:56Z  |  India   |

    When I try to register student registration with below attributes
      | StudentName  | registrationnumber    | dateofbirth           |  address |
      | ram kumar    | 2007093D              | 2016-01-23T12:34:56Z  |  India   |

    Then should return true: