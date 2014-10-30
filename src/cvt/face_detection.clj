(ns cvt.face-detection
  (:import
   org.opencv.core.Core
   org.opencv.core.Mat
   org.opencv.core.MatOfRect
   org.opencv.core.Point
   org.opencv.core.Rect
   org.opencv.core.Scalar
   org.opencv.highgui.Highgui
   org.opencv.objdetect.CascadeClassifier))

(defn detection-to-bbox
  [detections]
  (doall (map (fn [rect]
                (clojure.string/join
                 ","
                 [(.x rect) (.y rect)
                  (+ (.x rect) (.width rect))
                  (+ (.y rect) (.height rect))]))
              (.toArray detections))))

(defn detection-to-string
  [detections]
  (clojure.string/join " "
                       (detection-to-bbox detections)))

(defn face-detect
  [image-path]
  (let [image   (Highgui/imread image-path)
        face-classifier (CascadeClassifier. "/usr/share/opencv/lbpcascades/lbpcascade_frontalface.xml")
        face-detected   (atom [])]
    (reset! face-detected (MatOfRect.))
    (.detectMultiScale face-classifier image @face-detected)
    @face-detected))
