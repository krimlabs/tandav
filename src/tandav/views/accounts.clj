(ns tandav.views.accounts
  (:require [cljfx.api :as fx]
            [tandav.events :as events]))

(defn text-input [{:keys [fx/context state-key label]}]
  {:fx/type :v-box
   :spacing 4
   :children [{:fx/type :label
               :text label}
              {:fx/type :text-field
               :text (fx/sub context state-key)
               :on-text-changed {:event/type ::events/swap-kv!
                                 :k state-key}}]})

(defn new-account-form [{:keys [fx/context state]}]
  {:fx/type :v-box
   :spacing 8
   :children [{:fx/type :label
               :text "Add a new account"}
              {:fx/type text-input
               :label "Name"
               :state-key ::name}
              {:fx/type text-input
               :label "Secret Key"
               :state-key ::secret-key}
              {:fx/type text-input
               :label "Api Key"
               :state-key ::api-key}
              {:fx/type :button
               :text "Save account"}]})

(defn table [_]
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
   })

(defn root [_]
  {:fx/type :grid-pane
   :padding 10
   :column-constraints [{:fx/type :column-constraints
                         :percent-width 200/3}
                        {:fx/type :column-constraints
                         :percent-width 100/3}]
   :children [{:fx/type table
               :grid-pane/vgrow :always
               :grid-pane/column 0
               :grid-pane/margin 5}
              {:fx/type new-account-form
               :grid-pane/vgrow :always
               :grid-pane/column 1
               :grid-pane/margin 5}]})

(comment
  (user/re-render)
  )

