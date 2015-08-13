(ns glow.parse
  (:require [clojure.core]
            [clojure.java.io :as io]
            [clj-antlr.core :as antlr]
            [instaparse.core :as insta]))

(def antlr-clojure
  (antlr/parser (slurp (io/resource "parsers/Clojure.g4"))))

(defn parse
  "Parse a String of Clojure source code."
  [s]
  (antlr/parse antlr-clojure s))
