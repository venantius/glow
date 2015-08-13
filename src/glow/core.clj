(ns glow.core
  (:require [glow.ansi :as ansi]
            [glow.parse :as parse]
            [glow.regex :as regex]
            [instaparse.core :as insta]))

(def ansi-fn-map
  {:red ansi/red
   :green ansi/green
   :yellow ansi/yellow
   :blue ansi/blue
   :magenta ansi/magenta
   :cyan ansi/cyan
   :bright-red ansi/bright-red
   :bright-green ansi/bright-green
   :default str})

(def colorscheme
  {:exception :green
   :repeat  :green
   :conditional :green
   :variable :blue
   :core-fn :blue
   :definition :bright-red
   :reader-char :red
   :special-form :red
   :macro :bright-red
   :number :cyan
   :boolean :cyan
   :nil :cyan
   :s-exp :red
   :keyword :green
   :comment :bright-green
   :string :cyan
   :regex :red})

(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (clojure.string/split s (re-pattern (java.util.regex.Pattern/quote split)) 2))

(defn colorize
  "Colorize a string with its highlight color."
  [s hl-type]
  (((get colorscheme hl-type :default) ansi-fn-map) s))

(defn- highlight-exception
  "Highlight Clojure exception keywords."
  [s]
  (if-let [match (regex/match-exception s)]
    (colorize s :exception)
    s))

(defn highlight-repeat
  "Highlight clojure.core repeat keywords."
  [s]
  (if-let [match (regex/match-repeat s)]
    (colorize s :repeat)
    (highlight-exception s)))

(defn- highlight-conditional
  "Highlight clojure.core conditionals."
  [s]
  (if-let [match (regex/match-conditional s)]
    (colorize s :conditional)
    (highlight-repeat s)))

(defn- highlight-variable
  "Highlight clojure.core variables."
  [s]
  (if-let [match (regex/match-variable s)]
    (colorize s :variable)
    (highlight-conditional s)))

(defn- highlight-core-fn
  "Highlight functions in clojure.core."
  [s]
  (if-let [match (regex/match-core-fn s)]
    (colorize s :core-fn)
    (highlight-variable s)))

(defn- highlight-definition
  "Highlight definitions."
  [s]
  (if-let [match (regex/match-definition s)]
    (colorize s :definition)
    (highlight-core-fn s)))

(defn- highlight-reader-char
  "Highlight reader characters."
  [s]
  (if-let [match (regex/match-reader-char s)]
    (colorize s :reader-char)
    (highlight-definition s)))

(defn- highlight-special-form
  "Highlight special forms."
  [s]
  (if-let [match (regex/match-special s)]
    (colorize s :special-form)
    (highlight-reader-char s)))

(defn- highlight-macro
  "Highlight macros."
  [s]
  (if-let [match (regex/match-macro s)]
    (colorize s :macro)
    (highlight-special-form s)))

(defn highlight-special-symbol
  "Check to see if this symbol is a known macro, special form, function, etc."
  [s]
  (highlight-macro s))

(defn colorize-collection
  [& args]
  (str (ansi/red (first args))
       (apply str (rest (butlast args)))
       (ansi/red (last args))))

(defn colorize-reader-macro
  [& args]
  (str (ansi/red (first args)) (apply str (rest args))))

(defn colorize-antlr
  [d]
  (insta/transform
   {:simple_sym str
    :simple_keyword str
    :ns_symbol str
    :var_quote colorize-reader-macro
    :unquote colorize-reader-macro
    :unquote_splicing colorize-reader-macro
    :quote colorize-reader-macro
    :backtick colorize-reader-macro
    :tag colorize-reader-macro
    :lambda colorize-reader-macro
    :deref colorize-reader-macro
    :param_name (comp ansi/red str)
    :nil (comp ansi/cyan str)
    :boolean (comp ansi/cyan str)
    :comment (comp ansi/bright-green str)
    :reader_macro str
    :string (comp ansi/cyan str)
    :regex (comp ansi/red str)
    :keyword (comp ansi/green str)
    :symbol highlight-special-symbol
    :literal str
    :form str
    :forms str
    :whitespace str
    :file str
    :map colorize-collection
    :list colorize-collection
    :vector colorize-collection
    :set colorize-collection
    ;; numbers
    :number (comp ansi/cyan str)
    :float str
    :hex str
    :bin str
    :bign str
    :long str}
   (vec d)))

(defn highlight
  "Given a string of valid Clojure source code, parse it and return a
  syntax-highlighted string of the same.

  By default, highlight uses `glow.core/colorscheme` to figure out which
  ANSI colors to use. If you want to use a different colorscheme, just
  pass in a map in a style akin to `glow.core/colorscheme` as an optional
  second argument, e.g.:

    {:string :blue
     :number :green}"
  ([s]
   (colorize-antlr (parse/parse s)))
  ([s opts]
   (with-redefs [colorscheme (if opts
                               opts
                               colorscheme)]
     (highlight s))))
