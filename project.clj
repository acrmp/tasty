(defproject tasty "0.1.0-SNAPSHOT"
  :description "Tweets when cookbook changes are pushed to GitHub"
  :url "http://github.com/acrmp/tasty"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [twitter-api "0.6.4"]
                 [tentacles "0.1.3"]]
  :main tasty.core)
