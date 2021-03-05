(ns tandav.views.accounts
  (:require [cljfx.api :as fx]))

(defn text-input [{:keys [fx/context state-key label]}]
  {:fx/type :v-box
   :spacing 4
   :children [{:fx/type :label
               :text label}
              {:fx/type :text-field
               :text (fx/sub-val context state-key)
               :on-text-changed {:event/type :app/swap-form-kv!
                                 :k state-key}}]})
(defn unns-keys
  "Remove namespace of all keys of the map"
  [m]
  (zipmap
   (map (comp keyword name) (keys m))
   (vals m)))

(defn sub-new-acc [ctx]
  (-> ctx
      (fx/sub-val select-keys [::name ::address ::api-key ::secret-key])
      unns-keys))

(defn new-account-form [{:keys [fx/context state]}]
  {:fx/type :v-box
   :spacing 8
   :children [{:fx/type :label
               :text "Add a new account"}
              {:fx/type text-input
               :label "Name"
               :state-key ::name}
              {:fx/type text-input
               :label "Wallet address"
               :state-key ::address}
              {:fx/type text-input
               :label "Secret key"
               :state-key ::secret-key}
              {:fx/type text-input
               :label "Api key"
               :state-key ::api-key}
              {:fx/type :button
               :text "Save account"
               :on-action {:event/type :accounts/save
                           :acc (sub-new-acc context)}}]})

(defn table [{:keys [fx/context]}]
  {:fx/type :table-view
   :columns [{:fx/type :table-column
              :text "id"
              :cell-value-factory :id}
             {:fx/type :table-column
              :text "name"
              :cell-value-factory :name}
             {:fx/type :table-column
              :text "address"
              :cell-value-factory :address}
             {:fx/type :table-column
              :text "api-key"
              :cell-value-factory :api_key}
             {:fx/type :table-column
              :text "secret-key"
              :cell-value-factory :secret_key}
             {:fx/type :table-column
              :text "created-at"
              :cell-value-factory :created_at}]
   :items (or (fx/sub-val context :accounts) [])})

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

