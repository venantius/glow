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
   :character :cyan
   :regex :red})

(defn split-in-two
  "Given two strings, split the former on the first occurrence of the latter."
  [s split]
  (clojure.string/split s (re-pattern (java.util.regex.Pattern/quote split)) 2))

(defn colorize
  "Colorize a string with its highlight color."
  [s hl-type]
  (((get colorscheme hl-type :default) ansi-fn-map) s))

(defn colorize-special-symbol
  "Check to see if this symbol is a known macro, special form, function, etc."
  [s]
  (if-let [symbol-type
           (cond
             (regex/match-macro s) :macro
             (regex/match-special s) :special-form
             (regex/match-reader-char s) :reader-char
             (regex/match-definition s) :definition
             (regex/match-core-fn s) :core-fn
             (regex/match-variable s) :variable
             (regex/match-conditional s) :conditional
             (regex/match-repeat s) :repeat
             (regex/match-exception s) :exception)]
    (colorize s symbol-type)
    s))

(defn colorize-collection
  [& args]
  (str (colorize (first args) :s-exp)
       (apply str (rest (butlast args)))
       (colorize (last args) :s-exp)))

(defn colorize-reader-macro
  [& args]
  (str (colorize (first args) :reader-char)
       (apply str (rest args))))

(defn colorize-antlr
  [d]
  (insta/transform
   {:simple_sym str
    :simple_keyword str
    :ns_symbol str

    ;; literals
    :literal str
    :string (comp #(colorize % :string) str)
    :regex (comp #(colorize % :regex) str)
    :number (comp #(colorize % :number) str)
    :character (comp #(colorize % :character) str)
    :nil (comp #(colorize % :nil) str)
    :boolean (comp #(colorize % :boolean) str)
    :keyword (comp #(colorize % :keyword) str)
    :symbol colorize-special-symbol
    :param_name (comp #(colorize % :reader-char) str)

    ;; reader macro characters
    :reader_macro str
    :quote colorize-reader-macro
    :backtick colorize-reader-macro
    :unquote colorize-reader-macro
    :unquote_splicing colorize-reader-macro
    :tag colorize-reader-macro
    :deref colorize-reader-macro
    :gensym colorize-reader-macro
    :lambda colorize-reader-macro
    :meta_data colorize-reader-macro
    :var_quote colorize-reader-macro
    :host_expr colorize-reader-macro
    :discard colorize-reader-macro
    :dispatch colorize-reader-macro

    ;; top level
    :file str
    :forms str
    :form str

    ;; collections
    :map colorize-collection
    :list colorize-collection
    :vector colorize-collection
    :set colorize-collection

    ;; extras
    :comment (comp ansi/bright-green str)
    :whitespace str}
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
