(ns glow.regex
  (:require [clojure.java.io :as io]))

(defn keywords->regex-pattern
  "Given a string of whitespace-delimited keywords in alphabetical order,
  escape any problematic characters and delimit on pipes."
  [s]
  (-> (clojure.string/join
       "|"
       (-> s
           clojure.string/trim-newline
           (clojure.string/split #" ")
           reverse))
      (clojure.string/replace "+" "\\+")
      (clojure.string/replace "." "\\.")
      (clojure.string/replace "?" "\\?")
      (clojure.string/replace "*" "\\*")))

(defn core-keyword-pattern
  [resource]
  (re-pattern
   (str "(?<![\\w-])(?:"
        (keywords->regex-pattern (slurp (io/resource resource)))
        ")(?![\\w-])")))

(defn match-regex
  "Parse a string of source code and try to match on regex literals."
  [s]
  (first (re-find #"#\"(\\.|[^\"])*\"" s)))

(defn match-string
  "Parse a string of source code and try to match on string literals."
  [s]
  (first (re-find #"\"(\\.|[^\"])*\"" s)))

(defn match-comment
  "Parse a string of source code and try to match on comments"
  [s]
  (re-find #"(?:;.*\n)" s))

(defn match-keyword
  "Parse a string of source code and try to match on keywords"
  [s]
  (re-find #"(?::{1,2}[^\ ][0-9a-z:?.$!*-_]*)" s))

(defn match-s-exp
  "Parse a string of source code and try to match on s-expressions"
  [s]
  (re-find #"(?:[\[\]\(\){}])" s))

(defn match-nil
  "Parse a string of source code and try to match on nil."
  [s]
  (re-find #"(?s:nil)" s))

(defn match-bool
  "Parse a string of source code and try to match on booleans."
  [s]
  (re-find #"(?s:true|false)" s))

(defn match-number
  "Parse a string of source code and try to match on numbers."
  [s]
  (re-find #"(?:\d+\.\d+|\d+)" s))

(def special-keyword-regex
  (core-keyword-pattern "keywords/special.txt"))

(def definition-keyword-regex
  (core-keyword-pattern "keywords/definitions.txt"))

(def macro-keyword-regex
  (core-keyword-pattern "keywords/macros.txt"))

(def function-keyword-regex
  (core-keyword-pattern "keywords/functions.txt"))

(def variable-keyword-regex
  (core-keyword-pattern "keywords/variables.txt"))

(def cond-keyword-regex
  (core-keyword-pattern "keywords/cond.txt"))

(def repeat-keyword-regex
  (core-keyword-pattern "keywords/repeat.txt"))

(def exception-keyword-regex
  (core-keyword-pattern "keywords/exception.txt"))

(defn match-special
  "Match special forms."
  [s]
  (re-find special-keyword-regex s))

(defn match-definitions
  "Match a clojure.core definition form."
  [s]
  (re-find definition-keyword-regex s))

(defn match-macro
  "Match a clojure.core macro."
  [s]
  (re-find macro-keyword-regex s))

(defn match-func
  "Match a clojure.core function."
  [s]
  (re-find function-keyword-regex s))

(defn match-variable
  "Match a clojure.core variable."
  [s]
  (re-find variable-keyword-regex s))

(defn match-conditional
  [s]
  (re-find cond-keyword-regex s))

(defn match-repeats
  [s]
  (re-find repeat-keyword-regex s))

(defn match-exceptions
  [s]
  (re-find exception-keyword-regex s))
