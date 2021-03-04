(ns tandav.views.transactions)

(defn column-val [a b]
  #(str (-> % a) " " (-> % b)))

(defn table [_]
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

