(ns searchlight.regex)

;; match-number
;; match-nil
;; match-bool

;; match-vector?
;; match-parens

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

(defn match-regex
  "Parse a string of source code and try to match on regex literals."
  [s]
  (re-find #"(?s:#\"[^\"].*\")" s))

(defn match-string
  "Parse a string of source code and try to match on string literals."
  [s]
  (re-find #"(?s:\"[^\"].*\")" s))


