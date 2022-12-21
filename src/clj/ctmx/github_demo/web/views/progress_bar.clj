(ns ctmx.github-demo.web.views.progress-bar
    (:require
      [ctmx.core :as ctmx :refer [defcomponent]]
      [ctmx.github-demo.example :refer [defexample]]))

;; snippet
(defn- progress [width]
  [:div.progress
    [:div#pb.progress-bar {:style (str "width:" width "%")}]])

(defcomponent ^:endpoint start [req ^:double width]
  (let [width (if width (-> width (+ (rand 30)) (min 100)) 0)]
    (if (= width 100)
      [:div {:hx-target "this"}
        [:h3 "Complete"]
        (progress 100)
        [:button.btn {:hx-post "start"} "Restart"]]
      [:div {:hx-target "this"
             :hx-get "start"
             :hx-trigger "load delay:600ms"
             :hx-vals {:width width}}
        [:h3 "Running"]
        (progress width)])))

(defexample
  "/progress-bar-handler"
  (fn [req]
    start
    [:div {:style "height: 200px"}
      [:div {:hx-target "this"}
        [:h3 "Start Progress"]
        [:button.btn {:hx-post "start"} "Start Job"]]]))
;; snippet
