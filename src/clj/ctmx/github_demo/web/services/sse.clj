(ns ctmx.github-demo.web.services.sse
  (:refer-clojure :exclude [send])
  (:require
    [ctmx.render :as render]
    [ring.adapter.undertow.websocket :as ws])
  (:import
    java.util.UUID))

(def connections (atom {}))

(defn- assoc2 [m k v]
  (-> m
    (assoc-in [0 k] v)
    (assoc-in [1 v] k)))
(defn- dissoc-v [m v]
  (if-let [k (get-in m [1 v])]
    (-> m
      (update 0 dissoc k)
      (update 1 dissoc v))))

(defn add-connection [v]
  (let [k (UUID/randomUUID)]
    (swap! connections assoc2 k v)
    k))

(defn remove-connection [v]
  (swap! connections dissoc-v v))

(defn send [k hiccup]
  (when-let [connection (get-in @connections [0 k])]
    (ws/send (render/html hiccup) connection)))