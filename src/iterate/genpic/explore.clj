;; # Using the OpenAI API

(ns iterate.genpic.explore
  {:nextjournal.clerk/toc true}
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]
   [babashka.curl]))

(defn read-config []
  (-> (slurp "config.edn")
      edn/read-string))

^::clerk/no-cache
(def config (read-config))

(assert (:openapi-api-key config))
(assert (:openapi-org config))

;; ## Tokens and setup

;; This is Teodor's personal OpenAI key. Some usage for educational purposes is
;; fine.

(defn openapi-post
  [endpoint opts]
  (http/post endpoint {:headers (:headers opts)
                       :body (:body opts "")}))
(defn openapi-post-picture
  [endpoint]
  (def resp (http/post "https://api.openai.com/v1/images/generations"
                       {:headers {:content-type "application/json"
                                  :authorization (str "Bearer " (System/getenv "GENPIC_OPENAI_API_KEY"))}
                        :body (json/generate-string {:prompt "Generate a cute picture of a dog" 
                                            :n 2 
                                            :size "1024x1024"})})) 
  (:data (json/parse-string (:body resp) true)))
  
(defn resp->json [resp] (json/parse-string (:body resp) keyword))

;; ## What models are available? 

(defn gpt-ask [q api-key]
  (->>
   (openapi-post "https://api.openai.com/v1/chat/completions"
                 {:headers {:content-type "application/json"
                            :authorization (str "Bearer " api-key)}
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

(defn gpt-generate [p api-key]
  (->>
    (openapi-post-picture "https://api.openai.com/v1/images/generations")
 ))

(gpt-generate "Make a dog picture" (System/getenv))


(gpt-ask "What is 4+4" (System/getenv "GENPIC_OPENAI_API_KEY"))
#_(clerk/html [:p  
             (gpt-ask "What is the Norwegian company Iterate?" (System/getenv "GENPIC_OPENAI_API_KEY"))])
#_(clerk/html [:p (gpt-ask "Hva heter du?" (System/getenv "GENPIC_OPENAI_API_KEY"))]) 
(clerk/html [:p ( gpt-generate "White cat" (System/getenv "GENPIC_OPENAI_API_KEY"))])