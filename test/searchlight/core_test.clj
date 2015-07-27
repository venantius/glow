(ns searchlight.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [searchlight.core :as core]))

(deftest match-regex-works
  (let [a (slurp (io/resource "test/regex/regexes/example_1.txt"))
        b (slurp (io/resource "test/regex/regexes/example_2.txt"))]
    (is (= "#\"\\\"\"" (core/match-regex a)))
    (is (= "#\"(regex)\"" (core/match-regex b)))))

(deftest match-string-works
  (let [a (slurp (io/resource "test/regex/strings/example"))
        b (slurp (io/resource "test/regex/strings/example2"))
        c (slurp (io/resource "test/regex/strings/example3"))
        d (slurp (io/resource "test/regex/strings/example4"))]
    (is (= "\"test\"" (core/match-string a)))
    (is (nil? (core/match-string b)))
    (is (= "\"test \\\" asdf \"" (core/match-string c)))
    (is (= "\"will be an\nexample\"" (core/match-string d)))))
