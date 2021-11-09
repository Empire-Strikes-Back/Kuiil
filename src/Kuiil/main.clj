(ns Kuiil.main
  (:gen-class)
  (:require
   [clojure.core.async :as a :refer [<! >! <!! >!! chan put! take! go alt! alts! do-alts close! timeout pipe mult tap untap
                                     pub sub unsub mix admix unmix dropping-buffer sliding-buffer pipeline pipeline-async to-chan! thread]]
   [clojure.string]
   [clojure.java.io :as io]
   [clojure.repl :refer [doc dir source]])
  (:import
   (javax.swing JFrame WindowConstants ImageIcon JTextArea JScrollPane JPanel BoxLayout)
   (java.awt Canvas Graphics Graphics2D Shape Color Polygon)
   (java.awt.event KeyListener KeyEvent)
   (java.awt.geom Ellipse2D Ellipse2D$Double)))

#_(println (System/getProperty "clojure.core.async.pool-size"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defonce stateA (atom nil))
(def ^:dynamic ^JFrame jframe nil)
(def ^:dynamic ^Canvas canvas nil)
(def ^:dynamic ^JTextArea repl nil)
(def ^:dynamic ^JTextArea output nil)
(def ^:dynamic ^JScrollPane scroll-pane nil)
(def ^:dynamic ^Graphics2D graphics nil)
(defonce ns* (find-ns 'Kuiil.main))

(defn eval*
  [form]
  (let [string-writer (java.io.StringWriter.)
        result (binding [*ns* ns*
                         *out* string-writer]
                 (eval form))]
    (doto output
      (.append "=> ")
      (.append (str form))
      (.append "\n")
      (.append (str string-writer))
      (.append (pr-str result)))

    (go
      (<! (timeout 10))
      (let [scrollbar (.getVerticalScrollBar scroll-pane)]
        (.setValue scrollbar (.getMaximum scrollbar))))))

(defn draw-line
  []
  (.drawLine graphics 0 0 700 700))

(defn draw-word
  []
  (.drawString graphics "word" 500 500))

(defn clear-canvas
  []
  (.clearRect graphics 0 0  (.getWidth canvas)  (.getHeight canvas)))

(defn clear
  []
  (.setText output ""))

(defn window
  []
  (let [jframe (JFrame. "i am Kuiil, i have spoken")
        canvas (Canvas.)
        jpanel (JPanel.)
        repl (JTextArea. 1 100)
        output (JTextArea. 14 100)
        scroll-pane (JScrollPane.)
        layout (BoxLayout. jpanel BoxLayout/Y_AXIS)]

    (when-let [url (io/resource "icon.png")]
      (.setIconImage jframe (.getImage (ImageIcon. url))))

    (doto canvas
      (.setSize 1600 900))

    (doto output
      (.setEditable false))

    (doto repl
      (.addKeyListener (reify KeyListener
                         (keyPressed
                           [_ event]
                           (when (= (.getKeyCode ^KeyEvent event) KeyEvent/VK_ENTER)
                             (.consume ^KeyEvent event)))
                         (keyReleased
                           [_ event]
                           (when (= (.getKeyCode ^KeyEvent event) KeyEvent/VK_ENTER)
                             (-> (.getText repl) (clojure.string/trim) (clojure.string/trim-newline) (read-string) (eval*))
                             (.setText repl "")))
                         (keyTyped
                           [_ event]))))

    (doto scroll-pane
      (.setViewportView output))

    (doto jpanel
      (.setLayout layout)
      (.add canvas)
      (.add scroll-pane)
      (.add repl))

    (doto jframe
      (.setDefaultCloseOperation WindowConstants/DISPOSE_ON_CLOSE #_WindowConstants/EXIT_ON_CLOSE)
      (.setSize 1600 1200)
      (.setLocation 1700 300)
      (.add jpanel)
      (.setVisible true))

    (alter-var-root #'Kuiil.main/jframe (constantly jframe))
    (alter-var-root #'Kuiil.main/canvas (constantly canvas))
    (alter-var-root #'Kuiil.main/scroll-pane (constantly scroll-pane))
    (alter-var-root #'Kuiil.main/repl (constantly repl))
    (alter-var-root #'Kuiil.main/output (constantly output))
    (alter-var-root #'Kuiil.main/graphics (constantly (.getGraphics canvas)))

    (add-watch stateA :watch-fn
               (fn [ref wathc-key old-state new-state]

                 (clear-canvas)
                 (.setPaint graphics Color/BLACK)

                 (doseq [[k value] new-state]

                   )))

    (go
      (<! (timeout 100))
      (let []
        #_(swap! stateA merge heroes ship)))

    (eval* '(dir Kuiil.main))
    (eval* '(doc draw-word))

    nil))

(defn reload
  []
  (require
   '[Kuiil.main]
   :reload))

(defn -main
  [& args]
  (let []
    (reset! stateA {})

    (window)))
