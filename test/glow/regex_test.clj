(ns glow.regex-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [glow.regex :as regex]))

(deftest match-special-works
  (is (= "monitor-exit" (regex/match-special "(monitor-exit ...)")))
  (is (= "set!" (regex/match-special "set! foo bar"))))

(deftest match-definition-works
  (is (= "clojure.core/defn-" (regex/match-definition "(clojure.core/defn- bar [] true")))
  (is (= "defonce" (regex/match-definition "(defonce bar [])"))))

(deftest match-macro-works
  (is (= "->" (regex/match-macro "(-> 5 (* 2))")))
  (is (= "io!" (regex/match-macro "(io! ...)")))
  (is (= ".." (regex/match-macro "(.. System (getProperties) (get \"os.name\"))"))))

(deftest match-core-fn-works
  (is (= "*" (regex/match-core-fn "(* 1 1)")))
  (is (= "+" (regex/match-core-fn "(+ 1 1)")))
  (is (= "->ArrayChunk" (regex/match-core-fn "(->ArrayChunk [])")))
  (is (= "neg?" (regex/match-core-fn "(neg? -5")))
  (is (= "empty?" (regex/match-core-fn "(empty? {})")))
  (is (= "empty" (regex/match-core-fn "(empty [])"))))

(deftest match-variable-works
  (is (= "*1" (regex/match-variable "(def bar *1)")))
  (is (= "clojure.core/*print-length*" (regex/match-variable "clojure.core/*print-length*"))))

(deftest match-conditional-works
  (is (= "case" (regex/match-conditional "(case ...")))
  (is (= "clojure.core/cond->" (regex/match-conditional "(clojure.core/cond->")))
  (is (= "cond->>" (regex/match-conditional "cond->>")))
  (is (= "clojure.core/when-some" (regex/match-conditional "clojure.core/when-some"))))

(deftest match-repeat-works
  (is (= "doseq" (regex/match-repeat "(doseq ..)")))
  (is (= "clojure.core/dotimes" (regex/match-repeat "(clojure.core/dotimes ...)")))
  (is (= "while" (regex/match-repeat "while ..."))))

(deftest match-exception-works
  (is (= "catch" (regex/match-exception "(catch Exception e (println e))")))
  (is (= "finally" (regex/match-exception "(finally ...)")))
  (is (= "throw" (regex/match-exception "(throw ...)")))
  (is (= "try" (regex/match-exception "(try ..."))))
