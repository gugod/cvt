(ns cvt.take-picture
  (:import
   org.opencv.core.Mat
   org.opencv.highgui.Highgui
   org.opencv.highgui.VideoCapture))

(defn -main [output-image-path]
  (let [camera (VideoCapture.)
        frame  (Mat.)]
    (.open camera 0)
    (Thread/sleep 1000)
    (if (.isOpened camera)
      (if (.read camera frame)
        (Highgui/imwrite output-image-path frame)
        (println "Failed to read a camera frame"))
      (println "Failed to open camera"))))
