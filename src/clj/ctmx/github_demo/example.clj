(ns ctmx.github-demo.example
    (:require
      [ctmx.core :as ctmx]
      [ctmx.render :as render]
      [hiccup.core :as h]))

(def spit-dir "../../WebstormProjects/ctmx/ctmx-doc/_includes/examples")
(def render #(->> % render/walk-attrs (vector :div {:hx-ext "lambda-cors"}) h/html))

(defmacro defexample [endpoint f]
  (let [s (-> endpoint (.replace "/" "") symbol)]
    `(def ~s
      (do
        (->> (~f {})
             render
             (spit
              ~(str spit-dir (.replace endpoint "-" "_") ".html")))
        (drop 3 (ctmx/make-routes "" ~f))))))
