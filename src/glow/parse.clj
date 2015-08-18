(ns glow.parse
  (:require [clojure.core]
            [clojure.java.io :as io]
            [clj-antlr.core :as antlr]
            [instaparse.core :as insta]))

(def antlr-clojure
  (antlr/parser (slurp (io/resource "parsers/Clojure.g4"))))

(defn parse
  "Parse a String of Clojure source code into an enlive-style list of lists
  (sadly not vectors, though it's an easy transformation). Uses Glow's ANTLR
  grammar (see glow.parse/antlr-clojure)."
  [s]
  (antlr/parse antlr-clojure s))
