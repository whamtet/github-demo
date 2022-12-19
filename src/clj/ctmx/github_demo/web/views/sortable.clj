(ns ctmx.github-demo.web.views.sortable
    (:require
      [ctmx.core :as ctmx :refer [defcomponent]]
      [ctmx.github-demo.example :refer [defexample]]))

(defn- content [items]
  (list*
    [:div.htmx-indicator "Updating..."]
    (for [item items]
      [:div
        [:input {:type "hidden" :name "order" :value item}]
        "Item " item])))

(defcomponent ^:endpoint sortable [req ^:ints order]
  (if (not-empty order)
    (content order)
    [:form#to-sort {:hx-post "sortable" :hx-trigger "end"}
      (content (range 1 6))]))

(defexample
  "/demo"
  (fn [req]
    (sortable req nil)))
