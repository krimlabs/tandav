(ns tandav.position
  (:require [tandav.db :as db]))

(defn positions [])

(defn summary [])

(defn buy-price
  "Get the average buy-price of the currency-bought in currency-spent"
  [currency-bought currency-spent]
  (let [{:keys [total-units-bought total-units-spent]}
        (db/total-units-bought-and-spent currency-bought currency-spent)]
    (/ total-units-spent
       total-units-bought)))

(defn sell-price
  "Get the average sell-price of the currency-bought in currency-spent"
  [currency-bought currency-spent]
  (let [{:keys [total-units-bought total-units-spent]}
        (db/total-units-bought-and-spent currency-spent currency-bought)]
    (/ total-units-bought
       total-units-spent)))

(defn pair-position [bought spent]
  (let [{total-bought :total-units-bought}
        (db/total-units-bought-and-spent bought spent)

        {total-spent :total-units-spent}
        (db/total-units-bought-and-spent spent bought)]
    {:buy-price (buy-price bought spent)
     :sell-price (sell-price bought spent)
     :total-bought total-bought
     :total-sold total-spent
     :current-holding (- total-bought total-spent)}))


(db/record-trade {:currency-bought :dai
                  :currency-spent :eth
                  :units-bought 1500
                  :units-spent 1})

(buy-price :eth :dai)
(sell-price :eth :dai)

(pair-position :eth :dai)
(db/get-all-trades)
