# Glow

[![Build Status](https://travis-ci.org/venantius/glow.svg?branch=master)](https://travis-ci.org/venantius/glow)

A Clojure library for generating syntax-highlighted strings of Clojure source
code.

## Installation

To use Glow in your project, just add the following to the `:dependencies` key of your `project.clj`:

```clojure
[venantius/glow "0.1.1"]
```

## Usage

Usage is fairly straightforward.

Let's say you've got a clojure file with the following contents:

```clojure
(ns sample)

(defn func
 [^Throwable x & y]
 (conj {} [:a (+ 1.1 x)]))

(def variable
  @(future
     (if-let [x 5]
       true
       "false")))
```

All we have to do is slurp that file and pass the string to `glow.core/highlight`:

![](./doc/glow.png)

Nice.

## Configuration

Don't like the default colorscheme? No problem! 

Pass a map with whatever highlighting options you want as an optional
secondary argument:

![](./doc/glow_2.png)

Take a look at `glow.core/colorscheme` to see how this should be structured.

## Contributing

In general, bug reports, fixes, and code cleanup are always appreciated. 
Feature requests are liable to be subject to a bit more discussion. 

When filing issues, please include the following:

 * The operating system
 * The JDK version
 * The Leiningen version
 * The Clojure version
 * Any plugins and dependencies in your `project.clj` and your `~/.lein/profiles.clj`

## Special Thanks

I owe a big debt of gratitude to the authors, contributors and maintainers of [vim-clojure-static](https://github.com/guns/vim-clojure-static), which was a big inspiration. I also owe thanks to Alex Engelberg and Kyle Kingsbury (@aphyr) - the former helped me when I was trying to use Instaparse, and the latter I owe a huge favor to for making an accessible Clojure library for ANTLR.

## License

Copyright © 2015 W. David Jarvis

Distributed under the Eclipse Public License 1.0, the same as Clojure.
