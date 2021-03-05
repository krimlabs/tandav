(ns tandav.system
  (:require [integrant.core :as ig]
            [tandav.db]
            [tandav.ui]))

(def config
  {;; db/postgres is not being used right now
   :db/postgres {:dbtype "postgresql"
                 :dbname "tandav_dev"
                 :host "localhost"
                 :port 5432
                 :user "shivekkhurana"
                 :password ""}
   :db/sqlite {:subprotocol "sqlite"
               :subname "resources/main.sqlite"}
   :db/migrations {:resource "migrations"
                   :db/sqlite (ig/ref :db/sqlite)}
   :db/cp {:doc "Component to hold the connections pool"
           :db/sqlite (ig/ref :db/sqlite)}
   :ui/state {:init-val {}}
   :ui/event-handler {:ui/state (ig/ref :ui/state)
                      :db/cp (ig/ref :db/cp)
                      :init-events-doc "Events to be dispatched when the app boots"
                      :init-events [{:event/type :accounts/fetch-all}]}
   :ui/renderer {:doc "The cljfx renderer"
                 :ui/state (ig/ref :ui/state)
                 :ui/event-handler (ig/ref :ui/event-handler)
                 :db/migrations (ig/ref :db/migrations)}})
