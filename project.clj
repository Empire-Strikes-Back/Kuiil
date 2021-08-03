(def MAIN 'get-to-the-ship.main)
#_(prn (clojure-version))
#_(prn *command-line-args*)

(defproject program "0.1.0"

  :repositories [["central" {:url "https://repo1.maven.org/maven2/"}]
                 ["clojars" {:url "https://clojars.org/repo/"}]
                 ["conjars" {:url "https://conjars.org/repo"}]]

  :min-lein-version "2.9.3"

  :plugins [[org.clojure/tools.deps.alpha "0.8.677"]
            [lein-tools-deps "0.4.5" :exclusions [org.clojure/tools.deps.alpha]]
            [io.taylorwood/lein-native-image "0.3.1"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]

  :lein-tools-deps/config {:config-files [:install :user :project]
                           :aliases []}

  :global-vars {*warn-on-reflection* false
                *unchecked-math* false
                #_*assert* #_false}

  :repl-options {:init-ns          ~MAIN
                 :main             ~MAIN
                 :host             "0.0.0.0"
                 :port             7788}
  :profiles {:repl {:plugins [[nrepl/nrepl "0.8.3"]
                              [cider/cider-nrepl "0.25.5"]]}

             :uberjar {:main ~MAIN
                       :uberjar-name "get-to-the-ship.standalone.jar"
                       :jar-name     "get-to-the-ship.jar"
                       :uberjar-exclusions []
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                                  "-Dclojure.core.async.pool-size=1"
                                  #_"-Dcljfx.skip-javafx-initialization=true"]
                       :aot :all}}
  :target-path "out"

  :main ^{:skip-aot false} ~MAIN
  :jvm-opts ["-Xms768m" "-Xmx11998m" "-Dclojure.compiler.direct-linking=true" "-Dclojure.core.async.pool-size=1"]

  :source-paths []
  :java-source-paths []
  :resource-paths []
  :auto-clean false)
