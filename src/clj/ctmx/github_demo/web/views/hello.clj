(ns ctmx.github-demo.web.views.hello
  (:require
    [ctmx.core :as ctmx :refer [defcomponent]]
    [ctmx.github-demo.example :refer [defexample]]
    [ctmx.github-demo.util :as util]
    [ctmx.github-demo.web.views.active-search :refer [active-search-handler]]
    [ctmx.github-demo.web.views.bulk-update :refer [bulk-update-handler]]
    [ctmx.github-demo.web.views.click-to-edit :refer [click-to-edit-handler]]
    [ctmx.github-demo.web.views.click-to-load :refer [click-to-load-handler]]
    [ctmx.github-demo.web.views.delete-row :refer [delete-row-handler]]
    [ctmx.github-demo.web.views.dialogs :refer [dialogs-handler]]
    [ctmx.github-demo.web.views.infinite-scroll :refer [infinite-scroll-handler]]
    [ctmx.github-demo.web.views.inline-validation :refer [inline-validation-handler]]
    [ctmx.github-demo.web.views.modal-bootstrap :refer [modal-bootstrap-handler]]
    [ctmx.github-demo.web.views.progress-bar :refer [progress-bar-handler]]
    [ctmx.github-demo.web.views.sortable :refer [sortable-handler]]
    [ctmx.github-demo.web.views.tabs-hateoas :refer [tabs-hateoas-handler tabs-hateoas-handler2]]
    [ctmx.github-demo.web.views.value-select :refer [value-select-handler]]))

(defcomponent ^:endpoint hello [req my-name]
  [:div#hello "Hello " my-name])

(defexample
  "/demo"
  (fn [req]
    [:div {:style "padding: 10px"}
     [:label {:style "margin-right: 10px"}
      "What is your name?"]
     [:input {:type "text"
              :name "my-name"
              :hx-patch "hello"
              :hx-target "#hello"}]
     (hello req "")]))

(defcomponent ^:endpoint form [req ^:path first-name ^:path last-name]
  [:form {:id id :hx-post "form"}
    [:input {:type "text" :name (path "first-name") :value first-name}] [:br]
    [:input {:type "text" :name (path "last-name") :value last-name}] [:br]
    (when (= ["Barry" "Crump"] [first-name last-name])
      [:div "A good keen man!"])
    [:input {:type "submit"}]])

(defexample
  "/data-flow"
  (fn [req]
    (form req "Barry" "")))

(defcomponent table-row [req i first-name last-name]
  [:tr
    [:td first-name] [:td last-name]])

(defcomponent table [req]
  [:table
    (ctmx.rt/map-indexed
      table-row
      req
      [{:first-name "Matthew" :last-name "Molloy"}
       {:first-name "Chad" :last-name "Thomson"}])])

(defexample
  "/nesting-components"
  (fn [req]
    (table req)))

(defcomponent ^:endpoint click-div [req ^:int num-clicks]
  [:form {:id id :hx-get "click-div" :hx-trigger "click"}
    [:input {:type "hidden" :name "num-clicks" :value (inc num-clicks)}]
    "You have clicked me " num-clicks " times!"])

(defexample
  "/parameter-casting"
  (fn [req]
    (click-div req 0)))

(defn add-customer [_ {:keys [first-name last-name customer]}]
  {:customer
   (conj (or customer []) {:first-name first-name :last-name last-name})})

(defn- text [name value]
  [:input {:type "text"
           :name name
           :value value
           :required true
           :style "margin-right: 5px"}])

(defcomponent customer [req i {:keys [first-name last-name]}]
  [:div
    [:input {:type "hidden" :name (path "first-name") :value first-name}]
    [:input {:type "hidden" :name (path "last-name") :value last-name}]])

(defcomponent ^:endpoint ^{:params-stack add-customer} customer-list
  [req first-name last-name ^:json-stack customer]
  [:form {:id id :hx-post "customer-list"}
    ;; display the nested params
    [:pre (-> req :params ctmx.form/json-params util/pprint)]
    [:br]

    (ctmx.rt/map-indexed ctmx.github-demo.web.views.hello/customer req customer)
    (text (path "first-name") first-name)
    (text (path "last-name") last-name)
    [:input {:type "submit" :value "Add Customer"}]])

(defexample
  "/transforming"
  (fn [req]
    (customer-list req "Joe" "Stewart" [])))

(defn ui-routes [base-path]
  (mapcat
   #(drop 3 %)
   [demo
    data-flow
    nesting-components
    parameter-casting
    transforming
    ;; other demos
    active-search-handler
    bulk-update-handler
    click-to-edit-handler
    click-to-load-handler
    delete-row-handler
    dialogs-handler
    infinite-scroll-handler
    inline-validation-handler
    modal-bootstrap-handler
    progress-bar-handler
    sortable-handler
    tabs-hateoas-handler
    tabs-hateoas-handler2
    value-select-handler]))
