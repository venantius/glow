(ns searchlight.regex-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [searchlight.regex :as regex]))

(deftest match-keyword-works
  (let [a (slurp (io/resource "test/regex/keywords/example_1.clj"))
        b (slurp (io/resource "test/regex/keywords/example_2.clj"))
        c (slurp (io/resource "test/regex/keywords/example_3.clj"))]
    (is (= ":require" (regex/match-keyword a)))
    (is (= "::this:is:an:example" (regex/match-keyword b)))
    (is (= ":afwe!234$*-_+=<>:a" (regex/match-keyword c)))))

(deftest match-comment-works
  (let [a (slurp (io/resource "test/regex/comments/example_1.clj"))
        b (slurp (io/resource "test/regex/comments/example_2.clj"))]
    (is (= ";; some profound stuff goes here\n" (regex/match-comment a)))
    (is (= "; this is another example here\n" (regex/match-comment b)))))

(deftest match-regex-works
  (let [a (slurp (io/resource "test/regex/regexes/example_1.txt"))
        b (slurp (io/resource "test/regex/regexes/example_2.txt"))]
    (is (= "#\"\\\"\"" (regex/match-regex a)))
    (is (= "#\"(regex)\"" (regex/match-regex b)))))

(deftest match-string-works
  (let [a (slurp (io/resource "test/regex/strings/example"))
        b (slurp (io/resource "test/regex/strings/example2"))
        c (slurp (io/resource "test/regex/strings/example3"))
        d (slurp (io/resource "test/regex/strings/example4"))]
    (is (= "\"test\"" (regex/match-string a)))
    (is (nil? (regex/match-string b)))
    (is (= "\"test \\\" asdf \"" (regex/match-string c)))
    (is (= "\"will be an\nexample\"" (regex/match-string d)))))
