Feature: Create New Todo
  As a user, I want to create a todo task so that I can keep track of the tasks I have to do.

  Background:
    Given the service is running

  # Normal Flow
  Scenario Outline: Create a todo using a title, a doneStatus and a description
    When a user sends a POST request to "todos" containing a title "<title>", a doneStatus "<doneStatus>" and a description "<description>"
    Then the system shall return an HTTP response with status code 201
    And the response shall display a todo task with title "<title>", doneStatus "<doneStatus>" and description "<description>"
    And a todo with title "<title>", doneStatus "<doneStatus>" and description "<description>" shall exist in the system

    Examples:
      | title    | doneStatus | description   |
      | Todo 1   | true       | Description 1 |
      | Todo 2   | false      | Description 2 |

  # Alternate Flow
  Scenario Outline: Create a todo using only a title
    When a user sends a POST request to "todos" using title "<title>"
    Then the system shall return an HTTP response with status code 201
    And the response shall display a todo task with title "<title>", doneStatus "" and description ""
    And a todo with title "<title>", doneStatus "" and description "" shall exist in the system

    Examples:
      | title   |
      | Todo 1  |
      | Todo 2  |

  # Error Flow
  Scenario Outline: Create a todo without a title
    When a user sends a POST request to "todos" using title ""
    Then the system shall return an HTTP response with status code 400
    And the response shall contain the error message "[Failed Validation: title : can not be empty]"