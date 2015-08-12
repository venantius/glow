(ns glow.parse
  (:require [clojure.core]
            [clojure.java.io :as io]
            [clj-antlr.core :as antlr]
            [instaparse.core :as insta]))

(def clj-parser
  (insta/parser (slurp (io/resource "parsers/clojure.ebnf"))))

(defn un-nest
  [d]
  (insta/transform
   {:SEXPS str
    :SEXP str

    :WHITESPACE str

    :COMMENT str
    :KEYWORD str
    :STRING str
    :SYMBOL str

    :COLLECTION str
    :VECTOR str
    :LIST str
    :MAP str

    :READER_MACRO str
    :CARAT str
    :BOOLEAN str}
   d))

(def antlr-clojure
  (antlr/parser (slurp (io/resource "parsers/Clojure.g4"))))
