(ns glow.regex-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [glow.regex :as regex]))

(deftest match-regex-works
  (let [a (slurp (io/resource "test/regex/regexes/example_1.txt"))
        b (slurp (io/resource "test/regex/regexes/example_2.txt"))
        c (slurp (io/resource "test/regex/regexes/example_3.clj"))]
    (is (= "#\"\\\"\"" (regex/match-regex a)))
    (is (= "#\"(regex)\"" (regex/match-regex b)))
    (is (= "#\"\"" (regex/match-regex c)))))

(deftest match-string-works
  (let [a (slurp (io/resource "test/regex/strings/example"))
        b (slurp (io/resource "test/regex/strings/example2"))
        c (slurp (io/resource "test/regex/strings/example_3.clj"))
        d (slurp (io/resource "test/regex/strings/example4"))
        e (slurp (io/resource "test/regex/strings/example_5.clj"))]
    (is (= "\"test\"" (regex/match-string a)))
    (is (nil? (regex/match-string b)))
    (is (= "\"test \\\" asdf \"" (regex/match-string c)))
    (is (= "\"will be an\nexample\"" (regex/match-string d)))
    (is (= "\"Given two strings, split the former on the first occurrence of the latter.\"" (regex/match-string e)))))

(deftest match-comment-works
  (let [a (slurp (io/resource "test/regex/comments/example_1.clj"))
        b (slurp (io/resource "test/regex/comments/example_2.clj"))]
    (is (= ";; some profound stuff goes here\n" (regex/match-comment a)))
    (is (= "; this is another example here\n" (regex/match-comment b)))))

(deftest match-keyword-works
  (let [a "(ns basic\n  (:require clojure.java.io))"
        b "(def b ::this:is:an:example)"
        c "(ns example-the-third)\n\n(def with-symbols :afwe!234$*-_+=<>:a)"
        d "this is a :b single letter keyword"]
    (is (= ":require" (regex/match-keyword a)))
    (is (= "::this:is:an:example" (regex/match-keyword b)))
    (is (= ":afwe!234$*-_+=<>:a" (regex/match-keyword c)))
    (is (= ":b" (regex/match-keyword d)))))

(deftest match-s-exp-works
  (let [a "(def x true)"
        b "[5]"
        c "{:a :b}"
        d "a)b"
        e "asd ] "
        f " :b} * 2"]
    (is (= "(" (regex/match-s-exp a)))
    (is (= "[" (regex/match-s-exp b)))
    (is (= "{" (regex/match-s-exp c)))
    (is (= ")" (regex/match-s-exp d)))
    (is (= "]" (regex/match-s-exp e)))
    (is (= "}" (regex/match-s-exp f)))))

(deftest match-nil-works
  (is (= "nil" (regex/match-nil "nil")))
  (is (= "nil" (regex/match-nil "(if nil false)"))))

(deftest match-bool-works
  (is (= "true" (regex/match-bool "true")))
  (is (= "true" (regex/match-bool "(if true true)")))
  (is (= "false" (regex/match-bool "false")))
  (is (= "false" (regex/match-bool "(if false false)"))))

(deftest match-number-works
  (is (= "1" (regex/match-number "asdf 1")))
  (is (= "1" (regex/match-number "a 1 b")))
  (is (= "2" (regex/match-number "a 2 3 b")))
  (is (= "3.2" (regex/match-number "a 3.2 x")))
  (is (= "0.4" (regex/match-number "a 0.4 y")))
  (is (= "0.8" (regex/match-number "0.8 08"))))

(deftest match-special-works
  (is (= 0 1)))

(deftest match-reader-char-works
  (is (= "~@" (regex/match-reader-char " ~@ a")))
  (is (= "`" (regex/match-reader-char "`(def x 5)")))
  (is (= "@" (regex/match-reader-char " @(future 5)")))
  (is (= "~" (regex/match-reader-char " ~(conj [] a)")))
  (is (= "^" (regex/match-reader-char " (defn foo [^Throwable tr] tr)")))
  (is (= "@" (regex/match-reader-char " @ a")))
  (is (= "'" (regex/match-reader-char " 'asfd ")))
  (is (= "&" (regex/match-reader-char " (defn foo [x & y] x)"))))

(deftest match-definition-works
  (is (= 0 1)))

(deftest match-macro-works
  (is (= 0 1)))

(deftest match-func-works
  (is (= 0 1)))

(deftest match-variable-works
  (is (= 0 1)))

(deftest match-conditional-works
  (is (= 0 1)))

(deftest match-repeat-works
  (is (= 0 1)))

(deftest match-exception-works
  (is (= "catch" (regex/match-exception "(catch Exception e (println e))")))
  (is (= "finally" (regex/match-exception "(finally ...)")))
  (is (= "throw" (regex/match-exception "(throw ...)")))
  (is (= "try" (regex/match-exception "(try ..."))))
