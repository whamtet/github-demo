(ns ctmx.github-demo.web.routes.ui
  (:require
   [ctmx.github-demo.web.middleware.exception :as exception]
   [ctmx.github-demo.web.middleware.formats :as formats]
   [ctmx.github-demo.web.views.chrome-extension :as chrome-extension]
   [ctmx.github-demo.web.views.hello :as hello]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

(defn route-data [opts]
  (merge
   opts
   {:muuntaja   formats/instance
    :middleware
    [;; Default middleware for ui
     ;; query-params & form-params
      parameters/parameters-middleware
      ;; encoding response body
      muuntaja/format-response-middleware
      ;; exception handling
      exception/wrap-exception]}))

(derive :reitit.routes/ui :reitit/routes)

(defmethod ig/init-key :reitit.routes/ui
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  [[base-path (route-data opts) (hello/ui-routes base-path)]
   (chrome-extension/chrome-extension-handler)])
