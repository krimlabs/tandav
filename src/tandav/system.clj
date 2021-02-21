(ns tandav.system
  (:require [integrant.core :as ig]
            [tandav.db]
            [tandav.ui]))

(def config
  {:db/conf {:dbtype "postgresql"
             :dbname "tandav_dev"
             :host "localhost"
             :port 5432
             :user "shivekkhurana"
             :password ""}
   :db/migrations {:resource "migrations"
                   :db/conf (ig/ref :db/conf)}
   :db/cp {:doc "Component to hold the connections pool"
           :db/conf (ig/ref :db/conf)}
   :ui/state {:init-val {}}
   :ui/renderer {:doc "The cljfx renderer"
                 :ui/state (ig/ref :ui/state)
                 :db/cp (ig/ref :db/cp)
                 :db/migrations (ig/ref :db/migrations)}})

(def system
  (ig/init config [:ui/renderer]))

(comment
  system)
