Feature: Get a Specific Todo
  As a user, I want to get a specific todo task that was previously created so that I can view its information.

  Background:
    Given the service is running
    And the following todos exist in the system:
      | id | title        | doneStatus | description              |
      | 1  | Call manager | false      | Call 514-123-4567        |
      | 2  | Fill form    | true       | Write name and email     |
      | 3  | Send email   | true       | Send to example@mail.com |

  # Normal Flow
  Scenario Outline: Get a todo by its ID
    When a user sends a GET request to todos/<id>
    Then the system shall return an HTTP response with status code 200
    And the response shall contain a list of todos
    And the response shall contain a todo task with title "<title>", doneStatus "<doneStatus>" and description "<description>"

    Examples:
      | id | title        | doneStatus | description              |
      | 1  | Call manager | false      | Call 514-123-4567        |
      | 2  | Fill form    | true       | Write name and email     |
      | 3  | Send email   | true       | Send to example@mail.com |

  # Alternate Flow
  Scenario Outline: Get a todo by its title
    When a user sends a GET request to "todos" with parameter "<title>"
    Then the system shall return an HTTP response with status code 200
    And the response shall contain a list of todos
    And the response shall contain a todo task with title "<title>", doneStatus "<doneStatus>" and description "<description>"

    Examples:
      | title        | doneStatus | description              |
      | Call manager | false      | Call 514-123-4567        |
      | Fill form    | true       | Write name and email     |
      | Send email   | true       | Send to example@mail.com |

  # Error Flow
  Scenario Outline: Get a todo with an invalid ID
    When a user sends a GET request to todos/<invalid_id>
    Then the system shall return an HTTP response with status code 404
    And the response shall contain the error message "[Could not find an instance with todos/<invalid_id>]"

    Examples:
      | invalid_id |
      | -1         |
      | 999        |
