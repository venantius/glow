(ns searchlight.regex
  (:require [clojure.java.io :as io]))

(defn keywords->regex-pattern
  "Given a list of whitespace-delimited keywords in alphabetical order,
  escape any problematic characters and delimit on pipes."
  [resource]
  (-> (clojure.string/join
       "|"
       (-> (io/resource resource)
           slurp
           clojure.string/trim-newline
           (clojure.string/split #" ")
           reverse))
      (clojure.string/replace "+" "\\+")
      (clojure.string/replace "*" "\\*")))

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
  (re-find #"(?::{1,2}[^\ ][0-9a-z:?.$!*-_]+)" s))

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

(defn match-special
  "Match a special form"
  [s]
  (re-find #"(?<![\w-])(?:clojure.core/fn|clojure.core/let|clojure.core/loop|def|do|fn|if|let|loop|monitor-enter|monitor-exit|new|quote|recur|set!|var)(?![\w-])" s))

(defn match-definitions
  "Match a definition form"
  [s]
  (re-find #"(?<![\w-])(?:clojure.core/definline|clojure.core/definterface|clojure.core/defmacro|clojure.core/defmethod|clojure.core/defmulti|clojure.core/defn|clojure.core/defn-|clojure.core/defonce|clojure.core/defprotocol|clojure.core/defrecord|clojure.core/defstruct|clojure.core/deftype|definline|definterface|defmacro|defmethod|defmulti|defn|defn-|defonce|defprotocol|defrecord|defstruct|deftype)(?![\w-])" s))

(defn- core-keyword-pattern
  [s]
  (re-pattern (str "(?<![\\w-])(?:" s ")(?![\\w-])")))

(def macro-keyword-regex
  (core-keyword-pattern
   (keywords->regex-pattern "keywords/macros.txt")))

(def function-keyword-regex
  (core-keyword-pattern
   (keywords->regex-pattern "keywords/functions.txt")))

(defn match-macros
  "Match a clojure.core macro."
  [s]
  (re-find macro-keyword-regex s))

(defn match-func
  "Match a clojure.core function."
  [s]
  (re-find function-keyword-regex s))

;; variables
;; repeat
;; conditionals
;; exceptions
