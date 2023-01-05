(ns ctmx.github-demo.example
    (:require
      [clojure.string :as string]
      [ctmx.core :as ctmx]
      [ctmx.github-demo.env :as env]
      [ctmx.render :as render]
      [hiccup.core :as h])
    (:import
      java.io.File))

(def src-dir (File. "src/clj/ctmx/github_demo/web/views"))
(def html-dir "../ctmx-doc/_includes/examples")
(def snippets-dir (File. "../ctmx-doc/_includes/snippets"))

(defn- spit-lines [f lines]
  (spit f (string/join "\n" lines)))

(defn insert-snippet [snippet]
  (let [f "../ctmx-doc/index_dev.html"
        lines (-> f
                  slurp
                  (.split "\n"))
        snippet? #(-> % .trim (.startsWith "<!--snippet"))
        [head _ _ _ tail] (partition-by snippet? lines)]
    (spit-lines f
                (concat
                 head
                 ["<!--snippet-->"
                  snippet
                  "<!--snippet-->"]
                 tail))))

(def render #(->> % render/walk-attrs (vector :div {:hx-ext "lambda-cors"}) h/html))

(defmacro defexample [endpoint f]
  (let [s (-> endpoint (.replace "/" "") symbol)]
    `(def ~s
      (do
        (when env/dev?
              (->> (~f {})
                   render
                   (spit
                    ~(str html-dir (.replace endpoint "-" "_") ".html"))))
        (drop 3 (ctmx/make-routes "" ~f))))))

(defmacro defexample2 [endpoint f]
  (assert env/dev? "no defexample2 in production")
  (let [s (-> endpoint (.replace "/" "") symbol)]
    `(def ~s
      (let [s# (render (~f {}))]
        (spit ~(str html-dir (.replace endpoint "-" "_") ".html") s#)
        (insert-snippet s#)
        (drop 3 (ctmx/make-routes "" ~f))))))

(def snippet? #(= ";; snippet" %))
(def defexample? #(-> % .trim (.startsWith "(defexample")))
(def get-indent #(re-find #"^ +" %))

(defn get-snippets [f]
  (-> f
      slurp
      (.split "\n")
      (->>
       (partition-by snippet?)
       (take-nth 2)
       rest
       (take-nth 2))))

(defn prettify-snippet [lines]
  (let [[pre [defexample route fn-str & rest]] (split-with (complement defexample?) lines)
        indent (get-indent fn-str)]
    (string/join "\n"
                 (concat
                  ["```clojure"]
                   pre
                  ["(def ring-handler"
                   fn-str
                   (str indent "  ;; page renders initial html")
                   (str indent "  (page")]
                  (map #(str "  " %) (butlast rest))
                  [(str "  " (last rest) ")")
                   "```"]))))

(defn spit-snippets [f]
  (dorun
   (map-indexed
    (fn [i snippet]
      (let [out-name (.replace (.getName f) ".clj" (str i ".md"))]
        (spit (File. snippets-dir out-name)
              (prettify-snippet snippet))))
    (get-snippets f))))

(defn spit-all []
  (doseq [f (file-seq src-dir)
          :when (.endsWith (.getName f) ".clj")]
    (spit-snippets f)))
