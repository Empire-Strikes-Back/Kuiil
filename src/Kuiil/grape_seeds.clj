(ns get-to-the-ship.main
  (:gen-class)
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >! <!! >!!  take! put! offer! poll! alt! alts! close! onto-chan!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.string]
   [clojure.java.io :as io])
  (:import
   (javax.swing JFrame JLabel JButton SwingConstants JMenuBar JMenu JTextArea JScrollPane JPanel BoxLayout)
   (java.awt Canvas Graphics BorderLayout)
   (java.awt.event WindowListener KeyListener KeyEvent WindowAdapter WindowEvent)))

#_(println "clojure.compiler.direct-linking" (System/getProperty "clojure.compiler.direct-linking"))
#_(println "clojure.core.async.pool-size" (System/getProperty "clojure.core.async.pool-size"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defonce stateA (atom nil))
(defonce ^JFrame jframe nil)
(defonce ^Canvas canvas nil)
(defonce ^Graphics graphics nil)
(defonce ^JTextArea repl nil)
(defonce ^JTextArea output nil)

(defn window
  []
  (let [jframe (JFrame. "i am get-to-the-ship program")
        canvas (Canvas.)
        output (JTextArea. 10 100)
        repl (JTextArea. 1 100)
        jpanel (JPanel.)
        namespace (find-ns 'get-to-the-ship.main)
        key-listener (reify KeyListener
                       (keyTyped [_ event])
                       (keyPressed [_ event]
                         (when (== (.getKeyCode ^KeyEvent event) KeyEvent/VK_ENTER)
                           (.consume ^KeyEvent event)))
                       (keyReleased [_ event]
                         #_(println (.getKeyCode ^KeyEvent event))
                         (when (and (== (.getKeyCode ^KeyEvent event) KeyEvent/VK_ENTER)
                                    (not (empty? (.getText repl))))
                           (println :eval (.getText repl) (read-string (.getText repl)))
                           (binding [*ns* namespace]
                             (.append output (str (eval (read-string (.getText repl)))))
                             (.append output "\n"))
                           (.setText repl "")
                           (.consume ^KeyEvent event))))
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
      (.add (doto jpanel
              (.setLayout (BoxLayout. jpanel BoxLayout/Y_AXIS))
              (.add (doto canvas
                      (.setSize 1600 1000)))
              #_(.setSize 1600 200)
              (.add (JScrollPane. (doto output
                                    (.setEditable false))))
              (.add (doto repl))) BorderLayout/PAGE_END)
      (.setVisible true))

    (.addKeyListener repl key-listener)
    (.addWindowListener jframe window-listener)

    (do
      (alter-var-root #'get-to-the-ship.main/jframe (constantly jframe))
      (alter-var-root #'get-to-the-ship.main/canvas (constantly canvas))
      (alter-var-root #'get-to-the-ship.main/graphics (constantly (.getGraphics canvas)))
      (alter-var-root #'get-to-the-ship.main/repl (constantly repl))
      (alter-var-root #'get-to-the-ship.main/output (constantly output)))

    (go
      (<! (timeout 100))
      (doseq [[name position] (zipmap
                               (shuffle ["ship" "el" "cee" "bib" "ar"])
                               (shuffle [[(+ 50 (rand-int 250)) (+ 50 (rand-int 350))]
                                         [(+ 100 (rand-int 300)) (+ 800 (rand-int 100))]
                                         [(+ 1200 (rand-int 200)) (+ 700 (rand-int 250))]
                                         [(+ 300 (rand-int 200)) (+ 400 (rand-int 300))]
                                         [(+ 1300 (rand-int 200)) (+ 100 (rand-int 200))]]))]
        (.drawString graphics ^String name ^int (first position) ^int (second position))))

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

    (window)))

(defn draw-line
  []
  (.drawLine graphics 0 0 1000 1000))

(defn draw-word
  []
  (let [byte-arr (.getBytes "word" "UTF-8")]
    (.drawBytes graphics byte-arr 0 (alength byte-arr) 500 500)))

(defn clear-canvas
  []
  (.clearRect graphics 0 0 1000 1000))

(defn clear
  []
  (.setText output ""))

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