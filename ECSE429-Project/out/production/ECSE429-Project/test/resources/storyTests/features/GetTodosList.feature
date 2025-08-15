Feature: Get Todos List
  As a user, I want to get the current list of all the todo tasks that have previously been entered so that I can view
  the information of all the tasks I have to do.

  Background:
    Given the service is running
    And the following todos exist in the system:
      | title        | doneStatus | description              |
      | Call manager | false      | Call 514-123-4567        |
      | Fill form    | true       | Write name and email     |
      | Send email   | true       | Send to example@mail.com |

  # Normal Flow
  Scenario Outline: Get all todos
    When a user sends a GET request to "todos"
    Then the system shall return an HTTP response with status code 200
    And the response shall contain a list of todos
    And the list shall include todos with the following information:
      | title        | doneStatus | description              |
      | Call manager | false      | Call 514-123-4567        |
      | Fill form    | true       | Write name and email     |
      | Send email   | true       | Send to example@mail.com |

  # Alternate Flow
  Scenario Outline: Get all todos that are done
    When a user sends a GET request to "todos" with filter "?doneStatus=true"
    Then the system shall return an HTTP response with status code 200
    And the response shall contain a list of todos
    And the list shall include todos with the following information:
      | title        | doneStatus | description              |
      | Fill form    | true       | Write name and email     |
      | Send email   | true       | Send to example@mail.com |

  # Error Flow
  Scenario Outline: Get all todos with the wrong endpoint
    When a user sends a GET request to "todos/list"
    Then the system shall return an HTTP response with status code 404
    And the response shall contain the error message "[Could not find an instance with todos/list]"
