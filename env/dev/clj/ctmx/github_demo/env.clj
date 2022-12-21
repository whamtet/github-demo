(ns ctmx.github-demo.env
  (:require
    [clojure.tools.logging :as log]
    [ctmx.github-demo.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[ starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[ started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[ has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})

;(def cors #"http://localhost:8000")
(def cors #"https://whamtet.github.io")
