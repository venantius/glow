(ns searchlight.core)

;; match-comment
;; match-keyword
;; match-number

;; match-vector?
;; match-parens

(defn match-regex
  "Parse a string of source code and try to match on regex literals."
  [s]
  (re-find #"(?s:#\"[^\"].*\")" s))

(defn match-string
  "Parse a string of source code and try to match on string literals."
  [s]
  (re-find #"(?s:\"[^\"].*\")" s))
