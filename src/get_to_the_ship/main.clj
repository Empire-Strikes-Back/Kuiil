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
   (javafx.event Event EventHandler)
   (javafx.stage WindowEvent)
   (javafx.scene.control DialogEvent Dialog ButtonType ButtonBar$ButtonData)
   (javafx.scene.canvas Canvas)
   (javafx.scene.paint Color)
   #_javafx.application.Platform))

(println "clojure.compiler.direct-linking" (System/getProperty "clojure.compiler.direct-linking"))
(do (set! *warn-on-reflection* true) (set! *unchecked-math* true) (clojure.spec.alpha/check-asserts true))

#_(doto (.getGraphicsContext2D canvas)
    (.clearRect 0 0 canvas-width canvas-height)
    (.setStroke Color/LIGHTGREY)
    (.strokeRect 0 0 canvas-width canvas-height)
    #_(.setFill Color/LIGHTGREY)
    #_(.fillRoundRect 0 0 canvas-width canvas-height canvas-height canvas-height)
    #_(.setFill Color/GREEN)
    #_(.fillRoundRect 0 0 (* canvas-width 10) canvas-height canvas-height canvas-height))

(defonce stateA (atom nil))

(defn -main [& args]
  (println ::-main)
  (let []
    (reset! stateA {::program-name "get-to-the-ship"})
    (add-watch stateA :watch-fn (fn [k stateA old-state new-state] new-state))

    (javafx.application.Platform/setImplicitExit true)

    (go)))

(comment

  (-main)

  (do
    (require
     '[get-to-the-ship.main]
     :reload)
    (swap! stateA assoc ::random (rand-int 1000)))

  
  ;
  )