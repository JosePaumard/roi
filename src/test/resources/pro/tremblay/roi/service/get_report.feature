Feature: A report can be generated


    Scenario: An empty report is empty
        Given a starting date of 2018-07-13 and a current date of 2019-07-13
        When an empty report is generated in CAD
        Then the report has the following values
            | initial | current | fee  | net deposits | net variation amount | net variation percentage |
            | 0.00    | 0.00    | 0.00 | 0.00         | 0.00                 | 0.0000                   |
        And there are 366 values per day
        And there is no anomaly
        And the price is 0 for the day 0 2018-07-13
        And the price is 0 for the day 365 2019-07-13


    Scenario: A report does not modify its initial entities
        Given the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the following positions
            | security | quantity |
            | AAA      | 100      |
        And the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | sell             | 1      | CAD      | 2   | -10      | 2019-01-13 | AAA      |
        When the report is generated in CAD between 2018-07-13 and 2019-07-13
        Then the report has the following securities
            | symbol | price | currency |
            | AAA    | 2     | CAD      |
        And the report has the following accounts
            | name | cash | currency |
            | 123  | 10   | CAD      |
        And the report has the following positions
            | security | quantity |
            | AAA      | 100      |
        And the report has the following transactions
            | account_name | transaction_type | amount | currency | fee | quantity | trade_date | security |
            | 123          | sell             | 1      | CAD      | 2   | -10      | 2019-01-13 | AAA      |

#    Scenario: A report guarantees that the money at the beginning is still there at the end of period

#    Scenario: Some cash can be added

#    Scenario: A position can be added

#    Scenario: A transaction with fees can be added
