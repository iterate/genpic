(ns iterate.genpic.explore-generate
  (:require
   [babashka.http-client :as http]
   [cheshire.core :as json]
   [clojure.edn :as edn]
   [nextjournal.clerk :as clerk]
   [babashka.curl]))

(defn openai-post
  [endpoint opts]
  (http/post endpoint {:headers (:headers opts)
                       :body (:body opts "")}))

(def test-resp [{:url
                 "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-extgqg97n0k0RCTIbmYUPxMH.png?st=2023-08-23T06%3A00%3A14Z&se=2023-08-23T08%3A00%3A14Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-22T17%3A40%3A28Z&ske=2023-08-23T17%3A40%3A28Z&sks=b&skv=2021-08-06&sig=PNIWeAKtlG42Sy9gGvXslqJjE7fNthZA2f8IWu%2B7poA%3D"}
                {:url
                 "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Oopwoel5KbsbxWjAIopGCFs9/user-XOwmgWwuxZnViZucx9P3EXdR/img-N0WdKCnGsTlw3wPty4v8yuYH.png?st=2023-08-23T06%3A00%3A15Z&se=2023-08-23T08%3A00%3A15Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-22T17%3A40%3A28Z&ske=2023-08-23T17%3A40%3A28Z&sks=b&skv=2021-08-06&sig=bMuBXKnBKdEB8KqxSo1LzHyAW6SxzmlmfE1TqLzpsto%3D"}])