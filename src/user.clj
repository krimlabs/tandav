(ns user
  (:require [integrant.repl :refer [clear go halt prep init reset reset-all]]
            [integrant.repl.state :refer [system]]
            [tandav.system :refer [config]]))

(integrant.repl/set-prep! (constantly config))

(defn re-render
  "Call the cljfx renderer function"
  []
  (-> system
      :ui/renderer
      :renderer
      (apply [])))

(defn dispatch!
  "Dispatch an event from REPL"
  [e]
  (-> system
      :ui/event-handler
      (apply [e])))

(defn db-exec
  "Execute a db function without passing spec"
  ([f]
   (db-exec f {}))
  ([f params]
   (f (-> system :db/cp) params)))
