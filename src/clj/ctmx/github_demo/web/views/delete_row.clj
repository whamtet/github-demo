(ns ctmx.github-demo.web.views.delete-row
    (:require
      [ctmx.core :as ctmx :refer [defcomponent]]
      [ctmx.github-demo.example :refer [defexample]]))

(def data
  [{:name "Joe Smith" :email "joe@smith.org"}
   {:name "Angie MacDowell" :email "angie@macdowell.org"}
   {:name "Fuqua Tarkenton" :email "fuqua@tarkenton.org"}
   {:name "Kim Yee"	:email "kim@yee.org"}])

(defcomponent ^:endpoint tr [{:keys [request-method]} name email]
  (if (= :delete request-method)
    ""
    [:tr
      [:td name]
      [:td email]
      [:td "Active"]
      [:td [:button.btn.btn-danger {:hx-delete "tr"} "Delete"]]]))

(defexample
  "/delete-row-handler"
  (fn [req]
    [:table.table.delete-row-example
      [:thead
        [:tr [:th "Name"] [:th "Email"] [:th "Status"] [:th]]]
      [:tbody {:hx-confirm "Are you sure?" :hx-target "closest tr" :hx-swap "outerHTML swap:0.5s"}
        (ctmx.rt/map-indexed tr req data)]]))
