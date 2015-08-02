(ns glow.core
  (:require [glow.ansi :as ansi]
            [glow.regex :as regex]))

(def ansi-fn-map
  {:red ansi/red
   :green ansi/green
   :yellow ansi/yellow
   :blue ansi/blue
   :magenta ansi/magenta
   :cyan ansi/cyan
   :bright-red ansi/bright-red
   :bright-green ansi/bright-green
   :default str})

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
   :regex :red})

(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (clojure.string/split s (re-pattern (java.util.regex.Pattern/quote split)) 2))

(defn colorize-and-recurse
  "Given a string, a known substring, a callback function, and the Clojure
  literal type, split the string in half around the substring, then re-join:
   - the results of the callback function on the first half
   - the colorized substring
   - the results of the callback function on the second half"
  [s match callback hl-type]
  (let [[pre post] (split-in-two s match)]
    (str (callback pre)
         (((get colorscheme hl-type :default) ansi-fn-map) match)
         (callback post))))

(defn- highlight-exceptions
  "Highlight Clojure exception keywords."
  [s]
  (if-let [match (regex/match-exception s)]
    (colorize-and-recurse s match highlight-exceptions :exception)
    s))

(defn highlight-repeats
  "Highlight clojure.core repeat keywords."
  [s]
  (if-let [match (regex/match-repeat s)]
    (colorize-and-recurse s match highlight-repeats :repeat)
    (highlight-exceptions s)))

(defn- highlight-conditionals
  "Highlight clojure.core conditionals."
  [s]
  (if-let [match (regex/match-conditional s)]
    (colorize-and-recurse s match highlight-conditionals :conditional)
    (highlight-repeats s)))

(defn- highlight-variables
  "Highlight clojure.core variables."
  [s]
  (if-let [match (regex/match-variable s)]
    (colorize-and-recurse s match highlight-variables :variable)
    (highlight-conditionals s)))

(defn- highlight-core-fns
  "Highlight functions in clojure.core."
  [s]
  (if-let [match (regex/match-core-fn s)]
    (colorize-and-recurse s match highlight-core-fns :core-fn)
    (highlight-variables s)))

(defn- highlight-definitions
  "Highlight definitions."
  [s]
  (if-let [match (regex/match-definition s)]
    (colorize-and-recurse s match highlight-definitions :definition)
    (highlight-core-fns s)))

(defn- highlight-reader-chars
  "Highlight reader characters."
  [s]
  (if-let [match (regex/match-reader-char s)]
    (colorize-and-recurse s match highlight-reader-chars :reader-char)
    (highlight-definitions s)))

(defn- highlight-special-forms
  "Highlight special forms."
  [s]
  (if-let [match (regex/match-special s)]
    (colorize-and-recurse s match highlight-special-forms :special-form)
    (highlight-reader-chars s)))

(defn- highlight-macros
  "Highlight macros."
  [s]
  (if-let [match (regex/match-macro s)]
    (colorize-and-recurse s match highlight-macros :macro)
    (highlight-special-forms s)))

(defn- highlight-numbers
  "Highlight numbers."
  [s]
  (if-let [match (regex/match-number s)]
    (colorize-and-recurse s match highlight-macros :number)
    (highlight-macros s)))

(defn- highlight-booleans
  "Highlight booleans."
  [s]
  (if-let [match (regex/match-bool s)]
    (colorize-and-recurse s match highlight-booleans :boolean)
    (highlight-numbers s)))

(defn- highlight-nils
  "Highlight nils."
  [s]
  (if-let [match (regex/match-nil s)]
    (colorize-and-recurse s match highlight-nils :nil)
    (highlight-booleans s)))

(defn- highlight-s-exps
  "Highlight s-expressions."
  [s]
  (if-let [match (regex/match-s-exp s)]
    (colorize-and-recurse s match highlight-s-exps :s-exp)
    (highlight-nils s)))

(defn- highlight-keywords
  "Highlight keywords."
  [s]
  (if-let [match (regex/match-keyword s)]
    (colorize-and-recurse s match highlight-keywords :keyword)
    (highlight-s-exps s)))

(defn- highlight-comments
  "Highlight comments."
  [s]
  (if-let [match (regex/match-comment s)]
    (colorize-and-recurse s match highlight-comments :comment)
    (highlight-keywords s)))

(defn- highlight-strings
  "Highlight string literals."
  [s]
  (if-let [match (regex/match-string s)]
    (colorize-and-recurse s match highlight-strings :string)
    (highlight-comments s)))

(defn- highlight-regexes
  "Highlight regular expression literals."
  [s]
  (if-let [match (regex/match-regex s)]
    (colorize-and-recurse s match highlight-regexes :regex)
    (highlight-strings s)))

(defn highlight
  "Highlight a string of Clojure source. This functions by pattern matching
  on the following literals, in order: regular expressions, strings, comments,
  keywords, s-expressions, nils, booleans, numbers, clojure.core macros,
  special forms, reader characters, definitions, clojure.core functions,
  clojure.core variables, conditionals, repeat forms, and exceptions.

  By default, highlight uses `glow.core/colorscheme` to figure out which
  ANSI colors to use. If you want to use a different colorscheme, just
  pass in a map in a style akin to `glow.core/colorscheme` as an optional
  second argument, e.g.:

    {:string :blue
     :number :green}"
  ([s]
   (highlight-regexes s))
  ([s opts]
   (with-redefs [colorscheme (if opts
                               opts
                               colorscheme)]
     (highlight-regexes s))))
