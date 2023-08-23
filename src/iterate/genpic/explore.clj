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

(defn openai-post
  [endpoint opts]
  (http/post endpoint {:headers (:headers opts)
                       :body (:body opts "")}))
(defn openapi-post-generate
  [ opts api-key]
  (def resp (http/post "https://api.openai.com/v1/images/generations"
                       {:headers {:content-type "application/json"
                                  :authorization (str "Bearer " api-key)}
                        :body (json/generate-string {:prompt opts 
                                            :n 2 
                                            :size "1024x1024"})})) 
  (:data (json/parse-string (:body resp) true)))
  
(defn resp->json [resp] (json/parse-string (:body resp) keyword))

(def test-resp [{:url
                 "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-extgqg97n0k0RCTIbmYUPxMH.png?st=2023-08-23T06%3A00%3A14Z&se=2023-08-23T08%3A00%3A14Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-22T17%3A40%3A28Z&ske=2023-08-23T17%3A40%3A28Z&sks=b&skv=2021-08-06&sig=PNIWeAKtlG42Sy9gGvXslqJjE7fNthZA2f8IWu%2B7poA%3D"}
                {:url
                 "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-N0WdKCnGsTlw3wPty4v8yuYH.png?st=2023-08-23T06%3A00%3A15Z&se=2023-08-23T08%3A00%3A15Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-22T17%3A40%3A28Z&ske=2023-08-23T17%3A40%3A28Z&sks=b&skv=2021-08-06&sig=bMuBXKnBKdEB8KqxSo1LzHyAW6SxzmlmfE1TqLzpsto%3D"}])

(keys test-resp)
(get test-resp "data")
(defn resp->url [resp] 
   (->> resp
        (first)))
(->> test-resp 
     (map (fn [resp-list]
            (:url resp-list)))
     (type ))

(resp->url test-resp)


;; ## What models are available? 

(defn gpt-ask [q api-key]
  (->>
   (openai-post "https://api.openai.com/v1/chat/completions"
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

(def test-data (openai-post "https://api.openai.com/v1/chat/completions"
                            {:headers {:content-type "application/json"
                                       :authorization (str "Bearer " ( System/getenv "GENPIC_OPENAI_API_KEY"))}
                             :body (json/generate-string {:model "gpt-3.5-turbo"
                                                          :messages
                                                          [{"role" "user",
                                                            "content" "How large is Galdehøpiggen"}]
                                                          "temperature" 0.7})}))
(comment 
  
  (println test-data) 
  (->> (json/parse-string (:body test-data) keyword)
       ( :choices)
       (first)
       (:message )
       (:content)
       )
  )
  
(defn gpt-generate [p api-key]
  (->>
   (openapi-post-generate p api-key)
   
   ))

(openai-post "https://api.openai.com/v1/chat/completions"
 {:headers {:content-type "application/json"
            :authorization (str "Bearer " ( System/getenv "GENPIC_OPENAI_API_KEY"))}
  :body (json/generate-string {:model "gpt-3.5-turbo"
                               :messages
                               [{"role" "user",
                                 "content" "Gi meg de 10 minste landene i Europa"}]
                               "temperature" 0.7})} )
(gpt-generate "Make a dog picture with 5 dogs" (System/getenv "GENPIC_OPENAI_API_KEY"))

(def people [{:name "Teodor" :height 190 :age 32}
             {:name "Johan" :height 178 :age 25}
             {:name "Boddvar" :height 168 :age 37}])

;; navnene til alle som har alder mellom 20 og 30!

(comment
  (< 20 25 30)
  
  (map count
       (map (fn [person]
              (get person :name))
            (filter (fn [person]
                      (< 20 (get person :age) 30))
                    people)))
  
  (->> people
       (filter (fn [person]
                 (< 20 (get person :age) 30)) ,,,)
       (map (fn [person]
              (get person :name)) ,,,)
       (map count ,,,)
       )
  )

(macroexpand '(->> x f g h))


(macroexpand '(->> (openapi-post "https://api.openai.com/v1/chat/completions"
                                 {:headers {:content-type "application/json"
                                            :authorization (str "Bearer " (System/getenv "GENPIC_OPENAI_API_KEY"))}
                                  :body (json/generate-string {:model "gpt-3.5-turbo"
                                                               :messages
                                                               [{"role" "user",
                                                                 "content" "Gi meg de 10 minste landene i Europa"}]
                                                               "temperature" 0.7})}) (resp->json) (first :message :content)))
(gpt-ask "What is 4+4" (System/getenv "GENPIC_OPENAI_API_KEY"))
(clerk/html [:p  
             (gpt-ask "What is the Norwegian company Iterate?" (System/getenv "GENPIC_OPENAI_API_KEY"))])
(clerk/html [:p (gpt-ask "Hva heter du?" (System/getenv "GENPIC_OPENAI_API_KEY"))]) 
(clerk/html [:p ( gpt-generate "White cat" (System/getenv "GENPIC_OPENAI_API_KEY"))])
