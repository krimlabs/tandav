(ns tandav.ui
  (:require [cljfx.api :as fx]
            [integrant.core :as ig]
            [tandav.db :as db]))

(defn column-val [a b]
  #(str (-> % a) " " (-> % b)))

(defn trades-view [{:keys [trades]}]
  {:fx/type :table-view
   :columns [{:fx/type :table-column
              :text "id"
              :cell-value-factory :id}
             {:fx/type :table-column
              :text "buy"
              :cell-value-factory (column-val :buy_units :buy_cur)}
             {:fx/type :table-column
              :text "sell"
              :cell-value-factory (column-val :sell_units :sell_cur)}
             {:fx/type :table-column
              :text "fee"
              :cell-value-factory (column-val :fee_units :fee_cur)}
             {:fx/type :table-column
              :text "tx-time"
              :cell-value-factory :tx_time}]
   :items [] ;;(db/get-all-trades db/conf)
   })

(defn new-tx-view []
  {:fx/type :h-box
   :children [{:fx/type :v-box
               :children [{:fx/type :label
                           :text "Buy currency"}
                          {:fx/type :text-field}]}
              {:fx/type :v-box
               :children [{:fx/type :label
                           :text "Sell currency"}
                          {:fx/type :text-field}]}]})

(defn root-view [{{:keys []} :state}]
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :spacing 20
                  :children [{:fx/type trades-view}]}}})

(defmethod ig/init-key :ui/renderer [_ _]
  (let [state (atom {})
        renderer
        (fx/create-renderer
         ;; :opts {:fx.opt/map-event-handler event-handler}
         :middleware (fx/wrap-map-desc (fn [state]
                                         {:fx/type root-view
                                          :showing true
                                          :state state})))]
    ;; start the ui
    (fx/mount-renderer state renderer)

    ;; return components
    {:state state
     :renderer renderer}))

;; (defmethod ig/halt-key! :ui/renderer [_ {:keys [renderer]}]
;;   (renderer {:fx/type :root-view
;;              :showing false}))

