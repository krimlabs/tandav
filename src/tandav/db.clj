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

(defmethod ig/init-key :db/postgres [_ conf]
  conf)

(defmethod ig/init-key :db/sqlite [_ conf]
  conf)

(defmethod ig/init-key :db/migrations [_ {:keys [resource]
                                          :db/keys [sqlite]}]
  (let [ragtime-config {:datastore (ragtime.jdbc/sql-database sqlite)
                        :migrations (ragtime.jdbc/load-resources "migrations")}
        migrate-fn #(ragtime.repl/migrate ragtime-config)]
    (try
      (migrate-fn)
      (catch Exception e
        (.printStackTrace e)
        (prn "Database is not up")))

    {:ragtime-config ragtime-config
     :migrate-fn migrate-fn}))

(defn conf->datasource-opts [conf]
  (cond
    (= "postgresql" (-> conf :dbtype))
    {:username (-> conf :user)
     :password (-> conf :password)
     :database-name (-> conf :dbname)
     :server-name (-> conf :host)
     :port-number (-> conf :port)
     :adapter (-> conf :dbtype)
     :maximum-pool-size 5}

    (= "sqlite" (-> conf :subprotocol))
    {:jdbc-url (str "jdbc:sqlite:" (-> conf :subname))}))

(defmethod ig/init-key :db/cp [_ {:db/keys [sqlite]}]
  {:datasource (hikari/make-datasource (conf->datasource-opts sqlite))})

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
    (get-all-transactions db-spec))


  (let [rg-conf (-> system :db/migrations :ragtime-config)
        migrate (-> system :db/migrations :migrate)]
    (migrate)))
