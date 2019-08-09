Feature: Currency can convert amounts between CAD and USD

    Scenario Template: Convert an amount in CAD to CAD
        Given an amount of <amount in CAD> CAD
        When the amount is converted to CAD
        Then the converted amount is <converted amount in CAD>
        Examples:
            | amount in CAD | converted amount in CAD |
            | 0             | 0                       |
            | 10            | 10                      |

    Scenario Template: Convert an amount in USD to USD
        Given an amount of <amount in USD> USD
        When the amount is converted to USD
        Then the converted amount is <converted amount in USD>
        Examples:
            | amount in USD | converted amount in USD |
            | 0             | 0                       |
            | 10            | 10                      |

    Scenario Template: Convert an amount in CAD to USD
        Given an amount of <amount in CAD> CAD
        When the amount is converted to USD
        Then the converted amount is <converted amount in USD>
        Examples:
            | amount in CAD | converted amount in USD |
            | 0             | 0                       |
            | 10.0000       | 6.6667                  |
            | 100.0000      | 66.6667                 |
            | 1000.0000     | 666.6667                |

    Scenario Template: Convert an amount in USD to CAD
        Given an amount of <amount in USD> USD
        When the amount is converted to CAD
        Then the converted amount is <converted amount in CAD>
        Examples:
            | amount in USD | converted amount in CAD |
            | 0             | 0                       |
            | 10.0000       | 15.0000                 |
            | 100.0000      | 150.0000                |
            | 1000.0000     | 1500.0000               |
