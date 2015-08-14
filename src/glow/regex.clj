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
   (str "(?:"
        (keywords->regex-pattern (slurp (io/resource resource)))
        ")")))

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
  (= (re-find special-keyword-regex s) s))

(defn match-reader-char
  "Match a Clojure reader character."
  [s]
  (= (re-find #"(?:\~@|\~|@|`|\^|'|&)" s) s))

(defn match-definition
  "Match a clojure.core definition form."
  [s]
  (= (re-find definition-keyword-regex s) s))

(defn match-macro
  "Match a clojure.core macro."
  [s]
  (= (re-find macro-keyword-regex s) s))

(defn match-core-fn
  "Match a clojure.core function."
  [s]
  (= (re-find function-keyword-regex s) s))

(defn match-variable
  "Match a clojure.core variable."
  [s]
  (= (re-find variable-keyword-regex s) s))

(defn match-conditional
  "Match a clojure.core conditional."
  [s]
  (= (re-find cond-keyword-regex s) s))

(defn match-repeat
  [s]
  (= (re-find repeat-keyword-regex s) s))

(defn match-exception
  [s]
  (= (re-find exception-keyword-regex s) s))
