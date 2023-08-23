(ns iterate.genpic.create-image
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]
   [babashka.curl]))

(defn openapi-post-generate
  [opts api-key]
  (let [ resp (http/post "https://api.openai.com/v1/images/generations"
                         {:headers {:content-type "application/json"
                                    :authorization (str "Bearer " api-key)}
                          :body (json/generate-string {:prompt opts
                                                       :n 2
                                                       :size "1024x1024"})})]
    (:data (json/parse-string (:body resp) true))))

(defn gpt-generate [p api-key]
  (->>
   (openapi-post-generate p api-key)))
