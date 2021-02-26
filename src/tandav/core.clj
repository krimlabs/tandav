(ns tandav.core
  (:require [tandav.system :as system]
            [integrant.core :as ig]))

(defn- main []
    (ig/init system/config [:ui/renderer]))
