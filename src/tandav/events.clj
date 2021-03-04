(ns tandav.events
  (:require [cljfx.api :as fx]))

(defmulti handler :event/type)

(defmethod handler ::swap-kv! [{:keys [fx/context k fx/event]}]
  {:context (fx/swap-context context assoc k event)})
