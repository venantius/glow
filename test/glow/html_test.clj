(ns glow.html-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [glow.core :as core]
            [glow.html :as html]))

(defn generate-demo-page
  "Handy for proving that a large canon of Clojure text can be generated."
  []
  (spit "demo.css"
        (core/generate-css))
  (spit "demo.html"
        (str "<html>
         <link rel=\"stylesheet\"
         href=\"demo.css\">"
             (core/highlight-html (slurp
                                   (io/resource "test/core/sample.clj")))
             "</html>")))

(deftest generate-html-works
  (is (= (core/highlight-html (slurp (io/resource "test/core/sample.clj")))
         "<div class=\"syntax\"><pre><span class=\"s-exp\">(</span><span class=\"macro\">ns</span> <span class=\"symbol\">sample</span><span class=\"s-exp\">)</span>\n\n<span class=\"s-exp\">(</span><span class=\"definition\">defn</span> <span class=\"symbol\">func</span>\n  <span class=\"s-exp\">[</span><span class=\"reader-char\">^</span><span class=\"symbol\">Throwable</span> <span class=\"symbol\">x</span> <span class=\"reader-char\">&</span> <span class=\"symbol\">y</span><span class=\"s-exp\">]</span>\n  <span class=\"s-exp\">(</span><span class=\"core-fn\">conj</span> <span class=\"s-exp\">{</span><span class=\"s-exp\">}</span> <span class=\"s-exp\">[</span><span class=\"keyword\">:a</span> <span class=\"s-exp\">(</span><span class=\"core-fn\">+</span> <span class=\"number\">1.1</span> <span class=\"symbol\">x</span><span class=\"s-exp\">)</span><span class=\"s-exp\">]</span><span class=\"s-exp\">)</span><span class=\"s-exp\">)</span>\n\n<span class=\"s-exp\">(</span><span class=\"special-form\">def</span> <span class=\"symbol\">variable</span>\n  <span class=\"reader-char\">@</span><span class=\"s-exp\">(</span><span class=\"macro\">future</span>\n     <span class=\"s-exp\">(</span><span class=\"conditional\">if-let</span> <span class=\"s-exp\">[</span><span class=\"symbol\">x</span> <span class=\"number\">5</span><span class=\"s-exp\">]</span>\n       <span class=\"boolean\">true</span>\n       <span class=\"string\">\"false\"</span><span class=\"s-exp\">)</span><span class=\"s-exp\">)</span><span class=\"s-exp\">)</span>\n</pre></div>")))
