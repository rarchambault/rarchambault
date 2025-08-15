Feature: Delete Todo
  As a user, I want to delete a specific todo task that was previously created so that it no longer appears in my todo list.

  Background:
    Given the service is running
    And the following todos exist in the system:
      | id | title        | doneStatus | description              |
      | 1  | Call manager | false      | Call 514-123-4567        |
      | 2  | Fill form    | true       | Write name and email     |
      | 3  | Send email   | true       | Send to example@mail.com |

  # Normal Flow
  Scenario Outline: Delete a todo by its ID
    When a user sends a DELETE request to todos/<id>
    Then the system shall return an HTTP response with status code 200
    And a todo with <id> shall not exist in the system

    Examples:
      | id |
      | 1  |
      | 2  |
      | 3  |

  # Alternate Flow
  Scenario Outline: Delete a todo right after creating it
    When a user sends a POST request to "todos" containing a title "<title>", a doneStatus "<doneStatus>" and a description "<description>"
    And a user sends a DELETE request to todos/<id>
    Then the system shall return an HTTP response with status code 200
    And a todo with <id> shall not exist in the system

    Examples:
      | id | title    | doneStatus | description   |
      | 1  | Todo 1   | true       | Description 1 |
      | 1  | Todo 2   | false      | Description 2 |

  # Error Flow
  Scenario Outline: Delete a todo with invalid ID
    When a user sends a DELETE request to todos/<id>
    Then the system shall return an HTTP response with status code 404
    And the response shall contain the error message "[Could not find any instances with todos/<id>]"

    Examples:
      | id  |
      | -1  |
      | 999 |