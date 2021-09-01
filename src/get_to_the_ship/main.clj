(ns get-to-the-ship.main
  (:gen-class)
  (:require 
    [clojure.core.async :as a :refer [<! >! <!! >!! chan put! take! go alt! alts! do-alts close! timeout pipe mult tap untap 
                                      pub sub unsub mix admix unmix dropping-buffer sliding-buffer pipeline pipeline-async to-chan! thread]]
    [clojure.string]
    [clojure.java.io :as io])
  (:import
    (javax.swing JFrame WindowConstants ImageIcon JTextArea JScrollPane JPanel BoxLayout)
    (java.awt Canvas Graphics)
  )    
)

#_(println (System/getProperty "clojure.core.async.pool-size"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defonce stateA (atom nil))
(def ^:dynamic ^JFrame jframe nil)
(def ^:dynamic ^Canvas canvas nil)
(def ^:dynamic ^JTextArea repl nil)
(def ^:dynamic ^JTextArea output nil)
(def ^:dynamic ^JScrollPane scroll-pane nil)
(def ^:dynamic ^Graphics graphics nil)

(defn window
  []
  (let [jframe (JFrame. "i am get-to-the-ship program")
        canvas (Canvas.)
        graphics (.getGraphics canvas)
        jpanel (JPanel.)
        repl (JTextArea. 1 100)
        output (JTextArea. 14 100)
        scroll-pane (JScrollPane.)
        layout (BoxLayout. jpanel BoxLayout/Y_AXIS)]

  (when-let [url (io/resource "icon.png")]
    (.setIconImage jframe (.getImage (ImageIcon. url)))
  )

  (doto canvas
    (.setSize 1600 900)
  )
  
  (doto output
    (.setEditable false)
  )

  (doto scroll-pane
    (.setViewportView output)
  )

  (doto jpanel
    (.setLayout layout)
    (.add canvas)
    (.add scroll-pane)
    (.add repl)
    )

  (doto jframe
    (.setDefaultCloseOperation WindowConstants/DISPOSE_ON_CLOSE #_WindowConstants/EXIT_ON_CLOSE)
    (.setSize 1600 1200)
    (.setLocation 1700 300)
    (.add jpanel)
    (.setVisible true)
  )

  (alter-var-root #'get-to-the-ship.main/jframe (constantly jframe))
  (alter-var-root #'get-to-the-ship.main/canvas (constantly canvas))
  (alter-var-root #'get-to-the-ship.main/scroll-pane (constantly scroll-pane))
  (alter-var-root #'get-to-the-ship.main/repl (constantly repl))
  (alter-var-root #'get-to-the-ship.main/output (constantly output))
  (alter-var-root #'get-to-the-ship.main/graphics (constantly graphics))


  nil
  )
)

(defn reload
  []
  (require 
    '[get-to-the-ship.main]
    :reload)
)

(defn -main
  [& args]
  (let []
    (reset! stateA {})

    (window)

  )
)
