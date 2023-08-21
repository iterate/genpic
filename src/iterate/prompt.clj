(ns iterate.prompt
  {:nextjournal.clerk/toc true}
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]))

(defn read-config []
  (-> (slurp "config.edn")
      edn/read-string))

^::clerk/no-cache
(def config (read-config))

(assert (:openapi-api-key config))
(assert (:openapi-org config))

(def openapi-api-key (:openapi-api-key config))

(def authorization-header
  {:authorization (str "Bearer " openapi-api-key)
   "OpenAI-Organization" (:openapi-org config)})


(defn openapi-post
  [endpoint opts]
  (http/post endpoint {:headers (merge authorization-header (:headers opts))
                       :body (:body opts "")}))
(defn resp->json [resp] (json/parse-string (:body resp) keyword))
(defn first-choice-message [msg]
  (-> msg
      :choices
      first
      :message
      :content))

(defn gpt-ask [q]
  (->>
   (openapi-post "https://api.openai.com/v1/chat/completions"
                 {:headers {:content-type "application/json"
                            :authorization (str "Bearer " openapi-api-key)}
                  :body (json/generate-string {:model "gpt-3.5-turbo"
                                               :messages
                                               [{"role" "user",
                                                 "content" q}]
                                               "temperature" 0.7})})
   resp->json
   :choices
   first
   :message
   :content))
(clerk/html [:p
             (gpt-ask "What is the Norwegian company Iterate?")])


