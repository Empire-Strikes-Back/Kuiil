(ns get-to-the-ship.main
  (:require 
    [clojure.core.async :as a :refer [<! >! <!! >!! chan put! take! go alt! alts! do-alts close! timeout pipe mult tap untap 
                                      pub sub unsub mix admix unmix dropping-buffer sliding-buffer pipeline pipeline-async to-chan! thread]]
    [clojure.string]
    [clojure.java.io :as io])
  (:import
    (javax.swing JFrame)
  )    
)

#_(println (System/getProperty "clojure.core.async.pool-size"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defonce stateA (atom nil))
(def ^:dynamic jframe nil)

(defn window
  []
  (let [jframe (JFrame. "i am get-to-the-ship program")]

  (doto jframe
    (.setSize 1600 1200)
    (.setLocation 1700 300)
    (.setVisible true)
  )

  (alter-var-root #'get-to-the-ship.main/jframe (constantly jframe))

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
