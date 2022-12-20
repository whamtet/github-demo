(ns ctmx.github-demo.web.views.dialogs
    (:require
      [ctmx.core :as ctmx :refer [defcomponent]]
      [ctmx.github-demo.example :refer [defexample]]))

(defcomponent ^:endpoint reply [{:keys [headers]}]
  [:div#response.mmargin "You entered " (headers "hx-prompt")])

(defexample
  "/dialogs-handler"
  (fn [req]
    [:div
      [:button.btn.mb
        {:hx-post "reply"
         :hx-prompt "Enter a string"
         :hx-confirm "Are you sure?"
         :hx-target "#response"}
        "Prompt Submission"]
      [:div#response]]))
