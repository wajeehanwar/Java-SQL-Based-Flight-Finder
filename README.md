<h1 align="center">
  <br>
  Java SQL Based Flight Finder
  <br>
</h1>

<p align="center">
<a href="#overview">Overview</a> •
  <a href="#how-to-use">How To Use</a> •
  <a href="#credits">Credits</a> •
  <a href="#license">License</a>
</p>

## Overview

A Java program that accepts database credentials and then accepts two cities (Los-Angeles and Chicago for example) and finds the flights from the first to the second in the flights table.

For the selected flights, it prints out the flight number, distance, and duration of the flight, computed by time difference (and displayed in hours:min:sec).

## How To Use

For Oracle run with:

```bash
 $ java -classpath ojdbc6.jar;

 # Use ':' instead of ';' on UNIX

 # **Note**: change the variable oracleServer to "localhost"
 # for use with a tunnel.
```

## Credits

This software was developed using the following:

- Java
- SQL
- Oracle

## License

MIT

---

> GitHub [@wajeehanwar](https://github.com/wajeehanwar)
