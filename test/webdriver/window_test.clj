(ns webdriver.window-test
  "This namespace exercises Window manipulation code, but does not currently sport assertions. Attempts to do so over the years have resulted in non-deterministic test results. A fresh stab will be taken."
  (:require [clojure.test :refer :all]
            [webdriver.core :refer :all]
            [webdriver.test.helpers :refer :all])
  (:import org.openqa.selenium.firefox.FirefoxDriver))

(def driver (atom nil))

;; Fixtures
(defn restart-browser
  [f]
  (when-not @driver
    (reset! driver (FirefoxDriver.)))
  (to @driver *base-url*)
  (f))

(defn quit-browser
  [f]
  (f)
  (quit @driver))

(use-fixtures :once start-system! stop-system! quit-browser)
(use-fixtures :each restart-browser)

(defn test-window-size
  [this]
  (let [small {:width 500 :height 400}
        large {:width 1024 :height 800}]
    (resize this small)
    ;; (is (= (window-size this) small))
    (resize this large)
    ;; (is (= (window-size this) large))
    ))

(defn test-window-resize-with-one-dimension
  [this]
  (let [orig-size (window-size this)
        small {:height 400}
        large {:width 1024}]
    (resize this small)
    ;; (is (= (:width (window-size this)) (:width orig-size)))
    (resize this orig-size)
    ;; (is (= (window-size this) orig-size))
    (resize this large)
    ;; (is (= (:height (window-size this)) (:height orig-size)))
    ))

(defn test-window-position
  [this]
  (let [origin (position this)
        new-position {:x 100 :y 245}]
    (reposition this new-position)
    ;; (is (= (position this) new-position))
    (reposition this origin)
    ;; (is (= (position this) origin))
    ))

(defn test-window-reposition-with-one-coordinate
  [this]
  (let [origin (position this)
        position-y {:y 245}
        position-x {:x 100}]
    (reposition this position-y)
    ;; (is (= (:x (position this)) (:x origin)))
    (reposition this origin)
    ;; (is (= (position this) origin))
    (reposition this position-x)
    ;; (is (= (:y (position this)) (:y origin)))
    ))

(defn test-window-maximizing
  [this]
  (let [orig-size (window-size (resize this {:width 300 :height 300}))
        max-size (window-size (maximize this))]
    ;; (is (> (:width max-size) (:width orig-size)))
    ;; (is (> (:height max-size) (:height orig-size)))
    ))

(defn common-window-tests
  [this]
  (doseq [tst [test-window-size
               test-window-resize-with-one-dimension
               test-window-position
               test-window-reposition-with-one-coordinate
               test-window-maximizing]]
    (tst this)))

(deftest run-window-tests
  (common-window-tests @driver)
  ;; TODO: better test would be to open two windows
  ;; and pass in the second one here.
  (common-window-tests (window @driver)))
