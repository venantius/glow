(ns searchlight.core
  (:require [io.aviso.ansi :as ansi]
            [searchlight.regex :as regex]))

(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (clojure.string/split s (re-pattern (java.util.regex.Pattern/quote split)) 2))

(declare split-colorize-and-recurse)

(def ansi-fn-map
  {:red ansi/red
   :cyan ansi/cyan
   :green ansi/green})

(defn highlight
  "Highlight a Clojure string"
  [s]
  (if-let [regex-match (regex/match-regex s)]
    (split-colorize-and-recurse s regex-match :red)
    (if-let [string-match (regex/match-string s)]
      (split-colorize-and-recurse s string-match :cyan)
      (if-let [keyword-match (regex/match-keyword s)]
        (split-colorize-and-recurse s keyword-match :green)
        (if-let [s-exp-match (regex/match-s-exp s)]
          (split-colorize-and-recurse s s-exp-match :red)
          s)))))

(defn split-colorize-and-recurse
  [s match color]
  (let [[pre post] (split-in-two s match)]
    (str (highlight pre) ((color ansi-fn-map) match) (highlight post))))
