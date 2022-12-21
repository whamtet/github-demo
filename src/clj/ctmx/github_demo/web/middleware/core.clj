(ns ctmx.github-demo.web.middleware.core
    (:require
      [ctmx.github-demo.env :as env]
      [ring.middleware.cors :refer [wrap-cors]]
      [ring.middleware.defaults :as defaults]
      [ring.middleware.session.cookie :as cookie]))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (-> ((:middleware env/defaults) handler opts)
          (wrap-cors :access-control-allow-origin [env/cors]
                     :access-control-allow-methods [:get :put :post :delete :patch])
          (defaults/wrap-defaults
           (assoc-in site-defaults-config [:session :store] cookie-store))))))
