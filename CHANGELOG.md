0.1.2
 * Fixed a bug where our regular expressions were too aggressive.
 * Fixed a bug where Glow failed to transform macro keywords. 
 * Fixed a bug where Glow incorrectly colorized gensyms.

0.1.1
 * Moving from regular expression parsing to full lexical parsing using ANTLR as a lexer and grammar choice. This seems to be both faster and more resilient, though it will throw an exception if handed syntactically incorrect Clojure code.

0.1.0
 * Initial commit using regular expressions and recursive string splitting.
