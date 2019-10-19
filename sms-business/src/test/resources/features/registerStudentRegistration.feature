Feature: Register a Student

  Scenario: Register Student Already Exists.

    Given follow student registration exists in database with below attributes:
      | StudentName  |  registrationnumber   | dateofbirth           |  address |
      | Ram          |  2007093A             | 2016-01-23T12:34:56Z  |  India   |

    When I try to register student registration with below attributes:
      | StudentName  | registrationnumber    | dateofbirth           |  address |
      | ram          | 2007093A              | 2016-01-23T12:34:56Z  |  India   |

    Then should return error with code 106 and the message Student Record is already exisits

  Scenario: Add new Student Registration into the database

    Given follow student registration exists in database with below attributes:
      | StudentName  | registrationnmber     | dateofbirth           | address  |
      | ram          | 2007093A              | 2016-01-23T12:34:56Z  | India    |

    When I try to register student registration with below attributes:
      | StudentName  | registrationnmber     | dateofbirth           | address  |
      | kumar        | 2007093C              | 2018-01-23T12:34:56Z  | Europe   |
    Then should return the student registration below:
      | StudentName  | registrationnmber     | dateofbirth           | address  |
      | kumar        | 2007093C              | 2018-01-23T12:34:56Z  | Europe   |



