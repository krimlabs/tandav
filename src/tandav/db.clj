(ns tandav.db
  (:require [datahike.api :as d]
            [datahike-jdbc.core]))

(def config {:store {:backend :jdbc
                     :dbtype "sqlite"
                     :dbname "./resources/main.sqlite"}})

(def conn (d/connect config))

(defn record-trade
  [{:keys [currency-bought units-bought currency-spent units-spent] :as trade}]
  (d/transact conn [{:trade/currency-bought currency-bought
                     :trade/units-bought (float units-bought)
                     :trade/currency-spent currency-spent
                     :trade/units-spent (float units-spent)}]))

(defn get-all-trades []
  (d/q '[:find ?e ?cb ?ub ?cs ?us
         ;;:keys [id currency-bought units-bought currency-spent units-spent tx-instant]
         :where
         [?e :trade/currency-bought ?cb]
         [?e :trade/currency-spent ?cs]
         [?e :trade/units-bought ?ub]
         [?e :trade/units-spent ?us]
         [_ :db/txInstant ?tx-instant]]
       @conn))

(defn total-units-bought-and-spent
  "The total units of bought currency in spent currency"
  [conn currency-bought currency-spent]
  (d/q '[:find [(sum ?ub) (sum ?us)]
         :in $ ?currency-bought ?currency-spent
         :where
         [?e :trade/currency-bought ?currency-bought]
         [?e :trade/currency-spent ?currency-spent]
         [?e :trade/units-bought ?ub]
         [?e :trade/units-spent ?us]]
       @conn currency-bought currency-spent))

;; init
(comment
  (d/create-database config)
  (d/delete-database config))

;; migrations
(comment
  ;; setup trades
  (d/transact
   conn
   [{:db/ident :trade/currency-bought
     :db/valueType :db.type/keyword
     :db/cardinality :db.cardinality/one}
    {:db/ident :trade/units-bought
     :db/valueType :db.type/float
     :db/cardinality :db.cardinality/one}
    {:db/ident :trade/currency-spent
     :db/valueType :db.type/keyword
     :db/cardinality :db.cardinality/one}
    {:db/ident :trade/units-spent
     :db/valueType :db.type/float
     :db/cardinality :db.cardinality/one}]))

(comment
  (d/datoms @conn :eavt)

  (clojure.pprint/print-table (get-all-trades))

  (d/q '[:find ?e ?units-bought
         :where
         [?e :trade/currency-bought :eth]
         [?e :trade/units-bought ?units-bought]]
       @conn)

  ;; all currencies bought
  (d/q '[:find [?cb ...]
         :where [_ :trade/currency-bought ?cb]]
       @conn)

  ;; how much of each currency was bought?
  (d/q '[:find ?e ?cb ?ub
         :where
         [?e :trade/currency-bought ?cb]
         [?e :trade/units-bought ?ub]]
       @conn)

  ;; find all eth trades
  (d/q '[:find ?e ?ub
         :in $ ?currency
         :where
         [?e :trade/currency-bought ?currency]
         [?e :trade/units-bought ?ub]]
       @conn :eth)

  ;; find total eth at hand
  (d/q '[:find (sum ?ub) .
         :in $ ?currency
         :where
         [?e :trade/currency-bought ?currency]
         [?e :trade/units-bought ?ub]]
       @conn :eth)

  ;; find average cost of eth in dai
  (d/q '[:find [(sum ?ub) (sum ?us)]
         :in $ ?currency-bought ?currency-spent
         :where
         [?e :trade/currency-bought ?currency-bought]
         [?e :trade/currency-spent ?currency-spent]
         [?e :trade/units-bought ?ub]
         [?e :trade/units-spent ?us]]
       @conn :eth :dai)
  )
