0.1.6
 * Bugfix for single-line comment strings ending without a newline delimiter

0.1.4
 * Upgrade clj-antlr to "0.2.3"

0.1.3
 * Add support for the generation of server-side HTML to syntax highlight a block of input source code, as well as the associated CSS.

0.1.2
 * Fixed a bug where our regular expressions were too aggressive.
 * Fixed a bug where Glow failed to transform macro keywords.
 * Fixed a bug where Glow incorrectly colorized gensyms.

0.1.1
 * Moving from regular expression parsing to full lexical parsing using ANTLR as a lexer and grammar choice. This seems to be both faster and more resilient, though it will throw an exception if handed syntactically incorrect Clojure code.

0.1.0
 * Initial commit using regular expressions and recursive string splitting.
