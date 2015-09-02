(ns glow.colorschemes
  "A namespace for holding various terminal and web colorschemes.")

;; Terminal

(def terminal-default
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

;; web

(def solarized-dark
  {:background "#002b36"
   :exception "#859900"
   :repeat "#859900"
   :conditional "#859900"
   :variable "#268bd2"
   :core-fn "#268bd2"
   :definition "#cb4b16"
   :reader-char "#dc322f"
   :special-form "#dc322f"
   :macro "#cb4b16"
   :number "#2aa198"
   :boolean "#2aa198"
   :nil "#2aa198"
   :s-exp "#dc322f"
   :keyword "#859900"
   :comment "#586e75"
   :string "#2aa198"
   :character "#2aa198"
   :regex "#dc322f"
   :symbol "#93a1a1"})
