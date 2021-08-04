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
   (java.awt Canvas Graphics BorderLayout)
   (java.awt.event WindowListener KeyListener KeyEvent WindowAdapter WindowEvent)))

(println "clojure.compiler.direct-linking" (System/getProperty "clojure.compiler.direct-linking"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true) (clojure.spec.alpha/check-asserts true))

(defonce stateA (atom nil))
(defonce ^JFrame jframe nil)
(defonce ^Canvas canvas nil)
(defonce ^Graphics graphics nil)
(defonce ^JTextArea repl nil)

(defn window
  []
  (let [jframe (JFrame. "get-to-the-ship")
        canvas (Canvas.)
        repl (JTextArea. 10 100)
        namespace (find-ns 'get-to-the-ship.main)
        key-listener (reify KeyListener
                       (keyTyped [_ event])
                       (keyPressed [_ event])
                       (keyReleased [_ event]
                         #_(println (.getKeyCode ^KeyEvent event))
                         (when (and (== (.getKeyCode ^KeyEvent event) 10)
                                    (not (empty? (.getText repl))))
                           #_(println (.getText repl) (read-string (.getText repl)))
                           (binding [*ns* namespace]
                             (eval (read-string (.getText repl))))
                           (.setText repl ""))))
        window-listener (reify WindowListener
                          (windowActivated [_ event])
                          (windowClosed [_ event]
                            #_(println :removing-key-listener)
                            (.removeKeyListener repl key-listener))
                          (windowClosing [_ event])
                          (windowDeactivated [_ event])
                          (windowDeiconified [_ event])
                          (windowIconified [_ event])
                          (windowOpened [_ event]))]
    (doto jframe
      (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE #_JFrame/EXIT_ON_CLOSE)
      (.setSize 1600 1200)
      #_(.setLocationByPlatform true)
      (.setLocation 2000 200)
      (.setVisible true)
      (.add (doto canvas
              (.setSize 1600 1000)))
      (.add (doto repl
              (.setLocation 0 1000)) BorderLayout/PAGE_END))
    (.addKeyListener repl key-listener)
    (.addWindowListener jframe window-listener)

    (alter-var-root #'get-to-the-ship.main/jframe (constantly jframe))
    (alter-var-root #'get-to-the-ship.main/canvas (constantly canvas))
    (alter-var-root #'get-to-the-ship.main/graphics (constantly (.getGraphics canvas)))
    (alter-var-root #'get-to-the-ship.main/repl (constantly repl))
    nil))

(defn reload
  []
  (require
   '[get-to-the-ship.main]
   :reload))

(defn -main [& args]
  (println ::-main)
  (let []
    #_(reset! stateA {})
    #_(add-watch stateA :watch-fn (fn [k stateA old-state new-state] new-state))
    
    (window)
    
    (<!! (chan))))

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

  (require
   '[get-to-the-ship.main]
   :reload)

  (swap! stateA assoc ::random (rand-int 1000))

  (.drawLine graphics 0 0 1000 1000)

  (let [byte-arr (.getBytes "word" "UTF-8")]
    (.drawBytes graphics byte-arr 0 (alength byte-arr) 500 500))

  (.clearRect graphics 0 0 1000 1000)


  ;
  )