(ns searchlight.regex)

;; match-macro
;; match-special-form
;; match-core

(defn match-regex
  "Parse a string of source code and try to match on regex literals."
  [s]
  (first (re-find #"#\"(\\.|[^\"])*\"" s)) )

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

(defn match-macro
  "Parse a string of source code and try to match on macros."
  [s]
  (re-find #"(?<![\w-])(?:->|->>|\.\.|amap|and|areduce|as->|assert|binding|bound-fn|clojure\.core/->|clojure\.core/->>|clojure\.core/\.\.|clojure\.core/amap|clojure\.core/and|clojure\.core/areduce|clojure\.core/as->|clojure\.core/assert|clojure\.core/binding|clojure\.core/bound-fn|clojure\.core/comment|clojure\.core/declare|clojure\.core/delay|clojure\.core/dosync|clojure\.core/doto|clojure\.core/extend-protocol|clojure\.core/extend-type|clojure\.core/for|clojure\.core/future|clojure\.core/gen-class|clojure\.core/gen-interface|clojure\.core/import|clojure\.core/io!|clojure\.core/lazy-cat|clojure\.core/lazy-seq|clojure\.core/letfn|clojure\.core/locking|clojure\.core/memfn|clojure\.core/ns|clojure\.core/or|clojure\.core/proxy|clojure\.core/proxy-super|clojure\.core/pvalues|clojure\.core/refer-clojure|clojure\.core/reify|clojure\.core/some->|clojure\.core/some->>|clojure\.core/sync|clojure\.core/time|clojure\.core/with-bindings|clojure\.core/with-in-str|clojure\.core/with-loading-context|clojure\.core/with-local-vars|clojure\.core/with-open|clojure\.core/with-out-str|clojure\.core/with-precision|clojure\.core/with-redefs|comment|declare|delay|dosync|doto|extend-protocol|extend-type|for|future|gen-class|gen-interface|import|io!|lazy-cat|lazy-seq|letfn|locking|memfn|ns|or|proxy|proxy-super|pvalues|refer-clojure|reify|some->|some->>|sync|time|with-bindings|with-in-str|with-loading-context|with-local-vars|with-open|with-out-str|with-precision|with-redefs)(?![\w-])" s))

(defn match-special
  "Match a special form"
  [s]
  (re-find #"(?<![\w-])(?:clojure.core/fn|clojure.core/let|clojure.core/loop|def|do|fn|if|let|loop|monitor-enter|monitor-exit|new|quote|recur|set!|var)(?![\w-])" s))

(defn match-definitions
  "Match a definition form"
  [s]
  (re-find #"(?<![\w-])(?:clojure.core/definline|clojure.core/definterface|clojure.core/defmacro|clojure.core/defmethod|clojure.core/defmulti|clojure.core/defn|clojure.core/defn-|clojure.core/defonce|clojure.core/defprotocol|clojure.core/defrecord|clojure.core/defstruct|clojure.core/deftype|definline|definterface|defmacro|defmethod|defmulti|defn|defn-|defonce|defprotocol|defrecord|defstruct|deftype)(?![\w-])" s))
