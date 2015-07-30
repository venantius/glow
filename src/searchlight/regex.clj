(ns searchlight.regex)

;; match-number
;; match-nil
;; match-bool

(defn match-regex
  "Parse a string of source code and try to match on regex literals."
  [s]
  (re-find #"(?s:#\"[^\"].*\")|(?s:#\"\")" s))

;; this currently picks up too much
(defn match-string
  "Parse a string of source code and try to match on string literals."
  [s]
  (first (re-find #"\"(\\.|[^\"])*\"" s)))

(defn match-nil
  "Parse a string of source code and try to match on nil."
  [s]
  (re-find #"(nil)"))

(defn match-keyword
  "Parse a string of source code and try to match on keywords"
  [s]
  (re-find #"(?::{1,2}[^\ ][0-9a-z:?.$!*-_]+)" s))

(defn match-comment
  "Parse a string of source code and try to match on comments"
  [s]
  (re-find #"(?:;.*\n)" s))

(defn match-s-exp
  "Parse a string of source code and try to match on s-expressions"
  [s]
  (re-find #"(?:[\[\]\(\){}])" s))
