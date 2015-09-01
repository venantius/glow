(ns glow.terminal-test
  (:require [clojure.test :refer :all]
            [glow.terminal :as terminal]
            [glow.core :as core]))

(deftest colorize-special-sybol-works
  (testing "That this matches exactly 're' and not 'core.something' etc."
    (is (= "re" (terminal/colorize-special-symbol core/colorscheme "re")))))
