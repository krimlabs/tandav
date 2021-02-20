(ns tandav.db
  (:require [hugsql.core :as hugsql]
            [integrant.core :as ig]
            [hikari-cp.core :as hikari]
            [ragtime.jdbc]
            [ragtime.repl]
            [java-time :as time]
            [integrant.repl.state :refer [system]]))

;; sql function def macro call
(hugsql/def-db-fns "tandav/db.sql")

(defmethod ig/init-key :db/conf [_ conf]
  conf)

(defmethod ig/init-key :db/migrations [_ {:keys [resource conf]}]
  (let [ragtime-config {:datastore (ragtime.jdbc/sql-database conf)
                        :migrations (ragtime.jdbc/load-resources "migrations")}]
    {:migrate #(ragtime.repl/migrate ragtime-config)
     ;; rollback is a destructive action, use with caution
     ;; :rollback #(ragtime.repl/rollback ragtime-config)
     }))

(defmethod ig/init-key :db/cp [_ {:keys [conf]}]
  (let [datasource-opts {:username (-> conf :user)
                         :password (-> conf :password)
                         :database-name (-> conf :dbname)
                         :server-name (-> conf :host)
                         :port-number (-> conf :port)
                         :adapter (-> conf :dbtype)}
        datasource (hikari/make-datasource datasource-opts)]
    {:datasource datasource}))

(defmethod ig/halt-key! :db/cp [_ {:keys [datasource]}]
  (hikari/close-datasource datasource))

(comment
  (let [db-spec (-> system :db/cp)]
    (insert-transaction db-spec
                        {:buy-cur "eth"
                         :buy-units 1.35
                         :sell-cur "dai"
                         :sell-units 2002.13
                         :fee-cur "eth"
                         :fee-units 0.004
                         :tx-time (time/local-date-time)})
    (get-all-transactions db-spec)))
