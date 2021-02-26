(ns tandav.ui
  (:require [cljfx.api :as fx]
            [integrant.core :as ig]
            [tandav.db :as db]))

(defn column-val [a b]
  #(str (-> % a) " " (-> % b)))

(defn transactions-view [_]
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

(defn new-tx-view [_]
  {:fx/type :h-box
   :children [{:fx/type :v-box
               :children [{:fx/type :label
                           :text "Buy currency"}
                          {:fx/type :text-field}]}
              {:fx/type :v-box
               :children [{:fx/type :label
                           :text "Sell currency"}
                          {:fx/type :text-field}]}]})

(defn accounts [{:keys [state]}]
  {:fx/type :v-box
   :children [{:fx/type :tool-bar
               :items [{:fx/type :h-box
                        :alignment :center
                        :children [{:fx/type :button
                                    :text "Add account"
                                    :on-mouse-clicked #(swap! state assoc :view :accounts/new)}]}]}
              {:fx/type :table-view
               :columns [{:fx/type :table-column
                          :text "id"
                          :cell-value-factory :id}
                         {:fx/type :table-column
                          :text "name"
                          :cell-value-factory identity}
                         {:fx/type :table-column
                          :text "address"
                          :cell-value-factory identity}
                         {:fx/type :table-column
                          :text "api-key"
                          :cell-value-factory identity}
                         {:fx/type :table-column
                          :text "secret-key"
                          :cell-value-factory identity}
                         {:fx/type :table-column
                          :text "created-at"
                          :cell-value-factory :created_at}]
               :items [] ;;(db/get-all-trades db/conf)
               }]})

(defn new-account [_]
  {:fx/type :v-box
   :children [{:fx/type :label
               :text "Add new account"}
              {:fx/type :text-field}]})

(defn dash [{:keys [state]}]
  {:fx/type :tab-pane
   :pref-width 960
   :pref-height 540
   :tabs [{:fx/type :tab
           :text "Accounts"
           :closable false
           :content {:fx/type accounts
                     :state state}}
          {:fx/type :tab
           :text "Transactions"
           :closable false
           :content {:fx/type transactions-view}}
          {:fx/type :tab
           :text "Position"
           :closable false
           :content {:fx/type transactions-view}}]})

(def router
  {:app/dashboard dash
   :accounts/new new-account})

(defn root-view [{:keys [cp state]}]
  (let [view (get state :view :app/dashboard)]
    {:fx/type :stage
     :showing true
     :scene {:fx/type :scene
             :root {:fx/type (-> router view)
                    :state state}}}))

(defmethod ig/init-key :ui/state [_ {:keys [init-val]}]
  (atom init-val))

(defmethod ig/init-key :ui/renderer [_ {:db/keys [cp migrations]
                                        :ui/keys [state]}]
  (let [renderer (fx/create-renderer
                  ;; :opts {:fx.opt/map-event-handler event-handler}
                  :middleware (fx/wrap-map-desc (fn [state]
                                                  {:fx/type root-view
                                                   :cp cp
                                                   :migrations migrations
                                                   :showing true
                                                   :state state})))]
    ;; invoke renderer when the state changes
    (fx/mount-renderer state renderer)

    ;; return components
    {:state state
     :renderer renderer}))

(defmethod ig/halt-key! :ui/renderer [_ {:keys [state renderer]}]
  (fx/unmount-renderer state renderer))

(comment
  (require '[integrant.repl.state :refer [system]])
  )
