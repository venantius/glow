(ns glow.parser-test
  "Speed tests for different parsers."
  (:require [clojure.test :refer :all]
            [clj-antlr.core :as antlr]
            [criterium.core :as criterium]
            [instaparse.core :as insta]
            [glow.parse :as parse]))

(deftest ^:perf antlr-speed
  (let [blob (slurp (.getResourceAsStream (clojure.lang.RT/baseLoader) "clojure/core.clj"))]
    (println "Benchmarking instaparse")
    (criterium/bench (parse/clj-parser blob))

    (println "Benchmarking clj-antlr")
    (criterium/bench (parse/antlr-clojure blob))))
