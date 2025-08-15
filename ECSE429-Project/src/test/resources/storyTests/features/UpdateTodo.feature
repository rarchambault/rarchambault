Feature: Update Todo
  As a user, I want to update a specific todo task that was previously created so that I can modify its information.

  Background:
    Given the service is running
    And the following todos exist in the system:
      | id | title        | doneStatus | description              |
      | 1  | Call manager | false      | Call 514-123-4567        |
      | 2  | Fill form    | true       | Write name and email     |
      | 3  | Send email   | true       | Send to example@mail.com |

  # Normal Flow
  Scenario Outline: Update the title, doneStatus and description of a todo using PUT
    When a user sends a PUT request to todos/<id> containing a title "<title>", a doneStatus "<doneStatus>" and a description "<description>"
    Then the system shall return an HTTP response with status code 200
    And the response shall display a todo task with title "<title>", doneStatus "<doneStatus>" and description "<description>"
    And a todo with id <id>, title "<title>", doneStatus "<doneStatus>" and description "<description>" shall exist in the system

    Examples:
      | id | title          | doneStatus | description              |
      | 1  | Updated title  | true       | Updated description      |
      | 2  | Fill form      | true       | Updated description2     |
      | 3  | Updated title2 | true       | Send to example@mail.com |

  # Alternate Flow
  Scenario Outline: Update the title, doneStatus and description of a todo using POST
    When a user sends a POST request to todos/<id> containing a title "<title>", a doneStatus "<doneStatus>" and a description "<description>"
    Then the system shall return an HTTP response with status code 200
    And the response shall display a todo task with title "<title>", doneStatus "<doneStatus>" and description "<description>"
    And a todo with id <id>, title "<title>", doneStatus "<doneStatus>" and description "<description>" shall exist in the system

    Examples:
      | id | title          | doneStatus | description              |
      | 1  | Updated title  | true       | Updated description      |
      | 2  | Fill form      | true       | Updated description2     |
      | 3  | Updated title2 | true       | Send to example@mail.com |

  # Error Flow
  Scenario Outline: Update the title, doneStatus and description of a todo using PUT with an invalid ID
    When a user sends a PUT request to todos/<id> containing a title "<title>", a doneStatus "<doneStatus>" and a description "<description>"
    Then the system shall return an HTTP response with status code 404
    And the response shall contain the error message "[Invalid GUID for <id> entity todo]"

    Examples:
      | id  | title          | doneStatus | description              |
      | -1  | Updated title  | true       | Updated description      |
      | 999 | Fill form      | true       | Updated description2     |

  Scenario Outline: Update a todo without specifying a title using PUT
    When a user sends a PUT request to todos/<id> containing a doneStatus "<doneStatus>" and a description "<description>"
    Then the system shall return an HTTP response with status code 400
    And the response shall contain the error message "[title : field is mandatory]"

    Examples:
      | id | doneStatus | description          |
      | 1  | true       | Updated description  |
      | 2  | false      | Updated description2 |