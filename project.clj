(defproject cvt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "CC0 1.0 Universal"
            :url "http://creativecommons.org/publicdomain/zero/1.0/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [opencv/opencv "2.4.10"]
                 [opencv/opencv-native "2.4.10"]]
  :injections [(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)]
  :plugins [[lein-localrepo "0.5.2"]]
  :main ^:skip-aot cvt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
