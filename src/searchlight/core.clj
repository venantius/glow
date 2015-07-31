(ns searchlight.core
  (:require [searchlight.ansi :as ansi]
            [searchlight.regex :as regex]))

(def ansi-fn-map
  {:blue ansi/blue
   :red ansi/red
   :cyan ansi/cyan
   :green ansi/green
   :grey ansi/grey
   :orange ansi/orange})

(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (clojure.string/split s (re-pattern (java.util.regex.Pattern/quote split)) 2))

(defn colorize-and-recurse
  [s match callback color]
  (let [[pre post] (split-in-two s match)]
    (str (callback pre) ((color ansi-fn-map) match) (callback post))))

(defn highlight-exceptions
  "Highlight Clojure exception keywords."
  [s]
  (if-let [match (regex/match-exceptions s)]
    (colorize-and-recurse s match highlight-exceptions :green)
    s))

(defn highlight-repeats
  "Highlight clojure.core repeat keywords."
  [s]
  (if-let [match (regex/match-repeats s)]
    (colorize-and-recurse s match highlight-repeats :green)
    (highlight-exceptions s)))

(defn highlight-conditionals
  "Highlight clojure.core conditionals."
  [s]
  (if-let [match (regex/match-conditionals s)]
    (colorize-and-recurse s match highlight-conditionals :green)
    (highlight-repeats s)))

(defn highlight-variables
  "Highlight clojure.core variables."
  [s]
  (if-let [match (regex/match-variables s)]
    (colorize-and-recurse s match highlight-variables :blue)
    (highlight-conditionals s)))

(defn- highlight-core-fns
  "Highlight functions in clojure.core."
  [s]
  (if-let [match (regex/match-func s)]
    (colorize-and-recurse s match highlight-core-fns :blue)
    (highlight-variables s)))

(defn- highlight-definitions
  "Highlight definitions."
  [s]
  (if-let [match (regex/match-definitions s)]
    (colorize-and-recurse s match highlight-definitions :orange)
    (highlight-core-fns s)))

(defn- highlight-special-forms
  "Highlight special forms."
  [s]
  (if-let [match (regex/match-special s)]
    (colorize-and-recurse s match highlight-special-forms :red)
    (highlight-definitions s)))

(defn- highlight-macros
  "Highlight macros."
  [s]
  (if-let [match (regex/match-macros s)]
    (colorize-and-recurse s match highlight-macros :orange)
    (highlight-special-forms s)))

(defn- highlight-booleans
  "Highlight booleans."
  [s]
  (if-let [match (regex/match-bool s)]
    (colorize-and-recurse s match highlight-booleans :cyan)
    (highlight-macros s)))

(defn- highlight-nils
  "Highlight nils."
  [s]
  (if-let [match (regex/match-nil s)]
    (colorize-and-recurse s match highlight-nils :cyan)
    (highlight-booleans s)))

(defn- highlight-s-exps
  "Highlight s-expressions."
  [s]
  (if-let [match (regex/match-s-exp s)]
    (colorize-and-recurse s match highlight-s-exps :red)
    (highlight-nils s)))

(defn- highlight-keywords
  "Highlight keywords."
  [s]
  (if-let [match (regex/match-keyword s)]
    (colorize-and-recurse s match highlight-keywords :green)
    (highlight-s-exps s)))

(defn- highlight-comments
  "Highlight comments."
  [s]
  (if-let [match (regex/match-comment s)]
    (colorize-and-recurse s match highlight-comments :grey)
    (highlight-keywords s)))

(defn- highlight-strings
  "Highlight string literals."
  [s]
  (if-let [match (regex/match-string s)]
    (colorize-and-recurse s match highlight-strings :cyan)
    (highlight-comments s)))

(defn- highlight-regexes
  "Highlight regular expression literals."
  [s]
  (if-let [match (regex/match-regex s)]
    (colorize-and-recurse s match highlight-regexes :red)
    (highlight-strings s)))

(defn highlight
  "Highlight a string of Clojure source. This is a recursive algorithm that
  functions by performing regular expression pattern matches on the following
  Clojure literals, in order: regular expressions, strings, comments,
  keywords, s-expressions, nils, booleans,
  "
  [s]
  (highlight-regexes s))
