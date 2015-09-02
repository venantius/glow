(ns glow.core
  (:require [glow.colorschemes :as colorschemes]
            [glow.html :as html]
            [glow.parse :as parse]
            [glow.terminal :as terminal]
            [instaparse.core :as insta]))

(def default-web-colorscheme
  {:background :black
   :exception :green
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

  By default, highlight uses `glow.colorschemes/terminal-default` to figure out
  which ANSI colors to use. If you want to use a different colorscheme, just
  pass in a map in a style akin to that as an optional second argument, e.g.:

    {:string :blue
     :number :green}"
  ([s]
   (terminal/ansi-colorize colorschemes/terminal-default (parse/parse s)))
  ([s colorscheme]
   (terminal/ansi-colorize colorscheme (parse/parse s))))

(defn highlight-html
  "Given a string of valid Clojure source code, parse it and return an HTML
  document of the same with span classes set. This should be used in tandem
  with `generate-css`."
  [s]
  (html/generate-html (parse/parse s)))

(defn generate-css
  "By default, generate-css uses `glow.colorschemes/solarized-dark` to figure
  out which colors to use. If you want to use a different colorscheme, just
  pass in a map in a style akin to that as an optional second argument, e.g.:

    {:string :blue
     :number :green}"
  ([]
   (html/generate-css colorschemes/solarized-dark))
  ([colorscheme]
   (html/generate-css colorscheme)))
