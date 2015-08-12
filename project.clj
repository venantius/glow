(defproject venantius/glow "0.1.1-SNAPSHOT"
  :description "Syntax highlighting for Clojure source code."
  :url "https://github.com/venantius/glow"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-antlr "0.2.2"]
                 [instaparse "1.4.1"]]
  :test-selectors {:default #(not (:perf :slow %))
                   :perf :perf
                   :all  (fn [_] true)}
  :profiles {:dev {:dependencies
                   [[criterium "0.4.0"]
                    [cheshire "5.3.1"]
                    [org.clojure/test.check "0.5.7"]
                    [instaparse "1.2.6"]]}})
