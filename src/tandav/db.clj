(ns tandav.db
  (:require [hugsql.core :as hugsql]
            [ragtime.jdbc]
            [ragtime.repl]))

(def conf {:subprotocol "sqlite"
           :subname "resources/main.sqlite"})

(def ragtime-config
  {:datastore (ragtime.jdbc/sql-database conf)
   :migrations (ragtime.jdbc/load-resources "migrations")})

;; sql function def macro call
(hugsql/def-db-fns "tandav/db.sql")

(comment
  (ragtime.repl/migrate ragtime-config)
  (ragtime.repl/rollback ragtime-config)

  (get-all-trades conf)
  (insert-trade conf {:buy-cur "dai"
                      :sell-cur "eth"
                      :buy-units 43
                      :sell-units 0.04
                      :fee-cur "dai"
                      :fee-units "4"
                      :tx-time "2021-01-01"})
  )









