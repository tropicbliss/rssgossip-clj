(ns rssgossip-clj.core
  (:refer-clojure :exclude [parse-opts])
  (:require
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.string :as string]
   [clj-http.client :as http]
   [clojure.data.xml :as xml])
  (:gen-class))

(import 'java.text.Normalizer 'java.text.Normalizer$Form)

(def cli-options
  [["-u" "--urls" "Include URLs in output"]
   ["-h" "--help" "Show help message"]])

(defn usage [options-summary]
  (->> ["Usage:"
        "lein run [-uh] <search-regexp>"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "Error: " (string/join \newline errors)))

(defn validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors)}

      (not= 1 (count arguments))
      {:exit-message (usage summary)}

      :else
      {:search-regexp (first arguments) :include-urls (:urls options)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn split [text] (string/split text #"\s+"))

(defn remove-accents [text]
  (->> (Normalizer/normalize text Normalizer$Form/NFKD)
       (filter #(<= (int %) 127))
       (apply str)))

(defn re-ignorecase [pattern] (re-pattern (str "(?i)" pattern)))

(defn first-child-by-name [parent tag-name]
  (first (filter #(= (:tag %) tag-name) (:content parent))))

(defn get-node-content [node] (first (:content node)))

(defn -main [& args]
  (let [{:keys [search-regexp include-urls exit-message ok?]} (validate-args args) pattern (re-ignorecase search-regexp)]
    (if exit-message
      (exit (if ok? 0 2) exit-message)
      (let [rss-feed (System/getenv "RSS_FEED") urls (split rss-feed)]
        (doseq [url urls]
          (let [response (http/get url)]
            (try
              (let [dom (xml/parse-str (:body response)) nodes (filter #(= :item (:tag %)) (xml-seq dom))]
                (doseq [node nodes]
                  (let [title-node (first-child-by-name node :title) link-node (first-child-by-name node :link) txt (remove-accents (get-node-content title-node))]
                    (when (re-find pattern txt)
                      (println txt)
                      (if include-urls
                        (println (str "\t" (get-node-content link-node))))))))
              (catch Exception _ (System/exit 1)))))))))