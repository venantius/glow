# Glow

![](https://img.shields.io/clojars/v/venantius/glow.svg)

A Clojure library for generating syntax-highlighted strings of Clojure source
code.

## Usage

Usage is fairly straightforward: the key function is `glow.core/highlight`.

Let's say you've got a clojure file with the following contents:

```clojure
(ns sample)

(defn func
 [x]
 (conj {} [:a (+ 1 x)])
```

All we have to do is slurp it and pass that to `glow.core/highlight`:

#### TODO: IMAGE GOES HERE

## Contributing

#### TODO: FILL ME OUT

## Special Thanks

#### TODO: Fill me out

## License

Copyright © 2015 W. David Jarvis

Distributed under the Eclipse Public License 1.0, the same as Clojure.
