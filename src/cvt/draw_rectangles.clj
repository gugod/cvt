(ns cvt.draw-rectangles
  (:require [clojure.string :as str])
  (:import
   (org.opencv.core Core Point Scalar)
   (org.opencv.highgui Highgui)))

(defn draw-bbox
  [image bbox-str]
  (let [bbox (map read-string (str/split bbox-str #","))]
    (Core/rectangle image
                    (Point. (nth bbox 0) (nth bbox 1))
                    (Point. (nth bbox 2) (nth bbox 3))
                    (Scalar. 255.0)
                    2)))

(defn -main
  [output-image-path input-image-path & rectangles]
  (let [image (Highgui/imread input-image-path)]
    (doall (map (fn [bbox] (draw-bbox image bbox)) rectangles))
    (Highgui/imwrite output-image-path image)
    (println "done")))
