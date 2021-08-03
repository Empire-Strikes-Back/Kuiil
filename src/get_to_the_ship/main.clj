(ns get-to-the-ship.main
  (:gen-class)
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >! <!! >!!  take! put! offer! poll! alt! alts! close! onto-chan!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.string]
   [clojure.spec.alpha :as s]
   [clojure.java.io :as io])
  (:import
   (javax.swing JFrame JLabel JButton SwingConstants JMenuBar JMenu JTextArea)
   (java.awt Canvas Graphics)
   (java.awt.event WindowListener KeyListener KeyEvent)))

(println "clojure.compiler.direct-linking" (System/getProperty "clojure.compiler.direct-linking"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true) (clojure.spec.alpha/check-asserts true))

(defonce stateA (atom nil))
(defonce ^JFrame jframe nil)
(defonce ^Canvas canvas nil)
(defonce ^Graphics graphics nil)
(defonce ^JTextArea repl nil)

(defn -main [& args]
  (println ::-main)
  (let []
    (reset! stateA {})
    (add-watch stateA :watch-fn (fn [k stateA old-state new-state] new-state))

    (let [jframe (JFrame. "get-to-the-ship")
          canvas (Canvas.)
          repl (JTextArea. 10 100)
          namespace (find-ns 'get-to-the-ship.main)]
      (doto jframe
        (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
        (.setSize 1600 1200)
        (.setLocationByPlatform true)
        (.setVisible true)
        (.add (doto canvas
                (.setSize 1600 1000)))
        (.add (doto repl
                (.setLocation 0 1000)
                (.setSize 1600 200))))
      (.addKeyListener repl
                       (reify KeyListener
                         (keyTyped [_ event])
                         (keyPressed [_ event])
                         (keyReleased [_ event]
                           #_(println (.getKeyCode ^KeyEvent event))
                           (when (== (.getKeyCode ^KeyEvent event) 10)
                             (println (.getText repl) (read-string (.getText repl)))
                             (binding [*ns* namespace]
                               (eval (read-string (.getText repl))))
                             (.setText repl "")))))
      (alter-var-root #'get-to-the-ship.main/jframe (constantly jframe))
      (alter-var-root #'get-to-the-ship.main/canvas (constantly canvas))
      (alter-var-root #'get-to-the-ship.main/graphics (constantly (.getGraphics canvas)))
      (alter-var-root #'get-to-the-ship.main/repl (constantly repl)))
    (go)))

(defn draw-line
  []
  (.drawLine graphics 0 0 1000 1000))

(defn draw-word
  []
  (let [byte-arr (.getBytes "word" "UTF-8")]
    (.drawBytes graphics byte-arr 0 (alength byte-arr) 500 500)))

(defn clear
  []
  (.clearRect graphics 0 0 1000 1000))

(comment

  (-main)

  (do
    (require
     '[get-to-the-ship.main]
     :reload)
    (swap! stateA assoc ::random (rand-int 1000)))

  (.drawLine graphics 0 0 1000 1000)

  (let [byte-arr (.getBytes "word" "UTF-8")]
    (.drawBytes graphics byte-arr 0 (alength byte-arr) 500 500))
  
  (.clearRect graphics 0 0 1000 1000)


  ;
  )