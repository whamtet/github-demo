(ns ctmx.github-demo.web.views.chrome-extension
    (:require
      [ctmx.github-demo.web.htmx :as htmx]
      [ctmx.core :as ctmx :refer [defcomponent make-routes]]
      [ctmx.github-demo.web.services.sse :as sse]))

(defn- uuid-input [uuid]
  [:input#uuid-input {:type "hidden" :value uuid}])

(defcomponent ^:endpoint extension [req]
  [:div {:hx-ws "connect:/extension/sse"}
    (uuid-input nil)
    [:div#stats]])

(defn chrome-extension-handler []
  (conj
    (make-routes
     "/extension"
     (fn [req]
       (htmx/page-htmx
        (extension req))))
    ["/sse"
      (fn [req]
        {:undertow/websocket
          {:on-open (fn [{:keys [channel]}]
                      (let [k (sse/add-connection channel)]
                        (sse/send k (uuid-input k))))
           :on-close-message (fn [{:keys [channel]}]
                               (sse/remove-connection channel))}})]))
