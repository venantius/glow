# Glow

[![Build Status](https://travis-ci.org/venantius/glow.svg)](https://travis-ci.org/venantius/glow)

A Clojure library for generating syntax-highlighted strings of Clojure source
code.

## Installation

To use Glow in your project, just add the following to the `:dependencies` key of your `project.clj`:

```clojure
[venantius/glow "0.1.0"]
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

I'm interested in making the regular expressions in Glow faster and more robust. Frankly, I'm not an expert in regexes, and some of my implementations could use a more critical eye.

To that end, I'm actively soliciting pull requests to expand the regular expression test coverage and to detect and resolve failure cases.

If you're interested in making a different sort of contribution to the project, please make sure to open an issue before going straight to a pull request - I like to make sure there's a consensus that both of our time will be well spent.

## Special Thanks

I owe a big debt of gratitude to the authors, contributors and maintainers of [vim-clojure-static](https://github.com/guns/vim-clojure-static), whose work was a constant reference as I was writing Glow's regular expressions.

## License

Copyright © 2015 W. David Jarvis

Distributed under the Eclipse Public License 1.0, the same as Clojure.
