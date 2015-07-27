(ns searchlight.core)

(defn match-string
  "Parse a string of source code and try to match on strings"
  [s]
  (re-find #"(?:\"[^\"].*\")" s))
