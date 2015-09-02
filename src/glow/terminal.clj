(ns glow.terminal
  (:require [glow.ansi :as ansi]
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

(defn colorize
  [colorscheme s hl-type]
  (((get colorscheme hl-type :default) ansi-fn-map) s))

(defn colorize-special-symbol
  "Check to see if this symbol is a known macro, special form, function, etc."
  [colorscheme s]
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
    (colorize colorscheme s symbol-type)
    s))

(defn colorize-collection
  [colorscheme & args]
  (str (colorize colorscheme (first args) :s-exp)
       (apply str (rest (butlast args)))
       (colorize colorscheme (last args) :s-exp)))

(defn colorize-reader-macro
  [colorscheme & args]
  (str (colorize colorscheme (first args) :reader-char)
       (apply str (rest args))))

(defn reverse-colorize-reader-macro
  [colorscheme & args]
  (str (first args)
       (colorize colorscheme (second args) :reader-char)))

(defn ansi-colorize
  [colorscheme d]
  (let [colorize-macro (partial
                        colorize-reader-macro
                        colorscheme)
        reverse-colorize-macro (partial
                                reverse-colorize-reader-macro
                                colorscheme)
        colorize-coll (partial
                       colorize-collection
                       colorscheme)
        colorize-special-sym (partial
                              colorize-special-symbol
                              colorscheme)]
    (insta/transform
     {:simple_sym str
      :simple_keyword str
      :macro_keyword str
      :ns_symbol str

      ;; characters
      :named_char str
      :any_char str
      :u_hex_quad str

      ;; literals
      :literal str
      :string (comp #(colorize colorscheme % :string) str)
      :regex (comp #(colorize colorscheme % :regex) str)
      :number (comp #(colorize colorscheme % :number) str)
      :character (comp #(colorize colorscheme % :character) str)
      :nil (comp #(colorize colorscheme % :nil) str)
      :boolean (comp #(colorize colorscheme % :boolean) str)
      :keyword (comp #(colorize colorscheme % :keyword) str)
      :symbol colorize-special-sym
      :param_name (comp #(colorize colorscheme % :reader-char) str)

      ;; reader macro characters
      :reader_macro str
      :quote colorize-macro
      :backtick colorize-macro
      :unquote colorize-macro
      :unquote_splicing colorize-macro
      :tag colorize-macro
      :deref colorize-macro
      :gensym reverse-colorize-macro
      :lambda colorize-macro
      :meta_data colorize-macro
      :var_quote colorize-macro
      :host_expr colorize-macro
      :discard colorize-macro
      :dispatch colorize-macro

      ;; top level
      :file str
      :forms str
      :form str

      ;; collections
      :map colorize-coll
      :list colorize-coll
      :vector colorize-coll
      :set colorize-coll

      ;; extras
      :comment (comp ansi/bright-green str)
      :whitespace str}
     (vec d))))
