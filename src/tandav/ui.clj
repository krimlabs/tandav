(ns tandav.ui
  (:require [cljfx.api :as fx]
            [clojure.core.cache :as cache]
            [integrant.core :as ig]
            [tandav.db :as db]
            [tandav.events :as events]
            [tandav.views.transactions :as views.transactions]
            [tandav.views.accounts :as views.accounts]))

(defn root-view [_]
  {:fx/type :stage
   :showing true
   :scene {:fx/type :scene
           :root   {:fx/type :tab-pane
                    :pref-width 960
                    :pref-height 540
                    :tabs [{:fx/type :tab
                            :text "Accounts"
                            :closable false
                            :content {:fx/type views.accounts/root}}
                           {:fx/type :tab
                            :text "Transactions"
                            :closable false
                            :content {:fx/type views.transactions/table}}
                           {:fx/type :tab
                            :text "Position"
                            :closable false
                            :content {:fx/type views.transactions/table}}]}}})

(defmethod ig/init-key :ui/state [_ {:keys [init-val]}]
  (atom (fx/create-context init-val cache/lru-cache-factory)))

(defmethod ig/init-key :ui/event-handler [_ {:keys [ui/state]}]
 ;; https://github.com/cljfx/cljfx/blob/master/examples/e18_pure_event_handling.clj
  (-> events/handler
      (fx/wrap-co-effects
       {:fx/context (fx/make-deref-co-effect state)})
      (fx/wrap-effects
       {:context (fx/make-reset-effect state)
        :dispatch fx/dispatch-effect})))

(defmethod ig/init-key :ui/renderer [_ {:keys [db/cp db/migrations
                                               ui/state ui/event-handler]}]
  (let [renderer (fx/create-renderer
                  :middleware (comp
                               ;; Pass context to every lifecycle as part of option map
                               fx/wrap-context-desc
                               (fx/wrap-map-desc (fn [_]
                                                   {:fx/type root-view})))
                  :opts {:fx.opt/map-event-handler event-handler
                         :fx.opt/type->lifecycle #(or (fx/keyword->lifecycle %)
                                                      (fx/fn->lifecycle-with-context %))})]

    ;; invoke renderer when the state changes
    (fx/mount-renderer state renderer)

    ;; return components
    {:unmount-fn #(fx/unmount-renderer state renderer)
     :renderer renderer}))

(defmethod ig/halt-key! :ui/renderer [_ {:keys [unmount-fn]}]
  (unmount-fn))
