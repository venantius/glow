(ns glow.core
  (:require [glow.ansi :as ansi]
            [glow.parse :as parse]
            [glow.regex :as regex]
            [glow.terminal :as terminal]
            [instaparse.core :as insta]))

(def colorscheme
  {:exception :green
   :repeat  :green
   :conditional :green
   :variable :blue
   :core-fn :blue
   :definition :bright-red
   :reader-char :red
   :special-form :red
   :macro :bright-red
   :number :cyan
   :boolean :cyan
   :nil :cyan
   :s-exp :red
   :keyword :green
   :comment :bright-green
   :string :cyan
   :character :cyan
   :regex :red})

(defn highlight
  "Given a string of valid Clojure source code, parse it and return a
  syntax-highlighted string of the same.

  By default, highlight uses `glow.core/colorscheme` to figure out which
  ANSI colors to use. If you want to use a different colorscheme, just
  pass in a map in a style akin to `glow.core/colorscheme` as an optional
  second argument, e.g.:

    {:string :blue
     :number :green}"
  ([s]
   (terminal/ansi-colorize colorscheme (parse/parse s)))
  ([s opts]
   (with-redefs [colorscheme (if opts
                               opts
                               colorscheme)]
     (highlight s))))
