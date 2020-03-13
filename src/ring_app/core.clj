(ns ring-app.core
  (:require
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.util.response :refer [response file-response]]
    [clojure.data.json :as json]
    [ring.middleware.resource :refer [wrap-resource]]
    [ring.middleware.params :as p]))

(defn handler
  [request]
  (cond
    (= "/" (:uri request)) (response (json/json-str {:name "swappy" :d {:l [1 2 3 4 5 {:k 1}]}}))
    (= "/app" (:uri request)) {:status  200
                               :headers {"Content-Type" "text/html"}
                               :body    "App!!!"}
    (= "/file" (:uri request)) (file-response "project.clj")
    (and (= "/form" (:uri request)) (= :post (:request-method request)))
      (response (json/json-str (json/read-str (slurp (:body request)))))))

(def app
  (p/wrap-params (wrap-resource handler "public")))

(def reloadable-app
  (wrap-reload #'app))