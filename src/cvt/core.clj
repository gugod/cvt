(ns cvt.core
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]
   ))

(defn image-file-seq
  "file-seq + filter that gives only a sequence of image files."
  [path]
  (filter (fn [it] (and (.isFile it) (re-find #".((?i)jpg|png)$" (.getPath it)))) (file-seq (io/file path))))
