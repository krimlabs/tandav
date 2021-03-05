(ns tandav.events
  (:require [cljfx.api :as fx]
            [tandav.db :as db]))

(defmulti handler :event/type)

(defmethod handler :app/swap-form-kv! [{:keys [fx/context k fx/event]}]
  {:context (fx/swap-context context assoc k event)})

(defmethod handler :accounts/fetch-all [{:keys [db/cp fx/context]}]
  {:db {:db/cp cp
        :f db/get-all-accounts
        :on-success {:event/type :accounts.cb/fetch-all-success}
        :on-error {:event/type :accounts.cb/fetch-all-error}}})

(defmethod handler :accounts.cb/fetch-all-success [{:keys [fx/context res]}]
  {:context (fx/swap-context context assoc :accounts res)})

(defmethod handler :accounts.cb/fetch-all-error [{:keys [err]}]
  (prn err)
  {})

(defmethod handler :accounts/save [{:keys [db/cp fx/context acc] :as x}]
  {:db {:db/cp cp
        :f db/insert-account
        :params acc
        :on-success {:event/type :accounts.cb/save-success}
        :on-error {:event/type :accounts.cb/save-error}}})

(defmethod handler :accounts.cb/save-success [{:keys [fx/context res]}]
  {:dispatch {:event/type :accounts/fetch-all}})

(defmethod handler :accounts.cb/save-error [{:keys [err]}]
  (prn err)
  {})

