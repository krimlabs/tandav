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
                   :conf (ig/ref :db/conf)}
   :db/cp {:doc "Component to hold the connections pool"
           :conf (ig/ref :db/conf)}
   :ui/renderer {:cp (ig/ref :db/cp)
                 :migrations (ig/ref :db/migrations)}})

(def system
  (ig/init config))

(comment
  system)
