(ns searchlight.core-test
  (:require [clojure.test :refer :all]
            [searchlight.core :as core]))

(deftest match-string-works
  (let [a (slurp "test/example")
        b (slurp "test/example2")
        c (slurp "test/example3")]
    (is (= "\"test\"" (core/match-string a)))
    (is (nil? (core/match-string b)))
    (is (= "\"test \\\" asdf \"" (core/match-string c)))))
