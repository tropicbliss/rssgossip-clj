(defproject rssgossip-clj "0.1.0-SNAPSHOT"
  :description "Program to read the contents of an RSS feed and look for a key phrase"
  :url "http://example.com/FIXME"
  :license {:name "MIT" :url "https://mit-license.org/"}
  :dependencies [[org.clojure/clojure "1.11.1"] [org.clojure/tools.cli "1.1.230"] [clj-http "3.13.1"] [org.clojure/data.xml "0.0.8"]]
  :plugins [[dev.weavejester/lein-cljfmt "0.13.1"]]
  :main ^:skip-aot rssgossip-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
