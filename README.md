# PayStation

A parking pay station built with test-driven development, based on the PayStation example from Henrik Bærbak Christensen's *Flexible, Reliable Software*.

Customers put in 5, 10 or 25 cent coins, see how many minutes they've bought, and either buy a receipt or cancel and get their coins back. An admin panel empties the machine and switches the rate strategy by town.

## Rate strategies

| Town      | Strategy     | Rule |
|-----------|--------------|------|
| Alphatown | Linear1      | 2 minutes per 5 cents |
| Betatown  | Progressive  | 2 min/5¢ for the first hour, 1.5 min/5¢ for the second, 1 min/5¢ after that |
| Gammatown | Alternating1 | Linear1 on weekends, Progressive on weekdays |
| Deltatown | Linear2      | 1 minute per 5 cents |
| Omegatown | Alternating2 | Free on weekends, Linear1 on weekdays |

## Run

```bash
javac -d out src/main/java/edu/temple/cis/paystation/*.java
java -cp out edu.temple.cis.paystation.PayStationMain
```
