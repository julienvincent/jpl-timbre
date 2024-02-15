<div align="center">
  <h1>JPL-Timbre</h1>
</div>

<div align="center">
  <p>
    A <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/System.Logger.html">Java Platform Logger</a> 
    [<a href="https://openjdk.org/jeps/264">JEP264</a>] bridge to the <a href="https://github.com/taoensso/timbre">Timbre</a> 
    logging framework for Clojure.
  </p>
</div>

---

This bridge allows you to reuse all of your existing Timbre config with libraries that integrate with the Java Platform
Logging API.

---

## Installation

[![Clojars Project](https://img.shields.io/clojars/v/io.julienvincent/jpl-timbre.svg)](https://clojars.org/io.julienvincent/jpl-timbre)

Just add this dependency to your projects class-path and you will be good to go.

```clojure
io.julienvincent/jpl-timbre {:mvn/version "RELEASE"}
```
