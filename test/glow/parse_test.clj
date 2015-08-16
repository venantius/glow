(ns glow.parse-test
  (:require [clojure.test :refer :all]
            [glow.parse :as parse]))

;; what else is there?
(deftest keyword-edge-cases-parse
  (is (= (parse/parse ":nil") '(:file (:form (:literal (:keyword (:simple_keyword ":" "nil")))))))
  (is (= (parse/parse ":1") '(:file (:form (:literal (:keyword (:simple_keyword ":" "1")))))))
  (is (= (parse/parse ":a%/a") '(:file (:form (:literal (:keyword (:simple_keyword ":" "a%/a")))))))
  (is (= (parse/parse ":##a") '(:file (:form (:literal (:keyword (:simple_keyword ":" "##a")))))))
  (is (= (parse/parse ":true") '(:file (:form (:literal (:keyword (:simple_keyword ":" "true"))))))))

(deftest gensym-parse
  (is (= (parse/parse "gensym#") nil)))
