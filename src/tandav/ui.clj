(ns tandav.ui
  (:require [cljfx.api :as fx]
            [tandav.db :as db]))

(def state (atom {}))

(defn trades-view [{:keys [trades]}]
  {:fx/type :table-view
   :columns [{:fx/type :table-column
              :text "id"}
             {:fx/type :table-column
              :text "buy"}
             {:fx/type :table-column
              :text "sell"}]
   :items (db/get-all-trades db/conf)})

(defn root-view [{{:keys []} :state}]
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :spacing 20
                  :children [{:fx/type trades-view}]}}})
(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc (fn [state]
                                   {:fx/type root-view
                                    :state state}))
   :opts {:fx.opt/map-event-handler event-handler}))

(fx/mount-renderer state renderer)

(comment
  (renderer)
  )
