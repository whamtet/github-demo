(ns ctmx.github-demo.web.htmx
  (:require
   [ctmx.render :as render]
   [ring.util.http-response :as http-response]
   [hiccup.core :as h]
   [hiccup.page :as p]))

(defn page [opts & content]
  (-> (p/html5 opts content)
      http-response/ok
      (http-response/content-type "text/html")))

(defn ui [opts & content]
  (-> (h/html opts content)
      http-response/ok
      (http-response/content-type "text/html")))

(defn page-htmx [{:keys [js css]} & body]
  (page
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Htmx + Kit"]
    [:script {:src "https://unpkg.com/htmx.org@1.9.4/dist/htmx.min.js" :defer true}]
    (for [href css]
      [:link {:rel "stylesheet" :href href}])]
   [:body (render/walk-attrs body)]
   (for [src js]
     [:script {:src src}])))
