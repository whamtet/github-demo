(ns ctmx.github-demo.web.views.chrome-extension
    (:require
      [clojure.string :as string]
      [ctmx.core :as ctmx :refer [defcomponent]]
      [ctmx.github-demo.web.services.readability :as readability]))

(defn uuid-input [uuid]
  [:input#uuid-input {:type "hidden" :value uuid}])

(def ari-grades ["Kindergarten"
                 "First Grade"
                 "Second Grade"
                 "Third Grade"
                 "Fourth Grade"
                 "Fifth Grade"
                 "Sixth Grade"
                 "Seventh Grade"
                 "Eighth Grade"
                 "Ninth Grade"
                 "Tenth Grade"
                 "Eleventh Grade"
                 "Twelfth Grade"
                 "College Student"])

(defn- ari-grade [score]
  (let [i (-> score dec (max 0) Math/ceil long)]
    (get ari-grades i (peek ari-grades))))

(defn flesch-rating [score]
  (condp <= score
    90.0	["5th grade."	"Very easy to read, easily understood by an average 11-year-old student."]
    80.0	["6th grade."	"Easy to read, conversational English for consumers."]
    70.0	["7th grade."	"Fairly easy to read."]
    60.0	["8th & 9th grade."	"Plain English, easily understood by 13- to 15-year-old students."]
    50.0	["10th to 12th grade."	"Fairly difficult to read."]
    30.0	["College."	"Difficult to read."]
    10.0	["College graduate."	"Very difficult to read, best understood by university graduates."]
    ["Professional."	"Extremely difficult to read, best understood by university graduates."]))

(defn- header [s]
  [:h2.text-xl.mt-2 s])

(defn stats* [{stats :stats
              {:keys [ari
                      fre
                      smog
                      cl]} :readability}]
  [:div#stats
    [:div.mt-2 (pr-str stats)]
    (header  "Automated readability index")
    [:p (format "Score: %.1f. " ari) (ari-grade ari)]
    (header "Flesch reading ease")
    [:p (format "Score: %.1f. " fre) (string/join " " (flesch-rating fre))]
    (header "Simple measure of Gobbledygook (SMOG)")
    [:p (format "Grade %.0f" smog)]
    (header "Coleman-Liau index")
    [:p (format "Grade %.0f" cl)]])

(defcomponent ^:endpoint stats [req text]
  (if text
    (-> text readability/analysis stats*)
    [:div#stats]))

(defcomponent ^:endpoint extension [req]
  [:div.p-2
    [:form {:hx-post "stats" :hx-target "#stats"}
      [:input#text {:type "hidden" :name "text"}]
      [:input#text-update {:type "submit" :style "display: none"}]]
    (stats req nil)])